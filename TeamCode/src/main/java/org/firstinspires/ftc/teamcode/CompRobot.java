package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CompRobot extends BasicBot
{
    LinearOpMode linearOpMode;

    private DistanceSensor frontDistSens, frontRightDistSens, backDistSens;

    public CompRobot(HardwareMap hardwareMap)
    {
        super(hardwareMap);
    }

    public CompRobot(HardwareMap hardwareMap, LinearOpMode linearOpMode_In)
    {
        super(hardwareMap);
        initSensors(hardwareMap);
        linearOpMode = linearOpMode_In;
    }

    public void initSensors(HardwareMap hardwareMap)
    {
        frontRightDistSens = hardwareMap.get(DistanceSensor.class, "frontRightDistSens");
        frontDistSens = hardwareMap.get(DistanceSensor.class, "frontLeftDistSens");
    }

    public void driveStraight(float dist_In, float pow)
    {
        super.resetEncoders();
        if (dist_In > 0)
            super.goForward(pow, pow);
        else
            super.goBackwards(pow, pow);

        dist_In = Math.abs(dist_In);
        float encTarget = 19.4366f * dist_In -44.004f ;

        while (Math.abs(super.getDriveLeftOne().getCurrentPosition()) < encTarget && Math.abs(super.getDriveRightOne().getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
        {

        }
        super.stopDriveMotors();
    }

    public void pivotenc(float degrees, float pow)
    {
        pow = Math.abs(pow);
        super.resetEncoders();

        if (degrees > 0)
            super.driveMotors(-pow, pow);
        else
            super.driveMotors(pow, -pow);

        degrees = Math.abs(degrees);
        float encTarget = 2.36578f * degrees - 4.71071f;

        while (Math.abs(super.getDriveLeftOne().getCurrentPosition()) < encTarget && Math.abs(super.getDriveRightOne().getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
        {

        }
        super.stopDriveMotors();
    }

    public void hugWall(float lowerDistFromSideWall, float upperDistFromSideWall, float distAwayFromFrontWall, boolean isGoingForward)
    {
        double straightDist, rightDist;

        float stepDistance = 11;
        float stepPivotAmtDeg = 15;

        DistanceSensor usingDistSensor = frontDistSens;

        if (!isGoingForward)
        {
            usingDistSensor = backDistSens;
            stepDistance = -stepDistance;
            stepPivotAmtDeg = -stepPivotAmtDeg;
        }

        while (usingDistSensor.getDistance(DistanceUnit.INCH) > distAwayFromFrontWall && !linearOpMode.isStopRequested())
        {
            linearOpMode.sleep(400);
            straightDist = usingDistSensor.getDistance(DistanceUnit.INCH);
            if(straightDist < distAwayFromFrontWall - Math.abs(stepDistance)/2)
            {
                super.stopDriveMotors();
                break;
            }
            else
                driveStraight(stepDistance, .8f);

            if (straightDist > distAwayFromFrontWall)
            {
                rightDist = frontRightDistSens.getDistance(DistanceUnit.INCH);

                if (rightDist < lowerDistFromSideWall)
                {
                    pivotenc(-stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance , .8f);
                    pivotenc(stepPivotAmtDeg, .5f);
                }
                else if (rightDist > upperDistFromSideWall)
                {
                    pivotenc(stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance , .8f);
                    pivotenc(-stepPivotAmtDeg, .5f);
                }
                else
                {
                    driveStraight(stepDistance * .69f , .8f);
                }
            }
        }
    }

    /* Old hugwall code,
    public void hugWallForward(float lowerDistFromSideWall, float upperDistFromSideWall, float distAwayFromFrontWall)
    {
        double frontDist, rightDist;

        while (frontDistSens.getDistance(DistanceUnit.INCH) > distAwayFromFrontWall && !linearOpMode.isStopRequested())
        {
            linearOpMode.sleep(400);
            frontDist = frontDistSens.getDistance(DistanceUnit.INCH);
            if(frontDist < distAwayFromFrontWall - 6)
            {
                super.stopDriveMotors();
                break;
            }
            else
                driveStraight(11);

            if (frontDist > distAwayFromFrontWall)
            {
                rightDist = frontRightDistSens.getDistance(DistanceUnit.INCH);

                if (rightDist < lowerDistFromSideWall)
                {
                    pivotenc(-15);
                    driveStraight(11);
                    pivotenc(15);
                }
                else if (rightDist > upperDistFromSideWall)
                {
                    pivotenc(15);
                    driveStraight(11);
                    pivotenc(-15);
                }
                else if (lowerDistFromSideWall < rightDist & rightDist < upperDistFromSideWall) //To note in the future, maybe replace with else
                {
                    driveStraight(8);
                }
            }
        }
    }
    */

    public DistanceSensor getFrontRightDistSens()
    {
        return frontRightDistSens;
    }

    public DistanceSensor getFrontDistSens()
    {
        return frontDistSens;
    }
}
