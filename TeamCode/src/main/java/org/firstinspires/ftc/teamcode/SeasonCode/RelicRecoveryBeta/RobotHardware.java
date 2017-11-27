package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryBeta;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

import static org.firstinspires.ftc.teamcode.Utilities.ServoPositions.*;

/**
 * Created by Shane on 13-11-2017.
 */
public class RobotHardware {
    // ---------------------- Hardware Objects ----------------------
    private Map      map         = null;
    private SetRobot setRobot    = null;
    // ----------------- Hardware Devices -----------------
    // ---------------- DcMotors ----------------
    private DcMotor mLeft        = null;
    private DcMotor mRight       = null;
    private DcMotor mLift        = null;
    private DcMotor mArmLift     = null;
    // ------------ Standard Servos -------------
    private Servo ssArm          = null;
    private Servo ssRelicGrabber = null;
    private Servo ssBallPusher   = null;
    private Servo ssBallRotator  = null;
    // ------- Continuous Rotation Servos -------
    private CRServo crHand       = null;
    // ---------------------- Class Variables -----------------------
    // ---------------- Hardware Variables ----------------
    // ---------------- DcMotors ----------------
    double leftPower;
    double rightPower;
    double liftPower;
    double armLiftPower;
    // ------------ Standard Servos -------------
    double armPosition;
    double grabberPosition;
    double ballPusherPosition;
    double ballRotatorPosition;
    // ------- Continuous Rotation Servos -------
    double crHandPosition;
    // ------------------------ Constructor -------------------------
    RobotHardware(HardwareMap hardwareMap, Telemetry telemetry) {
        map = new Map(hardwareMap,telemetry);
        setRobot = new SetRobot(telemetry);
        leftPower           = 0;
        rightPower          = 0;
        liftPower           = 0;
        armLiftPower        = 0;
        armPosition         = ARM_IN;
        grabberPosition     = GRABBER_CLOSED;
        ballPusherPosition  = BALL_PUSHER_UP;
        ballRotatorPosition = BALL_ROTATOR_CENTER;
        crHandPosition      = HAND_STOPPED;
    }
    // ----------------------- Public Methods -----------------------
    // ----------------------- Init -----------------------
    void init() {
        mapMotors();
        mapServos();
        mapCRServos();
    }
    void setHardwarePower() {
        setMotorPowers();
        setServoPositions();
        setCRServoPowers();
    }
    // ---------------------- Private Methods -----------------------
    // --------------------- Mapping ----------------------
    private void mapMotors() {
        // -------------- DcMotors --------------
        mRight         = map.motor("r");
        mLeft          = map.revMotor("l");
        mLift          = map.motor("lift");
        mArmLift       = map.motor("armLift");
    }
    private void mapServos() {
        // ---------- Standard Servos -----------
        ssArm          = map.revServo("sArm", armPosition);
        ssRelicGrabber = map.servo("sGrabber", grabberPosition);
        ssBallPusher   = map.servo("sBall", ballPusherPosition);
        ssBallRotator  = map.servo("sBallRotator", ballRotatorPosition);
    }
    private void mapCRServos() {
        // ----- Continuous Rotation Servos -----
        crHand         = map.revCrservo("crHand");
    }
    // ---------------- Set Hardware Power ----------------
    private void setMotorPowers() {
        // -------------- DcMotors --------------
        setRobot.power(mRight,rightPower,"right motor");
        setRobot.power(mLeft,leftPower,"left motor");
        setRobot.power(mLift,liftPower,"lift motor");
        setRobot.power(mArmLift,armLiftPower,"arm lift motor");
    }
    private void setServoPositions() {
        // ---------- Standard Servos -----------
        setRobot.position(ssBallPusher,ballPusherPosition,"ball pusher servo");
        setRobot.position(ssArm,armPosition,"arm servo");
        setRobot.position(ssRelicGrabber,grabberPosition,"relic grabber servo");
        setRobot.position(ssBallRotator, ballRotatorPosition,"ball rotator servo");
    }
    private void setCRServoPowers() {
        // ----- Continuous Rotation Servos -----
        setRobot.position(crHand,crHandPosition,"hand crservo");
    }
}
