package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Accelorometer implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    public HardwareMap hardwareMap = new HardwareMap();
    private float[] acceleration = {0.0f,0.0f,0.0f};    // SI units (m/s^2)
    //values[0]: Acceleration minus Gx on the x-axis
    //values[1]: Acceleration minus Gy on the y-axis
    //values[2]: Acceleration minus Gz on the z-axis

    private float[] mAccelerometer;       // latest sensor values
    // see http://developer.android.com/reference/android/hardware/SensorEvent.html#values
    // for example that ends up compensating for gravity.
    // Probably better to use TYPE_LINEAR_ACCELERATION instead, but maybe the raw values
    // could be useful.

    /*
    * Constructor
    */
    public Accelorometer() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
    */
    public void init() {
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        acceleration[0] = 0.0f;
        acceleration[1] = 0.0f;
        acceleration[2] = 0.0f;
    }


    public void start() {

        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */

    /**
     * get the acceleration in the xaxis
     * @return acceleration
     */
    public float getXAxis() {
       return acceleration[0];
    }
    /**
     * get the acceleration in the yaxis
     * @return acceleration
     */
    public float getYAxis() {
        return acceleration[1];
    }
    /**
     * get the acceleration in the Zaxis
     * @return acceleration
     */
    public float getZAxis() {
        return acceleration[2];
    }
    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not sure if needed, placeholder just in case
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
}
