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

    double wheelCircumference = 8 * Math.PI;
    double ticksPerRotiation = 1440;

    public Drivetrain(){
    }

    public void init(HardwareMap hardwareMap){
        frontLeft = hardwareMap.dcMotor.get("leftMotor1");
        frontRight = hardwareMap.dcMotor.get("leftMotor2");
        backLeft = hardwareMap.dcMotor.get("rightMotor1");
        backRight = hardwareMap.dcMotor.get("rightMotor2");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
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

    public void resetEncoders(){
        this.brake();

        frontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        frontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        frontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        backLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        backRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public int getAverageEncoderValue(String side){
        switch(side){
            case "All":
                return (frontLeft.getCurrentPosition() + frontRight.getCurrentPosition() + backLeft.getCurrentPosition() + backRight.getCurrentPosition())/4;
            case "Left":
                return (frontLeft.getCurrentPosition() + backLeft.getCurrentPosition())/2;
            case "Right":
                return (frontRight.getCurrentPosition() + backRight.getCurrentPosition())/2;
            default:
                return 0;
        }
    }

    public void drive(double inches, double speed){
        this.resetEncoders();

        double targetDistance = ticksPerRotiation * inches/wheelCircumference;

        while(Math.abs(this.getAverageEncoderValue("All")) < Math.abs(targetDistance))
        {
            this.arcadeDrive(speed, 0);
        }

        this.brake();
    }

}
