package com.disnodeteam.dogecv.detectors;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Victo on 11/25/2017.
 */

public class GlyphDetector extends OpenCVPipeline {


    public enum GlyphDetectionMode {
        EDGE
    }

    public enum GlyphDetectionSpeed {
        VERY_FAST, FAST, BALANCED, SLOW, VERY_SLOW
    }

    //Settings

    public GlyphDetectionMode  detectionMode        = GlyphDetectionMode.EDGE;
    public double              downScaleFactor      = 0.6;
    public boolean             rotateMat            = false;
    public double              minScore             = 0.5;
    public double              scoreRatioWeight     = 0.5;
    public double              scoreDistanceXWeight = 0.8;
    public double              scoreDistanceYWeight = 1.1;
    public double              scoreAreaWeight       = 0.5;
    public GlyphDetectionSpeed speed                = GlyphDetectionSpeed.BALANCED;
    public boolean             debugDrawStats       = false;
    public boolean             debugDrawRects       = true;


    //results
    private Point chosenGlyphPosition = null;
    private double chosenGlyphOffset = 0;
    private boolean foundRect = false;

    private Mat workingMat = new Mat();
    private Mat edges = new Mat();
    private Mat processed = new Mat();
    private Mat structure = new Mat();
    private Size newSize  = new Size();
    @Override
    public Mat processFrame(Mat rgba, Mat gray) {

        Size initSize = rgba.size();
        newSize = new Size(initSize.width * downScaleFactor, initSize.height * downScaleFactor);

        rgba.copyTo(workingMat);
        Imgproc.resize(workingMat,workingMat, newSize);

        if(rotateMat){
            Mat tempBefore = workingMat.t();
            Core.flip(tempBefore, workingMat, 1); //mRgba.t() is the transpose
            tempBefore.release();
        }


        Imgproc.putText(workingMat,newSize.toString() + " - " + speed.toString(),new Point(5,15),0,0.5,new Scalar(0,255,0),1);

        Imgproc.cvtColor(workingMat,processed,Imgproc.COLOR_RGB2GRAY);

        switch (speed){
            case VERY_FAST:

                Imgproc.blur(processed,processed,new Size(2,2));

                Imgproc.bilateralFilter(processed.clone(),processed,11,17,17);

                Imgproc.Canny(processed,edges,15,45.0);

                structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3,3));
                Imgproc.morphologyEx(edges,edges,Imgproc.MORPH_CLOSE,structure);
                break;
            case FAST:
                Imgproc.blur(processed,processed,new Size(3,3));

                Imgproc.bilateralFilter(processed.clone(),processed,11,17,17);

                Imgproc.Canny(processed,edges,15,45.0);

                structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(6,6));
                Imgproc.morphologyEx(edges,edges,Imgproc.MORPH_CLOSE,structure);
                break;

            case BALANCED:
                Imgproc.blur(processed,processed,new Size(4,4));

                Imgproc.bilateralFilter(processed.clone(),processed,11,17,17);

                Imgproc.Canny(processed,edges,15,45.0);

                structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(7,7));
                Imgproc.morphologyEx(edges,edges,Imgproc.MORPH_CLOSE,structure);
                break;


            case SLOW:

                Imgproc.blur(processed,processed,new Size(6,6));

                Imgproc.bilateralFilter(processed.clone(),processed,11,17,17);

                Imgproc.Canny(processed,edges,15,45.0);


                structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(10,10));
                Imgproc.morphologyEx(edges,edges,Imgproc.MORPH_CLOSE,structure);
                break;

            case VERY_SLOW:

                Imgproc.blur(processed,processed,new Size(7,7));

                Imgproc.bilateralFilter(processed.clone(),processed,11,17,17);

                Imgproc.Canny(processed,edges,15,45.0);

                structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(15,15));
                Imgproc.morphologyEx(edges,edges,Imgproc.MORPH_CLOSE,structure);
                break;
        }

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();

        double chosenScore = 0;
        Rect chosenRect = null;

        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                if(Imgproc.contourArea(matOfPoint) > Imgproc.contourArea(t1)){
                    return -1;
                }else if(Imgproc.contourArea(matOfPoint) < Imgproc.contourArea(t1)){
                    return  1;
                } else{
                    return  0;
                }
            }
        });

        contours.remove(0); // Remove First Index which is usually a large square filling the entire screen,

        for(MatOfPoint c : contours) {

            if(Imgproc.contourArea(c) > 1000){
                Rect rect = Imgproc.boundingRect(c);

                double x = rect.x;
                double y = rect.y;
                double w = rect.width;
                double h = rect.height;

                Point centerPoint = new Point(x + ( w/2), y + (h/2));
                double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h));

                double score = 100;

                double diffrenceFromPerfect = Math.abs(1 - cubeRatio);
                double scoreRatioPunishment = 1 - diffrenceFromPerfect;
                double scoreRatio = scoreRatioPunishment * scoreRatioWeight;
                score *= scoreRatio;

                double distanceFromCenterX = (newSize.width / 2) - centerPoint.x;
                double distanceFromCenterY = newSize.height - centerPoint.y;
                distanceFromCenterX = Math.abs(distanceFromCenterX / newSize.width);
                distanceFromCenterY = Math.abs(distanceFromCenterY / newSize.height);

                double scoreDistanceFromCenterXPunishment = 1 - distanceFromCenterX;
                double scoreDistanceFromCenterYPunishment = 1 - distanceFromCenterY;

                double scoreDistanceFromCenterX = scoreDistanceFromCenterXPunishment * scoreDistanceXWeight;
                double scoreDistanceFromCenterY = scoreDistanceFromCenterYPunishment * scoreDistanceYWeight;

                score *= scoreDistanceFromCenterX;
                score *= scoreDistanceFromCenterY;

                double minArea = GetMinArea(contours);
                double maxArea = GetMaxArea(contours);
                double area = Imgproc.contourArea(c);
                double normalizedArea = (area - minArea) / (maxArea - minArea);
                double scoreAreaPunishment = normalizedArea;
                double scoreArea  =scoreAreaPunishment * scoreAreaWeight;
                score *= scoreArea;

                if(chosenRect == null){
                    chosenRect = rect;
                    chosenScore = score;
                }

                if(score > chosenScore){
                    chosenRect = rect;
                    chosenScore = score;
                }

                if(debugDrawRects){
                    Imgproc.rectangle(workingMat,new Point(x,y), new Point((x+w),(y+h)),new Scalar(0,255,255),1);
                }

                if(debugDrawRects){
                    String toPrint = String.format("Score: %.2f",score);
                    Imgproc.putText(workingMat,toPrint , new Point(x+5,y+5),0,0.5, new Scalar(0,255,255));
                }
            }




        }
        if(chosenRect != null && chosenScore > minScore){
            double x = chosenRect.x;
            double y = chosenRect.y;
            double w = chosenRect.width;
            double h = chosenRect.height;
            Imgproc.rectangle(workingMat,new Point(x,y), new Point((x+w),(y+h)),new Scalar(0,255,0),3);


            chosenGlyphPosition = new Point((x+(w/2)), (y+(h/2)));
            chosenGlyphOffset = newSize.width - (x+(w/2)) ;

            foundRect = false;
        }else{
            foundRect = true;
        }

        Imgproc.resize(workingMat,workingMat,initSize);

        return workingMat;
    }


    private double GetMaxArea(List<MatOfPoint> allConturs){
        double currentMax = 0;

        for (MatOfPoint c: allConturs){
            double area= Imgproc.contourArea(c);
            if(area>currentMax){
                currentMax = area;
            }
        }

        return currentMax;
    }

    private double GetMinArea(List<MatOfPoint> allConturs){
        double currentMax = Double.MAX_VALUE;

        for (MatOfPoint c: allConturs){
            double area= Imgproc.contourArea(c);
            if(area<currentMax){
                currentMax = area;
            }
        }

        return currentMax;
    }

    public Point getChosenGlyphPosition() {
        return chosenGlyphPosition;
    }

    public double getChosenGlyphOffset() {
        return chosenGlyphOffset;
    }

    public boolean isFoundRect() {
        return foundRect;
    }

    public Size getFrameSize() {
        return newSize;
    }
}
