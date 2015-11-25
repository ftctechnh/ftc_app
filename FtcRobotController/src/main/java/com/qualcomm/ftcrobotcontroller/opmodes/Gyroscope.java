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
    private float[] mGyroscope = {0.0f, 0.0f, 0.0f};
    private float[] mAcceleration = {0.0f, 0.0f, 0.0f};
    float azimuth = 0.0f;
    float pitch = 0.0f;
    float roll = 0.0f;

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
            boolean result = SensorManager.getRotationMatrix(R,I,mAcceleration,mGyroscope);
            if(result)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[0];
                pitch = orientation[1];
                roll = orientation[2];
            }
        }





        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGyroscope = event.values;
            if (mGyroscope != null) {
                gyroscopeVel[0] = mGyroscope[0];
                gyroscopeVel[1] = mGyroscope[1];
                gyroscopeVel[2] = mGyroscope[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}