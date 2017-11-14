package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.Map;

/**
 * Created by Shane on 13-11-2017.
 */
public class RelicRecoveryHardware extends OpMode {
    // ------------------------- Constants --------------------------
    // constants for the servos
    protected static final double BALL_PUSHER_UP = .5;
    protected static final double BALL_PUSHER_DOWN = 1;
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
    // ---------------- DcMotors ----------------
    DcMotor mRight;
    DcMotor mLeft;
    DcMotor mLift;
    DcMotor mArm;
    DcMotor mArmLift;
    // ------------ Standard Servos -------------
    Servo ssBallPusher;
    Servo ssArm;
    Servo ssRelicGrabber;
    // ------- Continuous Rotation Servos -------
    CRServo crHand;
    CRServo crRelicGrabber;
    CRServo crArmLift;
    CRServo crArm;
    // --------------------- Hardware Variables ---------------------
    protected double rightPower;
    protected double leftPower;
    protected double liftPower;
    protected double armPower;
    protected double armLifterPwr = STOPPED_ARM_LIFTER;
    protected double armLifterSPosition = STOPPED_ARM_LIFTER_S;

    protected double ballPusherPosition = BALL_PUSHER_UP;
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
        // -------------- DcMotors --------------
        Map map = new Map();
        mRight = map.revMotor(mRight,"r");
        mLeft = map.motor(mLeft, "l");
        mLift = map.motor(mLift, "l");
        mArm = map.motor(mArm, "arm");
        mArmLift = map.motor(mArmLift, "armlift");
        // ---------- Standard Servos -----------
        try {
            ssBallPusher = hardwareMap.servo.get("ball");
            ssBallPusher.setPosition(ballPusherPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "ball pusher (ball)");
        }
        try {
            ssRelicGrabber = hardwareMap.servo.get("hand");
            ssRelicGrabber.setPosition(oneHandPosition);
        } catch (Exception opModeException){
            telemetry.addData("Cant map", "one hand (hand)");
        }
        // ------- Continuous Rotation Servos -------
        try {
            crHand = hardwareMap.crservo.get("crh");
            crHand.setDirection(DcMotorSimple.Direction.REVERSE);
            crHand.setPower(crHandPosition);
        } catch (Exception opModeException){
            telemetry.addData("Cant map", "cr hand (crh)");
        }
        try {
            crRelicGrabber = hardwareMap.crservo.get("relic");
            crRelicGrabber.setDirection(DcMotorSimple.Direction.REVERSE);
            crRelicGrabber.setPower(relicPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant map", "relic grabber (relic)");
        }
        try {
            crArmLift = hardwareMap.crservo.get("armLifterS");
            crArmLift.setDirection(DcMotorSimple.Direction.REVERSE);
            crArmLift.setPower(armLifterSPosition);
        } catch (Exception opModeException){
            telemetry.addData("Cant map", "arm lifter (armLifterS)");
        }
        try {
            crArm = hardwareMap.crservo.get("armServo");
            //add direction here
            crArm.setPower(armPower);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map", "arm cr(armServo)");
        }
        try {
            ssArm = hardwareMap.servo.get("armS");
            ssArm.setPosition(armPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map", "servo arm (armS)");
        }
    }
}
