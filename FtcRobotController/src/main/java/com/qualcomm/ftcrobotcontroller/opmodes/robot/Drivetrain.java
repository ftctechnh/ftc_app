package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Carlos on 11/12/2015.
 */
public class Drivetrain {

    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;

    double wheelCircumference = 6 * Math.PI;
    double ticksPerRotation = 1049;

    public Drivetrain(){
    }

    public void init(HardwareMap hardwareMap){
        frontLeft = hardwareMap.dcMotor.get("leftMotor1");
        backLeft = hardwareMap.dcMotor.get("leftMotor2");
        frontRight = hardwareMap.dcMotor.get("rightMotor1");
        backRight = hardwareMap.dcMotor.get("rightMotor2");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        try {
            resetEncoders();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tankDrive(double leftSpeed,double rightSpeed) {
        frontLeft.setPower(leftSpeed);
        backLeft.setPower(leftSpeed);

        frontRight.setPower(rightSpeed);
        backRight.setPower(rightSpeed);
    }

    public void arcadeDrive(double throttle, double turn){
        frontLeft.setPower(Range.clip(throttle - turn, -1, 1));
        backLeft.setPower(Range.clip(throttle - turn, -1, 1));

        frontRight.setPower(Range.clip(throttle + turn, -1, 1));
        backRight.setPower(Range.clip(throttle + turn, -1, 1));
    }

    public void brake(){
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void resetEncoders() throws InterruptedException {
        brake();

        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        sleep(50);

        frontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        sleep(50);

    }

    public int getAverageEncoderValue(String side) {
        if (side == "All") {
            return (frontLeft.getCurrentPosition() + frontRight.getCurrentPosition() + backLeft.getCurrentPosition() + backRight.getCurrentPosition()) / 4;
        } else if (side == "Left"){
            return (frontLeft.getCurrentPosition() + backLeft.getCurrentPosition()) / 2;
        } else if(side == "Right"){
            return (frontRight.getCurrentPosition() + backRight.getCurrentPosition()) / 2;
        } else{
            return 0;
        }
    }

    public void drive(double inches, double speed) throws InterruptedException {
        resetEncoders();

        double targetDistance = ticksPerRotation * inches/wheelCircumference;

        while(Math.abs(this.getAverageEncoderValue("All")) < Math.abs(targetDistance))
        {
            this.arcadeDrive(speed, 0);
        }

        this.brake();
    }

    public void moveDistance(int targetEncoderValue, double speed, double turn) throws InterruptedException {
        resetEncoders();

        while(Math.abs(getAverageEncoderValue("All")) < Math.abs(targetEncoderValue))
        {
            arcadeDrive(speed, turn);
        }

        arcadeDrive(0, 0);
    }

    public void moveDistance(int targetEncoderValue, double speed) throws InterruptedException {
        resetEncoders();

        while(Math.abs(getAverageEncoderValue("All")) < Math.abs(targetEncoderValue))
        {
            arcadeDrive(speed, 0);
        }

        arcadeDrive(0, 0);
    }

    public void turnDistance(int targetEncoderValue, double turn) throws InterruptedException {
        resetEncoders();

        while(Math.abs(getAverageEncoderValue("Left")) < Math.abs(targetEncoderValue) && Math.abs(getAverageEncoderValue("Right")) < Math.abs(targetEncoderValue))
        {
            arcadeDrive(0, turn);
        }

        arcadeDrive(0, 0);
    }

    public void turnLeftDistance(int targetEncoderValue, double speed) throws InterruptedException {
        resetEncoders();

        while(Math.abs(getAverageEncoderValue("Left")) < Math.abs(targetEncoderValue))
        {
            tankDrive(speed, 0);
        }

        arcadeDrive(0, 0);
    }

    public void turnRightDistance(int targetEncoderValue, double speed) throws InterruptedException {
        resetEncoders();

        while(Math.abs(getAverageEncoderValue("Right")) < Math.abs(targetEncoderValue))
        {
            tankDrive(0, speed);
        }

        arcadeDrive(0, 0);
    }

    public void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}
