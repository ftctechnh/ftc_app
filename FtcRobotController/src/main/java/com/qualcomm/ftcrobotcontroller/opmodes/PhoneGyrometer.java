package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.ContactsContract;
import android.service.textservice.SpellCheckerService;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import java.util.ServiceConfigurationError;

/**
 * Created by subash on 11/27/2015.
 */
public class PhoneGyrometer implements SensorEventListener
{
    private static SensorManager manager;
    private static Sensor accelSensor;
    private static Sensor magFieldSensor;
    private static float[] accelFloats = {0.0f, 0.0f, 0.0f};
    private static float[] magFieldFloats = {0.0f, 0.0f, 0.0f};
    static float azimuth = 0.0f;
    static float pitch = 0.0f;
    static float roll = 0.0f;
    static HardwareMap hardwareMap;

    public PhoneGyrometer(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;
        onCreate(this.hardwareMap);
    }

    private void onCreate(HardwareMap hardwareMap)
    {
        manager = (SensorManager)hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magFieldSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        manager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI);
        manager.registerListener(this, magFieldSensor, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            accelFloats = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            magFieldFloats = event.values;
        }
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelFloats, magFieldFloats);
        float[] orientation = new float[3];
        SensorManager.getOrientation(R, orientation);
        azimuth = (float) Math.toDegrees(orientation[0]);
        pitch = (float) Math.toDegrees(orientation[1]);
        roll = (float) Math.toDegrees(orientation[2]);
    }

    public float getAzimuth()
    {
        return azimuth;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getRoll()
    {
        return roll;
    }

    public void onDestroy()
    {
        manager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
