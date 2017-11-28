package org.firstinspires.ftc.team9853.opmodes;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import org.chathamrobotics.common.opmode.TeleOpTemplate;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * A Teleop template for 9853
 */
@SuppressWarnings("unused")
public abstract class Tele9853 extends TeleOpTemplate<Robot9853> {
    @Override
    public void init() {
        super.init();

        robot = Robot9853.build(this);
        robot.init();
    }
}
