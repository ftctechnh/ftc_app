package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Peter on 11/25/2015.
 */
public class TestMove extends OpMode
{
    DcMotor leftMotor;
    DcMotor rightMotor;

    public void init()
    {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void loop()
    {
        leftMotor.setPower(gamepad1.left_stick_y);
        rightMotor.setPower(gamepad1.right_stick_y);
        telemetry.addData("left", leftMotor.getCurrentPosition());
        telemetry.addData("right", rightMotor.getCurrentPosition());

    }








}
