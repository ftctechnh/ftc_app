package org.firstinspires.ftc.teamcode.Salsa.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

/**
 * Created by adityamavalankar on 11/12/18.
 */

@TeleOp(name="Sampling Test DogeCV")
public class TestSampling extends OpMode {

    Robot robot = new Robot();

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
