package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


/**
 * Created by Ian Ramsey on 9/19/2015.
 */
public class NEWTeam4799Teleop extends OpMode {
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    //Servo servoClimber1;
    //Servo servoClimber2;
    Servo hookServo;
    ServoController Servocontroller;
    DcMotorController winchController;
    DcMotor hookMotor;
    DcMotor liftMotor;

    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        hookServo = hardwareMap.servo.get("hookServo");
        Servocontroller = hardwareMap.servoController.get("ServoController");
        winchController = hardwareMap.dcMotorController.get("winchController");
        hookMotor = hardwareMap.dcMotor.get("hookMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        wheelControllerRight.setMotorChannelMode(1,DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelControllerRight.setMotorChannelMode(2,DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelControllerLeft.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelControllerLeft.setMotorChannelMode(2, DcMotorController.RunMode.RUN_USING_ENCODERS);
        winchController.setMotorChannelMode(1, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        winchController.setMotorChannelMode(2, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        hookServo.setPosition(0.5);

    }

    public void loop() {
        float leftthrottle = -gamepad1.left_stick_y;
        float rightthrottle = -gamepad1.right_stick_y;


        if (gamepad1.right_bumper) {
            hookServo.setPosition(1);
        } else if (gamepad1.left_bumper) {
            hookServo.setPosition(0);
        } else {
           hookServo.setPosition(0.5);
        }

        if (gamepad1.right_trigger > 0) {
            hookMotor.setPower(1);
        } else if (gamepad1.left_trigger > 0) {
            hookMotor.setPower(-1);
        } else {
            hookMotor.setPower(0);
        }

        if (gamepad1.y) {
            liftMotor.setPower(1);
        } else if (gamepad1.a) {
            liftMotor.setPower(-1);
        } else {
            liftMotor.setPower(0);
        }

        motorBackLeft.setPower(leftthrottle);
        motorFrontLeft.setPower(leftthrottle);
        motorBackRight.setPower(-rightthrottle);
        motorFrontRight.setPower(-rightthrottle);
    }
}
