package com.disnodeteam.dogecv.detectors.relicrecovery;


import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 11/5/2017.
 */

public class JewelDetector extends OpenCVPipeline {

    public enum JewelOrder {
        RED_BLUE,
        BLUE_RED,
        UNKNOWN
    }




    public enum JewelDetectionMode {
        PERFECT_AREA, MAX_AREA
    }

    public enum JewelDetectionSpeed {
        VERY_FAST, FAST, BALANCED, SLOW, VERY_SLOW
    }


    public JewelDetectionMode  detectionMode    = JewelDetectionMode.MAX_AREA;
    public double              downScaleFactor  = 0.4;
    public double              perfectRatio     = 1;
    public boolean             rotateMat        = false;
    public JewelDetectionSpeed speed            = JewelDetectionSpeed.BALANCED;
    public double              perfectArea      = 6500;
    public double              areaWeight       = 0.05; // Since we're dealing with 100's of pixels
    public double              minArea          = 700;
    public double              ratioWeight      = 15; // Since most of the time the area diffrence is a decimal place
    public double              maxDiffrence     = 10; // Since most of the time the area diffrence is a decimal place
    public boolean             debugContours    = false;
    public DogeCVColorFilter   colorFilterRed   = new LeviColorFilter(LeviColorFilter.ColorPreset.RED);
    public DogeCVColorFilter   colorFilterBlue  = new LeviColorFilter(LeviColorFilter.ColorPreset.BLUE);


    private JewelOrder currentOrder = JewelOrder.UNKNOWN;
    private JewelOrder lastOrder    = JewelOrder.UNKNOWN;


    private Mat workingMat = new Mat();
    private Mat blurredMat  = new Mat();
    private Mat maskRed  = new Mat();
    private Mat maskBlue  = new Mat();
    private Mat hiarchy  = new Mat();


    private Size newSize = new Size();

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {

        Size initSize= rgba.size();
        newSize  = new Size(initSize.width * downScaleFactor, initSize.height * downScaleFactor);
        rgba.copyTo(workingMat);

        Imgproc.resize(workingMat, workingMat,newSize);

        if(rotateMat){
            Mat tempBefore = workingMat.t();

            Core.flip(tempBefore, workingMat, -1); //mRgba.t() is the transpose

            tempBefore.release();
        }

        Mat redConvert = workingMat.clone();
        Mat blueConvert = workingMat.clone();

        colorFilterRed.process(redConvert, maskRed);
        colorFilterBlue.process(blueConvert, maskBlue);

        List<MatOfPoint> contoursRed = new ArrayList<>();

        Imgproc.findContours(maskRed, contoursRed, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursRed,-1,new Scalar(230,70,70),2);
        Rect chosenRedRect = null;
        double chosenRedScore = Integer.MAX_VALUE;

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for(MatOfPoint c : contoursRed) {
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            // You can find this by printing the area of each found rect, then looking and finding what u deem to be perfect.
            // Run this with the bot, on a balance board, with jewels in their desired location. Since jewels should mostly be
            // in the same position, this hack could work nicely.


            double area = Imgproc.contourArea(c);
            double areaDiffrence = 0;

            switch(detectionMode){
                case MAX_AREA:
                    areaDiffrence = -area * areaWeight;
                    break;
                case PERFECT_AREA:
                    areaDiffrence = Math.abs(perfectArea - area);
                    break;
            }

            // Just declaring vars to make my life eassy
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));



            double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
            double ratioDiffrence = Math.abs(cubeRatio - perfectRatio);


            double finalDiffrence = (ratioDiffrence * ratioWeight) + (areaDiffrence * areaWeight);


            // Optional to ALWAYS return a result.

            // Update the chosen rect if the diffrence is lower then the curreny chosen
            // Also can add a condition for min diffrence to filter out VERY wrong answers
            // Think of diffrence as score. 0 = perfect
            if(finalDiffrence < chosenRedScore && finalDiffrence < maxDiffrence && area > minArea){
                chosenRedScore = finalDiffrence;
                chosenRedRect = rect;
            }

