/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to Brendan Hollaway (Venom) for parts of the algorithm.
 */
package org.lasarobotics.vision.opmode.extensions;

import android.hardware.Camera;
import android.util.Log;

import org.lasarobotics.vision.opmode.VisionOpMode;
import org.lasarobotics.vision.util.MathUtil;
import org.opencv.core.Mat;

/**
 * Camera control extension
 * <p/>
 * Allows manual control of white balance, exposure, etc.
 */
@SuppressWarnings("deprecation")
public class CameraControlExtension implements VisionExtension {

    private boolean paramsSet = false;
    private ColorTemperature colorTemp = ColorTemperature.AUTO;
    private int expoComp = 0;
    private int minExpo = 0;
    private int maxExpo = 0;
    private boolean autoExpoComp = true;

    /**
     * Enable automatic exposure compensation
     */
    public void setAutoExposureCompensation() {
        this.expoComp = 0;
        this.autoExpoComp = true;
        paramsSet = false;
    }

    /**
     * Set a manual exposure compensation value
     * <p/>
     * Use getMinExposureCompensation() and getMaxExposureCompensation()
     * to figure this out. If using these two variables, be sure to run this method
     * AFTER init to ensure they are non-zero.
     * <p/>
     * Typically, this is a value from -12 to 12, with 12 being bright.
     *
     * @param expoComp Exposure compensation value
     */
    public void setManualExposureCompensation(int expoComp) {
        this.expoComp = expoComp;
        this.autoExpoComp = false;
        paramsSet = false;
    }

    /**
     * Set a target color temperature
     *
     * @param colorTemp Target color temperature
     */
    public void setColorTemperature(ColorTemperature colorTemp) {
        this.colorTemp = colorTemp;
        paramsSet = false;
    }

    /**
     * Get a minimum exposure compensation value, typically -12.
     * <p/>
     * Be sure to run this method AFTER init to ensure it is non-zero.
     *
     * @return Minimum exposure compensation value
     */
    public int getMinExposureCompensation() {
        return minExpo;
    }

    /**
     * Get a maximum exposure compensation value, typically 12.
     * <p/>
     * Be sure to run this method AFTER init to ensure it is non-zero.
     *
     * @return Maximum exposure compensation value
     */
    public int getMaxExposureCompensation() {
        return maxExpo;
    }

    /**
     * Get color temperature setting
     *
     * @return Color temperature
     */
    public ColorTemperature getColorTemp() {
        return colorTemp;
    }

    /**
     * Get current exposure compensation
     *
     * @return Current exposure compensation or zero if not present
     */
    public int getExposureCompensation() {
        return expoComp;
    }

    /**
     * Gets whether exposure compensation is automated
     *
     * @return True if exposure compensation is automatic, false if manual
     */
    public boolean isAutomaticExposureCompensation() {
        return autoExpoComp;
    }

    @Override
    public void init(VisionOpMode opmode) {
        paramsSet = false;
        colorTemp = ColorTemperature.AUTO;
        expoComp = 0;
        autoExpoComp = true;
    }

    @Override
    public void loop(VisionOpMode opmode) {

    }

    @Override
    @SuppressWarnings("AccessStaticViaInstance")
    public Mat frame(VisionOpMode opmode, Mat rgba, Mat gray) {
        if (opmode.openCVCamera == null)
            paramsSet = false;
        if (paramsSet || opmode.openCVCamera == null) return rgba;

        Camera.Parameters p = opmode.openCVCamera.getCamera().getParameters();

        //Get camera info
        this.minExpo = p.getMinExposureCompensation();
        this.maxExpo = p.getMaxExposureCompensation();
        expoComp = (int) MathUtil.coerce(minExpo, maxExpo, expoComp);

        //Set white balance
        p.setWhiteBalance(colorTemp.s);
        if (p.isAutoWhiteBalanceLockSupported())
            p.setAutoWhiteBalanceLock(colorTemp.lock);
        else
            Log.w("Vision", "Manual white balance not supported.");

        //Set exposure compensation
        if (!autoExpoComp)
            p.setExposureCompensation(expoComp);
        if (p.isAutoExposureLockSupported())
            p.setAutoExposureLock(!autoExpoComp);
        else
            Log.w("Vision", "Manual exposure compensation not supported.");

        //Update camera parameters
        try {
            opmode.openCVCamera.getCamera().setParameters(p);
        } catch (RuntimeException e) {
            //Sometimes, we fail to set the parameters
            e.printStackTrace();
            paramsSet = false;
            return rgba;
        }
        paramsSet = true;

        return rgba;
    }

    @SuppressWarnings("AccessStaticViaInstance")
    @Override
    public void stop(VisionOpMode opmode) {
        if (opmode.openCVCamera == null)
            return;

        Camera.Parameters p = opmode.openCVCamera.getCamera().getParameters();

        if (p.isAutoWhiteBalanceLockSupported())
            p.setAutoWhiteBalanceLock(false);
        if (p.isAutoExposureLockSupported())
            p.setAutoExposureLock(false);

        //Update camera parameters
        try {
            opmode.openCVCamera.getCamera().setParameters(p);
        } catch (RuntimeException e) {
            //Sometimes, we fail to set the parameters
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    /**
     * Approximate color temperature
     */
    public enum ColorTemperature {
        AUTO(Camera.Parameters.WHITE_BALANCE_AUTO, false),
        K10000_TWILIGHT(Camera.Parameters.WHITE_BALANCE_TWILIGHT, 10000),
        K8000_SHADE(Camera.Parameters.WHITE_BALANCE_SHADE, 8000),
        K3000_WARM_FLOURESCENT(Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT, 3000),
        K5000_FLOURESCENT(Camera.Parameters.WHITE_BALANCE_FLUORESCENT, 5000),
        K2400_INCANDESCENT(Camera.Parameters.WHITE_BALANCE_INCANDESCENT, 2400),
        K6500_DAYLIGHT(Camera.Parameters.WHITE_BALANCE_DAYLIGHT, 6500),
        K4000_CLOUDY_DAYLIGHT(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT, 4000);

        final String s;
        final boolean lock;
        final int kelvin;

        ColorTemperature(String s, int kelvin) {
            this.s = s;
            this.lock = true;
            this.kelvin = kelvin;
        }

        ColorTemperature(String s, boolean lock) {
            this.s = s;
            this.lock = lock;
            this.kelvin = 0;
        }

        public int getApproxTemperatureKelvin() {
            return kelvin;
        }

        public boolean isAutomatic() {
            return this.kelvin == 0;
        }
    }
}
