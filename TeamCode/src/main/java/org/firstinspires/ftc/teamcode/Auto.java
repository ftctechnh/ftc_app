package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Auto {
    Bogg robot = null;
    Telemetry telemetry = null;
    private ElapsedTime timer = null;
    private int iSP = -1; //initialSlopePositivity

    Auto(Bogg robot, Telemetry telemetry)
    {
        this.robot = robot;
        robot.dropMarker(Bogg.Direction.Straight);
        this.telemetry = telemetry;
        robot.camera = new Camera(robot.hardwareMap, telemetry);
        telemetry.addLine("Made it to Point X");
        telemetry.update();
    }

    public enum Mode
    {
        Drop,
        LookForMinerals,
        Slide1,
        PushGold,
        Slide2,
        TurnByCamera,
        MoveToDepot,
        DropMarker,
        MoveToCrater,
        Stop
    }


    Mode drop()
    {
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
            robot.lift(.2); //dropMarker up, which drops the robot
        }
        else {
            timer.reset();
            robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.lift(0);
            robot.driveEngine.resetDistances();

            return Mode.Slide1;
        }
        return Mode.Drop;
    }


    int goldPosition = -1;
    Mode lookForMinerals()
    {
        switch(robot.camera.getGoldPosition())
        {
            case 0:
                goldPosition = 0;
                return Mode.Slide1;
            case 1:
                goldPosition = 1;
                return Mode.Slide1;
            case 2:
                goldPosition = 2;
                return Mode.Slide1;
            default:
                return Mode.LookForMinerals;
        }
    }


    Mode slide1()
    {
        robot.lift(0);
        if (robot.driveEngine.moveOnPath("slide1",
                new double[]{-6, 0},
                new double[]{0, 4},
                new double[]{6, 0},
                new double[]{0, 24}))
        {
            robot.driveEngine.drive(0,0);
            return Mode.Slide2;
        }
        return Mode.Slide1;
    }

    Mode pushGold()
    {
        switch (goldPosition)
        {
            case 0:
                if(robot.driveEngine.moveOnPath("pushGold",
                        new double[]{-34,0},
                        new double[]{0,12},
                        new double[]{0,-12},
                        new double[]{34,0}))
                    return Mode.Slide2;
                break;
            case 1:
                if(robot.driveEngine.moveOnPath("pushGold",
                        new double[]{0,12}))
                    return Mode.Slide2;
                break;
            case 2:
                if(robot.driveEngine.moveOnPath("pushGold",
                        new double[]{34,0},
                        new double[]{0,12},
                        new double[]{0,-12},
                        new double[]{-34,0}))
                    return Mode.Slide2;
                break;
        }
        return Mode.PushGold;
    }

    Mode slide2()
    {
        robot.lift(0);
        if (robot.driveEngine.moveOnPath("slide2",
                new double[]{Math.PI / 4},
                new double[]{-24, 24}))
        {
            robot.driveEngine.drive(0,0);
            return Mode.TurnByCamera;
        }
        return Mode.Slide2;
    }


//    If you are standing in the Red Alliance Station looking towards the center of the field,
//    The X axis runs from your left to the right. (positive from the center to the right)
//    The Y axis runs from the Red Alliance Station towards the other side of the field
//    where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
//    The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)

    Mode turnByCamera()
    {
        if(robot.rotateToWall(2)) {
            double[] location = robot.camera.getLocation();
            double max = Math.max(Math.abs(location[0]), Math.abs(location[1]));
            if(max == Math.abs(location[0])) {
                iSP = -1;
                robot.rotateMobile(Bogg.Direction.Right);
            }
            else {
                iSP = 1;
                robot.rotateMobile(Bogg.Direction.Left);
            }
            timer.reset();
            return Mode.MoveToDepot;
        }
        return Mode.TurnByCamera;
    }


    Mode moveToDepot()
    {
        if(timer.seconds() < 1.5) {
            return Mode.MoveToDepot;  //time to move the sensor
        }
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);
        telemetry.addData("fixedDistance", fixedDistance);
        telemetry.addData("mobileDistance", mobileDistance);

        robot.driveEngine.drive(-iSP * .2,(4 - fixedDistance)/10.0);

        if(mobileDistance < 18) {
            if(iSP == -1)
                robot.rotateMobile(Bogg.Direction.Left);
            else
                robot.rotateMobile(Bogg.Direction.Right);
            timer.reset();
            robot.driveEngine.resetDistances();
            return Mode.DropMarker;
        }
        return Mode.MoveToDepot;
    }


    Mode dropMarker()
    {
        switch (iSP)
        {
            case -1: //drop on right side
                robot.dropMarker(Bogg.Direction.Right);
                break;
            case 1:  //drop on left side
                robot.dropMarker(Bogg.Direction.Left);
                break;
        }
        if(timer.seconds() > 4)
            return Mode.MoveToCrater;

        return Mode.DropMarker;
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
        robot.dropMarker(Bogg.Direction.Straight);
        robot.lift(0);
    }

    double getTime()
    {
        return timer.seconds();
    }
}
