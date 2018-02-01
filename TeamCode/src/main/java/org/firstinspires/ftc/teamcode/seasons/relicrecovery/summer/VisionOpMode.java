package org.firstinspires.ftc.teamcode.seasons.relicrecovery.summer;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;

/**
 * Created by ftc6347 on 6/20/17.
 */

public abstract class VisionOpMode extends OpMode implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String LOG_TAG = "VisionOpMode";
    private static RobotControllerCameraView cameraView;

    private Activity activity;

    public void initializeOpenCV() {
        activity = (Activity)hardwareMap.appContext;

        final BaseLoaderCallback openCvCallBack = new BaseLoaderCallback(hardwareMap.appContext) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        Log.d(LOG_TAG, "OpenCV Manager connected");

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout cameraMonitorLayout =
                                        (LinearLayout)activity.findViewById(R.id.cameraMonitorViewId);

                                if(cameraView == null) {
                                    cameraView = new RobotControllerCameraView(activity, 0);
                                    cameraMonitorLayout.addView(cameraView);
                                }

                                cameraView.enableView();
                                cameraView.enableFpsMeter();
                                cameraView.setVisibility(View.VISIBLE);
                            }
                        });
                    default:
                        super.onManagerConnected(status);
                }
            }
        };

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11,
                hardwareMap.appContext, openCvCallBack);
    }

    @Override
    public void stop() {
        super.stop();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cameraView.disableView();
                cameraView.disableFpsMeter();
                cameraView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }
}
