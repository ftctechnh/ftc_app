package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class ExampleTestReflectance extends OpMode {
    OpticalDistanceSensor opticalDistanceSensor;

    @Override
    public void init() {
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
    }

    @Override
    public void loop() {
        double reflectance = opticalDistanceSensor.getLightDetected();
        telemetry.addData("Reflectance", reflectance);
    }

}




