package org.firstinspires.ftc.teamcode;

import android.view.SurfaceView;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * Created by Robotics on 10/28/2016.
 */

@TeleOp(name = "OpenCVTest", group = "Test")
//@Disabled
public class OpenCVTest extends OpMode implements CameraBridgeViewBase.CvCameraViewListener {

    private CameraBridgeViewBase mOpenCvCameraView;

    private boolean cvLoaded = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(FtcRobotControllerActivity.getAppContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    cvLoaded = true;
                } break;
                default:
                {
                    cvLoaded = false;
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(Mat outputFrame) {
        return outputFrame;
    }

    @Override
    public void init() {
        if(cvLoaded && OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, FtcRobotControllerActivity.getAppContext(), mLoaderCallback)){
            telemetry.addData("OpenCV:", "Loaded");
        }
        else{
            telemetry.addData("OpenCV:", "Hah Nope");
        }

        //mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void start() {}

    @Override
    public void loop() {

    }

}
