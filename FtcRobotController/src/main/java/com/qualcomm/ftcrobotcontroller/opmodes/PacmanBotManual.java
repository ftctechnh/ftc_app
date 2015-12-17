package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by tdoylend on 2015-12-17.
 *
 * Change log:
 * 1.0.0 - First version.
 */

public class PacmanBotManual extends PacmanBotHWB2 {

    VersionNumber version = new VersionNumber(1,0,0);

    public void init() {
        telemetry.addData("OpMode","PacmanBotManual");
        telemetry.addData("Version",version.string());
        telemetry.addData("HWB Version", hwbVersion.string());

        setupHardware();
    }

    public void loop() {
        update();

        drive(-gamepad1.left_stick_y,gamepad1.right_stick_x);
    }
}
