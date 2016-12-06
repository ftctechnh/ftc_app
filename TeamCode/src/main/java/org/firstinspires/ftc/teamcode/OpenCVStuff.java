package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "OpenCV Testing", group = "Line Follow")
//@Disabled
public class OpenCVStuff extends OpenCVLib {

    private int yVal;

    private BotHardware robot = new BotHardware();

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        Mat thing = frame.gray();
        Imgproc.threshold(thing, thing, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
        return thing;
    }

    @Override
    public void init(){
        robot.init(this, true);

        initOpenCV();
        startCamera();
    }

    @Override
    public void start(){
        //wait ten seconds
    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){
        stopCamera();
    }
}