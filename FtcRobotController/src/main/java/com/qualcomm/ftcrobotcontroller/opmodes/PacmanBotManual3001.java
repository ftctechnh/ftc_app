package com.qualcomm.ftcrobotcontroller.opmodes;

import junit.runner.Version;

/**
 * Created by tdoylend on 2015-12-12.
 */
public class PacmanBotManual3001 extends PacmanBotHWB2 {

    final static VersionNumber version = new VersionNumber(1,0,0);

    public void init() {
        telemetry.addData("Program","Manual Drive 3001");
        telemetry.addData("Version",version.string());
        telemetry.addData("HWB Version", hwbVersion.string());

        setupHardware();
    }

    public void loop() {

        drive(-gamepad1.left_stick_y,gamepad1.right_stick_x);
    }
}
