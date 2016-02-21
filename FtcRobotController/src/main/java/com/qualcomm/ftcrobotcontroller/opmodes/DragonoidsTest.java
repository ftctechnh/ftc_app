package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class DragonoidsTest extends OpMode {
    private ColorSensor colorSensor;
    private OpticalDistanceSensor opticalDistanceSensor;
    @Override
    public void init() {
        DragonoidsGlobal.init(hardwareMap);
        colorSensor = hardwareMap.colorSensor.get("color");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("distance");
        // Change for testing
        colorSensor.enableLed(true);
    }
    @Override
    public void loop() {
        this.outputTelemetry();
    }
    private void outputTelemetry() {
        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Blue", colorSensor.blue());
        telemetry.addData("Light", opticalDistanceSensor.getLightDetected());
        telemetry.addData("Light raw", opticalDistanceSensor.getLightDetectedRaw());
        // Stop when > 0.1 on ODS
    }
    @Override
    public void stop() {
        // Stop all motors
        DragonoidsGlobal.stopAll();
        super.stop();
    }
}