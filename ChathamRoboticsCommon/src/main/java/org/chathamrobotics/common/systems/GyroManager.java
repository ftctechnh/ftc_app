package org.chathamrobotics.common.systems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by carsonstorm on 11/17/2017.
 */

public class GyroManager {
    private static final int GYRO_MIN_THRESHOLD = 5;
    private static final double MIN_ROTATE_POWER = 0.15;

    private GyroSensor gyro;
    private double initialHeading = 0;

    public GyroManager(GyroSensor gyro) {
        this.gyro = gyro;
    }

    public void init() {
        gyro.calibrate();
    }

    public boolean isInitialized() {
        return gyro.isCalibrating();
    }

    public void start() throws InterruptedException {
        while (! isInitialized()) Thread.sleep(10);

        initialHeading = gyro.getHeading();
    }

    public void rotate(double targetHeading, Driver driver) {
        targetHeading = (targetHeading + initialHeading) % 360;
        double currentHeading = gyro.getHeading();

        double diff = getAngleDiff(currentHeading, targetHeading);

        while (diff > GYRO_MIN_THRESHOLD) {
            driver.rotate(diff / 180);
        }
    }

    private double getAngleDiff(double currentHeading, double targetHeading) {
        double diff = currentHeading - targetHeading;

        if (Math.abs(diff) > 180)
            diff += (currentHeading > 180 ? -360 : 360);

        return diff;
    }
}
