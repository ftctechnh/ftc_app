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
import org.chathamrobotics.common.systems.IsBusyException;
import org.firstinspires.ftc.team9853.opmodes.Tele9853;

/**
 * Tests the jewel displacer
 */
@SuppressWarnings("unused")
@TeleOp(name = "Jewel Tester", group = "Test")
public class JewelTester extends Tele9853 {

    @Override
    public void start() {
        super.start();
        robot.start();
        robot.jewelDisplacer.raise();
    }

    @Override
    public void loop() {
        controller1.update();

        try {
            if (controller1.aState == Controller.ButtonState.TAPPED) {
                robot.jewelDisplacer.shiftLeft();
            }

            if (controller1.bState == Controller.ButtonState.TAPPED) {
                robot.jewelDisplacer.shiftRight();
            }

            if (controller1.xState == Controller.ButtonState.TAPPED) {
                robot.jewelDisplacer.raise();
            }
            
            if (controller1.yState == Controller.ButtonState.TAPPED) {
                robot.jewelDisplacer.drop();
            }
        } catch (IsBusyException e) {
            // Do Nothing
        }
    }
}
