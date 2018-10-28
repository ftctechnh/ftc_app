package com.disnodeteam.dogecv.scoring;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 9/10/2018.
 */

public class PerfectAreaScorer extends DogeCVScorer {
    public double weight       = 1.0;
    public double perfectArea  = 5000;
    /**
     * Constructor
     * @param perfectArea - Perfect area in pixels
     * @param weight - How much to weight the final score (0.001 - 0.1 is usually good)
     */
    public PerfectAreaScorer(double perfectArea, double weight){
        this.weight = weight;
        this.perfectArea = perfectArea;

    }
    /**
     * @param input - Input mat (Can be MatOfPoint for contours)
     * @return - Difference from perfect score
     */
    @Override
    public double calculateScore(Mat input) {
        if(!(input instanceof MatOfPoint)) return Double.MAX_VALUE;
        MatOfPoint contour = (MatOfPoint) input;
        double area = Imgproc.contourArea(contour);
        double areaDiffrence = Math.abs(perfectArea - area);
        return areaDiffrence * weight;
    }
}
