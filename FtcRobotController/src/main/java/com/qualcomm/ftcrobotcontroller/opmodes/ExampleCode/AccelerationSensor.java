package com.qualcomm.ftcrobotcontroller.opmodes.ExampleCode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Code written by Mike Silversides (username- acharraggi)
 * Created by Kaitlin, Andrew, and Jermey on 11/1/15.
 * Edited by Anya and Kaitlin on 11/3/15
 */
public class AccelerationSensor extends OpMode implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private float[] acceleration = {0.0f,0.0f,0.0f};
    private float[] mAccelerometer;



    public AccelerationSensor(){}

    @Override
    public void init()
    {
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void start()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void loop()
    {
        telemetry.addData("x-axis",  acceleration[0]);
        telemetry.addData("y-axis",  acceleration[1]);
        telemetry.addData("z-axis", acceleration[2]);
    }

    public void stop()
    {
        mSensorManager.unregisterListener(this);
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometer = event.values;
            if (mAccelerometer != null) {
                acceleration[0] = mAccelerometer[0]; // Acceleration minus Gx on the x-axis
                acceleration[1] = mAccelerometer[1]; // Acceleration minus Gy on the y-axis
                acceleration[2] = mAccelerometer[2]; // Acceleration minus Gz on the z-axis
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}
