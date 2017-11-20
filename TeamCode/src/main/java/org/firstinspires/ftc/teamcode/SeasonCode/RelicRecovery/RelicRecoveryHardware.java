package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREVColorDistance;
import org.firstinspires.ftc.teamcode.Utilities.Map;

/**
 * Created by Shane on 13-11-2017.
 */
public class  RelicRecoveryHardware extends OpMode {
    // ------------------------- Constants --------------------------
    // constants for the servos
    protected static final double BALL_PUSHER_UP = .44;
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
    protected static final double ARM_OUT = 0.75;
    protected static final double POOP_OPEN= .9;
    protected static final double POOP_CLOSED = 0.1;
    protected static final double BALL_ROTATE_RIGNT = 0.4;
    protected static final double BALL_ROTATE_CENTER = 0.47;
    protected static final double BALL_ROTATE_LEFT = 0.6;

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
    Servo ssBallRotator;
    // ------- Continuous Rotation Servos -------
    CRServo crHand;
    CRServo crRelicGrabber;
    CRServo crArmLift;
    CRServo crArm;
    // ---------------- Sensors -----------------
    ColorSensor color;

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
    protected double ballRotatorPosition = BALL_ROTATE_CENTER;

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
        mArmLift = map.motor("armLift");
        // ---------- Standard Servos -----------
        ssBallPusher = map.revServo("sBall", ballPusherPosition);
        ssArm = map.revServo("sArm", armPosition);
        ssRelicGrabber = map.servo("sGrabber", oneHandPosition);
        ssPoop = map.revServo("sPoop",poopPosition);
        ssBallRotator = map.servo("sBallRotator", ballRotatorPosition);
        // ----- Continuous Rotation Servos -----
        crHand = map.revCrservo("crHand");
        crRelicGrabber = map.revCrservo("crRelic");
        crArmLift = map.revCrservo("crArmLift");
        crArm = map.crservo("crArm");


        try {
            color = hardwareMap.get(ColorSensor.class, "cd");
        } catch (Exception e) {
            telemetry.addData("color-distance", "well I tried");
        }
    }
}
