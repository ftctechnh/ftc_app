package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "mecanum drive")

public class mecanumDrive extends OpMode{

    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;


    public void init(){

        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");


            /* lets reverse the direction of the right wheel motor*/
        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

        mecanumDrive();

    }

    @Override
    public void stop(){

    }

    public void mecanumDrive(){

        float leftY = gamepad1.left_stick_y;
        float leftX = gamepad1.left_stick_x;
        float rightY = gamepad1.right_stick_y;

        leftWheelMotorBack.setPower(leftY - leftX);
        rightWheelMotorBack.setPower(rightY + leftX);
        leftWheelMotorFront.setPower(leftY + leftX);
        rightWheelMotorFront.setPower(rightY - leftX);
    }

}