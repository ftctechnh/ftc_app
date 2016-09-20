package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by robotics on 10/20/2015.
 */
public class Ethansopmode extends OpMode {

    DcMotor leftBackMotor;
    DcMotor leftFrontMotor;
    DcMotor rightBackMotor;
    DcMotor rightFrontMotor;
    DcMotorController rightController;
    DcMotorController leftController;




    @Override
    public void init() {
        //Get hardware references
        leftBackMotor = hardwareMap.dcMotor.get("back_left_drive");
        leftFrontMotor = hardwareMap.dcMotor.get("front_left_drive");
        rightBackMotor = hardwareMap.dcMotor.get("back_right_motor");
        rightFrontMotor = hardwareMap.dcMotor.get("front_right_motor");
        leftController = hardwareMap.dcMotorController.get("right_controller");
        rightController = hardwareMap.dcMotorController.get("left_controller");


        //Reverses right motors
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftFrontMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightBackMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightFrontMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void loop() {
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        rightBackMotor.setPower(rightY);
        rightFrontMotor.setPower(rightY);
        leftBackMotor.setPower(leftY);
        leftFrontMotor.setPower(leftY);


    }
}
