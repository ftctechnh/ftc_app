/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.opmode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.lasarobotics.vision.android.Sensors;
import org.lasarobotics.vision.util.FPS;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * Initiates a VisionEnabledActivity
 */
public abstract class VisionEnabledActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static CameraBridgeViewBase openCVCamera;
    private TestableVisionOpMode opMode;
    private final BaseLoaderCallback openCVLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Woohoo!
                    openCVCamera.enableView();
                    opMode.init();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    protected VisionEnabledActivity() {

    }

    protected final void initializeVision(int framePreview, TestableVisionOpMode opMode) {
        openCVCamera = (CameraBridgeViewBase) findViewById(framePreview);
        openCVCamera.setVisibility(SurfaceView.VISIBLE);
        openCVCamera.setCvCameraViewListener(this);

        this.opMode = opMode;

        opMode.sensors = new Sensors();
        opMode.fps = new FPS();
        //FIXME this is the line that causes glitchiness
        TestableVisionOpMode.openCVCamera = (JavaCameraView) openCVCamera;
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        opMode.loop();
        opMode.fps.update();
        return opMode.frame(inputFrame.rgba(), inputFrame.gray());
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        opMode.width = width;
        opMode.height = height;
    }

    @Override
    public void onCameraViewStopped() {
        openCVCamera.disableView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (openCVCamera != null)
            openCVCamera.disableView();
        opMode.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openCVCamera != null)
            openCVCamera.disableView();
        opMode.stop();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (opMode == null)
            return;

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, openCVLoaderCallback);
            if (!success)
                Log.e("OpenCV", "Asynchronous initialization failed!");
            else
                Log.d("OpenCV", "Asynchronous initialization succeeded!");
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            openCVLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}