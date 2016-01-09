package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This is a template for writing opmodes with Treebeard.
 *
 * Robot Type: CutieBot
 * Config File: CutieBot
 */

public class OpModeTemplate extends HardwareBaseTemplate {

    public String version = "1.0.0";

    @Override
    public void init() {
        telemetry.addData("Program", "Manual Drive");
        telemetry.addData("Robot Type", "CutieBot");
        telemetry.addData("Config File","CutieBot");
        telemetry.addData("Opmode Version",version);
        telemetry.addData("HWB Version",hwbVersion);

        setupHardware();
    }

    @Override
    public void loop() {
        //Gamepad Y values are -1 at TOP and 1 at BOTTOM. Therefore, Y values from them
        //need to be negated before they go into any of the drive routines.

        double driveRate = -gamepad1.left_stick_y; //Negate the Y as mentioned above
        double turnRate  = gamepad1.right_stick_x;

        drive.driveStd(driveRate,turnRate); //Drive as desired!
    }
}
