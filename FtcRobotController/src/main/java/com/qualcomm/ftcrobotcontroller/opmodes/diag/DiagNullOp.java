package com.qualcomm.ftcrobotcontroller.opmodes.diag;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by tdoylend on 2016-01-02.
 *
 * This diagnostic does absolutely nothing.
 */
public class DiagNullOp extends OpMode {

    @Override
    public void init() {
        telemetry.addData("Diagnostic","Null Operation");
    }

    @Override
    public void loop() {
        telemetry.addData("Diagnostic","Null Operation");
        telemetry.addData("Doing Nothing","As Desired");
    }
}
