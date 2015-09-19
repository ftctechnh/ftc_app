package com.qualcomm.ftcrobotcontroller.opmodes.FTC9926;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ibravo on 9/19/15.
 * And still not working Argh!
 */

public class MoveForward2 extends OpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        hardwareMap.dcMotor.get("DC1");
        hardwareMap.dcMotor.get("DC2");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        rightMotor.setPower(gamepad1.right_stick_y);
        leftMotor.setPower(gamepad1.left_stick_y);
    }

    @Override
    public void stop() {

    }
}