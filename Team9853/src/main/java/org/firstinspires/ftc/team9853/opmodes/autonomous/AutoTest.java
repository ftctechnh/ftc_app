package org.firstinspires.ftc.team9853.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.chathamrobotics.common.utils.AutonomousRnB;
import org.firstinspires.ftc.team9853.Robot9853;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/3/2017
 */
@AutonomousRnB(name = "Auto Test")
@SuppressWarnings("unused")
public class AutoTest extends LinearOpMode {
    private Boolean isRedTeam;
    private Robot9853 robot;

    public AutoTest(boolean isRedTeam) {
        this.isRedTeam = isRedTeam;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot9853(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addLine("Hello, World! I am " + (isRedTeam ? "Red" : "Blue"));
            telemetry.update();
        }
    }
}
