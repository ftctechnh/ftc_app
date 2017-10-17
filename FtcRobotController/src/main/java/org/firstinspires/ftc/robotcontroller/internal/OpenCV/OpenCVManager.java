package org.firstinspires.ftc.robotcontroller.internal.OpenCV;


import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


/**
 *
 */
public class OpenCVManager implements CameraBridgeViewBase.CvCameraViewListener2
{
    private Activity _mainActivity;                 // Main activity of the app

    private JavaCameraView _javaCameraView;         // Camera view in the robot controller

    // Mat values
    private Mat _rgba;
    private Mat _grayScale;
    private Mat _canny;

    // Loader callback, for when activity state changes
    private BaseLoaderCallback _loaderCallBack;

    private MAT_TYPE _currentMat;                   // Current Mat type we're using


    /**
     * Type of Mat to use/get/display
     */
    public enum MAT_TYPE
    {
        RGBA ,
        GRAY ,
        CANNY
    }


    /**
     * Constructor- takes the object holding the main activity
     *
     * @param MAIN_ACTIVITY The main activity of the app
     */
    public OpenCVManager(final Activity MAIN_ACTIVITY)
    {
        _mainActivity = MAIN_ACTIVITY;

        _loaderCallBack = new BaseLoaderCallback(_mainActivity)
        {
            @Override
            public void onManagerConnected(int status)
            {
                switch(status)
                {
                    case BaseLoaderCallback.SUCCESS:
                        _javaCameraView.enableView();
                        break;

                    default:
                        super.onManagerConnected(status);
                        break;
                }

                super.onManagerConnected(status);
            }
        };

        _currentMat = MAT_TYPE.RGBA;

    }


    @Override
    public void onCameraViewStarted(int width, int height)
    {
        _rgba = new Mat(height , width , CvType.CV_8UC4);
        _grayScale = new Mat(height , width , CvType.CV_8UC1);
        _canny = new Mat(height , width , CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped()
    {
        _rgba.release();
        _grayScale.release();
        _canny.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        _rgba = inputFrame.rgba();

        Imgproc.cvtColor(_rgba , _grayScale , Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(_grayScale , _canny , 50 , 100);

        switch(_currentMat)
        {
            case RGBA:
                return _rgba;

            case GRAY:
                return _grayScale;

            case CANNY:
                return _canny;
        }

        return null;
    }


    public void init()
    {
        if (!OpenCVLoader.initDebug())
        {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        }
        else
        {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }

        _javaCameraView = (JavaCameraView)_mainActivity.findViewById(R.id.java_camera_view);
        _javaCameraView.setVisibility(SurfaceView.VISIBLE);
        _javaCameraView.setCvCameraViewListener(this);
    }


    public void enableView()
    {
        if (!OpenCVLoader.initDebug())
        {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0 , _mainActivity ,
                    _loaderCallBack);
        }
        else
        {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
            _loaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    public void disableView()
    {
        if(_javaCameraView != null)
        {
            _javaCameraView.disableView();
        }
    }


    public void enableCameraView()
    {
        _javaCameraView.setVisibility(View.VISIBLE);
    }


    public void disableCameraView()
    {
        _javaCameraView.setVisibility(View.GONE);
    }
}
