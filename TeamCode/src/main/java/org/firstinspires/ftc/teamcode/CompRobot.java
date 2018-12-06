package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CompRobot extends BasicBot
{
    LinearOpMode linearOpMode;

    private DistanceSensor frontDistSens, frontRightDistSens, leftDistSens;
    private DcMotorImplEx collectorPivoterMotor, collectorLifterMotor, climberMotor;
    private Servo wristCollectorServo;
    private CRServo rightGrabCRServo, leftGrabCRServo;

    public CompRobot(HardwareMap hardwareMap)
    {
        super(hardwareMap);
        initMotorsAndMechParts(hardwareMap);
    }

    public CompRobot(HardwareMap hardwareMap, LinearOpMode linearOpMode_In)
    {
        super(hardwareMap);
        initSensors(hardwareMap);
        linearOpMode = linearOpMode_In;
        initMotorsAndMechParts(hardwareMap);
    }

    public void initSensors(HardwareMap hardwareMap)
    {
        frontRightDistSens = hardwareMap.get(DistanceSensor.class, "rightDistSens");
        frontDistSens = hardwareMap.get(DistanceSensor.class, "frontDistSens");
        leftDistSens = hardwareMap.get(DistanceSensor.class, "leftDistSens");
    }

    public void initMotorsAndMechParts(HardwareMap hardwareMap)
    {

        wristCollectorServo = hardwareMap.servo.get("wristCollectorServo");
        wristCollectorServo.setPosition(0);

        rightGrabCRServo = hardwareMap.crservo.get("rightGrabCRServo");
        rightGrabCRServo.setDirection(DcMotorSimple.Direction.REVERSE);

        leftGrabCRServo = hardwareMap.crservo.get("leftGrabCRServo");
        leftGrabCRServo.setDirection(DcMotorSimple.Direction.FORWARD);

        initCRServoAndServoPos();
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

    public DistanceSensor getFrontRightDistSens()
    {
        return frontRightDistSens;
    }

    public double getRightDistance_IN()
    {
        return frontRightDistSens.getDistance(DistanceUnit.INCH);
    }
    public DistanceSensor getLeftDistSens()
    {
        return leftDistSens;
    }
    public double getLeftDistance_IN()
    {
        return leftDistSens.getDistance(DistanceUnit.INCH);
    }
    public DistanceSensor getFrontDistSens()
    {
        return frontDistSens;
    }

    public double getFrontDistance_IN()
    {
        return frontDistSens.getDistance(DistanceUnit.INCH);
    }

    public void hugWall(float lowerDistFromSideWall, float upperDistFromSideWall, float distAwayFromFrontWall, boolean isGoingForward, float maximumDistance)
    {
        double straightDist, rightDist, leftDist;
        double straightDistanceTraveled = 0;
        float stepDistance = 8;
        float stepPivotAmtDeg = 15;

        DistanceSensor usingDistSensor = frontDistSens;


        if (isGoingForward)
        {
            while (usingDistSensor.getDistance(DistanceUnit.INCH) > distAwayFromFrontWall && !linearOpMode.isStopRequested()) {
                straightDist = usingDistSensor.getDistance(DistanceUnit.INCH);
                if (straightDist < distAwayFromFrontWall - Math.abs(stepDistance)) {
                    super.stopDriveMotors();
                    break;
                } else {
                    linearOpMode.telemetry.addData("Going forward 11", null);
                    driveStraight(stepDistance, .8f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                }
                linearOpMode.telemetry.addData("front Dist: ", straightDist);

                if (straightDist > distAwayFromFrontWall) {
                    rightDist = getRightDistance_IN();

                    linearOpMode.telemetry.addData("front Dist>18", null);
                    linearOpMode.telemetry.addData("right Dist ", rightDist);
                    if (rightDist < lowerDistFromSideWall) {
                        linearOpMode.telemetry.addData("rightdist < 4", null);
                        pivotenc(stepPivotAmtDeg, .5f);
                        driveStraight(stepDistance, .5f);
                        straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                        pivotenc(-stepPivotAmtDeg, .5f);
                    } else if (rightDist > upperDistFromSideWall) {
                        linearOpMode.telemetry.addData("right dist > 7", null);
                        pivotenc(-stepPivotAmtDeg, .5f);
                        driveStraight(stepDistance, .5f);
                        straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                        pivotenc(stepPivotAmtDeg, .5f);
                    } else //need this null zone for logic, this is where it goes straight, do not comment out
                    {
                        driveStraight(stepDistance, .8f);
                        straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                    }
                    linearOpMode.telemetry.update();
                    if (straightDistanceTraveled >= maximumDistance) {
                        stopDriveMotors();
                    }
                }
            }
        }
        else
        {
            while (usingDistSensor.getDistance(DistanceUnit.INCH) > distAwayFromFrontWall && !linearOpMode.isStopRequested()) {
                straightDist = usingDistSensor.getDistance(DistanceUnit.INCH);
                if (straightDist < distAwayFromFrontWall - Math.abs(stepDistance)) {
                    super.stopDriveMotors();
                    break;
                } else {
                    linearOpMode.telemetry.addData("Going forward 11", null);
                    driveStraight(stepDistance, .8f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                }
                linearOpMode.telemetry.addData("front Dist: ", straightDist);

                if (straightDist > distAwayFromFrontWall) {
                    leftDist = getLeftDistance_IN();
                    linearOpMode.telemetry.addData("front Dist>18", null);
                    linearOpMode.telemetry.addData("left Dist ", leftDist);
                    if (leftDist < lowerDistFromSideWall) {
                        linearOpMode.telemetry.addData("left dist < 4", null);
                        pivotenc(-stepPivotAmtDeg, .5f);
                        driveStraight(stepDistance, .5f);
                        straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                        pivotenc(stepPivotAmtDeg, .5f);
                    } else if (leftDist > upperDistFromSideWall) {
                        linearOpMode.telemetry.addData("left dist > 7", null);
                        pivotenc(stepPivotAmtDeg, .5f);
                        driveStraight(stepDistance, .5f);
                        straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                        pivotenc(-stepPivotAmtDeg, .5f);
                    } else //need this null zone for logic, this is where it goes straight, do not comment out
                    {
                        driveStraight(stepDistance, .8f);
                        straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                    }
                    linearOpMode.telemetry.update();
                    if (straightDistanceTraveled >= maximumDistance) {
                        stopDriveMotors();
                    }
                }
            }
        }           // if (!isGoingForward)

    }

    public void initCRServoAndServoPos()
    {
        wristCollectorServo.setPosition(0);
        rightGrabCRServo.setPower(0);
        leftGrabCRServo.setPower(0);
    }

    public void deployMarker()
    {
        wristCollectorServo.setPosition(.49);
        linearOpMode.sleep(1000);
        leftGrabCRServo.setPower(1);
        rightGrabCRServo.setPower(1);
        linearOpMode.sleep(2069);

        initCRServoAndServoPos();
    }


    public void setGrabberWheelPower(double pow)
    {
        rightGrabCRServo.setPower(pow);
        leftGrabCRServo.setPower(pow);
    }

}
