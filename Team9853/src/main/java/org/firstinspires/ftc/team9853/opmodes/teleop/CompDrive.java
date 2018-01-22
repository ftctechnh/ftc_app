package org.firstinspires.ftc.team9853.opmodes.teleop;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.chathamrobotics.common.Controller;
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
            robot.setLiftPower(controller1.right_trigger);
        else
            robot.setLiftPower(
                    -controller1.left_trigger
            );

        if (controller1.aState == Controller.ButtonState.TAPPED)
            robot.glyphGripper.open();

        if (controller1.xState == Controller.ButtonState.TAPPED)
            robot.glyphGripper.grip();

        if (controller1.yState == Controller.ButtonState.TAPPED)
            robot.glyphGripper.minOpen();

        robot.driveWithControls(gamepad1);
    }
}
