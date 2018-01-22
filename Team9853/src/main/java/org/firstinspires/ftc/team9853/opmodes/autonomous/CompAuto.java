package org.firstinspires.ftc.team9853.opmodes.autonomous;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import org.chathamrobotics.common.RGBAColor;
import org.chathamrobotics.common.opmode.AutonomousRnB;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.chathamrobotics.common.robot.RobotFace;
import org.firstinspires.ftc.team9853.opmodes.Autonomous9853;

/**
 * Hits the jewel
 */
@SuppressWarnings("WeakerAccess")
@AutonomousRnB
public class CompAuto extends Autonomous9853 {
    protected boolean isHit;

    public CompAuto(boolean isRedTeam) {
        super(isRedTeam);
    }

    @Override
    public void run() throws InterruptedException, StoppedException {
        robot.start();
        robot.driver.setFront(RobotFace.BACK);

        robot.jewelDisplacer.drop();
        debug();

        Thread.sleep(1000);

        int color = robot.jewelDisplacer.getColor();

        if ((isRedTeam() && isRed(color)) || (! isRedTeam() && isBlue(color)))
            hitJewel(-1);
        else if ((isRedTeam() && isBlue(color)) || (! isRedTeam() && isRed(color)))
            hitJewel(1);
    }

    private void hitJewel(double power) throws InterruptedException, StoppedException {
        robot.driver.setDrivePower(0, 0, power);
        debug();

        Thread.sleep(1000/4);
        robot.driver.stop();

        robot.jewelDisplacer.raise();
        Thread.sleep(1000);

        robot.driver.setDrivePower(0, 0, -power);
        debug();

        Thread.sleep(1000/4);
        robot.driver.stop();

        isHit = true;
    }

    private boolean isRed(int color) {
        return color > 9;
    }

    private boolean isBlue(int color) {
        return color < 4 && color > 1;
    }

    private boolean isTeamColor(int color) {
        if (isRedTeam()) return isRed(color);

        return isBlue(color);
    }
}
