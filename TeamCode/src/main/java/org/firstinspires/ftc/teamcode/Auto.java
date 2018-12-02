package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Auto {
    Bogg robot = null;
    Telemetry telemetry = null;
    private ElapsedTime timer = null;
    private double iSP; //initialSlopePositivity

    Auto(Bogg robot, Telemetry telemetry)
    {
        this.robot = robot;
        this.telemetry = telemetry;
        robot.camera = new Camera(robot.hardwareMap, telemetry);
        telemetry.addLine("Made it to Point X");
        telemetry.update();
    }

    public enum Mode
    {
        Stop,
        Drop,
        Slide,
        Spin,
        MoveToWall,
        MoveToDepot,
        DropMarker,
        MoveToCrater
    }

    boolean midTargetAchieved = false;

    Mode drop()
    {
        robot.push(true);
        if(timer == null) //TODO: Don't fix what's not broken
        {
            timer = new ElapsedTime();
        }
        if (timer.seconds() < 2) //for the first 2 seconds
        {
            robot.lift(-1); //pull while we
            robot.setBrake(false); //disengage the brake
        } else if (!robot.sensors.touchTop.isPressed()) //if the robot is still off the ground
        {
            robot.lift(.2); //push up, which drops the robot
        }
        else {
            timer.reset();
            return Mode.Slide;
        }
        return Mode.Drop;
    }



    Mode slide()
    {
        double inchesMovedX = Math.abs(robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
        double inchesMovedY = Math.abs(robot.driveEngine.right.getCurrentPosition() * DriveEngine.inPerTicks) - inchesMovedX/2;
        if(timer.seconds() < .2) //for an additional .2 seconds
        {
            robot.lift(.2); //drop a bit more
        }
        else if(inchesMovedX < 6) //the back encoder has moved less than 4 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(-.3,0); //drive to the side to unhook
        }
        else if(inchesMovedY < 8) //the back encoder has moved less than 6 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(0,.4); //drive diagonally
        }
        else  //if the robot has unhooked
        {
            robot.driveEngine.drive(0,0);
            robot.driveEngine.back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.driveEngine.back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            timer.reset(); //Needed for DropPark
            return Mode.Spin;
        }
        return Mode.Slide;
    }

    VuforiaTrackable target = null;
    double[] targetLocation = new double[2];
    double[] unitTargetLocation = new double[2];

    int count = 0;
    Mode spin()
    {
        count += 1;
        telemetry.addLine("Made it to point A" + count);
        telemetry.update();

        double t = timer.seconds();

        if(Math.abs(robot.driveEngine.back.getCurrentPosition()) < Math.PI * 2/3 * 9)  //rotates 60 degrees
            robot.driveEngine.rotate(.15);
        else
            robot.driveEngine.rotate(0);  //and stops so we can see the target

        telemetry.addLine("Made it to point B" + count);
        telemetry.update();

        double[] location = robot.camera.getLocation();//get a location, looks like [5.65,-2.54]
        if(!(null == location)) {

            if(robot.camera.targetVisible(0))
                target = robot.camera.allTrackables.get(0);
            else if(robot.camera.targetVisible(1))
                target = robot.camera.allTrackables.get(1);
            else if(robot.camera.targetVisible(2))
                target = robot.camera.allTrackables.get(2);
            else if(robot.camera.targetVisible(3))
                target = robot.camera.allTrackables.get(3);

            targetLocation[0] = target.getLocation().getTranslation().get(0);
            unitTargetLocation[0] = targetLocation[0] / Math.abs(targetLocation[0]);

            targetLocation[1] = target.getLocation().getTranslation().get(1);
            unitTargetLocation[1] = targetLocation[1] / Math.abs(targetLocation[1]);

            double x = location[0];
            double y = location[1];

            iSP = Math.signum(x * y);

            telemetry.update();
            return Mode.MoveToWall;
        }

        telemetry.addLine("Made it to point Gingerbread");
        telemetry.update();
        return Mode.Spin;
    }

    boolean wallTargetAchieved = false;

//    If you are standing in the Red Alliance Station looking towards the center of the field,
//    The X axis runs from your left to the right. (positive from the center to the right)
//    The Y axis runs from the Red Alliance Station towards the other side of the field
//    where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
//    The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
    Mode moveToWall()
    {
        double[] location = robot.camera.getLocation();//get a location, looks like [5.65,-2.54]
        telemetry.addLine("Made it to point Harpoon");
        if((null == location)){
            double t = timer.seconds();
            robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .2 : 0);
            return Mode.MoveToWall;
        }


        double[] wallTarget;
        double targetAngle;

        wallTarget = new double[]{5 * unitTargetLocation[0], 5 * unitTargetLocation[1]};
        targetAngle = Math.atan2(wallTarget[1], wallTarget[0]) * 180 / Math.PI;

        robot.sensors.rotateMobile(iSP * 90);

        //TODO: it would be cool to combine moving to the right place and spinning like Josh did right here.
        //Use an and statement with the two conditions to move to Depot.
        if(!wallTargetAchieved)
        {
            wallTargetAchieved = robot.driveToTarget(wallTarget[0], wallTarget[1], .3, 4); //drive to the wall
        }
        else
        {
            if(robot.rotateToTarget(targetLocation[0], targetLocation[1], 5)) //align to the wall; if we're good then
                return Mode.MoveToDepot;                           //move to the depot
        }

        return Mode.MoveToWall;
    }


    Mode moveToDepot()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);

        robot.driveEngine.drive(iSP * .7,(6 - fixedDistance)/6.0);

        if(mobileDistance < 6) {
            robot.sensors.rotateMobile(-iSP * 90);
            timer.reset();
            robot.driveEngine.back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.driveEngine.back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            return Mode.DropMarker;
        }
        return Mode.MoveToDepot;
    }


    Mode dropMarker()
    {
        double inchesMoved = Math.abs(robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
        if(iSP == 1 && inchesMoved < Math.PI * 9.0) //half a rotation
        {
            robot.driveEngine.rotate(.5);
            timer.reset();
            return Mode.DropMarker;
        }

        robot.driveEngine.drive(0,0);
        robot.push(false);
        if(iSP != 1) {
            if (timer.seconds() > 3)
                return Mode.MoveToCrater;
            return Mode.DropMarker;
        }

        if(timer.seconds() > 3)
        {
            if (inchesMoved < 2 * Math.PI * 9.0) //full rotation
                robot.driveEngine.rotate(.5);
            return Mode.DropMarker;
        }
        return Mode.MoveToCrater;
    }


    Mode moveToCrater()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        robot.push(false);
        robot.driveEngine.drive(-iSP * .7,(6 - fixedDistance)/6.0);
        if(robot.sensors.isTilted())
        {
            return Mode.Stop;
        }
        return Mode.MoveToCrater;
    }


    void stop()
    {
        robot.driveEngine.drive(0,0);
        robot.lift(0);
    }

    double getTime()
    {
        return timer.seconds();
    }
}
