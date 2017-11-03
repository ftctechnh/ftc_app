package org.firstinspires.ftc.teamcode.libraries;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Noah on 11/10/2016.
 */

public abstract class OpenCVLib extends OpenCVLoad implements CameraBridgeViewBase.CvCameraViewListener2{

    public static final String cvTAG = "OpenCVCamInit";

    private CameraBridgeViewBase mOpenCvCameraView;

    private View mView;

    private BlockingQueue<Mat> frameStore = new LinkedBlockingQueue<>(1);

    private boolean loaded = false;

    public OpenCVLib(){

    }

    public OpenCVLib(View view){
        mView = view;
    }



    public void loadOpenCV() {
        super.initOpenCV();
    }

    @Override
    public void initOpenCV(){
        super.initOpenCV();

        if(mView == null) mView = ((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.image_manipulations_activity_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) mView;
        //mOpenCvCameraView.setAlpha(0.0f);
        mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        //mOpenCvCameraView.setMaxFrameSize(400,400);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void stopOpenCV(){
        Runnable mCameraStop = new Runnable() {
            @Override
            public void run() {
                if(mOpenCvCameraView != null)
                    mOpenCvCameraView.disableView();
                mOpenCvCameraView.setAlpha(0.0f);
            }
        };

        mOpenCvCameraView.getHandler().post(mCameraStop);
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
        Runnable mCameraStart = new Runnable() {
            @Override
            public void run() {
                mOpenCvCameraView.setAlpha(1.0f);
                mOpenCvCameraView.enableView();
                //while(!mOpenCvCameraView.isActivated() && !mOpenCvCameraView.isEnabled());
                //RobotLog.vv(cvTAG, "Camera Activation Success!");
            }
        };

       mOpenCvCameraView.getHandler().post(mCameraStart);
    }

    public void stopCamera(){
        frameStore.clear();
        stopOpenCV();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        if(frameStore.isEmpty()) frameStore.add(frame.gray());

        Mat src = frame.gray();

        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        return src;
    }

    public BlockingQueue<Mat> getFrameQueue() {
        return frameStore;
    }

    public Mat getCameraFrame() {
        //catch a frame
        try {
            return getFrameQueue().take().clone();
        }
        catch (InterruptedException e) {
            throw new RuntimeException("Oh fuck");
        }
    }
}
