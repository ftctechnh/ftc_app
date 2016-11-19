package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;



import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


/**
 * Created by Ian Ramsey on 9/19/2015. Updated to include arm functionality 5/3/16.
 */
@TeleOp(name = "4799TeleOp", group = "")
public class TeleOp4799 extends OpMode {
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    Servo buttonPusher;
    DcMotor motorElevator;
    Servo pineapple;

    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        buttonPusher = hardwareMap.servo.get("buttonPusher");
        motorElevator = hardwareMap.dcMotor.get("Elevator");
        pineapple = hardwareMap.servo.get("Rack");
    }


    public void loop() {
        float leftthrottle = -gamepad1.left_stick_y;
        float rightthrottle = -gamepad1.right_stick_y;

        motorBackLeft.setPower(leftthrottle); //Note that I switched the sides so the tank drive goes in the opp. direction such that it's more intuitive
        motorFrontLeft.setPower(leftthrottle);
        motorBackRight.setPower(-rightthrottle);
        motorFrontRight.setPower(-rightthrottle);

        motorElevator.setPower(gamepad2.left_stick_y);

        if (gamepad1.left_bumper)
            pineapple.setPosition(1);
        if (gamepad1.right_bumper)
            pineapple.setPosition(-1);
    }
}

