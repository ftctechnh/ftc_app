package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;


/**
 * Created by Anton on 1/27/2016.
 */
@org.swerverobotics.library.interfaces.Autonomous(name = "LinearAutonomous")
public class LinearAutonomous extends LinearOpMode{
    static DcMotor rightWheel;
    static DcMotor leftWheel;

    static DcMotor linearSlideR;
    static DcMotor linearSlideL;

    static DcMotor sweeper;

    static Servo tubeTilt;
    static Servo tubeExtender;

    static Servo mountainClimber;
    static Servo mountainClimberRelease;

    static Servo bumper;

    static ColorSensor lineColor;
    static GyroSensor gyro;
    static UltrasonicSensor ultrasonic;




    @Override
    public void runOpMode() throws InterruptedException {

        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        leftWheel = hardwareMap.dcMotor.get("leftWheel");

        sweeper = hardwareMap.dcMotor.get("sweeper");

        lineColor = hardwareMap.colorSensor.get("lineColor");
        gyro = hardwareMap.gyroSensor.get("gyro");
        ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
        tubeTilt = hardwareMap.servo.get("tubeTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");
        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");

        bumper = hardwareMap.servo.get("bumper");

        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        leftWheel.setDirection(DcMotor.Direction.FORWARD);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeTilt.setDirection(Servo.Direction.REVERSE);
        autonomousInit();

        waitForStart();

        if(opModeIsActive()){
            //Autonomous Routine

        }
    }
    public static void autonomousInit(){
        mountainClimber.setPosition(0.1);
        mountainClimberRelease.setPosition(0.0);
        tubeExtender.setPosition(0.55);
        tubeTilt.setPosition(0.5);
        bumper.setPosition(0);
    }

    public static void moveRobotForward(double leftPower,double rightPower) {
        leftWheel.setPower(leftPower);
        rightWheel.setPower(rightPower);
    }
    public static void moveRobotBackward(double leftPower,double rightPower) {
        leftWheel.setPower(-leftPower);
        rightWheel.setPower(-rightPower);
    }
    public static void stopRobot() {
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }
    public static void turnRobotLeftForward(double power) {
        rightWheel.setPower(power);
        leftWheel.setPower(0);
    }
    public static void turnRobotRightForward(double power) {
        rightWheel.setPower(0);
        leftWheel.setPower(power);
    }
    public static void turnRobotLeftBackward(double power) {
        rightWheel.setPower(0);
        leftWheel.setPower(-power);
    }
    public static void turnRobotRightBackward(double power) {
        rightWheel.setPower(-power);
        leftWheel.setPower(0);
    }
    public static void spinRobotLeft(double power) {
        rightWheel.setPower(power);
        leftWheel.setPower(-power);
    }
    public static void spinRobotRight(double power) {
        rightWheel.setPower(power);
        leftWheel.setPower(-power);
    }
    public static void moveRobotBackRotations() {
    }
    public static void moveRobotForwardRotations() {
    }
    public static void spinRobotLeftDegrees() {
    }
    public static void spinRobotRightDegrees() {
    }
    public static void turnRobotRightForwardDegrees() {
    }
    public static void turnRobotBackForwardDegrees() {
    }
    public static void turnRobotRightBackwardDegrees() {
    }
    public static void turnRobotLeftBackwardDegrees() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }
    public static void ___() {
    }





    }
