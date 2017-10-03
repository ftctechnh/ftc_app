package org.firstinspires.ftc.team9853.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.chathamrobotics.common.utils.AutonomousOpMode;
import org.chathamrobotics.common.utils.AutonomousRnB;
import org.chathamrobotics.common.utils.StoppedException;
import org.firstinspires.ftc.team9853.Robot9853;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/3/2017
 */
//@AutonomousRnB(name = "Auto Test")
@SuppressWarnings("unused")
public class AutoTest extends AutonomousOpMode<Robot9853> {
    private Boolean isRedTeam;

    public AutoTest(boolean isRedTeam) {
        super();
        this.isRedTeam = isRedTeam;
    }

    @Override
    public void run() throws StoppedException {
        waitForStart();

        while (opModeIsActive()) {
            robot.log.info("Hello, World! I am " + (isRedTeam ? "Red" : "Blue"));
            telemetry.update();
        }
    }
}
