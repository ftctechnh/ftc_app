package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.CV_HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.HoughCircles;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by ftc6347 on 10/3/16.
 */
@Disabled
@Autonomous(name = "OpenCV test", group = "Tests")
public class OpenCVTest extends LinearOpMode {

    static {
        System.loadLibrary("opencv_java");
    }

//    private VideoCapture capture;

    @Override
    public void runOpMode() throws InterruptedException {
//        this.capture = new VideoCapture(0); // Use camera index 0

        waitForStart();

//        Mat frame = new Mat();
//        Mat grey = new Mat();
//        Mat circles = new Mat();
//
//        // Read a single frame and copy its contents to the frame object
//        capture.read(frame);
//        // Convert the image into a grey-scale image
//        cvtColor(frame, grey, COLOR_BGR2GRAY);
//        // Smooth out the image so that too many circles aren't detected
//        GaussianBlur(grey, grey, new Size(9, 9), 2, 2);
//        HoughCircles(grey, circles, CV_HOUGH_GRADIENT,
//                2, grey.rows() / 4, 200, 100, 0, 0);
//
//        // Not sure how the data is to be interpreted when the documentation
//        // describes "circles" as a 3 float vector when it is actually a matrix
//        telemetry.addData("Circle matrix: ", circles);
//        telemetry.update();

//        capture.release();
    }

}
