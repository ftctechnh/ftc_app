package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/13/2015.
 */
public class ChassisTankTest extends OpMode {

    DcMotor leftMotor1;
    DcMotor leftMotor2;
    DcMotor rightMotor1;
    DcMotor rightMotor2;

    public void init() {

        leftMotor1 = hardwareMap.dcMotor.get("leftMotor1");
        leftMotor2 = hardwareMap.dcMotor.get("leftMotor2");
        rightMotor1 = hardwareMap.dcMotor.get("rightMotor1");
        rightMotor2 = hardwareMap.dcMotor.get("rightMotor2");

        leftMotor2.setDirection(DcMotor.Direction.REVERSE);
        leftMotor1.setDirection(DcMotor.Direction.REVERSE);

        leftMotor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        leftMotor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void loop() {

        leftMotor1.setPower(gamepad1.left_stick_y);
        leftMotor2.setPower(gamepad1.left_stick_y);
        rightMotor1.setPower(gamepad1.right_stick_y);
        rightMotor2.setPower(gamepad1.right_stick_y);

        telemetry.addData("rightMotor1", rightMotor1.getCurrentPosition());
        telemetry.addData("leftMotor1", leftMotor1.getCurrentPosition());

    }
}
