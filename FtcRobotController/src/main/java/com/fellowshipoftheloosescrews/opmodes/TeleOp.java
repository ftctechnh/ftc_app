package com.fellowshipoftheloosescrews.opmodes;

import com.fellowshipoftheloosescrews.utilities.Util;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Thomas on 8/12/2015.
 *
 * A basic tele-op program for our tankdrive robot
 */
public class TeleOp extends OpMode
{
    DcMotor rightMotor;
    DcMotor leftMotor;

    private double nudgePower = -0.25;

    @Override
    public void init() {
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        //TODO: reverse one of the motors (check which one first)
    }

    @Override
    public void loop() {
        rightMotor.setPower(gamepad1.right_stick_y);
        leftMotor.setPower(gamepad1.left_stick_y);
        telemetry.addData("right", gamepad1.right_stick_y);
        telemetry.addData("left", gamepad1.left_stick_y);
        if(gamepad1.dpad_up)
        {
            rightMotor.setPower(nudgePower);
            leftMotor.setPower(nudgePower);
        }
        if(gamepad1.dpad_down)
        {
            rightMotor.setPower(-nudgePower);
            leftMotor.setPower(-nudgePower);
        }
        if(gamepad1.dpad_right)
        {
            rightMotor.setPower(-nudgePower);
            leftMotor.setPower(nudgePower);
        }
        if(gamepad1.dpad_left)
        {
            rightMotor.setPower(nudgePower);
            leftMotor.setPower(-nudgePower);
        }
    }
}
