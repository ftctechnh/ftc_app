package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;
/*
This is a class left over from last season...
We will update this class to have both a drive engine and sensors
that are appropriate for the new robot.
 */
public class Bogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
    Sensors sensors;
    DcMotor pusherLeftMotor;
    DcMotor pusherRightMotor;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;
//        driveEngine = new DriveEngine(DriveEngine.engineMode.directMode, hardwareMap, gamepad);

        pusherLeftMotor = hardwareMap.dcMotor.get("pusher_left");
        pusherRightMotor = hardwareMap.dcMotor.get("pusher_right");
        pusherLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        pusherRightMotor.setDirection(DcMotor.Direction.FORWARD);

        sensors = new Sensors(hardwareMap);
    }
    
    //methods that utilize both driveEngine and sensors

    public void driveAlongWall(double targetDistanceFromWall, double distanceBetweenMotors, double maxPower, boolean sensorsOnLeft)
    {
        double inchesAway = sensors.minDistanceAway();
        double distanceToTarget = inchesAway - targetDistanceFromWall;
        double radius = Math.tan(90-sensors.angleToWall()) * Math.abs(distanceToTarget);
        boolean targetOnRight = sensorsOnLeft == (distanceToTarget < 0);

        //If not facing the direction we want to go, always curve towards the target
        if(sensors.angleToWall() < 0 && distanceToTarget > 0 || sensors.angleToWall() > 0 && distanceToTarget < 0)
        {
            curveTowards(targetDistanceFromWall, maxPower, targetOnRight);
        }


        else if(distanceToTarget < 3)
        {
            curveAway(distanceToTarget,  maxPower, targetOnRight);
        }

        //ensures that we can curve without going over the target line
        else if(radius < (distanceBetweenMotors+2))
        {
            curveAway(distanceToTarget, maxPower, targetOnRight);
        }

        else
        {
            curveTowards(distanceToTarget, maxPower, targetOnRight);
        }
    }

    public void curveAway(double distanceToTarget, double maxPower, boolean targetOnRight)
    {
        //Finds the radius of the target circular path
        //Turns away from the target line

        double radius = Math.tan(90-sensors.angleToWall()) * Math.abs(distanceToTarget);


//        driveEngine.setCircleMotorPower(radius, maxPower, !targetOnRight);
    }

    public void curveTowards(double distanceToTarget, double maxPower, boolean targetOnRight)
    {
        // First, curves robot to  60 degrees from the target based on a
        // circle centered at where the distance beams hit the wall.
        // This ensures that they will always hit the correct wall, thus making the curve the most efficient for our robot
        // Then continues at 60 degrees until the curve radius is less than the distance between the motors (driveAlongWall switches to curve)
        // Turns towards the target line

        double radius = distanceToTarget / Math.cos(sensors.angleToWall());

        if(sensors.angleToWall() < 60)
        {
//            driveEngine.setCircleMotorPower(radius, maxPower, targetOnRight);
        }

    }
}
