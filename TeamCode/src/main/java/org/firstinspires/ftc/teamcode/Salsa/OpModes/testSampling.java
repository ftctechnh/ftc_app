package org.firstinspires.ftc.teamcode.Salsa.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Salsa.Vision.SamplingDetector;

/**
 * Created by adityamavalankar on 11/12/18.
 */

public class testSampling extends OpMode {

    SamplingDetector sampler = new SamplingDetector();

    @Override
    public void init() {

        sampler.initVision(hardwareMap);
    }

    @Override
    public void loop() {

        telemetry.addData("Current Order", sampler.getSamplingOrder());
        telemetry.addData("Previous Order", sampler.getLastOrder());
        telemetry.update();
    }

    @Override
    public void stop() {
        sampler.disableVision();
    }
}
