package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Auto {
    Bogg robot;
    Camera camera;
    Telemetry telemetry;
    private ElapsedTime timer = null;
    private int iSP = -1; //initialSlopePositivity
    private double slide2X = 28;
    private double slide2Y = 28;

    Auto(Bogg.Name name, HardwareMap hardwareMap, Telemetry telemetry)
    {
        this.robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(0);
        this.telemetry = telemetry;
        if(name != Bogg.Name.Fakebot)
            camera = new Camera(hardwareMap, telemetry);
        telemetry.addLine("Camera Loaded");
        telemetry.addLine("Wait for start");
        telemetry.update();
        timer = new ElapsedTime();
    }

    public enum Mode
    {
        Drop,
        Slide1,
        LookForMinerals,
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
        if (getTime() < 1) //for the first second
        {
            telemetry.addData("time", getTime());
            if (!robot.sensors.touchTopIsPressed()) //helps with testing
                robot.lift(-1); //pull while we
            robot.setBrake(Bogg.Direction.Off); //disengage the brake
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
        switch(camera.getGoldPosition())
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
        if (robot.driveEngine.moveOnPath(
                new double[]{-2.5, 0}))
        {
            return Mode.PushGold;
        }
        return Mode.Slide1;
    }

    Mode pushGold()
    {
        telemetry.addData("gold position", goldPosition);
        switch (goldPosition) {
            case 0:
                if (robot.driveEngine.moveOnPath(
                        new double[]{0, 28},
                        new double[]{-17,0},
                        new double[]{0, 12})) {
                    slide2X = 17;
                    slide2Y = -12;
                    return Mode.Slide2;
                }
                break;
            case 1:
                if (robot.driveEngine.moveOnPath(
                        new double[]{0, 40},
                        new double[]{0, -12})) {
                    slide2Y = 0;
                    return Mode.Slide2;
                }
                break;
            case 2:
                if (robot.driveEngine.moveOnPath(
                        new double[]{17,40},
                        new double[]{0, -12})) {
                    slide2X = 51;
                    slide2Y = 0;
                    return Mode.Slide2;
                }
                break;
        }

        return Mode.PushGold;
    }


    double approachGold = 5;
    private int i = 0;
    Mode pushGoldNoCamera()
    {
        if (robot.driveEngine.moveOnPath( "Move Right",
                new double[]{23, 28 + approachGold})) {

            if (i < 3) {
                if(robot.sensors.getLowDistance() > 6)
                    if (robot.driveEngine.moveOnPath( "Push gold" + i,
                            new double[]{0, 12},
                            new double[]{0, -(12 + approachGold)})){
                        slide2X = 51 - 17 * (i);
                        return Mode.Slide2;
                    }
                else
                    if(robot.driveEngine.moveOnPath("moveLeft" + i,
                            new double[]{-17,0}))
                        i++;
            }
            else {
                // i==3: shouldn't happen
                slide2X = 0;
                if(robot.driveEngine.moveOnPath(
                        new double[]{0, -approachGold}))
                    return Mode.Slide2;
            }
        }
        return Mode.PushGold;
    }

    Mode slide2()
    {
        if (robot.driveEngine.moveOnPath(
                new double[]{0, slide2Y},
                new double[]{-slide2X, 0},
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
        if(robot.name == Bogg.Name.Fakebot)
            return Mode.MoveToDepot;
            double[] location = camera.getLocation();
        if(location != null)
        {
            double max = Math.max(Math.abs(location[0]), Math.abs(location[1]));
            iSP = max == Math.abs(location[1])? 1 : -1;
            return Mode.MoveToDepot;

        }
        return Mode.TurnByCamera;
    }


    Mode moveToDepot()
    {
        if(robot.driveEngine.moveOnPath(new double[]{-iSP * 48,0},
                                        new double[]{iSP * Math.PI/2})) {
            timer.reset();
            return Mode.DropMarker;
        }
        return Mode.MoveToDepot;
    }


    Mode dropMarker()
    {
        robot.dropMarker(Bogg.Direction.Down);
        //robot.endEffector.

        if(getTime() > .5)  //time to drop marker
            return Mode.MoveToCrater;

        return Mode.DropMarker;
    }


    Mode moveToCrater()
    {
        double[] drive = robot.driveEngine.smoothDrive(0, -1, 2, false);
        robot.driveEngine.drive(drive[0], drive[1]);
        telemetry.addData("yDist", robot.driveEngine.yDist());
        telemetry.addData("usingImu", robot.sensors.usingImu);

        if(robot.sensors.usingImu) {
            if (robot.sensors.isTilted())
                return Mode.Stop;
        }
        else if (robot.driveEngine.yDist() < -12 * 5.5)
        {
            return Mode.Stop;
        }

        return Mode.MoveToCrater;
    }


    Mode stop()
    {
        robot.driveEngine.stop();
        telemetry.addLine("Done!!!");
        return Mode.Stop;
    }

    private double getTime()
    {
        return timer.seconds();
    }

    boolean rotateToWall(double accuracy_angle)
    {
        accuracy_angle = MyMath.radians(accuracy_angle);
        Double wallHeading = camera.headingToWall();
        telemetry.addData("wall heading", wallHeading);
        if(wallHeading != null)
        {
            if(Math.abs(wallHeading) < accuracy_angle) {
                robot.driveEngine.resetDistances();
                return true;
            }
            else
            {
                if(wallHeading > 0)
                    robot.driveEngine.rotate(.01);
                else
                    robot.driveEngine.rotate(-.01);
            }

            return false;
        }
        return false;
    }

    boolean driveCurvyToWall()
    {
        double[] location = camera.getLocation();

        if(location != null)
        {
            VuforiaTrackable target = null;
            for(int i = 0; i < 4; i++)
                if(camera.targetVisible(i))
                    target = camera.allTrackables.get(i);

            double[] drive = camera.getMoveToWall(location, camera.getHeading(), target);
            double delta_x = drive[0];
            double delta_y = drive[1];
            double headingToTarget = drive[2];
            double r = Math.hypot(delta_x, delta_y);

            if(Math.abs(headingToTarget) < 5 && r < .5)
                return true;
            else
            {
                robot.driveEngine.moveOnPath(true, drive);
            }
        }
        return false;
    }

    boolean driveCurvyToTarget(double[] driveTarget)
    {
        double[] location = camera.getLocation();

        if(location != null)
        {
            VuforiaTrackable target = null;
            for(int i = 0; i < 4; i++)
                if(camera.targetVisible(i))
                    target = camera.allTrackables.get(i);
            double[] wallTargetLocation = new double[2];

            wallTargetLocation[0] = Math.round(target.getLocation().getTranslation().get(0) / 25.4);
            wallTargetLocation[1] = Math.round(target.getLocation().getTranslation().get(1) / 25.4);

            double[] drive = camera.getMoveToPosition(location, camera.getHeading(), driveTarget);
            double delta_x = drive[0];
            double delta_y = drive[1];
            double headingToTarget = drive[2];
            double r = Math.hypot(delta_x, delta_y);

            if(Math.abs(MyMath.degrees(headingToTarget)) < 5 && r < .5)
                return true;
            else
            {
                robot.driveEngine.moveOnPath(true, drive);
            }
        }
        return false;
    }

    void update()
    {
        robot.update();
    }
}
