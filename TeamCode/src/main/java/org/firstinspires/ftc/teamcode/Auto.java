package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Auto {
    Bogg robot = null;
    StartPosition startPosition = StartPosition.HighRed;
    Telemetry telemetry = null;
    private ElapsedTime timer = null;
    private double iSP; //initialSlopePositivity

    Auto(Bogg robot, Telemetry telemetry)
    {
        this.robot = robot;
        robot.driveEngine.driveAtAngle(Math.PI);
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

    private enum StartPosition
    {
        HighBlue,
        LowBlue,
        HighRed,
        LowRed
    }
    private boolean midTargetAchieved = false;

    Mode drop()
    {
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
        double inchesMoved = Math.abs(robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
        if(timer.seconds() < .2) //for an additional .2 seconds
        {
            robot.lift(.2); //drop a bit more
        }
        else if(inchesMoved < 4.5) //the back encoder has moved less than 4 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(.4,0); //drive to the side to unhook
        }
        else if(inchesMoved < 6) //the back encoder has moved less than 6 inches
        {
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(.4,.6); //drive diagonally
        }
        else  //if the robot has unhooked
        {
            robot.driveEngine.drive(0,0);
            timer.reset(); //Needed for DropPark
            return Mode.Spin;
        }
        return Mode.Slide;
    }



    int count = 0;
    Mode spin()
    {
        count += 1;
        telemetry.addLine("Made it to point A" + count);
        telemetry.update();

        double t = timer.seconds();
        if(t/1.5 - Math.floor(t)/1.5 < .60)  //rotates for 60% of 1.5 seconds = .9 and .6
            robot.driveEngine.rotate(.2);
        else
            robot.driveEngine.rotate(0);  //and stops so we can see the target

        telemetry.addLine("Made it to point B" + count);
        telemetry.update();

        double[] location = robot.camera.getLocation();//get a location, looks like [5.65,-2.54]
        if(!(null == location)) {
            telemetry.addLine("Made it to point Quilt");
            telemetry.update();

            double angle = Math.atan2(location[0], location[1]); //get the angle looking down on the field, lander to robot
            telemetry.addLine("Made it to point Needle");
            telemetry.update();

            if (angle < -Math.PI / 2)                          //Quadrant 3
                startPosition = StartPosition.LowBlue;
            else if (angle < 0)                              //Quadrant 4
                startPosition = StartPosition.LowRed;
            else if (angle < Math.PI / 2)                      //Quadrant 1
                startPosition = StartPosition.HighRed;
            else // angle between pi/2 and pi               //Quadrant 2
                startPosition = StartPosition.HighBlue;

            telemetry.addLine("Made it to point 33");
            telemetry.update();
            return Mode.Spin;
        }

        telemetry.addLine("Made it to point Gingerbread");
        telemetry.update();
        return Mode.MoveToWall;
    }


    Mode moveToWall()
    {
        telemetry.addLine("Made it to point Harpoon");
        if(robot.camera.targetVisible()){
            double t = timer.seconds();
            robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .2 : 0);
        }

        double[] midTarget;
        double[] wallTarget;
        double targetAngle;

        switch(startPosition)
        {
            case HighRed:
                midTarget = new double[]{0,3};
                wallTarget = new double[]{0,5.5};
                targetAngle = 90;
                iSP = 1;
                break;
            case HighBlue:
                midTarget = new double[]{-3,0};
                wallTarget = new double[]{-5.5,0};
                targetAngle = 180;
                iSP = -1;
                break;
            case LowBlue:
                midTarget = new double[]{0,-3};
                wallTarget = new double[]{0,-5.5};
                targetAngle = 270;
                iSP = 1;
                break;
            default: //case LowRed:
                midTarget = new double[]{3,0};
                wallTarget = new double[]{5.5,0};
                targetAngle = 0;
                iSP = -1;
                break;
        }
        if(!midTargetAchieved) { //if we haven't achieved the midtarget
            if(robot.driveToTarget(midTarget[0], midTarget[0], .6, 4))  //keep driving to the midtarget, also returns if we have, if so
                midTargetAchieved = true; //say that we have acheived the midtarget
        }
        else if(robot.driveToTarget(wallTarget[0], wallTarget[1], .6, 4)) //drive to the wall
        {
            if(robot.rotateToTargetAngle(targetAngle, 5)) //align to the wall; if we're good then
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
            robot.sensors.rotateMobile(-90);
            timer.reset();
            return Mode.DropMarker;
        }
        return Mode.MoveToDepot;
    }


    Mode dropMarker()
    {
        robot.driveEngine.drive(0,0);
        robot.push(true);
        if(timer.seconds() > 3)
            return Mode.MoveToCrater;
        return Mode.DropMarker;
    }


    Mode moveToCrater()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        robot.push(false);
        robot.driveEngine.drive(iSP * .7,(6 - fixedDistance)/6.0);
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
