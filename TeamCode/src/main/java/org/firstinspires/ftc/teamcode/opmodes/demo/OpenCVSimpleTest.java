package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.libraries.LineFollowLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;
import org.opencv.core.Mat;


/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "Line Follow Algorithm 2", group = "Line Follow")
@Disabled
public class OpenCVSimpleTest extends OpenCVLib {

    private int[] yValStore = new int[3];

    private BotHardwareOld robot = new BotHardwareOld();

    @Override
    public void init(){
        robot.init(this, true);

        initOpenCV();
        startCamera();

        //catch a frame
        Mat frame = getCameraFrame();

        //init scanline Y values
        yValStore[0] = frame.rows() / 4;
        yValStore[1] = frame.rows() / 2;
        yValStore[2] = (frame.rows() * 3) / 4;

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
        telemetry.addData("Angle", LineFollowLib.getAngle(frame, yValStore[0], yValStore[2]));
        telemetry.addData("Displacement", LineFollowLib.getDisplacment(frame, yValStore[1]));
    }

    @Override
    public void stop(){
        stopCamera();
    }
}