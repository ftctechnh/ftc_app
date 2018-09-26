package com.disnodeteam.dogecv.scoring;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 9/10/2018.
 */

public class MaxAreaScorer extends DogeCVScorer{
    public double weight       = 1.0;

    public MaxAreaScorer( double weight){
        this.weight = weight;

    }

    @Override
    public double calculateDifference(MatOfPoint contours) {


        double area = Imgproc.contourArea(contours);

        return -area * weight;
    }

}
