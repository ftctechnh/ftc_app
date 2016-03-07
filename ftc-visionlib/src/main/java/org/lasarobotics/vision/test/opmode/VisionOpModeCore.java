package org.lasarobotics.vision.test.opmode;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.lasarobotics.vision.test.android.Cameras;
import org.lasarobotics.vision.test.util.FPS;
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
    private static JavaCameraView openCVCamera;
    private static boolean initialized = false;
    private static boolean openCVInitialized = false;
    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(hardwareMap.appContext) {
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
    protected int width, height;
    protected FPS fps;

    public final void setCamera(Cameras camera) {
        openCVCamera.disconnectCamera();
        openCVCamera.setCameraIndex(camera.getID());
        openCVCamera.connectCamera(width, height);
    }

    public final void setFrameSize(Size frameSize) {
        openCVCamera.setMaxFrameSize((int) frameSize.width, (int) frameSize.height);

        openCVCamera.disconnectCamera();
        openCVCamera.connectCamera((int) frameSize.width, (int) frameSize.height);

        width = openCVCamera.getFrameWidth();
        height = openCVCamera.getFrameHeight();
    }

    @Override
    public void init() {
        //Initialize camera view
        final Activity activity = (Activity) hardwareMap.appContext;
        final VisionOpModeCore t = this;

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, hardwareMap.appContext, mLoaderCallback);
            if (!success)
                Log.e("OpenCV", "Asynchronous initialization failed!");
            else
                Log.d("OpenCV", "Asynchronous initialization succeeded!");
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
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
                openCVCamera.connectCamera(initialMaxSize, initialMaxSize);

                //Initialize FPS counter
                fps = new FPS();

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
        super.stop();

        if (openCVCamera != null)
            openCVCamera.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (!initialized) {
            return inputFrame.rgba();
        }

        telemetry.addData("Vision Status", "Ready!");

        fps.update();
        return frame(inputFrame.rgba(), inputFrame.gray());
    }

    public abstract Mat frame(Mat rgba, Mat gray);
}
