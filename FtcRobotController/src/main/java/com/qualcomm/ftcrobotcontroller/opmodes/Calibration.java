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
    UltrasonicSensor ultrasonicSensor;


    @Override
    public void init() {
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");

    }

    public void loop() {
        double reflectance = opticalDistanceSensor.getLightDetected();
        double distance = ultrasonicSensor.getUltrasonicLevel();

        telemetry.addData("Reflectance Value", reflectance);
        telemetry.addData("Ultrasonic Value", distance);
    }

}
