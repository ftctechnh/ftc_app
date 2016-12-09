package org.firstinspires.ftc.teamcode;

import android.provider.ContactsContract;

import com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "OpenCV Testing", group = "Line Follow")
//@Disabled
public class OpenCVStuff extends OpenCVLib {

    private int yVal;
    private Mat lastMat;
    private boolean firstCall = true;

    private double xPos = 0;
    private double yPos = 0;

    FilterLib.MovingWindowAngleFilter filter = new FilterLib.MovingWindowAngleFilter();

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        Mat grey = frame.gray();

        Mat optic = grey.clone();

        if(!firstCall) {

        }

        lastMat = grey;

        if(!lastMat.empty()) firstCall = false;

        return optic;
    }

    @Override
    public void init(){
        initOpenCV();

        lastMat = new Mat();
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
        lastMat.release();
    }
}