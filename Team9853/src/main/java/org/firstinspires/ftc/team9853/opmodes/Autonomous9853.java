package org.firstinspires.ftc.team9853.opmodes;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import org.chathamrobotics.common.opmode.AutonomousTemplate;
import org.firstinspires.ftc.team9853.Robot9853;


/**
 * A autonomous template for 9853
 */
@SuppressWarnings("unused")
public abstract class Autonomous9853 extends AutonomousTemplate<Robot9853> {
    public Autonomous9853(boolean isRedTeam) {super(isRedTeam);}

    @Override
    public void setup() {
        robot = Robot9853.build(this);
        robot.init();
    }
}
