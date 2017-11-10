package org.firstinspires.ftc.teamcode.Shane;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by team on 7/18/2017. During FTC JAVA and robotics software workshop
 */
public class MyTestCodeHardware extends OpMode {

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

    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor liftMotor;
    DcMotor armMotor;

    Servo ballPusher;
    Servo rightHand;
    Servo leftHand;
    Servo oneHand;
    CRServo crHand;

    protected double rightPower;
    protected double leftPower;
    protected double liftPower;
    protected double armPower;


    protected double ballPusherPosition = BALL_PUSHER_UP;
    protected double leftHandPosition = OPEN_LEFT_HAND;
    protected double rightHandPosition = OPEN_RIGHT_HAND;
    protected double oneHandPosition = OPEN_ONE_HAND;
    protected double crHandPosition = STOPPED_CR_HAND;

    protected String driveMode;

    @Override
    public void init() {
        hardwareInit();
    }

    @Override
    public void loop() {

    }

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
    }
}

