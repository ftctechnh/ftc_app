package org.firstinspires.ftc.team4799;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by FTC Team 4799-4800 on 11/5/2016.
 */
@TeleOp(name = "4799", group = "")
public class TeleOp4799 extends OpMode{
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    Servo buttonPusher;


    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        buttonPusher = hardwareMap.servo.get("ButtonPusher");
    }


    public void loop() {
        float leftthrottle = -gamepad1.left_stick_y;
        float rightthrottle = -gamepad1.right_stick_y;

        motorBackLeft.setPower(rightthrottle); //Note that I switched the sides so the tank drive goes in the opp. direction such that it's more intuitive
        motorFrontLeft.setPower(rightthrottle);
        motorBackRight.setPower(-leftthrottle);
        motorFrontRight.setPower(-leftthrottle);

        if (gamepad1.right_bumper)
            buttonPusher.setPosition(-1);
        if (gamepad1.left_bumper)
            buttonPusher.setPosition(1);
    }
}
