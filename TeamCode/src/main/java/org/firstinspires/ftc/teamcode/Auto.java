package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
        robot.push(true);
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
            robot.driveEngine.resetDistances();
            return Mode.Slide;
        }
        return Mode.Drop;
    }


    boolean hasMovedSideways = false;
    Mode slide()
    {
        double inchesMovedX = robot.driveEngine.xDist();
        double inchesMovedY = robot.driveEngine.yDist();
        if(timer.seconds() < .2) //for an additional .2 seconds
        {
            robot.lift(.2); //drop a bit more
        }
        else if(inchesMovedX < 6 && !hasMovedSideways) //the back encoder has moved less than 4 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(-.2,0); //drive to the side to unhook
        }
        else if(inchesMovedY < 8)
        {
            hasMovedSideways = true;
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(0,.2); //drive diagonally
            robot.driveEngine.resetXDist();
        }
        else  //if the robot has unhooked
        {
            robot.driveEngine.drive(0,0);
            robot.driveEngine.resetDistances();
            timer.reset(); //Needed for DropPark
            return Mode.Spin;
        }
        return Mode.Slide;
    }

    VuforiaTrackable target = null;
    double[] walltargetLocation = new double[2];
    double[] unitTargetLocation = new double[2];

    Mode spin()
    {
        if(robot.driveEngine.spinAngle() < Math.PI * 1/4)  //rotates 45 degrees
            robot.driveEngine.rotate(.15);
        else
            robot.driveEngine.rotate(0);  //and stops so we can see the target

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

            walltargetLocation[0] = Math.round(target.getLocation().getTranslation().get(0) / 25.4);
            walltargetLocation[1] = Math.round(target.getLocation().getTranslation().get(1) / 25.4);

            unitTargetLocation[0] = Math.signum(walltargetLocation[0]);
            unitTargetLocation[1] = Math.signum(walltargetLocation[1]);

            double x = location[0];
            double y = location[1];

            iSP = Math.signum(x * y); //initial slope positivity, 1 for craters, -1 for depots.

            telemetry.update();
            return Mode.MoveToWall;
        }
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

        robot.sensors.rotateMobile(-iSP * 90);

        double[] drive = robot.getMoveToWall();
        if(robot.sensors.dFixed.getDistance(DistanceUnit.INCH) < 6)
        {
            return Mode.MoveToDepot;
        }
        if(drive.length == 2){
            robot.driveEngine.drive(drive[0], drive[1]);
            return Mode.MoveToWall;
        }
        if(drive.length == 3)
            return Mode.MoveToDepot;

        return Mode.MoveToWall;
    }


    Mode moveToDepot()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);

        robot.driveEngine.drive(-iSP * .7,(6 - fixedDistance)/6.0);

        if(mobileDistance < 6) {
            robot.sensors.rotateMobile(iSP * 90);
            timer.reset();
            robot.driveEngine.resetDistances();
            return Mode.DropMarker;
        }
        return Mode.MoveToDepot;
    }


    Mode dropMarker()
    {
        double rotation = robot.driveEngine.spinAngle();
        if(iSP == -1 && rotation < Math.PI) //half a rotation
        {
            robot.driveEngine.rotate(.5);
            timer.reset();
            return Mode.DropMarker;
        }

        if(timer.seconds() < 2)
        {
            robot.driveEngine.drive(0, 0);
            robot.push(false);
            return Mode.DropMarker;
        }

        if (iSP == -1 && rotation < 2 * Math.PI) //full rotation
        {
            robot.driveEngine.rotate(.5);
            return Mode.DropMarker;
        }

        return Mode.MoveToCrater;

    }


    Mode moveToCrater()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        robot.driveEngine.drive(iSP * .7,(6 - fixedDistance)/6.0);

        if(robot.sensors.isTilted())
        {
            return Mode.Stop;
        }
        else
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
