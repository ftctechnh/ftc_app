package org.firstinspires.ftc.teamcode.algorithms.impl;

import android.os.Environment;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.RETR_TREE;

/**
 */

public class JewelDetectionAlgorithm {
    private VisionHelper visionHelper;

    public enum JewelConfiguration {
        RED_BLUE, BLUE_RED
    }

    public JewelDetectionAlgorithm(VisionHelper visionHelper) {
        this.visionHelper = visionHelper;
    }

    public JewelConfiguration detect() {
        Mat frame = visionHelper.readOpenCVFrame();

//        Mat filteredRed = new Mat();
//        Core.inRange(frame, new Scalar(0, 0, 200), new Scalar(0, 0, 255), filteredRed);

//        Imgproc.erode(filteredRed, filteredRed, new Mat(), new Point(-1, -1), 4);
//        Imgproc.dilate(filteredRed, filteredRed, new Mat(), new Point(-1, -1), 2);

//        Mat edges = new Mat();
//        Imgproc.Canny(filteredRed, edges, 100, 500);

//        List<MatOfPoint> contours = new ArrayList<>();
//        Imgproc.findContours(edges.clone(), contours, new Mat(), RETR_TREE, CHAIN_APPROX_SIMPLE, new Point(0, 0));

//        double largestArea = 0;
//        MatOfPoint largestContour = new MatOfPoint();
//
//        for(MatOfPoint contour: contours) {
//            double area = Imgproc.contourArea(contour);
//
//            // first point in contour
//            Point firstPoint = contour.toArray()[0];
//
//            if(area > largestArea) {
//                largestArea = area;
//                largestContour = contour;
//            }
//        }

        //Imgproc.drawContours(frame, contours, contours.indexOf(largestContour), new Scalar(0, 255, 0), 4);

        String path = AppUtil.getInstance().getSettingsFile("frame.jpg").getAbsolutePath();

        Highgui.imwrite(path, frame);

//        if(contours.size() > 0) {
//            return JewelConfiguration.BLUE_RED;
//        }
        return JewelConfiguration.RED_BLUE;
    }
}
