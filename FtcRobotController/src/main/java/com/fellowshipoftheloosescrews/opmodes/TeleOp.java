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
    // Defining variables
    DcMotor rightMotor;
    DcMotor leftMotor;

    // sets the nudge power
    private double nudgePower = -0.25;

    @Override
    public void init() {
        // gets startup data from the hardwaremap
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        // takes value of joystick and sets motors
        rightMotor.setPower(gamepad1.right_stick_y);
        leftMotor.setPower(gamepad1.left_stick_y);

        
        // the joystick data in the telemetry
        telemetry.addData("right", gamepad1.right_stick_y);
        telemetry.addData("left", gamepad1.left_stick_y);

        // makes motors move in specified direction from dpad
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
