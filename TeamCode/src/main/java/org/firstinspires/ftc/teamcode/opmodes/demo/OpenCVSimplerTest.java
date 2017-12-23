package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.libraries.LineFollowLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;
import org.opencv.core.Mat;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "Line Follow Algorithm 3", group = "Line Follow")
@Disabled
public class OpenCVSimplerTest extends OpenCVLib {

    private int yVal;

    private BotHardwareOld robot = new BotHardwareOld();

    @Override
    public void init(){
        robot.init(this, true);

        initOpenCV();
        startCamera();

        //catch a frame
        Mat frame = getCameraFrame();

        //init scanline Y values
        yVal = frame.rows() / 8;

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