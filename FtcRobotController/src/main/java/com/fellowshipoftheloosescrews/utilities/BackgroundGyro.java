package com.fellowshipoftheloosescrews.utilities;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by FTC7123A on 8/31/2015.
 */
public class BackgroundGyro implements Runnable {
    GyroSensor sensor;

    private double offset;

    private Thread thread;
    private boolean isRunning = false;

    public BackgroundGyro(GyroSensor g)
    {
        sensor = g;
        calibrate(1);
    }

    public void calibrate(int numberOfCalibrationTests)
    {
        double totalTests = 0;

        for(int i = 0; i < numberOfCalibrationTests; i++)
        {
            totalTests += sensor.getRotation();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        offset = totalTests / numberOfCalibrationTests;
    }

    public void startHeading()
    {
        if(isRunning)
            return;

        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public void stopHeading()
    {
        isRunning = false;
    }

    public double getCalibratedRotation()
    {
        return sensor.getRotation() - offset;
    }

    public double getCurrentHeading()
    {
        return currentHeading;
    }

    // heading value
    private double currentHeading = 0;

    private int headingDelay = 20;

    @Override
    public void run() {
        double currentTime;
        double lastTime = System.nanoTime() / 1000000.0;
        while(isRunning)
        {
            currentTime = System.nanoTime() / 1000000.0;
            double deltaTime = currentTime - lastTime;

            currentHeading += getCalibratedRotation() * deltaTime / 1000.0;

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("Gyro reading", "" + getCalibratedRotation());

            lastTime = currentTime;
        }
    }
}
