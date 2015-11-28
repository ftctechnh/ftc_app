package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by tdoylend on 2015-11-28.
 *
 * Change log:
 * 1.0.0 - First version.
 */
public class PacmanBotManual3000 extends PacmanBotHardwareBase {

    VersionNumber version = new VersionNumber(1,0,0);

    @Override
    public void init() {
        telemetry.addData("Program","Manual Drive 3000");
        telemetry.addData("Version",version.string());
        telemetry.addData("HWB Version", hwbVersion.string());

        setupHardware();
    }

    @Override
    public void loop() {
        checkUsers();

        setFinalRateMultiplier(gamepad.left_stick_button ? 1.0 : 0.25);

        double drive_rate = -gamepad.left_stick_y;
        double turn_rate  = gamepad.right_stick_x;

        drive(drive_rate,turn_rate);

        setBrush(threeWay(gamepad.a,gamepad.b));
        setBelt(threeWay(gamepad.dpad_left,gamepad.dpad_right));

        if (gamepad.x) releaseHook();
    }
}
