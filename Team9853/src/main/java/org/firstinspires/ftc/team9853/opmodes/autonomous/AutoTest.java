package org.firstinspires.ftc.team9853;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.chathamrobotics.common.utils.AutonomousRnB;

/**
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/3/2017
 */
@AutonomousRnB(name = "Auto Test")
public class AutoTest extends LinearOpMode {
    private Boolean isRedTeam;

    public AutoTest(boolean isRedTeam) {
        this.isRedTeam = isRedTeam;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addLine("Hello, World! I am " + (isRedTeam ? "Red" : "Blue"));
            telemetry.update();
        }
    }
}
