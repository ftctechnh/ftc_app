package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Jerry on 10/10/2015.
 */


public class Calibration extends OpMode {

    OpticalDistanceSensor opticalDistanceSensor;


    @Override
    public void init() {
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");

    }

    public void loop() {
        double reflectance = opticalDistanceSensor.getLightDetected();

        telemetry.addData("Reflectance Value", reflectance);
    }

}
