package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Derek on 9/26/2015.
 */
public class OpticalTelemetry extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    OpticalDistanceSensor opticalSensor;

    @Override
    public void init() {



        motorRight = hardwareMap.dcMotor.get("motor_1");
        motorLeft = hardwareMap.dcMotor.get("motor_2");
        opticalSensor = hardwareMap.opticalDistanceSensor.get("optical_sensor");
    }

    @Override
    public void loop() {

        int light = opticalSensor.getLightDetectedRaw();

        telemetry.addData("opticalsense", "opticalsense: " + String.format("%d", light));
    }
}

