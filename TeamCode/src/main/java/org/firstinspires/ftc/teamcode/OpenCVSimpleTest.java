package org.firstinspires.ftc.teamcode;

import android.provider.Settings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.*;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "OpenCVSimpleTest", group = "Test")
//@Disabled
public class OpenCVSimpleTest extends OpenCVLib {

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        //final long startTime = System.nanoTime();

        //here we gooooooooo!
        Mat src = frame.gray();

        final int topScanY = src.rows() / 4;
        final int middleScanY = src.rows() / 2;
        final int bottomScanY = (src.rows() * 3) / 4;

        final int thresh = 255;

        byte[] topRay = new byte[src.cols()];
        byte[] middleRay = new byte[src.cols()];
        byte[] bottomRay = new byte[src.cols()];

        //apply adaptive threshold to image
        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        //do all the position calculations
        src.row(topScanY).get(0, 0, topRay);
        src.row(middleScanY).get(0, 0, middleRay);
        src.row(bottomScanY).get(0, 0, bottomRay);

        //final int topThresh = threshFind(topRay);
        //final int middleThresh = threshFind(middleRay);
        //final int bottomThresh = threshFind(bottomRay);

        final int topPos = findAvgOverZero(topRay);
        final int middlePos = findAvgOverZero(middleRay);
        final int bottomPos = findAvgOverZero(bottomRay);

        //final long stopTime = System.nanoTime();

        //telemetry.addData("Exec. Time", stopTime - startTime);

        Imgproc.circle(src, new Point(topPos, topScanY), 10, new Scalar(255,0,0));

        Imgproc.circle(src, new Point(middlePos, middleScanY), 10, new Scalar(255,0,0));

        Imgproc.circle(src, new Point(bottomPos, bottomScanY), 10, new Scalar(255,0,0));

        return src;
    }

    @Override
    public void init(){
        initOpenCV();
    }

    @Override
    public void start(){
        startCamera();
    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){
        stopCamera();
    }

    //takes a n array of bytes, and returns (min + max)/2 for a threshold
    private int threshFind(int[] ray){
        int min = 0;
        int max = 0;
        for(int i = 0; i < ray.length; i++){
            if(min > ray[i]) min = ray[i];
            else if(max < ray[i]) max = ray[i];
        }

        return (min + max) / 2;
    }

    private int findAvgOverZero(byte[] ray){
        int totalPos = 0;
        int totalNum = 0;
        for(int i = 0; i < ray.length; i++){
            if(ray[i] != 0){
                totalPos += i;
                totalNum++;
            }
        }

        if(totalNum == 0) return 0;
        else return totalPos/totalNum;
    }

}

