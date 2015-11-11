package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaitlin 11/10/15.
 */
public class Gyroscope extends OpMode implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor gyroscope;
    private float[] gyroscopeVel = {0.0f,0.0f,0.0f};
    private float[] mGyroscope;


    public Gyroscope()
    {}

    @Override
    public void init()
    {
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void start()
    {
        mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    public void loop()
    {
        telemetry.addData("x-axis",  gyroscopeVel[0]);
        telemetry.addData("y-axis",  gyroscopeVel[1]);
        telemetry.addData("z-axis", gyroscopeVel[2]);
    }

    public void stop()
    {
        mSensorManager.unregisterListener(this);
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGyroscope = event.values;
            if (mGyroscope != null) {
                gyroscopeVel[0] = mGyroscope[0]; // Acceleration minus Gx on the x-axis
                gyroscopeVel[1] = mGyroscope[1]; // Acceleration minus Gy on the y-axis
                gyroscopeVel[2] = mGyroscope[2]; // Acceleration minus Gz on the z-axis
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}