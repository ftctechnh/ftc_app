package org.ftcgearedup.opencvtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.core.Core.*;

public class MainActivity extends Activity implements LoaderCallbackInterface {

    private VideoCapture mCamera;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if(status == LoaderCallbackInterface.SUCCESS) {
                Log.i("OpenCV", "OpenCV loaded successfully!");
                onOpenCVLoaded();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    private void onOpenCVLoaded() {
        Toast.makeText(this, "OpenCV loaded properly!", Toast.LENGTH_LONG).show();

        mCamera = new VideoCapture(Highgui.CV_CAP_ANDROID_BACK);

        if(!mCamera.isOpened()) {
            mCamera.release();
            mCamera = null;
            Log.e("OpenCV", "Failed to open native camera");
        }

//        Toast.makeText(this, "mCamera opened", Toast.LENGTH_LONG).show();

//        Mat frame = new Mat();
//
//        if(mCamera != null && !mCamera.isOpened())
//            return;
//        if(!mCamera.read(frame))
//            return;
//
//        Toast.makeText(this, String.format("Frame width/height: %d,%d",
//                frame.width(), frame.height()), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("OpenCV", "Trying to load OpenCV library");
        if(!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, loaderCallback)) {
            Log.e("OpenCV", "Unable to load the OpenCV library");
        }
    }

    @Override
    public void onManagerConnected(int status) {

    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {

    }
}
