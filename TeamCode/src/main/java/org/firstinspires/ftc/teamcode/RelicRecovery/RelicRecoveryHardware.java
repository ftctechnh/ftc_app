package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    protected static final double POOP_OPEN= .9;
    protected static final double POOP_CLOSED = 0.1;
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
    Servo ssPoop;
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

    protected double ballPusherPosition = BALL_PUSHER_UP;
    protected double oneHandPosition = OPEN_ONE_HAND;
    protected double crHandPosition = STOPPED_CR_HAND;
    protected double relicPosition = STOPPED_RELIC;
    protected double armLifterSPosition = STOPPED_ARM_LIFTER_S;
    protected double armPosition = ARM_IN;
    protected double poopPosition = POOP_CLOSED;

    protected String driveMode;
    // ----------------------- Public Methods -----------------------
    // ------------------ Init ------------------
    @Override
    public void init() {
        hardwareInit();
    }
    // ------------------ Loop ------------------
    @Override
    public void loop() {}
    // ---------------------- Private Methods -----------------------
    private void hardwareInit() {
        Map map = new Map(hardwareMap,telemetry);
        // -------------- DcMotors --------------
        mRight = map.revMotor("r");
        mLeft = map.motor("l");
        mLift = map.motor("lift");
        mArm = map.motor("arm");
        mArmLift = map.motor("armlift");
        // ---------- Standard Servos -----------
        ssBallPusher = map.servo("ball", ballPusherPosition);
        ssArm = map.servo("armS", armPosition);
        ssRelicGrabber = map.servo("hand", oneHandPosition);
        ssPoop = map.revServo("poop",poopPosition);
        // ------- Continuous Rotation Servos -------
        crHand = map.revCrservo("crh");
        crRelicGrabber = map.revCrservo("relic");
        crArmLift = map.revCrservo("armLifters");
        crArm = map.crservo("armServo");
    }
}
