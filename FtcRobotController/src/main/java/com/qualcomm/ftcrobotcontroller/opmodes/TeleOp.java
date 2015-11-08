package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Nikhil on 9/19/2015.
 */
public class TeleOp extends OpMode {

    DcMotor leftMotor1;
    DcMotor leftMotor2;
    DcMotor rightMotor1;
    DcMotor rightMotor2;
    DcMotor rightLiftMotor;
    DcMotor leftLiftMotor;

    Servo leftLiftServo;
    Servo rightLiftServo;

    double leftLiftServoUnlockedPosition = 0.37;
    double leftLiftServoLockedPosition = 0.75;

    double rightLiftServoUnlockedPosition = 0.35;
    double rightLiftServoLockedPosition = 0.50;

    boolean isLiftLocked = false;

    @Override
    public void init() {
        leftMotor1 = hardwareMap.dcMotor.get("leftMotor1");
        leftMotor2 = hardwareMap.dcMotor.get("leftMotor2");
        rightMotor1 = hardwareMap.dcMotor.get("rightMotor1");
        rightMotor2 = hardwareMap.dcMotor.get("rightMotor2");
        leftLiftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightLiftMotor = hardwareMap.dcMotor.get("rightLiftMotor");

        leftLiftServo = hardwareMap.servo.get("leftLiftServo");
        rightLiftServo = hardwareMap.servo.get("rightLiftServo");

        leftMotor1.setDirection(DcMotor.Direction.REVERSE);
        leftMotor2.setDirection(DcMotor.Direction.REVERSE);
        rightLiftMotor.setDirection(DcMotor.Direction.REVERSE);

        leftLiftServo.setPosition(leftLiftServoUnlockedPosition);
        rightLiftServo.setPosition(rightLiftServoUnlockedPosition);
    }

    @Override
    public void loop() {

        if(!isLiftLocked) {
            leftMotor1.setPower(gamepad1.left_stick_y);
            leftMotor2.setPower(gamepad1.left_stick_y);
            rightMotor1.setPower(gamepad1.right_stick_y);
            rightMotor2.setPower(gamepad1.right_stick_y);
        }

        if(!isLiftLocked) {
            leftLiftMotor.setPower(gamepad2.left_stick_y);
            rightLiftMotor.setPower(gamepad2.left_stick_y);
        }
        else
        {
            leftLiftMotor.setPower(gamepad2.left_stick_y);
            rightLiftMotor.setPower(gamepad2.left_stick_y);
            leftMotor1.setPower(gamepad2.left_stick_y);
            leftMotor2.setPower(gamepad2.left_stick_y);
            rightMotor1.setPower(gamepad2.left_stick_y);
            rightMotor2.setPower(gamepad2.left_stick_y);
        }

        if(gamepad2.a){
            isLiftLocked = true;
            leftMotor1.setPower(0);
            leftMotor2.setPower(0);
            rightMotor1.setPower(0);
            rightMotor2.setPower(0);

            leftLiftServo.setPosition(leftLiftServoLockedPosition);
            rightLiftServo.setPosition(rightLiftServoLockedPosition);
        }

        if(gamepad2.b){
            isLiftLocked = false;

            leftLiftServo.setPosition(leftLiftServoUnlockedPosition);
            rightLiftServo.setPosition(rightLiftServoUnlockedPosition);
        }

    }
}
