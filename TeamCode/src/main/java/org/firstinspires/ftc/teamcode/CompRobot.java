package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
/
public class CompRobot extends BasicBot
{
    LinearOpMode linearOpMode;

    private DistanceSensor frontDistSens, frontRightDistSens, backDistSens;
    private DcMotorImplEx collectorPivoterMotor, collectorLifterMotor, climberMotor;
    private Servo wristCollectorServo;
    private CRServo rightGrabServo, leftGrabServo;

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
        frontRightDistSens = hardwareMap.get(DistanceSensor.class, "rightDistSens");
        frontDistSens = hardwareMap.get(DistanceSensor.class, "frontDistSens");
    }

    public void initMotorsAndMechParts(HardwareMap hardwareMap)
    {
        wristCollectorServo = hardwareMap.get(Servo.class, "wristCollectorServo");
        rightGrabServo = hardwareMap.get(CRServo.class, "rightGrabServo");
        rightGrabServo.setDirection(DcMotorSimple.Direction.FORWARD);
        leftGrabServo = hardwareMap.get(CRServo.class, "leftGrabServo");
        leftGrabServo.setDirection(DcMotorSimple.Direction.REVERSE);
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
        float encTarget;

        if (Math.abs(degrees) < 45)
            encTarget = 2.715f * degrees + .10386f;
        else
            encTarget = 2.3313f * degrees - 2.11769f;

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
            straightDist = usingDistSensor.getDistance(DistanceUnit.INCH);
            if(straightDist < distAwayFromFrontWall - Math.abs(stepDistance))
            {
                super.stopDriveMotors();
                break;
            }
            else
            {
                linearOpMode.telemetry.addData("Going forward 11", null);
                driveStraight(stepDistance, .8f);
            }
            linearOpMode.telemetry.addData("front Dist: ", straightDist);

            if (straightDist > distAwayFromFrontWall)
            {
                rightDist = getRightDistance_IN();

                linearOpMode.telemetry.addData("front Dist>18", null);
                linearOpMode.telemetry.addData("right Dist ", rightDist);
                if (rightDist < lowerDistFromSideWall)
                {
                    linearOpMode.telemetry.addData("rightdist < 4", null);
                    pivotenc(stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance , .8f);
                    pivotenc(-stepPivotAmtDeg, .5f);
                }
                else if (rightDist > upperDistFromSideWall)
                {
                    linearOpMode.telemetry.addData("right dist > 7", null);
                    pivotenc(-stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance , .8f);
                    pivotenc(stepPivotAmtDeg, .5f);
                }
                else //need this null zone for logic, this is where it goes straight, do not comment out
                {
                    driveStraight(stepDistance, .8f);
                }
               linearOpMode.telemetry.update();
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

    public double getRightDistance_IN()
    {
        return frontRightDistSens.getDistance(DistanceUnit.INCH);
    }

    public DistanceSensor getFrontDistSens()
{
    return frontDistSens;
}

    public double getFrontDistance_IN()
    {
        return frontDistSens.getDistance(DistanceUnit.INCH);
    }

    public void lowerCollectorWristAndExpel()
    {


        //Okay dood time for pseudocode

        //So the wrist-pivot inits in its very back position
        //wristpivot servo to low position
        //then set the two CR servos to rotate
        //After x amount of seconds, the two servos stop rotating

        //Some notes taken
        //left for going in 0 - .5
        // right .5 to 1 


    }


}
