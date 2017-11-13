package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Shane on 13-11-2017.
 */
public class RelicRecoveryHardware extends OpMode {
    // ------------------------- Constants --------------------------
    // constants for the servos
    protected static final double BALL_PUSHER_UP = .5;
    protected static final double BALL_PUSHER_DOWN = 1;
    protected static final double OPEN_RIGHT_HAND = 0.6;
    protected static final double OPEN_LEFT_HAND = 0.4;
    protected static final double CLOSED_RIGHT_HAND = 0.5;
    protected static final double CLOSED_LEFT_HAND = 0.5;
    protected static final double OPEN_ONE_HAND = 1;
    protected static final double CLOSED_ONE_HAND = 0;
    protected static final double STOPPED_CR_HAND = 0;
    protected static final double OPEN_CR_HAND = 1;
    protected static final double CLOSED_CR_HAND = -1;
    protected static final double UP_ARM_LIFTER = 1;
    protected static final double DOWN_ARM_LIFTER = -1;
    protected static final double STOPPED_ARM_LIFTER = 0;
    protected static final double STOPPED_RELIC = 0;
    protected static final double OPEN_RELIC = 1;
    protected static final double CLOSED_RELIC = -1;
    protected static final double UP_ARM_LIFTER_S = 1;
    protected static final double DOWN_ARM_LIFTER_S = -1;
    protected static final double STOPPED_ARM_LIFTER_S = 0;
    protected static final double ARM_IN = 0;
    protected static final double ARM_OUT = 1;
    // ---------------------- Hardware Devices ----------------------
    // DcMotors
    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor liftMotor;
    DcMotor armMotor;
    DcMotor armLifter;
    // Servos
    Servo ballPusher;
    Servo rightHand;
    Servo leftHand;
    Servo oneHand;
    CRServo crHand;
    CRServo relic;
    CRServo armLifterS;
    CRServo armServo;
    Servo arm;
    // --------------------- Hardware Variables ---------------------
    protected double rightPower;
    protected double leftPower;
    protected double liftPower;
    protected double armPower;
    protected double armLifterPwr = STOPPED_ARM_LIFTER;
    protected double armLifterSPosition = STOPPED_ARM_LIFTER_S;

    protected double ballPusherPosition = BALL_PUSHER_UP;
    protected double leftHandPosition = OPEN_LEFT_HAND;
    protected double rightHandPosition = OPEN_RIGHT_HAND;
    protected double oneHandPosition = OPEN_ONE_HAND;
    protected double crHandPosition = STOPPED_CR_HAND;
    protected double relicPosition = STOPPED_RELIC;
    protected double armPosition = ARM_IN;

    protected String driveMode;
    // ----------------------- Public Methods -----------------------
    @Override
    public void init() {
        hardwareInit();
    }

    @Override
    public void loop() {

    }
    // ---------------------- Private Methods -----------------------
    private void hardwareInit() {
        // Motors
        try {
            rightMotor = hardwareMap.dcMotor.get("r");
            rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "right motor (r)");
        }
        try {
            leftMotor = hardwareMap.dcMotor.get("l");
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "left motor (l)");
        }
        try {
            liftMotor = hardwareMap.dcMotor.get("lift");
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "lift motor (lift)");
        }
        try {
            rightMotor = hardwareMap.dcMotor.get("arm");
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "arm motor (arm)");
        }
        try {
            armLifter = hardwareMap.dcMotor.get("armlift");
            armLifter.setPower(armLifterPwr);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map", "armlifter motor (armlift)");
        }
        //Servos
        try {
            ballPusher = hardwareMap.servo.get("ball");
            ballPusher.setPosition(ballPusherPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "ball pusher (ball)");
        }
        try {
            rightHand = hardwareMap.servo.get("rh");
            rightHand.setPosition(rightHandPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "right hand (rh)");
        }
        try {
            leftHand = hardwareMap.servo.get("lh");
            leftHand.setPosition(leftHandPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "left hand (lh)");

        }
        try {
            oneHand = hardwareMap.servo.get("hand");
            oneHand.setPosition(oneHandPosition);
        } catch (Exception opModeException){
            telemetry.addData("Cant map", "one hand (hand)");
        }
        try {
            crHand = hardwareMap.crservo.get("crh");
            crHand.setDirection(DcMotorSimple.Direction.REVERSE);
            crHand.setPower(crHandPosition);
        } catch (Exception opModeException){
            telemetry.addData("Cant map", "cr hand (crh)");
        }
        try {
            relic = hardwareMap.crservo.get("relic");
            relic.setDirection(DcMotorSimple.Direction.REVERSE);
            relic.setPower(relicPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "relic grabber (relic)");
        }
        try {
            armLifterS = hardwareMap.crservo.get("armLifterS");
            armLifterS.setDirection(DcMotorSimple.Direction.REVERSE);
            armLifterS.setPower(armLifterSPosition);
        } catch (Exception opModeException){
            telemetry.addData("Cant map", "arm lifter (armLifterS)");
        }
        try {
            armServo = hardwareMap.crservo.get("armServo");
            //add direction here
            armServo.setPower(armPower);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map", "arm cr(armServo)");
        }
        try {
            arm = hardwareMap.servo.get("armS");
            //add direction here
            arm.setPosition(armPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map", "servo arm (armS)");
        }
    }
}
