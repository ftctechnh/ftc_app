package com.qualcomm.ftcrobotcontroller.opmodes.FTC6347;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by FTCGearedUP on 5/15/2016.
 */
public class BuildBotOp extends OpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotorController drive;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("lw");
        rightMotor = hardwareMap.dcMotor.get("rw");
        drive = hardwareMap.dcMotorController.get("c1");

        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE); // motors are facing opposite direction, needs to be reversed

    }

    @Override
    public void loop() {
        if (Math.abs(gamepad1.left_stick_y) > 0.2) {
            leftMotor.setPower(gamepad1.left_stick_y);
        } else {
            leftMotor.setPower(0);
        }

        if (Math.abs(gamepad1.right_stick_y) > 0.2) {
            rightMotor.setPower(gamepad1.right_stick_y);
        } else {
            rightMotor.setPower(0);
        }

    }
}
