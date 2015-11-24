package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;
/**
 * Created by Carlos on 11/12/2015.
 */
public class Lift {
    public DcMotor rightMotor;
    public DcMotor leftMotor;

    public Servo leftShifter;
    double leftShifterHighGear =  0.93;
    double leftShifterLowGear = 0.75;

    public Servo rightShifter;
    double rightShifterHighGear = 0.35;
    double rightShifterLowGear = 0.50;

    public boolean isShiftedHigh = true;

    /*
    public Servo leftLock;
    double leftLockedPosition;
    double leftUnlockedPosition;

    public Servo rightLock;
    double rightLockedPosition;
    double rightUnlockedPosition;

    public boolean isLocked = false;
    */

    public int targetPosition = 0;
    double KP = 0.05;

    public Lift(){

    }

    public void init(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightLiftMotor");


        leftShifter = hardwareMap.servo.get("leftLiftServo");
        rightShifter = hardwareMap.servo.get("rightLiftServo");


        /*
        leftLock = hardwareMap.servo.get("leftLockServo");
        rightLock = hardwareMap.servo.get("rightLockServo");
        */

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftShifter.setPosition(leftShifterHighGear);
        rightShifter.setPosition(rightShifterHighGear);


        /*
        leftLock.setPosition(leftUnlockedPosition);
        rightLock.setPosition(rightUnlockedPosition);
        */

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

        if(gear == "Low") {
            leftShifter.setPosition(leftShifterLowGear);
            rightShifter.setPosition(rightShifterLowGear);
        }
        else if(gear == "High"){
            leftShifter.setPosition(leftShifterHighGear);
            rightShifter.setPosition(rightShifterHighGear);
        }
    }

    /*
    public void lock(){
        leftLock.setPosition(leftLockedPosition);
        rightLock.setPosition(rightLockedPosition);
    }

    public void unlock(){
        leftLock.setPosition(leftUnlockedPosition);
        rightLock.setPosition(rightUnlockedPosition);
    }
    */
}

