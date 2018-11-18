package org.firstinspires.ftc.teamcode.Salsa.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.OpModes.SalsaOpMode;

/**
 * Created by adityamavalankar on 11/12/18.
 */

@TeleOp(name="Sampling Test DogeCV")
public class TestSampling extends SalsaOpMode {

    /**
     * Here is an implementation of the SalsaOpMode being used in vision. If you'd like to know how
     * it works @see SalsaLinearOpMode, and @SamplingDetector in the Vision folder of the Team Salsa code
     */

    @Override
    public void init() {
        robot.samplingDetector.initVision(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Order", robot.samplingDetector.getSamplingOrder());
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.samplingDetector.disableVision();
    }
}
