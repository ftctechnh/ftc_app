package com.disnodeteam.dogecv.scoring;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public abstract class DogeCVScorer {
    public abstract double calculateDifference(MatOfPoint contours);
}
