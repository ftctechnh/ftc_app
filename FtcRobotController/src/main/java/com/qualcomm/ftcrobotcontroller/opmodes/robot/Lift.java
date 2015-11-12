package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;
/**
 * Created by Carlos on 11/12/2015.
 */
public class Lift {
    public DcMotor rightMotor;
    public DcMotor leftMotor;

    public Servo leftShifter;
    double leftShifterHighGear = 0.93;
    double leftShifterLowGear = 0.75;

    public Servo rightShifter;
    double rightShifterHighGear = 0.35;
    double rightShifterLowGear = 0.50;

    boolean isLocked = false;

    public int targetPosition = 0;
    double KP = 0.05;

    public Lift(){

    }

    public void init(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightLiftMotor");

        leftShifter = hardwareMap.servo.get("leftLiftServo");
        rightShifter = hardwareMap.servo.get("rightLiftServo");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftShifter.setPosition(leftShifterHighGear);
        rightShifter.setPosition(rightShifterHighGear);

        leftMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void setSpeed(double speed){
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
    }

    public void updatePosition(){

        int error = targetPosition - (leftMotor.getCurrentPosition()+rightMotor.getCurrentPosition())/2;

        this.setSpeed(error * KP);
    }

    public void setGear(String gear){

        switch (gear) {
            case "Low":
                leftShifter.setPosition(leftShifterLowGear);
                rightShifter.setPosition(rightShifterLowGear);
                break;
            case "High":
                leftShifter.setPosition(leftShifterHighGear);
                rightShifter.setPosition(rightShifterHighGear);
                break;
            default:
                break;
        }
    }
}