            if(debugContours && area > 100){
                Imgproc.circle(workingMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(workingMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
            }

        }

        List<MatOfPoint> contoursBlue = new ArrayList<>();

        Imgproc.findContours(maskBlue, contoursBlue,hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursBlue,-1,new Scalar(70,130,230),2);
        Rect chosenBlueRect = null;
        double chosenBlueScore = Integer.MAX_VALUE;

        for(MatOfPoint c : contoursBlue) {
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            // You can find this by printing the area of each found rect, then looking and finding what u deem to be perfect.
            // Run this with the bot, on a balance board, with jewels in their desired location. Since jewels should mostly be
            // in the same position, this hack could work nicely.


            double area = Imgproc.contourArea(c);
            double areaDiffrence = 0;

            switch(detectionMode){
                case MAX_AREA:
                    areaDiffrence = -area * areaWeight;
                    break;
                case PERFECT_AREA:
                    areaDiffrence = Math.abs(perfectArea - area);
                    break;
            }


            // Just declaring vars to make my life eassy
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));

            double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
            double ratioDiffrence = Math.abs(cubeRatio - 1);

            double finalDiffrence = (ratioDiffrence * ratioWeight) + (areaDiffrence * areaWeight);



            // Update the chosen rect if the diffrence is lower then the curreny chosen
            // Also can add a condition for min diffrence to filter out VERY wrong answers
            // Think of diffrence as score. 0 = perfect
            if(finalDiffrence < chosenBlueScore && finalDiffrence < maxDiffrence && area > minArea){
                chosenBlueScore = finalDiffrence;
                chosenBlueRect = rect;
            }

            if(debugContours && area > 100){
                Imgproc.circle(workingMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(workingMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
            }

        }

        if(chosenRedRect != null){
            Imgproc.rectangle(workingMat,
                    new Point(chosenRedRect.x, chosenRedRect.y),
                    new Point(chosenRedRect.x + chosenRedRect.width, chosenRedRect.y + chosenRedRect.height),
                    new Scalar(255, 0, 0), 2);

            Imgproc.putText(workingMat,
                    "Red: " + String.format("%.2f", chosenRedScore),
                    new Point(chosenRedRect.x - 5, chosenRedRect.y - 10),
                    Core.FONT_HERSHEY_PLAIN,
                    1.3,
                    new Scalar(255, 0, 0),
                    2);
        }

        if(chosenBlueRect != null){
            Imgproc.rectangle(workingMat,
                    new Point(chosenBlueRect.x, chosenBlueRect.y),
                    new Point(chosenBlueRect.x + chosenBlueRect.width, chosenBlueRect.y + chosenBlueRect.height),
                    new Scalar(0, 0, 255), 2);

            Imgproc.putText(workingMat,
                    "Blue: " + String.format("%.2f", chosenBlueScore),
                    new Point(chosenBlueRect.x - 5, chosenBlueRect.y - 10),
                    Core.FONT_HERSHEY_PLAIN,
                    1.3,
                    new Scalar(0, 0, 255),
                    2);
        }

        if(chosenBlueRect != null && chosenRedRect != null){
            if(chosenBlueRect.x < chosenRedRect.x){
                currentOrder = JewelOrder.BLUE_RED;
                lastOrder = currentOrder;
            }else{
                currentOrder = JewelOrder.RED_BLUE;
                lastOrder = currentOrder;
            }
        }else{
            currentOrder = JewelOrder.UNKNOWN;
        }

        Imgproc.putText(workingMat,"Result: " + lastOrder.toString(),new Point(10,newSize.height - 30),0,1, new Scalar(255,255,0),1);
        Imgproc.putText(workingMat,"Current Track: " + currentOrder.toString(),new Point(10,newSize.height - 10),0,0.5, new Scalar(255,255,255),1);

        Imgproc.resize(workingMat,workingMat,initSize);

        redConvert.release();
        blueConvert.release();
        Imgproc.putText(workingMat,"DogeCV 1.1 Jewel: " + newSize.toString() + " - " + speed.toString() + " - " + detectionMode.toString() ,new Point(5,30),0,1.2,new Scalar(0,255,255),2);

        return workingMat;
    }


    public JewelOrder getCurrentOrder() {
        return currentOrder;
    }

    public JewelOrder getLastOrder() {
        return lastOrder;
    }
}
