/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.opmode.extensions;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.util.Log;

import org.lasarobotics.vision.android.Sensors;
import org.lasarobotics.vision.android.Util;
import org.lasarobotics.vision.image.Transform;
import org.lasarobotics.vision.opmode.VisionOpMode;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Mat;

/**
 * Implements image rotation correction to ensure the camera is facing the correct direction
 */
public class ImageRotationExtension implements VisionExtension {

    private final Sensors sensors = new Sensors();
    private boolean isInverted = false;
    private ScreenOrientation zeroOrientation = ScreenOrientation.LANDSCAPE;

    /**
     * Set the zero, or default, orientation for the camera
     * <p/>
     * Typically this is LANDSCAPE, but certain phone models may need this set to LANDSCAPE_REVERSE
     * if you are seeing the image as upside down
     *
     * @param zeroOrientation Zero, or default, orientation, typically LANDSCAPE
     */
    public void setZeroOrientation(ScreenOrientation zeroOrientation) {
        this.zeroOrientation = zeroOrientation;
    }

    /**
     * Enable Auto Rotate in Android settings
     * <p/>
     * This will revert to system setting on app exit.
     */
    public void enableAutoRotate() {
        setAutoRotateState(true);
    }

    /**
     * Disable Auto Rotate in Android settings
     * <p/>
     * This will revert to system setting on app exit.
     */
    public void disableAutoRotate() {
        setAutoRotateState(false);
    }

    private void setAutoRotateState(boolean enabled) {
        Settings.System.putInt(Util.getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    /**
     * Allow the screen to rotate freely
     * <p/>
     * This will also allow the screen to rotate into a PORTRAIT_REVERSE position which is usually
     * disabled by the Android system. Please use this method even when you want the auto rotate
     * behavior.
     */
    public void setActivityOrientationAutoRotate() {
        setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    /**
     * Set a fixed orientation for the app
     *
     * @param orientation Fixed screen orientation
     */
    public void setActivityOrientationFixed(ScreenOrientation orientation) {
        switch (orientation) {
            case LANDSCAPE:
            default:
                setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case PORTRAIT:
                setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case LANDSCAPE_REVERSE:
                setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case PORTRAIT_REVERSE:
                setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
        }
    }

    private void setActivityOrientation(int state) {
        try {
            Activity activity = Util.getActivity();
            activity.setRequestedOrientation(state);
        } catch (IllegalArgumentException e) {
            Log.e("ScreenOrientation", "Looks like screen orientation changed and the app crashed!\r\n" +
                    "It's likely you are using an incompatible Activity or a TestableVisionOpMode.\r\n" +
                    "Refrain from setting screen orientation settings to fix this issue.");
        }
    }

    /**
     * Get the screen orientation of the current activity
     * This is the native orientation of the display
     * Some rotations, including upside down, are natively disabled in Android
     * <p/>
     * Do not use this to figure out the current phone orientation as Android can lock the screen
     * orientation and therefore force your program into one orientation.
     *
     * @return Screen orientation as reported by the current activity
     */
    public ScreenOrientation getScreenOrientationDisplay() {
        return sensors.getActivityScreenOrientation();
    }

    /**
     * Get the screen orientation as returned by Android sensors
     * Use getRotationCompensationAngle() instead if you want to correct for the screen rotation
     * for purposes such as drawing to the screen.
     * If all you need is to figure out which way the phone is facing, you can use this method.
     *
     * @return Screen orientation as reported by Android sensors
     */
    public ScreenOrientation getScreenOrientationActual() {
        return sensors.getScreenOrientation();
    }

    /**
     * Get rotation compensation angle as reported by fusing data from the Android sensors and
     * the native Android drawing API. Use this when you need to figure out which way you need to
     * draw onto the screen.
     * <p/>
     * This is a compensation between the activity orientation and the actual phone orientation.
     * If you need to get the actual phone orientation alone then use getScreenOrientationActual().
     *
     * @return Fused angle compensating for the difference between the actual orientation and the
     * Android API drawing orientation.
     */
    public double getRotationCompensationAngle() {
        return (isInverted ? -1 : 1) * ScreenOrientation.getFromAngle(sensors.getScreenOrientationCompensation() + zeroOrientation.getAngle()).getAngle();
    }

    private double getRotationCompensationAngleUnbiased() {
        return (isInverted ? -1 : 1) * sensors.getScreenOrientationCompensation();
    }

    /**
     * Get rotation compensation as reported by fusing data from the Android sensors and
     * the native Android drawing API. Use this when you need to figure out which way you need to
     * draw onto the screen.
     * <p/>
     * This is a compensation between the activity orientation and the actual phone orientation.
     * If you need to get the actual phone orientation alone then use getScreenOrientationActual().
     *
     * @return Fused orientation compensating for the difference between the actual orientation and the
     * Android API drawing orientation.
     */
    public ScreenOrientation getRotationCompensation() {
        return ScreenOrientation.getFromAngle(getRotationCompensationAngle());
    }

    /**
     * Set whether the direction of rotation should be inverted.
     * This may be necessary when using the inner camera
     *
     * @param inverted True to rotate counterclockwise, false for clockwise
     */
    public void setIsUsingSecondaryCamera(boolean inverted) {
        isInverted = inverted;
    }

    /**
     * Returns whether rotation is inverted
     *
     * @return True is rotating counterclockwise, false otherwise
     */
    public boolean isRotationInverted() {
        return isInverted;
    }


    @Override
    public void init(VisionOpMode opmode) {
        sensors.resume();
    }

    @Override
    public void loop(VisionOpMode opmode) {

    }

    @Override
    public Mat frame(VisionOpMode opmode, Mat rgba, Mat gray) {
        if (isInverted)
            Transform.flip(rgba, Transform.FlipType.FLIP_ACROSS_X);
        if (zeroOrientation != ScreenOrientation.LANDSCAPE)
            Transform.rotate(rgba, zeroOrientation.getAngle());
        return rgba;
    }

    @Override
    public void stop(VisionOpMode opmode) {
        sensors.stop();
    }
}
