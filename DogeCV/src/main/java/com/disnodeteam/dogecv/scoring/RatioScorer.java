package com.disnodeteam.dogecv.scoring;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public class RatioScorer extends DogeCVScorer{

    public double weight       = 1.0;
    public double perfectRatio = 1.0;
    public RatioScorer(){

    }
    public RatioScorer(double perfectRatio, double weight){
        this.weight = weight;
        this.perfectRatio = perfectRatio;
    }

    @Override
    public double calculateDifference(MatOfPoint contours) {
        double score = Double.MAX_VALUE;

        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(contours);
        double x = rect.x;
        double y = rect.y;
        double w = rect.width;
        double h = rect.height;

        double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
        double ratioDiffrence = Math.abs(cubeRatio - perfectRatio);
        return ratioDiffrence * weight;
    }
}
