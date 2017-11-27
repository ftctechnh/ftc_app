package org.firstinspires.ftc.team9853.opmodes.teleop;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.chathamrobotics.common.Controller;
import org.firstinspires.ftc.team9853.Robot9853;
import org.firstinspires.ftc.team9853.opmodes.Tele9853;

/**
 *
 */
@SuppressWarnings("unused")
@TeleOp(name = "CompDrive")
public class CompDrive extends Tele9853 {
    @Override
    public void loop() {
        if (controller1.right_trigger > 0)
            robot.lift.setPower(controller1.right_trigger);
        else
            robot.lift.setPower(
                    controller2.invert(controller1.left_trigger)
            );

        if (controller1.aState == Controller.ButtonState.TAPPED)
            robot.glyphGripper.open();

        if (controller1.xState == Controller.ButtonState.TAPPED)
            robot.glyphGripper.grip();

        robot.driveWithControls(gamepad1);
    }
}
