package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by Noah on 11/10/2017.
 * OpenCV Peak Finding Using a Dilation Algorithm
 */

@Autonomous(name="OpenCV Peaks")
public class OpenCVPeakFinding extends OpenCVLib{
    private Mat kernel;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Mat in = frame.rgba();
        ArrayList<Mat> out = new ArrayList<> (in.channels());

        //extract only red data
        Core.split(in, out);
        Mat red = out.get(0);
        //emptey memory
        out.clear();
        //dilate red data
        Mat dilated = new Mat(red.size(), red.type());
        Imgproc.dilate(red, dilated, kernel);
        //filter out any that increased
        Core.compare(red, dilated, red, Core.CMP_GE);
        dilated.release();
        //mask to remove the non-peak pixels
        Mat ret = new Mat();
        in.copyTo(ret, red);
        //memory
        in.release();
        red.release();
        return ret;
    }

    @Override
    public void init() {
        initOpenCV();

        //create kernel
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15));
    }

    @Override
    public void start() {
        startCamera();
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        stopCamera();
    }
}
