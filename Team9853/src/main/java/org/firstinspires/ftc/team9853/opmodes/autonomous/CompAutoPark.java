package org.firstinspires.ftc.team9853.opmodes.autonomous;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import org.chathamrobotics.common.opmode.AutonomousRnB;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Hits the jewel and parks
 */
@AutonomousRnB
@SuppressWarnings("unused")
public class CompAutoPark extends CompAuto {
    public CompAutoPark(boolean isRedTeam) {super(isRedTeam);}

    @Override
    public void run() throws InterruptedException, StoppedException {
        super.run();
        if (! isHit) return;

        robot.driver.setDrivePower(90, AngleUnit.DEGREES, 1, 0);
        Thread.sleep(600);

        robot.rotateSync( 135, AngleUnit.DEGREES);
        robot.driver.setDrivePower(90, AngleUnit.DEGREES, 1, 0);

        Thread.sleep(1250);
        robot.driver.stop();
    }
}
