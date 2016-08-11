package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.BuildConfig;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Naman on 8/12/2016.
 */

public class TeleOpHelper extends OpMode {

    //driving motors
    DcMotor frontLeft,
            backLeft;

    DcMotor frontRight,
            backRight;

    //MOTOR RANGES
    private final double MOTOR_MAX = 1,
            MOTOR_MIN = -1;

    protected String driveType = "normal";



    public TeleOpHelper() {

    }


    public void init() {
        //left drive
        frontLeft = hardwareMap.dcMotor.get("l1");
        backLeft = hardwareMap.dcMotor.get("l2");

        //right drive
        frontRight = hardwareMap.dcMotor.get("r1");
        backRight = hardwareMap.dcMotor.get("r2");

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

    public void setToWOEncoderMode() {
        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    //MANUAL MOVEMENT (DRIVING)
    public void manualDrive(boolean turtleDrive) {
        setToWOEncoderMode();

        double rightPower = gamepad1.right_stick_y;
        double leftPower = gamepad1.left_stick_y;

        if (turtleDrive) {
            setMotorPower(rightPower * .3, leftPower * .3);
        } else {
            setMotorPower(rightPower, leftPower);
        }

    }

    public void backDrive() {
        setToWOEncoderMode();

        double rightPower = -gamepad1.right_stick_y;
        double leftPower = -gamepad1.left_stick_y;

        setMotorPower(.8 * rightPower, .8 * leftPower);
    }

    public void driveControl(String driveType){
        if (driveType.equals("normal")){
            manualDrive(false);
        }
        else if (driveType.equals("slow")){
            manualDrive(true);
        }
        else if (driveType.equals("backwards")){
            backDrive();
        }
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
        telemetry.addData("01 frontLeftPos: ", frontLeft.getCurrentPosition());
        telemetry.addData("02 backLeftPos: ", backLeft.getCurrentPosition());
        telemetry.addData("03a Back Left Power ", backLeft.getPower());
        telemetry.addData("03b Front Left Power ", frontLeft.getPower());

        telemetry.addData("04 frontRightPos: ", frontRight.getCurrentPosition());
        telemetry.addData("05 backRightPos: ", backRight.getCurrentPosition());
        telemetry.addData("06a Back Right Power ", backRight.getPower());
        telemetry.addData("06b Front Right Power ", frontRight.getPower());

    }


    //MANDATORY METHODS
    public void loop() {
    }

    @Override
    public void stop() {
        setMotorPower(0, 0);//brake the movement of drive

    }

}