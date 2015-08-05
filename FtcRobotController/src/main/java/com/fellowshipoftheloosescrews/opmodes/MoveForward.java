package com.fellowshipoftheloosescrews.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Thomas on 6/24/2015.
 *
 * Moves da bot forward
 */
public class MoveForward extends OpMode{

    DcMotor rightMotor;
    DcMotor leftMotor;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        hardwareMap.dcMotor.get("RightDriveMotor");
        hardwareMap.dcMotor.get("LeftDriveMotor");

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
