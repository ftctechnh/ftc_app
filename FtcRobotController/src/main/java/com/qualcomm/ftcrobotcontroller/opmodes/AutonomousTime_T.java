package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Peter on 1/31/2016.
 * Edited by Kaitlin on 1/31/2016
 */
public class AutonomousTime_T extends LinearOpMode {
    DcMotor left;
    DcMotor right;

    static DcMotor leftMotor;
    static DcMotor rightMotor;
    static DcMotor frontLeftMotor;
    static DcMotor frontRightMotor;
    static DcMotor backLeftMotor;
    static DcMotor backRightMotor;
    static DcMotor grabberMotor;
    static DcMotor winchMotor;
    static Servo diverterServo;
    static Servo dispenserServo;
    static Servo dispenserFlipperServo;
    //static Servo tapeMeasureServo;
    static Servo climberReleaseServoRight;
    static Servo climberReleaseServoLeft;
    private static final String frontLeftMotorName = "frontLeft";
    private static final String frontRightMotorName = "frontRight";
    private static final String backLeftMotorName = "backLeft";
    private static final String backRightMotorName = "backRight";
    private static final String leftMotorName = "left";
    private static final String rightMotorName = "right";
    private static final String grabberMotorName = "grabber";
    private static final String winchMotorName = "winch";
    private static final String diverterServoName = "diverter";
    private static final String dispenserServoName = "dispenser";
    private static final String dispenserFlipperServoName = "dispenserFlipper";
    //private static final String tapeMeasureServoName = "tapeMeasure";
    private static final String climberReleaseServoRightName = "climberReleaseRight";
    private static final String climberReleaseServoLeftName = "climberReleaseLeft";
    //PhoneGyrometer gyro;
    boolean vendDeploy = false;
    boolean buttonArm = true;
    boolean speedSwitch = false;
    boolean buttonReady = true;
    boolean switchSides = false;
    boolean buttonStatus = true;

    boolean climberArm = true;
    int climberPosition = 0;

    boolean rotatorArmUp = true;
    boolean rotatorArmDown = true;
    float rotatorSpeed = 1.0f;

    public void runOpMode() throws InterruptedException
    {

    }
    public void Forwards (float inches, boolean isForward) throws InterruptedException
    {
        if (isForward) {
            leftMotor.setPower(1.0f);
            rightMotor.setPower(1.0f);
            sleep((long) (inches * 50.9554));
            leftMotor.setPower(0f);
            rightMotor.setPower(0f);
        }
        else
        {
            leftMotor.setPower(-1.0f);
            rightMotor.setPower(-1.0f);
            sleep((long) (inches * 50.9554));
            leftMotor.setPower(0f);
            rightMotor.setPower(0f);
        }
        sleep((long) (inches * 50.9554));
        left.setPower(0f);
        right.setPower(0f);
        sleep(100);
    }

    public void turnOnCenter (float degrees, boolean isLeft) throws InterruptedException
    {
        float inches = (float) ((degrees)*(Math.PI * 20)/180);
        if(isLeft)
        {
            left.setPower(-1.0f);
            right.setPower(1.0f);
        }
        else
        {
            left.setPower(1.0f);
            right.setPower(-1.0f);
        }
        sleep((long) (inches * 50.9554));
        left.setPower(0f);
        right.setPower(0f);
        sleep(100);
    }

    //m public void flippythethingy (float degrees, boolean isFullyDispensed)
    public void initialize()
    {
        // gyro = new PhoneGyrometer(hardwareMap);
        leftMotor = hardwareMap.dcMotor.get(leftMotorName);
        rightMotor = hardwareMap.dcMotor.get(rightMotorName);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor = hardwareMap.dcMotor.get(frontLeftMotorName);
        frontRightMotor = hardwareMap.dcMotor.get(frontRightMotorName);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor = hardwareMap.dcMotor.get(backLeftMotorName);
        backRightMotor = hardwareMap.dcMotor.get(backRightMotorName);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        grabberMotor = hardwareMap.dcMotor.get(grabberMotorName);
        grabberMotor.setDirection(DcMotor.Direction.REVERSE);
        winchMotor = hardwareMap.dcMotor.get(winchMotorName);
        diverterServo = hardwareMap.servo.get(diverterServoName);
        dispenserServo = hardwareMap.servo.get(dispenserServoName);
        dispenserFlipperServo = hardwareMap.servo.get(dispenserFlipperServoName);
        //tapeMeasureServo = hardwareMap.servo.get(tapeMeasureServoName);
        climberReleaseServoRight = hardwareMap.servo.get(climberReleaseServoRightName);
        climberReleaseServoLeft = hardwareMap.servo.get(climberReleaseServoLeftName);
        dispenserServo.setPosition(0.5f);
        dispenserFlipperServo.setPosition(1.0f);
        //tapeMeasureServo.setPosition(0.5f);
        climberReleaseServoRight.setPosition(1.0f);
        climberReleaseServoLeft.setPosition(0.0f);
        diverterServo.setPosition(0.5f);
    }
}
