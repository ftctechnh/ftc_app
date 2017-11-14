package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;

/**
 * Created by Noah on 11/9/2017.
 */

@Autonomous(name="Vuforia Ball Demo", group ="Demo")
public class VuforiaBallDemo extends VuforiaBallLib {

    @Override
    public void init() {
        initVuforia(true);
    }

    @Override
    public void start() {
        super.startTracking();
    }

    @Override
    public void loop() {
        telemetry.addData("Tracking", isTracking().toString());
        telemetry.addData("Ball Color", getBallColor());
    }

    @Override
    public void stop() {
        super.stopVuforia();
    }
}
