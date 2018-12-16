package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Auto {
    Bogg robot = null;
    Telemetry telemetry = null;
    private ElapsedTime timer = null;
    private double iSP = -1; //initialSlopePositivity

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
            telemetry.addData("time", getTime());
            robot.lift(-1); //pull while we
            robot.setBrake(false); //disengage the brake
        } else if (!robot.sensors.touchTop.isPressed()) //if the robot is still off the ground
        {
            telemetry.addData("touchTop", robot.sensors.touchTop.isPressed());
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
            telemetry.addData("time", getTime());
            robot.lift(.2); //drop a bit more
        }
        else if(inchesMovedX < 6 && !hasMovedSideways) //the back encoder has moved less than 4 inches
        {
            telemetry.addData("inchesMovedX", inchesMovedX);
            telemetry.addData("hasMovedSideways", hasMovedSideways);
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(-.2,0); //drive to the side to unhook
        }
        else if(inchesMovedY < 8)
        {
            telemetry.addData("inchesMovedY", inchesMovedY);
            hasMovedSideways = true;
            robot.lift(0); //stop the lift motor
            robot.driveEngine.drive(0,.2); //drive diagonally
            robot.driveEngine.resetXDist();
        }
        else  //if the robot has unhooked
        {
            robot.sensors.dServo.setPosition(-.6);
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
        if(robot.driveEngine.spinAngle() < Math.PI * 1/4) {  //rotates 45 degrees
            telemetry.addData("spinAngle", robot.driveEngine.spinAngle());

            robot.driveEngine.rotate(.15);
            return Mode.Spin;
        }
        else {
            robot.driveEngine.rotate(0);  //and stops so we can see the target
            return Mode.MoveToWall;
        }
    }

    boolean wallTargetAchieved = false;

//    If you are standing in the Red Alliance Station looking towards the center of the field,
//    The X axis runs from your left to the right. (positive from the center to the right)
//    The Y axis runs from the Red Alliance Station towards the other side of the field
//    where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
//    The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
    Mode moveToWall()
    {
        robot.sensors.rotateMobile(-iSP * 90);

        if(robot.sensors.dFixed.getDistance(DistanceUnit.INCH) < 6)
        {
            telemetry.addData("fixedDistance", robot.sensors.dFixed.getDistance(DistanceUnit.INCH));
            return Mode.MoveToDepot;
        }
        else
        {
            robot.driveEngine.drive(0,.1);
        }

        return Mode.MoveToWall;
    }


    Mode moveToDepot()
    {
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);
        telemetry.addData("fixedDistance", fixedDistance);
        telemetry.addData("mobileDistance", mobileDistance);

        robot.driveEngine.drive(-iSP * .2,(6 - fixedDistance)/10.0);

        if(mobileDistance < 18) {
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
        robot.sensors.rotateMobile(iSP * 90);
        if(iSP == -1 && rotation < Math.PI) //half a rotation
        {
            robot.driveEngine.rotate(.5);
            timer.reset();
            return Mode.DropMarker;
        }

        if(timer.seconds() < 4)
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
        robot.driveEngine.drive(iSP * .2,(6 - fixedDistance)/10);

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
