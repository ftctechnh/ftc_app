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
    DcMotor armMotor;

    Servo leftLiftServo;
    Servo rightLiftServo;
    Servo armServo;

    double leftLiftServoUnlockedPosition = 0.93;
    double leftLiftServoLockedPosition = 0.75;

    double rightLiftServoUnlockedPosition = 0.35;
    double rightLiftServoLockedPosition = 0.50;

    double armServoUpwardSpeed = 0.6;
    double armServoStoppedSpeed = 0.5;
    double armServoDownwardSpeed = 0.4;

    double armMotorForwardSpeed = 0.5;
    double armMotorStoppedSpeed = 0.0;
    double armMotorBackwardSpeed = -0.5;

    boolean isLiftLocked = false;

    @Override
    public void init() {
        leftMotor1 = hardwareMap.dcMotor.get("leftMotor1");
        leftMotor2 = hardwareMap.dcMotor.get("leftMotor2");
        rightMotor1 = hardwareMap.dcMotor.get("rightMotor1");
        rightMotor2 = hardwareMap.dcMotor.get("rightMotor2");
        leftLiftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightLiftMotor = hardwareMap.dcMotor.get("rightLiftMotor");
        armMotor = hardwareMap.dcMotor.get("armMotor");

        leftLiftServo = hardwareMap.servo.get("leftLiftServo");
        rightLiftServo = hardwareMap.servo.get("rightLiftServo");
        armServo = hardwareMap.servo.get("armServo");

        leftMotor1.setDirection(DcMotor.Direction.REVERSE);
        leftMotor2.setDirection(DcMotor.Direction.REVERSE);
        rightLiftMotor.setDirection(DcMotor.Direction.REVERSE);

        leftLiftServo.setPosition(leftLiftServoUnlockedPosition);
        rightLiftServo.setPosition(rightLiftServoUnlockedPosition);

        armServo.setPosition(armServoStoppedSpeed);
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
            leftLiftMotor.setPower(gamepad2.left_stick_y * 3.0/4.0);
            rightLiftMotor.setPower(gamepad2.left_stick_y * 3.0/4.0);

            leftMotor1.setPower(-gamepad2.left_stick_y);
            leftMotor2.setPower(-gamepad2.left_stick_y);
            rightMotor1.setPower(-gamepad2.left_stick_y);
            rightMotor2.setPower(-gamepad2.left_stick_y);
        }

        if(gamepad2.dpad_up){
            isLiftLocked = true;
            leftMotor1.setPower(0);
            leftMotor2.setPower(0);
            rightMotor1.setPower(0);
            rightMotor2.setPower(0);

            leftLiftServo.setPosition(leftLiftServoLockedPosition);
            rightLiftServo.setPosition(rightLiftServoLockedPosition);
        }

        if(gamepad2.dpad_down){
            isLiftLocked = false;

            leftLiftServo.setPosition(leftLiftServoUnlockedPosition);
            rightLiftServo.setPosition(rightLiftServoUnlockedPosition);

            leftMotor1.setPower(0);
            leftMotor2.setPower(0);
            rightMotor1.setPower(0);
            rightMotor2.setPower(0);
            leftLiftMotor.setPower(0);
            rightLiftMotor.setPower(0);
        }

        if(gamepad2.y){
            armMotor.setPower(armMotorForwardSpeed);
        }


        if(gamepad2.a){
            armMotor.setPower(armMotorBackwardSpeed);
        }


        if(!(gamepad2.y || gamepad2.a)){
            armMotor.setPower(armMotorStoppedSpeed);
        }

        if(gamepad2.right_bumper){
            armServo.setPosition(armMotorForwardSpeed);
        }

        if(gamepad2.right_trigger > 0.5){
            armServo.setPosition(armServoDownwardSpeed);
        }

        if(!(gamepad2.right_bumper || gamepad2.right_trigger > 0.5)){
            armServo.setPosition(armServoStoppedSpeed);
        }


    }
}
