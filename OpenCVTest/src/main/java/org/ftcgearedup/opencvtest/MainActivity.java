package org.ftcgearedup.opencvtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.core.Core.*;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String LOG_TAG = "OCVTest::MainActivity";

    private CameraBridgeViewBase cameraView;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if(status == LoaderCallbackInterface.SUCCESS) {
                Log.i(LOG_TAG, "OpenCV loaded successfully!");
                cameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        cameraView = (CameraBridgeViewBase)findViewById(R.id.surfaceView);
        cameraView.setCvCameraViewListener(this);
        cameraView.setCameraIndex(0); // 0 is back, 1 is front
        cameraView.enableFpsMeter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, loaderCallback);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat hsvImg = new Mat();
        Mat rgbImg = new Mat();

        cvtColor(inputFrame.rgba(), rgbImg, COLOR_RGBA2RGB);
        cvtColor(rgbImg, hsvImg, COLOR_RGB2HSV, 3);

        inRange(hsvImg, new Scalar(0, 50, 15), new Scalar(15, 255, 255), hsvImg);

        // TODO: dilate/erode the image
        // TODO: use findContours()
        // TODO: use boundingRect() to create a bounding rect from those contours

        return hsvImg;

//        Mat gray = inputFrame.gray();
//
//        GaussianBlur(gray, gray, new Size(9, 9), 2, 2);
//
//        Mat circles = new Mat();
////        HoughCircles(gray, circles, CV_HOUGH_GRADIENT, 2, gray.rows() / 4, 220, 60, 0, 100);
//
//        HoughCircles(gray, circles, CV_HOUGH_GRADIENT, 2, gray.rows() / 4, 220, 60, 0, 0);
//
//        for(int i = 0; i < circles.size().width; i++) {
//            double[] circle = circles.get(0, i);
//            Point center = new Point(circle[0], circle[1]);
//            int radius = (int)circle[2];
//
//            circle(img, center, 9, new Scalar(0, 255, 0), 2, 8, 0);
//            circle(img, center, radius, new Scalar(255, 0, 0), 2, 8, 0);
//        }
//
//        return img;

//        Mat gray = inputFrame.gray();
//        blur(gray, gray, new Size(3, 3));
//
//        List<MatOfPoint> contours = new ArrayList<>();
//        Mat cannyOutput = new Mat();
//        Mat hierarchy = new Mat();
//
//        int thresh = 500;
//
//        Canny(gray, cannyOutput, thresh, thresh * 2, 3, false);
//        findContours(gray, contours, hierarchy, RETR_TREE, CHAIN_APPROX_NONE, new Point(0, 0));
//
//        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
//        Scalar color = new Scalar(0, 255, 0);
//        for(int i = 0; i < contours.size(); i++) {
//            drawContours(drawing, contours, i, color, 2, 8, hierarchy, 0, new Point());
//        }
//
//        return drawing;
    }
}
