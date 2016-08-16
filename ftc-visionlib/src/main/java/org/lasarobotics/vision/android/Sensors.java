/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.android;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;

import org.lasarobotics.vision.util.ScreenOrientation;
import org.lasarobotics.vision.util.Vector3;

/**
 * Contains methods for reading Android native sensors, other than the camera
 */
public final class Sensors implements SensorEventListener {

    private static final float PITCH_TOLERANCE = 20.0f;
    private static final float PITCH_TOLERANCE_HIGH = 45.0f;
    private static final float ROLL_MINIMUM = 0.0f;
    private static final int READ_SPEED = SensorManager.SENSOR_DELAY_NORMAL;
    private static float[] gravity = new float[3];
    private static float[] linear_acceleration = new float[3];
    private static float[] acceleration = new float[3];
    private static float[] geomagnetic = new float[3];
    private static boolean activated = false;
    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private final Sensor mMagneticField;
    private ScreenOrientation screenOrientation = null;

    /**
     * Instantiate a sensor reader class
     */
    public Sensors() {
        mSensorManager = (SensorManager) Util.getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        resume();
    }

    /**
     * Resume reading sensors after reading was stopped
     */
    public void resume() {
        activated = true;
        mSensorManager.registerListener(this, mAccelerometer, READ_SPEED);
        mSensorManager.registerListener(this, mMagneticField, READ_SPEED);
    }

    /**
     * Pause sensor reading
     */
    public void stop() {
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagneticField);
        activated = false;
    }

    /**
     * Get a gravity vector, which points towards the earth
     *
     * @return Gravity vector, the sum of which is roughly 9.81 m/s^2
     */
    public Vector3<Float> getGravityVector() {
        return new Vector3<>(gravity[0], gravity[1], gravity[2]);
    }

    /**
     * Get the current acceleration vector
     * <p/>
     * This vector includes the gravity vector; to eliminate gravitational noise,
     * consider the Linear Acceleration vector
     *
     * @return Acceleration vector, in m/s^2
     */
    public Vector3<Float> getAccelerationVector() {
        return new Vector3<>(acceleration[0], acceleration[1], acceleration[2]);
    }

    /**
     * Get the current linear (gravity-excluded) acceleration vector
     *
     * @return Linear acceleration in m/s^2
     */
    public Vector3<Float> getLinearAccelerationVector() {
        return new Vector3<>(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
    }

    /**
     * Get the vector pointing toward the current geomagnetic field
     *
     * @return The geomagnetic vector
     */
    public Vector3<Float> getGeomagneticVector() {
        return new Vector3<>(geomagnetic[0], geomagnetic[1], geomagnetic[2]);
    }

    /**
     * Test if we have enough sensor data to calculate a screen orientation
     *
     * @return True if enough data exists, false otherwise
     */
    public boolean hasOrientation() {
        return screenOrientation != null;
    }

    /**
     * Get the current actual screen orientation based on sensor data
     * This is independent from the activity screen orientation, which is the current visible
     * orientation. Even if the screen is locked, this orientation will be calculated.
     *
     * @return Actual, sensor-based screen orientation
     */
    public ScreenOrientation getScreenOrientation() {
        return screenOrientation != null ? screenOrientation : ScreenOrientation.LANDSCAPE;
    }

    /**
     * Get the activity's screen orientation, which can be locked by the activity
     *
     * @return The activity's screen orietnation
     */
    public ScreenOrientation getActivityScreenOrientation() {
        WindowManager windowManager = (WindowManager) Util.getContext().getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        return ScreenOrientation.getFromSurface(rotation);
    }

    /**
     * Get the natural orientation of the device. For larger phones it is often LANDSCAPE - for smaller, PORTRAIT.
     *
     * @return LANDSCAPE or PORTRAIT, based on the screen information
     */
    public ScreenOrientation getDeviceDefaultOrientation() {

        WindowManager windowManager = (WindowManager) Util.getContext().getSystemService(Context.WINDOW_SERVICE);

        Configuration config = Util.getContext().getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return ScreenOrientation.LANDSCAPE;
        } else {
            return ScreenOrientation.PORTRAIT;
        }
    }

    /**
     * Get the screen orientation compensation, in degrees, between the activity and the target screen orientation
     *
     * @return Sensor orientation - Activity orientation + Zero orientation (typically 90 degrees)
     */
    public double getScreenOrientationCompensation() {
        //BUGFIX reverse orientation for primary cameras
        return -getScreenOrientation().getAngle();
    }

    private void updateScreenOrientation() {
        float[] R = new float[9];
        float[] I = new float[9];
        SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
        float[] orientation = new float[3];
        SensorManager.getOrientation(R, orientation);

        //device rotation angle = pitch (first value) [clockwise from horizontal]

        double pitch = orientation[1] / 2 / Math.PI * 360.0;
        double roll = orientation[2] / 2 / Math.PI * 360.0;
        double azimuth = orientation[0] / 2 / Math.PI * 360.0;

        //If the phone is too close to the ground, don't update
        if (Math.abs(roll) <= ROLL_MINIMUM)
            return;

        ScreenOrientation current = screenOrientation;

        if (Math.abs(pitch) <= PITCH_TOLERANCE)
            if (roll > 0.0f)
                current = ScreenOrientation.LANDSCAPE_REVERSE;
            else
                current = ScreenOrientation.LANDSCAPE;
        else if (Math.abs(pitch) >= PITCH_TOLERANCE_HIGH)
            if (pitch > 0.0f)
                current = ScreenOrientation.PORTRAIT_REVERSE;
            else
                current = ScreenOrientation.PORTRAIT;

        screenOrientation = current;
    }

    /**
     * Event that fires when sensor data is updated
     *
     * @param event Event data
     */
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate
                final float alpha = 0.8f;

                acceleration[0] = event.values[0];
                acceleration[1] = event.values[1];
                acceleration[2] = event.values[2];

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];

                updateScreenOrientation();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic[0] = event.values[0];
                geomagnetic[1] = event.values[1];
                geomagnetic[2] = event.values[2];
                updateScreenOrientation();
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
