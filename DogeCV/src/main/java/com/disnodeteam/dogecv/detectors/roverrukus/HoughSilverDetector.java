package com.disnodeteam.dogecv.detectors.roverrukus;

import android.util.Log;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.math.Circle;
import com.disnodeteam.dogecv.scoring.ColorDevScorer;
import com.disnodeteam.dogecv.scoring.DogeCVScorer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public class HoughSilverDetector extends DogeCVDetector {

    //The scorer used for this class. Based upon minimizing the standard deviation of color within each mineral,
    //I.e, if the region is actually a mineral it should be fairly flat. (Levi is op AF - Alex)
    public DogeCVScorer stdDevScorer = new ColorDevScorer();

    public double sensitivity = 1.4; //Sensitivity of circle detector; between about 1.2 and 2.1;
    public double minDistance = 60; //Adjust with frame size! This is the minimum distance between circles

    private Mat workingMat = new Mat(); //The working mat used for internal calculations, single object to avoid memory leak
    private Mat displayMat = new Mat(); //The matrix to be displayed
    private int results; //How many potential minerals were detected
    private Circle foundCircle; //The best detection found, if any
    private boolean isFound = false; //Whether a circle has been found at all

    /**
     * Simple constructor.
     */
    public HoughSilverDetector() {
        super();
        this.detectorName = "Hough Silver Detector";
    }

    @Override
    public Mat process(Mat input) {
        if(input.channels() < 0 || input.cols() <= 0){
            Log.e("DogeCV", "Bad INPUT MAT!");
        }
        input.copyTo(workingMat); //Copies input to working matrix
        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGBA2RGB); //Converts from RGBA to simply RGB
        displayMat = new Mat(); //Creates the display matrix
        Imgproc.bilateralFilter(workingMat, displayMat, 5, 175, 175); //Similar to a Gaussian blur, but preserves edges far better.
        displayMat.copyTo(workingMat); //Copies blurred image onto working matrix
        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2Lab); //Converts image to Lab color space for better mineral differentiation

        Imgproc.erode(workingMat, workingMat, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3))); //Removes noise
        Imgproc.GaussianBlur(workingMat, workingMat, new Size(3,3), 0); //Blurs image
        List<Mat> channels = new ArrayList<Mat>();
        Core.split(workingMat, channels); //Splits the three channels of the Lab image into a List

        Mat circles = new Mat(); //A matrix of circles; each entry is an array of doubles, first coordinate is the x of the circle, second is y, third is the radius.
        Imgproc.HoughCircles(channels.get(0), circles, Imgproc.CV_HOUGH_GRADIENT, sensitivity, minDistance); //Applies the Hough Circular transformation to find circles in the image

        results = 0; //The number of detected circles
        Circle bestCircle = null; //Resets the best detected circle
        double bestDifference = Double.MAX_VALUE; //The worst possible image variance

        //Iterates over each circle, scoring it in and checking if its better than the previous
        for (int i = 0; i < circles.width(); i++) {
            Circle circle = new Circle(circles.get(0,i)[0],circles.get(0,i)[1],circles.get(0,i)[2]); //Retrieves circle object from matrix
            Mat mask = Mat.zeros(workingMat.size(), CvType.CV_8UC1); //Creates an empty image to contain the mask of the circle
            Imgproc.circle(mask, new Point((int) circle.x, (int) circle.y), (int) circle.radius, new Scalar(255), -1); //Draws a filled-in circle on the mask
            Mat masked = new Mat((int) getAdjustedSize().height, (int) getAdjustedSize().width, CvType.CV_8UC3); //Creates a blank matrix of the appropriate type to receive the sections of the input image
            workingMat.copyTo(masked, mask); //Copies only the regions of the input image contained in the mask, and therefore the circle drawn in the mask
            double score = calculateScore(masked); //Calculates the score of the circle
            //Releases undeeded matrices to avoid memory leak
            mask.release();
            masked.release();
            results++; //Increments circle count by one

            Imgproc.circle(displayMat, new Point(circle.x, circle.y), (int) circle.radius, new Scalar(0,0,255),2); //Draws the detected circle
            //If the current circle has a better score than the prior best, then it is now the current best circle
            if(score < bestDifference){
                bestDifference = score;
                bestCircle = circle;
            }
        }
        //Draws a red circle around the best circle, if one is detected at all
        if(bestCircle != null){
            Imgproc.circle(displayMat, new Point(bestCircle.x, bestCircle.y), (int) bestCircle.radius, new Scalar(255,0,0),4);
            Imgproc.putText(displayMat, "Chosen", new Point(bestCircle.x, bestCircle.y),0,.8,new Scalar(255,255,255));
            foundCircle = bestCircle;
            isFound = true;
        }else{
            isFound = false;
            foundCircle = null;
        }
        //The ActivityViewDisplay accepts RGBA images, so converts to that format
        Imgproc.cvtColor(displayMat, displayMat, Imgproc.COLOR_RGB2RGBA);
        return displayMat;
    }

    @Override
    public void useDefaults() {
        addScorer(stdDevScorer);
    }

    public boolean isFound() {
        return isFound;
    }

    /**
     * Returns the best circle detected
     * @return A circle object
     */
    public Circle getFoundCircle() {
        return foundCircle;
    }


}
