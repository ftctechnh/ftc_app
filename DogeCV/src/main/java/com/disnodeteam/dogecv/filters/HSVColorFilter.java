package com.disnodeteam.dogecv.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 1/1/2018.
 */

public class HSVColorFilter extends DogeCVColorFilter{

    private Scalar perfect = new Scalar(255,255,255); // Prefect color
    private Scalar range = new Scalar(0,0,0); // Range around perfect color

    private Mat workingMat = new Mat(); // Mat used for processing

    /**
     * Constructor
     * @param color - Perfect Color
     * @param range - Range of color around the perfect (margin of error)
     */
    public HSVColorFilter(Scalar color, Scalar range){
        updateSettings(color, range);
    }


    /**
     * Update settings
     * @param color - Perfect Color
     * @param range - Range of color around the perfect (margin of error)
     */
    public void updateSettings(Scalar color, Scalar range){
        this.perfect = color;
        this.range = range;
    }

    /**
     * Process a image and return a mask
     * @param input - Input image to process
     * @param mask - Output mask
     */
    @Override
    public void process(Mat input, Mat mask) {
        // Copy the input to working mat
        workingMat = input.clone();
        // Convert the input to HSV color space
        Imgproc.cvtColor(workingMat,workingMat,Imgproc.COLOR_RGB2HSV_FULL);

        // Blur the imgae
        Imgproc.GaussianBlur(workingMat,workingMat,new Size(5,5),0);

        // Run a inRange mask using the color and range
        Scalar lower = new Scalar(perfect.val[0] - (range.val[0]/2), perfect.val[1] - (range.val[1]/2),perfect.val[2] - (range.val[2]/2));
        Scalar upper = new Scalar(perfect.val[0] + (range.val[0]/2), perfect.val[1] + (range.val[1]/2),perfect.val[2] + (range.val[2]/2));
        Core.inRange(workingMat,lower,upper,mask);
    }
}
