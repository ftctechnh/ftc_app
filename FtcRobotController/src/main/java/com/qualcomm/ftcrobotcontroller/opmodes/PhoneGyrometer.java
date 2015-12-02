package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;

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
    private static ArrayList <Double> azimuthArr = new ArrayList<Double>();
    private static ArrayList <Double> pitchArr = new ArrayList<Double>();
    private static ArrayList <Double> rollArr = new ArrayList<Double>();
    static float azimuth = 0.0f;
    static float pitch = 0.0f;
    static float roll = 0.0f;
    static HardwareMap hardwareMap;

    public PhoneGyrometer(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;
        onCreate(this.hardwareMap);
        for(int i = 0; i < 7; i++)
        {
            azimuthArr.add(0.0);
            pitchArr.add(0.0);
            rollArr.add(0.0);
        }
    }

    private void onCreate(HardwareMap hardwareMap)
    {
        manager = (SensorManager)hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magFieldSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        manager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI);
        manager.registerListener(this, magFieldSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private double aveArr(ArrayList<Double> arr)
    {
        double ave = 0.0f;
        for(Double temp:arr)
        {
            ave += temp;
        }
        return (ave/arr.size());
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
        azimuthArr.add(new Double(orientation[0]));
        pitchArr.add(new Double(orientation[0]));
        rollArr.add(new Double(orientation[0]));
        azimuthArr.remove(0);
        pitchArr.remove(0);
        rollArr.remove(0);
        azimuth = (float) Math.toDegrees(aveArr(azimuthArr));
        pitch = (float) Math.toDegrees(aveArr(pitchArr));
        roll = (float) Math.toDegrees(aveArr(rollArr));
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
