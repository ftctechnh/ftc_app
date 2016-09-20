package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


/**
 * Created by Ian Ramsey on 9/19/2015. Updated to include arm functionality 5/3/16.
 */
public class DemoBot extends OpMode {
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor arm;
    DcMotorController armController;
    ServoController hand;
    Servo leftFinger;
    Servo rightFinger;



    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        arm = hardwareMap.dcMotor.get("Arm1");
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        armController = hardwareMap.dcMotorController.get("Arm");
        hand = hardwareMap.servoController.get("Hand");
        rightFinger = hardwareMap.servo.get("RightFinger");
        leftFinger = hardwareMap.servo.get("LeftFinger");

    }

    public void loop() {
        float leftthrottle = -gamepad1.left_stick_y;
        float rightthrottle = -gamepad1.right_stick_y;
        float armthrottle = -gamepad2.left_stick_y;
        float fingerstate = (float) ((1.0-gamepad2.right_stick_y)/2.0);


        motorBackLeft.setPower(rightthrottle); //Note that I switched the sides so the tank drive goes in the opp. direction such that it's more intuitive
        motorFrontLeft.setPower(rightthrottle);
        motorBackRight.setPower(-leftthrottle);
        motorFrontRight.setPower(-leftthrottle);
        arm.setPower(-armthrottle);
        rightFinger.setPosition(fingerstate);
        leftFinger.setPosition(1-fingerstate);
    }
}
