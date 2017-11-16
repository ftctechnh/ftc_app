package org.firstinspires.ftc.team9853.opmodes.autonomous;


import org.chathamrobotics.common.opmode.AutonomousTemplate;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.firstinspires.ftc.team9853.Robot9853;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/3/2017
 */
//@AutonomousRnB(name = "Auto T est")
@SuppressWarnings("unused")
public class AutoTest extends AutonomousTemplate<Robot9853> {
    private Boolean isRedTeam;

    public AutoTest(boolean isRedTeam) {
    super(true); //super(); FIXME: it wont compile
        this.isRedTeam = isRedTeam;
    }

    @Override
    public void initialize() {
        robot = Robot9853.build(this);
    }

    @Override
    public void run() throws StoppedException {
        while (opModeIsActive()) {
            robot.log.info("Hello, World! I am " + (isRedTeam ? "Red" : "Blue"));
            telemetry.update();
        }
    }
}
