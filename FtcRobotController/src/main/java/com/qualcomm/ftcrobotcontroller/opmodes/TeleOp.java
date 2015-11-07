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

    double leftLiftServoUnlockedPosition;
    double leftLiftServoLockedPosition;

    double rightLiftServoUnlockedPosition;
    double rightLiftServoLockedPosition;

    @Override
    public void init() {
        leftMotor1 = hardwareMap.dcMotor.get("leftMotor1");
        leftMotor2 = hardwareMap.dcMotor.get("leftmotor2");
        rightMotor1 = hardwareMap.dcMotor.get("rightMotor1");
        rightMotor2 = hardwareMap.dcMotor.get("rightMotor2");
        leftLiftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightLiftMotor = hardwareMap.dcMotor.get("rightLiftMotor");

        leftLiftServo = hardwareMap.servo.get("leftLiftServo");
        rightLiftServo = hardwareMap.servo.get("rightLiftServo");

        rightMotor1.setDirection(DcMotor.Direction.REVERSE);
        rightMotor2.setDirection(DcMotor.Direction.REVERSE);
        rightLiftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {

        leftMotor1.setPower(gamepad1.left_stick_y);
        leftMotor2.setPower(gamepad1.left_stick_y);
        rightMotor1.setPower(gamepad1.right_stick_y);
        rightMotor2.setPower(gamepad1.right_stick_y);

        leftLiftMotor.setPower(gamepad2.left_stick_y);
        rightLiftMotor.setPower(gamepad2.left_stick_y);

        if(gamepad2.a){
            leftLiftServo.setPosition(leftLiftServoLockedPosition);
            rightLiftServo.setPosition(rightLiftServoLockedPosition);
        }

        if(gamepad2.b){
            leftLiftServo.setPosition(leftLiftServoUnlockedPosition);
            rightLiftServo.setPosition(rightLiftServoUnlockedPosition);
        }

    }
}
