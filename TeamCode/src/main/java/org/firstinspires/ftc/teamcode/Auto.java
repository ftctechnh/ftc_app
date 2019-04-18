package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public class Auto {
    Bogg robot;
    Camera camera;
    Telemetry telemetry;
    ElapsedTime timer = null;
    private int iSP = -1; //initialSlopePositivity
    private int goldPosition = -1;

    Auto(HardwareMap hardwareMap, Telemetry telemetry)
    {
        this.robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(0);
        this.telemetry = telemetry;
        camera = new Camera(hardwareMap, telemetry, false, true);
        telemetry.addLine("Camera Loaded");
        telemetry.addData("Gold Pos: ", camera.getGoldPosition());
        telemetry.addLine("Wait for start");
        telemetry.addData("TouchTop: ", robot.sensors.touchTopIsPressed());
        telemetry.addData("TouchBottom: ", robot.sensors.touchBottomIsPressed());
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


    //Robot starts in the air with touchBottom and touchTop both false
    //Pull robot up until touchBottom is true
    //Then move brake
    //Then lower robot until touchTop is true
    boolean breakIsActuallySet = true;
    boolean breakIsSet = false;
    boolean movingBreak = false;
    Mode drop()
    {
        if(breakIsSet && !robot.sensors.touchTopIsPressed() && !robot.sensors.touchBottomIsPressed())
        {
            robot.lift(-1);
        }
        else if(breakIsSet && !robot.sensors.touchTopIsPressed())
        {
            if(!movingBreak)
            {
                movingBreak = true;
                timer.reset();
                robot.setBrake(Bogg.Direction.Off);
            }
            else if(timer.seconds() > 1)
                breakIsSet = false;
        }
        else if(!breakIsSet && !robot.sensors.touchTopIsPressed())
        {
            robot.lift(.2); //push up, which drops the robot
        }
        else if(robot.sensors.touchTopIsPressed()){
            timer.reset();
            robot.lift(0); //Turns motor off
            robot.driveEngine.trueX = -4;
            robot.driveEngine.trueY = 0;
            return Mode.LookForMinerals;
        }
        return Mode.Drop;
    }

    Mode actuallyDrop()
    {
        if(!movingBreak)
        {
            timer.reset();
            movingBreak = true;
        }
        if(timer.seconds() < 2)
        {
            robot.lift(-1);
        }
        else if(timer.seconds() < 3)
        {
            robot.setBrake(Bogg.Direction.Off);
        }
        else if(!robot.sensors.touchTopIsPressed())
        {
            robot.lift(.2); //push up, which drops the robot
        }
        else if(robot.sensors.touchTopIsPressed()){
            timer.reset();
            robot.lift(0); //Turns motor off
            robot.driveEngine.trueX = -4;
            robot.driveEngine.trueY = 0;
            return Mode.LookForMinerals;
        }
        return Mode.Drop;
    }

    Mode lookForMinerals()
    {
        telemetry.addLine("Looking for minerals");
        goldPosition = camera.getGoldPosition();
        if(goldPosition != -1) {
            return Mode.Slide1;
        }
        return Mode.LookForMinerals;
    }

    Mode slide1()
    {
        if (robot.driveEngine.moveOnPath(DriveEngine.Positioning.Absolute,false,
                new double[]{0, 0}))
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
                        new double[]{-17, 36},
                        new double[]{0, -16})) {
                    return Mode.Slide2;
                }
                break;
            case 1:
                if (robot.driveEngine.moveOnPath(
                        new double[]{0, 36},
                        new double[]{0, -16})) {
                    return Mode.Slide2;
                }
                break;
            case 2:
                if (robot.driveEngine.moveOnPath(
                        new double[]{17, 36},
                        new double[]{0, -16})) {
                    return Mode.Slide2;
                }
                break;
        }

        return Mode.PushGold;
    }


    Mode slide2()
    {
        telemetry.addData("time", getTime());
        if(robot.driveEngine.moveOnPath(DriveEngine.Positioning.Absolute,false,
                new double[]{-38, 25},
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
        if(robot.driveEngine.moveOnPath(new double[]{0, 2},
                                        new double[]{-iSP * 45,0})) {
//                                        new double[]{iSP * Math.PI/2})) {
            timer.reset();
            return Mode.DropMarker;
        }
        return Mode.MoveToDepot;
    }


    Mode dropMarker()
    {
        robot.dropMarker(Bogg.Direction.Down);

        if(getTime() > .5) {  //time to drop marker
            robot.driveEngine.moveOnPath(new double[]{iSP * Math.PI/2});
            return Mode.MoveToCrater;
        }

        return Mode.DropMarker;
    }


    Mode moveToCrater()
    {
        double[] drive = robot.driveEngine.smoothDrive(0, -1, 2);
        robot.driveEngine.drive(drive[0], drive[1]);
        telemetry.addData("yDist", robot.driveEngine.yDist());
        telemetry.addData("usingImu", robot.sensors.usingImu);

        if(robot.sensors.usingImu) {
            if (robot.sensors.isTilted() || robot.driveEngine.yDist() < -12 * 10)
                return Mode.Stop;
        }
        else if (robot.driveEngine.yDist() < -12 * 10)
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
                robot.driveEngine.moveOnPath(DriveEngine.Positioning.Relative, true,
                        drive);
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
                robot.driveEngine.moveOnPath(DriveEngine.Positioning.Relative,true,
                        drive);
            }
        }
        return false;
    }

    void update()
    {
        robot.update();
    }
}
