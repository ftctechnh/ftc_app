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
    private Sensor acceleration;
    private float[] mGyroscope = {0.0f, 0.0f, 0.0f};
    private float[] mAcceleration = {0.0f, 0.0f, 0.0f};
    double azimuth = 0.0f;
    double pitch = 0.0f;
    double roll = 0.0f;

    @Override
    public void init()
    {
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        acceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void start()
    {
        mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, acceleration, SensorManager.SENSOR_DELAY_UI);
    }

    public void loop()
    {
        telemetry.addData("azimuth ",  (azimuth));
        telemetry.addData("pitch ",  (pitch));
        telemetry.addData("roll ",  (roll));
    }

    public void stop()
    {
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
    {
        mAcceleration = event.values;
    }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            mGyroscope = event.values;
        }
        if(mAcceleration != null && mGyroscope != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];
            telemetry.addData("stuffs", mAcceleration[0]);
            boolean result = SensorManager.getRotationMatrix(R,null,mAcceleration,mGyroscope);

            if(result)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = Math.toDegrees(orientation[0]);
                pitch = Math.toDegrees(orientation[1]);
                roll = Math.toDegrees(orientation[2]);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}