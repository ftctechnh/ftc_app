package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Naman on 8/12/2016.
 */

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class AutonHelper extends OpMode {

    //driving motors
    DcMotor frontLeft,
            backLeft;

    DcMotor frontRight,
            backRight;

    GyroSensor gyro;

    //encoder targets
    private int rightTarget,
            leftTarget;

    private final double MOTOR_MAX = 1,
            MOTOR_MIN = -1;


    //ENCODER CONSTANTS
    private final double CIRCUMFERENCE_INCHES = 4 * Math.PI,
            TICKS_PER_ROTATION = 1200 / 1.05,
            TICKS_PER_INCH = TICKS_PER_ROTATION / CIRCUMFERENCE_INCHES,
            TOLERANCE = 40,
            ROBOT_WIDTH = 14.5;


    public AutonHelper() {}


    public void init() {
        //left drive
        frontLeft = hardwareMap.dcMotor.get("l1");
        backLeft = hardwareMap.dcMotor.get("l2");

        //right drive
        frontRight = hardwareMap.dcMotor.get("r1");
        backRight = hardwareMap.dcMotor.get("r2");


        gyro = hardwareMap.gyroSensor.get("gyro");


        setDirection();
        resetEncoders();
    }


    public void setDirection() {
        if (frontLeft.getDirection() == DcMotor.Direction.REVERSE) {
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
        }

        if (backLeft.getDirection() == DcMotor.Direction.REVERSE) {
            backLeft.setDirection(DcMotor.Direction.FORWARD);
        }

        if (frontRight.getDirection() == DcMotor.Direction.FORWARD) {
            frontRight.setDirection(DcMotor.Direction.REVERSE);
        }

        if (backRight.getDirection() == DcMotor.Direction.FORWARD) {
            backRight.setDirection(DcMotor.Direction.REVERSE);
        }
    }

    //ENCODER MANIPULATION
    public boolean resetEncoders() {
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        return ((frontLeft.getCurrentPosition() == 0) &&
                (backLeft.getCurrentPosition() == 0) &&
                (frontRight.getCurrentPosition() == 0) &&
                (backRight.getCurrentPosition() == 0));

    }

    public double elapsedTime(){
        return time;
    }

    public void setToEncoderMode() {

        frontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

    }

    public void setToWOEncoderMode() {

        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }


    //ENCODER BASED MOVEMENT
    public boolean runStraight(double distance_in_inches, boolean speed) {
        leftTarget = (int) (distance_in_inches * TICKS_PER_INCH);
        rightTarget = leftTarget;
        setTargetValueMotor();

        if (speed) {
            setMotorPower(.9, .9);
        } else {
            setMotorPower(.2, .2);
        }

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }

    public boolean setTargetValueTurn(double degrees) {
        int encoderTarget = (int) (degrees / 360 * Math.PI * ROBOT_WIDTH * TICKS_PER_INCH);     //theta/360*PI*D
        leftTarget = encoderTarget;
        rightTarget = -encoderTarget;
        setTargetValueMotor();
        setMotorPower(.3, .3);

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }

    public boolean setTargetValueTurnRight(double degrees) {
        int encoderTarget = (int) (degrees / 360 * Math.PI * ROBOT_WIDTH * TICKS_PER_INCH);     //theta/360*PI*D
        leftTarget = encoderTarget;
        rightTarget = -encoderTarget;
        setTargetValueMotor();
        setMotorPower(.27, .33);

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }


    public boolean turnRightToDegree(int degrees){
        int robotAngle = gyro.getHeading();
        if (robotAngle<=degrees){
            setMotorPower(.3, -.3);
            return false;
        }
        else{
            setMotorPower(0,0);
            return true;
        }
    }

    public boolean turnLeftToDegree(int degrees){
        int robotAngle = gyro.getHeading();
        if (robotAngle<=degrees){
            setMotorPower(-.3, .3);
            return false;
        }
        else{
            setMotorPower(0, 0);
            return true;
        }
    }

    public boolean driveWithoutVeer(int inches, boolean speed){
        leftTarget = (int) (inches * TICKS_PER_INCH);
        rightTarget = leftTarget;
        setTargetValueMotor();

        if (speed) {
            if (gyro.getHeading()>3 && (gyro.getHeading()<10)){
                setMotorPower(.85 , .9);
            }
            else if (gyro.getHeading()<357 && (gyro.getHeading()>350)) {
                setMotorPower(.9 , .85);
            }
            else if (gyro.getHeading()>357 && (gyro.getHeading()<3)){
                setMotorPower(9 , 9);
            }

        }
        else {
            if (gyro.getHeading()>2 && (gyro.getHeading()<10)){
                setMotorPower(.2 , .25);
            }
            else if (gyro.getHeading()<358 && (gyro.getHeading()>350)) {
                setMotorPower(.25 , .2);
            }
            else if (gyro.getHeading()>358 && (gyro.getHeading()<2)){
                setMotorPower(.25 , .25);
            }
        }

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }



    public void setTargetValueMotor() {
        frontLeft.setTargetPosition(leftTarget);
        backLeft.setTargetPosition(leftTarget);

        frontRight.setTargetPosition(rightTarget);
        backRight.setTargetPosition(rightTarget);
    }

    public boolean hasReached() {
        return (Math.abs(frontLeft.getCurrentPosition() - leftTarget) <= TOLERANCE &&
                Math.abs(backLeft.getCurrentPosition() - leftTarget) <= TOLERANCE &&
                Math.abs(frontRight.getCurrentPosition() - rightTarget) <= TOLERANCE &&
                Math.abs(backRight.getCurrentPosition() - rightTarget) <= TOLERANCE);
    }

    public void setMotorPower(double leftPower, double rightPower) {
        clipValues(leftPower, ComponentType.MOTOR);
        clipValues(rightPower, ComponentType.MOTOR);

        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);

        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    //HELPER METHODS
    enum ComponentType {
        NONE,
        MOTOR,
        SERVO
    }

    public double clipValues(double initialValue, ComponentType type) {
        double finalval = 0;
        if (type == ComponentType.MOTOR)
            finalval = Range.clip(initialValue, MOTOR_MIN, MOTOR_MAX);
        return finalval;
    }


    //DEBUG
    public void basicTel() {

        //Left Side Drive
        telemetry.addData("01 frontLeftPos: ", frontLeft.getCurrentPosition());
        telemetry.addData("02 backLeftPos: ", backLeft.getCurrentPosition());
        telemetry.addData("03 LeftTarget: ", leftTarget);

        //Right Side Drive
        telemetry.addData("04 frontRightPos: ", frontRight.getCurrentPosition());
        telemetry.addData("05 backRightPos: ", backRight.getCurrentPosition());
        telemetry.addData("06 RightTarget: ", rightTarget);

        //Sensor Values
        telemetry.addData("07 robot Heading: ", gyro.getHeading());

        telemetry.addData("0 Time Elapsed: ", time);


    }

    //MANDATORY METHODS
    public void loop() {
    }

    @Override
    public void stop() {
        setMotorPower(0, 0);//brake the movement of drive
    }

}