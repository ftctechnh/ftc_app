package com.disnodeteam.dogecv.filters;

import org.opencv.core.Mat;

/**
 * Created by Victo on 1/1/2018.
 */

public abstract class DogeCVColorFilter {
    public abstract void process(Mat input, Mat mask);

}
