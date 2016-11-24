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

    private boolean catchFrame;

    private Mat frameStore;

    private int[] yValStore = new int[3];

    private BotHardware robot = new BotHardware();

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        if(catchFrame){
            frameStore = frame.gray();
            catchFrame = false;
        }
        return frame.rgba();
    }

    private Mat getCameraFrame(){
        catchFrame = true;
        while (catchFrame);
        return frameStore.clone();
    }

    @Override
    public void init(){
        robot.init(this, true);

        initOpenCV();
        startCamera();

        //catch a frame
        Mat frame = getCameraFrame();

        //init scanline Y values
        yValStore[0] = frame.cols() / 4;
        yValStore[1] = frame.cols() / 2;
        yValStore[2] = (frame.cols() * 3) / 4;

        //log all the data
        telemetry.addData("Top Y Value", yValStore[0]);
        telemetry.addData("Middle Y Value", yValStore[1]);
        telemetry.addData("Bottom Y Value", yValStore[2]);

        telemetry.addData("Frame Width", frame.width());
        telemetry.addData("Frame Height", frame.height());
    }

    @Override
    public void start(){
        //wait ten seconds
    }

    @Override
    public void loop(){
        //get frame
        Mat frame = getCameraFrame();

        //log data
        telemetry.addData("Angle", LineFollow.getAngle(frame, yValStore[0], yValStore[2]));
        telemetry.addData("Displacement", LineFollow.getDisplacment(frame, yValStore[1]));
    }

    @Override
    public void stop(){
        stopCamera();
        frameStore.empty();
        catchFrame = false;
    }

    private class LineHeading implements HeadingSensor{
        public float getHeading(){

        }
    }
}

class LineFollow{

    public static final int ERROR_TOO_NOISY = -255;

    //get the angle of the phone relative to robot through line following
    public static double getAngle(Mat src, int topY, int bottomY){
        int[] linePos;

        linePos = scanlineAvg(src, new int[]{topY, bottomY});

        if(linePos[0] == ERROR_TOO_NOISY || linePos[1] == ERROR_TOO_NOISY) return ERROR_TOO_NOISY;

        final int yDist = linePos[1] - linePos[0];
        final int xDist = bottomY - topY;

        double headingToTarget = Math.atan2(yDist, xDist);
        headingToTarget = Math.toDegrees(headingToTarget); //to degrees

        return headingToTarget;
    }

    //gets the line relative to the camera view on the horizontal axis, from -1 to 1
    public static double getDisplacment(Mat src, int middleY){
        int[] linePos;

        linePos = scanlineAvg(src, new int[]{middleY});

        if(linePos[0] == ERROR_TOO_NOISY) return ERROR_TOO_NOISY;

        //convert to value from -1 to 1
        return  (double)(linePos[0] - (src.cols() / 2))  / (double)(src.cols() / 2);
    }

    private static int[] scanlineAvg(Mat src, int[] scanlineYs) {
        //here we gooooooooo!

        byte[][]ray = new byte[scanlineYs.length][src.cols()];

        //apply adaptive threshold to image
        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        //copy scan lines into arrays
        for (int i = 0; i < scanlineYs.length; i++) {
            src.row(scanlineYs[i]).get(0, 0, ray[i]);
        }

        //calculate average position for each
        int[] ret = new int[scanlineYs.length];
        for (int i = 0; i < scanlineYs.length; i++) {
            final int avg = findAvgOverZero(ray[i]);
            if(avg == -400) ret[i] = ERROR_TOO_NOISY;
            else ret[i] = avg;
        }

        return ret;
    }


    //takes a n array of bytes, and returns (min + max)/2 for a threshold
    private static int threshFind(int[] ray){
        int min = 0;
        int max = 0;
        for(int i = 0; i < ray.length; i++){
            if(min > ray[i]) min = ray[i];
            else if(max < ray[i]) max = ray[i];
        }

        return (min + max) / 2;
    }

    //find the average position of all numbers > 0 in an array
    private static int findAvgOverZero(byte[] ray){
        int totalPos = 0;
        int totalNum = 0;
        for(int i = 0; i < ray.length; i++){
            if(ray[i] != 0){
                totalPos += i;
                totalNum++;
            }
        }

        //and return it in a value between -1 and 1, or and error if there is too much noise
        if(totalNum > ray.length * 2 / 3 || totalNum < 25) return -400;
        else if(totalNum == 0) return 0;
        else return totalPos/totalNum;
    }
}