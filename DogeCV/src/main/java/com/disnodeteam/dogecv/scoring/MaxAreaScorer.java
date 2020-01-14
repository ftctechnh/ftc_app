package com.disnodeteam.dogecv.scoring;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 9/10/2018.
 */

public class MaxAreaScorer extends DogeCVScorer{
    public double weight       = 1.0;
    /**
     * Constructor
     * @param weight - How much to weight the final score (1-10 is usually good)
     */
    public MaxAreaScorer( double weight){
        this.weight = weight;

    }

    /**
     * Calculate the score
     * @param input - Input mat (Can be MatOfPoint for contours)
     * @return - Difference from perfect score
     */
    @Override
    public double calculateScore(Mat input) {
        if(!(input instanceof MatOfPoint)) return Double.MAX_VALUE;
        MatOfPoint contour = (MatOfPoint) input;
        double area = Imgproc.contourArea(contour);

        return -area * weight;
    }

}
