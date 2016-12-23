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

    private int[] yValStore = new int[3];

    @Override
    public void init(){
        initOpenCV();
        startCamera();

        Mat frame = getCameraFrame();

        //init scanline Y values
        yValStore[0] = frame.rows() / 4;
        yValStore[1] = frame.rows() / 2;
        yValStore[2] = (frame.rows() * 3) / 4;
    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){
        Mat frame = getCameraFrame();
        telemetry.addData("Find Avg", LineFollowLib.scanlineAvgDebug(frame, yValStore[1], this));
    }

    @Override
    public void stop(){
        stopCamera();
    }
}