package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.drive.Simplex;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This is a template for use when writing hardware bases.
 *
 * Robot Type: CutieBot
 * Robot Version: 1.0
 * Config File: CutieBot
 *
 * Change log:
 * 1.0.0 - First version.
 */

public class HardwareBaseTemplate extends OpMode {

    public String hwbVersion = "1.0.0";

    @Override
    public void loop() {}
    @Override
    public void init() {}

    DcMotor left;
    DcMotor right;

    Simplex drive;

    public void setupHardware() {
        //Everyone's old friend, setupHardware!
        left = hardwareMap.dcMotor.get("left");
        right= hardwareMap.dcMotor.get("right");

        drive = new Simplex(left,right);
    }
}
