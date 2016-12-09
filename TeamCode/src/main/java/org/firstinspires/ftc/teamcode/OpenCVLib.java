package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Noah on 11/10/2016.
 */

public abstract class OpenCVLib extends OpMode implements CameraBridgeViewBase.CvCameraViewListener2{

    public static final String cvTAG = "OpenCVInit";

    private CameraBridgeViewBase mOpenCvCameraView;

    private Context mContext;
    private View mView;

    private boolean catchFrame = false;
    private boolean updateNeeded = true;

    private Mat frameStore;

    OpenCVLib(){
        mContext = FtcRobotControllerActivity.getAppContext();
        mView = FtcRobotControllerActivity.getCameraView();
    }

    OpenCVLib(Context context, View view){
        mContext = context;
        mView = view;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(mContext) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public void initOpenCV(){

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (!OpenCVLoader.initDebug()) {
            RobotLog.vv(cvTAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, mContext, mLoaderCallback);
        } else {
            RobotLog.vv(cvTAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        frameStore = new Mat();

        mOpenCvCameraView = (CameraBridgeViewBase) mView;
        //mOpenCvCameraView.setAlpha(0.0f);
        mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        //mOpenCvCameraView.setMaxFrameSize(400,400);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    public void stopOpenCV(){
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
            RobotLog.vv(cvTAG, "Camera Disabled");
        }
    }


    public void onCameraViewStarted(int width, int height){
        RobotLog.vv(cvTAG, "Camera view started: " + width + ", " + height);
        //mOpenCvCameraView.setAlpha(1.0f);
    }

    public void onCameraViewStopped(){
        RobotLog.vv(cvTAG, "Camera view stopped");
        //mOpenCvCameraView.setAlpha(0.0f);
    }

    public void startCamera(){
        mOpenCvCameraView.enableView();
        while(!mOpenCvCameraView.isActivated() && !mOpenCvCameraView.isEnabled());
        RobotLog.vv(cvTAG, "Camera Activation Success!");
    }

    public void stopCamera(){
        frameStore.release();
        catchFrame = false;
        stopOpenCV();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        if(catchFrame){
            frameStore = frame.gray();
            catchFrame = false;
        }

        if(!updateNeeded) updateNeeded = true;

        return frame.rgba();
    }

    public Mat getCameraFrame(){
        catchFrame = true;
        while (catchFrame && updateNeeded);
        updateNeeded = false;
        return frameStore.clone();
    }
}
