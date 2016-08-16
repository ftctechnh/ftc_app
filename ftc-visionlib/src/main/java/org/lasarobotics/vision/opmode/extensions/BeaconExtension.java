/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.opmode.extensions;

import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.VisionOpMode;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Mat;

/**
 * Extension that supports finding and reading beacon color data
 */
public class BeaconExtension implements VisionExtension {
    private Beacon beacon;

    private Beacon.BeaconAnalysis analysis = new Beacon.BeaconAnalysis();

    /**
     * Get latest beacon analysis
     *
     * @return A Beacon.BeaconAnalysis struct
     */
    public Beacon.BeaconAnalysis getAnalysis() {
        return analysis;
    }

    /**
     * Get the currently used analysis method
     *
     * @return Analysis method
     */
    public Beacon.AnalysisMethod getAnalysisMethod() {
        return beacon.getAnalysisMethod();
    }

    /**
     * Set the analysis method to use for beacon analysis
     *
     * @param method Analysis method to use
     */
    public void setAnalysisMethod(Beacon.AnalysisMethod method) {
        beacon.setAnalysisMethod(method);
    }

    /**
     * Set color tolerance for red beacon detector
     *
     * @param tolerance A color tolerance value from -1 to 1, where 0 is unmodified, 1 is maximum
     *                  tolerance (more colors detect as red), -1 is minimum (fery vew colors detect
     *                  as red)
     */
    public void setColorToleranceRed(double tolerance) {
        beacon.setColorToleranceRed(tolerance);
    }

    /**
     * Set color tolerance for blue beacon detector
     *
     * @param tolerance A color tolerance value from -1 to 1, where 0 is unmodified, 1 is maximum
     *                  tolerance (more colors detect as blue), -1 is minimum (fery vew colors detect
     *                  as blue)
     */
    public void setColorToleranceBlue(double tolerance) {
        beacon.setColorToleranceBlue(tolerance);
    }

    /**
     * Set analysis bounds
     * Areas of the image outside of the bounded area will not be processed
     *
     * @param bounds A rectangle containing the boundary
     */
    public void setAnalysisBounds(Rectangle bounds) {
        beacon.setAnalysisBounds(bounds);
    }

    /**
     * Enable debug drawing. Use this on testing apps only, not the robot controller.
     */
    public void enableDebug() {
        beacon.enableDebug();
    }

    /**
     * Disable debug drawing (default). Use this on the robot controller.
     */
    public void disableDebug() {
        beacon.disableDebug();
    }

    @Override
    public void init(VisionOpMode opmode) {
        //Initialize all detectors here
        beacon = new Beacon();
    }

    @Override
    public void loop(VisionOpMode opmode) {

    }

    @Override
    public Mat frame(VisionOpMode opmode, Mat rgba, Mat gray) {
        try {
            //Get screen orientation data
            ScreenOrientation orientation = ScreenOrientation.getFromAngle(
                    VisionOpMode.rotation.getRotationCompensationAngle());

            //Get color analysis
            this.analysis = beacon.analyzeFrame(rgba, gray, orientation);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rgba;
    }

    @Override
    public void stop(VisionOpMode opmode) {

    }
}