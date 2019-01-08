package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Auto {
    Bogg robot;
    Telemetry telemetry;
    private ElapsedTime timer = null;
    private int iSP = -1; //initialSlopePositivity
    private double slide2distance = 34;

    Auto(Bogg robot, Telemetry telemetry)
    {
        this.robot = robot;
        robot.driveEngine.driveAtAngle(0);
        robot.dropMarker(Bogg.Direction.Up);
        this.telemetry = telemetry;
        robot.camera = new Camera(robot.hardwareMap, telemetry);
        telemetry.addLine("Wait for start");
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
        if (getTime() < 2) //for the first 2 seconds
        {
            telemetry.addData("time", getTime());
            if (!robot.sensors.touchTopIsPressed()) //helps with testing
                robot.lift(-1); //pull while we
            robot.setBrake(false); //disengage the brake
        } else if (!robot.sensors.touchTopIsPressed()) //if the robot is still off the ground
        {
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());
            robot.lift(.2); //push up, which drops the robot
        }
        else {
            timer.reset();
            robot.lift(0);
            robot.driveEngine.resetDistances();

            return Mode.LookForMinerals;
        }
        return Mode.Drop;
    }


    private int goldPosition = -1;
    Mode lookForMinerals()
    {
        telemetry.addLine("Looking for minerals");
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
        if (robot.driveEngine.moveOnPath(
                new double[]{-6, 0},
                new double[]{0, 4},
                new double[]{6, 0},
                new double[]{0, 24}))
        {
            return Mode.PushGold;
        }
        return Mode.Slide1;
    }

    Mode pushGold()
    {
        telemetry.addData("gold position", goldPosition);
        switch (goldPosition)
        {
            case 0:
                if(robot.driveEngine.moveOnPath(
                        new double[]{-17,0},
                        new double[]{0,12},
                        new double[]{0,-12})) {
                    slide2distance = 17;
                    return Mode.Slide2;
                }
                break;
            case 1:
                if(robot.driveEngine.moveOnPath(
                        new double[]{0,12}))
                    return Mode.Slide2;
                break;
            case 2:
                if(robot.driveEngine.moveOnPath(
                        new double[]{17,0},
                        new double[]{0,12},
                        new double[]{0,-12},
                        new double[]{-17,0}))
                    return Mode.Slide2;
                break;
        }
        return Mode.PushGold;
    }

    private int i = 0;
    Mode pushGoldNoCamera()
    {
        if (robot.driveEngine.moveOnPath(
                new double[]{17, 0})) {
            if (i < 3) {
                if (robot.driveEngine.moveOnPath(
                        new double[]{0, 12})) {
                    robot.push(Bogg.Direction.Down);

                    if (robot.driveEngine.moveOnPath(
                            new double[]{0, -12},
                            new double[]{-17, 0})) {
                        robot.push(Bogg.Direction.Up);
                        i++;
                    }
                }
            }
            else {
                slide2distance = 0;
                return Mode.Slide2;
            }
        }
        return Mode.PushGold;
    }

    Mode slide2()
    {
        robot.lift(0);
        if (robot.driveEngine.moveOnPath(
                new double[]{-slide2distance, 0},
                new double[]{Math.PI / 4}))
        {
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
        if(getTime() < 1.5) {
            return Mode.MoveToDepot;  //time to move the sensor
        }


        if(6 < 18) {
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
        if(getTime() > 4)
            return Mode.MoveToCrater;

        return Mode.DropMarker;
    }


    Mode moveToCrater()
    {
        double fixedDistance = 6;
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
        robot.driveEngine.stop();
        robot.dropMarker(Bogg.Direction.Up);
        robot.lift(0);
    }

    private double getTime()
    {
        return timer.seconds();
    }
}
