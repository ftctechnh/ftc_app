/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */

package org.lasarobotics.vision.opmode;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.android.Sensors;
import org.lasarobotics.vision.util.FPS;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * Core OpMode class containing most OpenCV functionality
 */
abstract class VisionOpModeCore extends OpMode implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final int initialMaxSize = 1200;
    public static JavaCameraView openCVCamera;
    private static boolean initialized = false;
    private static boolean openCVInitialized = false;
    public int width, height;
    public FPS fps;
    public Sensors sensors;

    public VisionOpModeCore() {
        initialized = false;
        openCVCamera = null;
    }

    boolean isInitialized() {
        return initialized;
    }

    private void error(String message) {
        Log.e("FTCVision", message);
        telemetry.addData("Vision Status", message);
    }

    /**
     * Set the camera to use
     * This method may fail if the camera is locked.
     *
     * @param camera Camera to use
     */
    public void setCamera(Cameras camera) {
        if (openCVCamera == null)
            return;
        openCVCamera.disableView();
        if (initialized) openCVCamera.disconnectCamera();
        openCVCamera.setCameraIndex(camera.getID());
        if (initialized)
            if (!openCVCamera.connectCamera(width, height))
                error("Could not initialize camera!\r\n" +
                        "This may occur because the OpenCV Manager is not installed,\r\n" +
                        "CAMERA permission is not allowed in AndroidManifest.xml,\r\n" +
                        "or because another app is currently locking it.");
        openCVCamera.enableView();
    }

    /**
     * Set the maximum frame size that the camera uses
     * This method will fail if the camera is locked - it is recommended to check the result.
     *
     * @param frameSize Maximum (target) frame size
     * @return Actual frame size or null if cannot be set
     */
    public Size setFrameSize(Size frameSize) {
        if (openCVCamera == null)
            return null;

        openCVCamera.disableView();
        if (initialized) openCVCamera.disconnectCamera();
        openCVCamera.setMaxFrameSize((int) frameSize.width, (int) frameSize.height);
        if (initialized)
            if (!openCVCamera.connectCamera((int) frameSize.width, (int) frameSize.height))
                error("Could not initialize camera!\r\n" +
                        "This may occur because the OpenCV Manager is not installed,\r\n" +
                        "CAMERA permission is not allowed in AndroidManifest.xml,\r\n" +
                        "or because another app is currently locking it.");
        openCVCamera.enableView();

        width = openCVCamera.getFrameWidth();
        height = openCVCamera.getFrameHeight();
        if (width == 0 || height == 0) {
            Log.w("FTCVision", "OpenCV Camera failed to initialize width and height properties on startup.\r\n" +
                    "This is generally okay, but if you use width or height during init() you may\r\n" +
                    "run into a problem.");
        }

        return new Size(width, height);
    }

    /**
     * Get the actual frame size
     *
     * @return Actual frame size in pixels
     */
    public Size getFrameSize() {
        return new Size(width, height);
    }

    @Override
    public void init() {
        //Initialize camera view
        BaseLoaderCallback openCVLoaderCallback = null;
        try {
            openCVLoaderCallback = new BaseLoaderCallback(hardwareMap.appContext) {
                @Override
                public void onManagerConnected(int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS: {
                            //Woohoo!
                            Log.d("OpenCV", "OpenCV Manager connected!");
                            openCVInitialized = true;
                        }
                        break;
                        default: {
                            super.onManagerConnected(status);
                        }
                        break;
                    }
                }
            };
        } catch (NullPointerException e) {
            error("Could not find OpenCV Manager!\r\n" +
                    "Please install the app from the Google Play Store.");
        }

        final Activity activity = (Activity) hardwareMap.appContext;
        final VisionOpModeCore t = this;

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, hardwareMap.appContext, openCVLoaderCallback);
            if (!success) {
                Log.e("OpenCV", "Asynchronous initialization failed!");
                error("Could not initialize OpenCV!\r\n" +
                        "Did you install the OpenCV Manager from the Play Store?");
            } else {
                Log.d("OpenCV", "Asynchronous initialization succeeded!");
            }
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            if (openCVLoaderCallback != null)
                openCVLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            else {
                Log.e("OpenCV", "Failed to load OpenCV from package!");
                return;
            }
        }

        while (!openCVInitialized) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout layout = new LinearLayout(activity);
                layout.setOrientation(LinearLayout.VERTICAL);

                layout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                openCVCamera = new JavaCameraView(hardwareMap.appContext, 0);

                layout.addView(openCVCamera);
                layout.setVisibility(View.VISIBLE);

                openCVCamera.setCvCameraViewListener(t);
                if (openCVCamera != null)
                    openCVCamera.disableView();
                openCVCamera.enableView();
                if (!openCVCamera.connectCamera(initialMaxSize, initialMaxSize))
                    error("Could not initialize camera!\r\n" +
                            "This may occur because the OpenCV Manager is not installed,\r\n" +
                            "CAMERA permission is not allowed in AndroidManifest.xml,\r\n" +
                            "or because another app is currently locking it.");

                //Initialize FPS counter and sensors
                fps = new FPS();
                sensors = new Sensors();

                //Done!
                width = openCVCamera.getFrameWidth();
                height = openCVCamera.getFrameHeight();
                initialized = true;
            }
        });

        while (!initialized) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        if (openCVCamera != null) {
            openCVCamera.disableView();
            openCVCamera.disconnectCamera();
        }

        if (sensors != null)
            sensors.stop();

        initialized = false;
        openCVCamera = null;

        super.stop();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        this.width = width;
        this.height = height;
        Log.d("CAMERA", "STARTED");
    }

    @Override
    public void onCameraViewStopped() {
        Log.d("CAMERA", "STOPPED");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (!initialized) {
            return inputFrame.rgba();
        }

        // telemetry.addData("Vision Status", "Ready!");

        fps.update();
        return frame(inputFrame.rgba(), inputFrame.gray());
    }

    public abstract Mat frame(Mat rgba, Mat gray);
}
