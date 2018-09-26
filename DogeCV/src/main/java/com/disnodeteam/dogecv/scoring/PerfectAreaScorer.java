package com.disnodeteam.dogecv.scoring;

import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 9/10/2018.
 */

public class PerfectAreaScorer extends DogeCVScorer {
    public double weight       = 1.0;
    public double perfectArea  = 5000;

    public PerfectAreaScorer(double perfectArea, double weight){
        this.weight = weight;
        this.perfectArea = perfectArea;

    }
    @Override
    public double calculateDifference(MatOfPoint contours) {
        double area = Imgproc.contourArea(contours);
        double areaDiffrence = Math.abs(perfectArea - area);
        return areaDiffrence * weight;
    }
}
