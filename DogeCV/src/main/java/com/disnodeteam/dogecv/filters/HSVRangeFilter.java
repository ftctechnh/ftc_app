package com.disnodeteam.dogecv.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 1/1/2018.
 */

public class HSVRangeFilter extends DogeCVColorFilter{

    private Scalar lower = new Scalar(255,255,255);
    private Scalar upper = new Scalar(0,0,0);

    public HSVRangeFilter(Scalar lower, Scalar upper){
        updateSettings(lower, upper);
    }

    public void updateSettings(Scalar lower, Scalar upper){
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public void process(Mat input, Mat mask) {
        Imgproc.cvtColor(input,input,Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.GaussianBlur(input,input,new Size(5,5),0);

        Core.inRange(input,lower,upper,mask);
        input.release();
    }
}
