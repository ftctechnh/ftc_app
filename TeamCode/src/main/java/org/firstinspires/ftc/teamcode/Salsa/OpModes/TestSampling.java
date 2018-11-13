package org.firstinspires.ftc.teamcode.Salsa.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Vision.SamplingDetector;

/**
 * Created by adityamavalankar on 11/12/18.
 */

@TeleOp(name="Sampling Test DogeCV")
public class TestSampling extends OpMode {

    SamplingDetector detector = null;

    @Override
    public void init() {
        detector = new SamplingDetector();
        detector.initVision(hardwareMap);
        detector.beginTracking();
    }

    @Override
    public void loop() {
        telemetry.addData("Order", detector.getSamplingOrder());
        telemetry.update();
    }

    @Override
    public void stop() {
        detector.disableVision();
    }
}
