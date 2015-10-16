package com.fellowshipoftheloosescrews.testing;

import com.fellowshipoftheloosescrews.utilities.BackgroundGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by FTC7123A on 8/31/2015.
 */
public class GyroTesting extends OpMode {

    BackgroundGyro sensor;

    @Override
    public void init() {
        sensor = new BackgroundGyro(hardwareMap.gyroSensor.get("gyro1"));
        sensor.startHeading();
    }

    @Override
    public void loop() {
        telemetry.addData("gyro reading", sensor.getCalibratedRotation());
        telemetry.addData("gyro heading", sensor.getCurrentHeading());
    }
}
