package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.*;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "Line Follow Algorithm 3", group = "Line Follow")
//@Disabled
public class OpenCVSimplerTest extends OpenCVLib {

    private int yVal;

    private BotHardware robot = new BotHardware();

    @Override
    public void init(){
        robot.init(this, true);

        initOpenCV();
        startCamera();

        //catch a frame
        Mat frame = getCameraFrame();

        //init scanline Y values
        yVal = frame.cols() / 8;

        //log all the data
        telemetry.addData("Y Value", yVal);

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

        double linePos = LineFollowLib.getDisplacment(frame, yVal);

        if(linePos == LineFollowLib.ERROR_TOO_NOISY) telemetry.addData("Line Position", "ERROR");
        else telemetry.addData("Line Position", linePos);
    }

    @Override
    public void stop(){
        stopCamera();
    }
}