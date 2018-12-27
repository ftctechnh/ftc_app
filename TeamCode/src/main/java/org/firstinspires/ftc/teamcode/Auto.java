package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

import java.util.ArrayList;

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
        MoveByEncoder,
        TurnByCamera,
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
            robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.lift(0);
            robot.driveEngine.resetDistances();
            for(int i = 0; i < 6; i++){robot.driveEngine.checkpoint.add(false);}

            return Mode.Slide;
        }
        return Mode.Drop;
    }


    Mode slide()
    {
            robot.lift(0);
            if (robot.driveEngine.moveOnPath(
                    new double[]{-6, 0},
                    new double[]{0, 4},
                    new double[]{6, 0},
                    new double[]{0, 24},
                    new double[]{Math.PI / 4},
                    new double[]{-24, 24}))
            {
                robot.driveEngine.drive(0,0);
                return Mode.TurnByCamera;
            }
            return Mode.Slide;
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
            iSP = Math.signum(location[0] * location[1]);
            timer.reset();
            return Mode.MoveToDepot;
        }
        return Mode.TurnByCamera;
    }


    Mode moveToDepot()
    {
        if(timer.seconds() < 1.5) {
            robot.sensors.rotateMobile(-iSP * 90);
            return Mode.MoveToDepot;
        }
        double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
        double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);
        telemetry.addData("fixedDistance", fixedDistance);
        telemetry.addData("mobileDistance", mobileDistance);

        robot.driveEngine.drive(-iSP * .2,(4 - fixedDistance)/10.0);

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
