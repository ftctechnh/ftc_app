package com.disnodeteam.dogecv.detectors;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.math.MathFTC;
import com.disnodeteam.dogecv.scoring.DogeCVScorer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public abstract class DogeCVDetector extends OpenCVPipeline{

    public abstract Mat process(Mat input);
    public abstract void useDefaults();

    private Size initSize;
    private Size adjustedSize;
    private Mat workingMat = new Mat();
    public double maxDifference = 10;

    public Point cropTLCorner = null; //The top left corner of the image used for processing
    public Point cropBRCorner = null; //The bottom right corner of the image used for processing

    public DogeCV.DetectionSpeed speed = DogeCV.DetectionSpeed.BALANCED;
    public double downscale = 0.5;
    public Size   downscaleResolution = new Size(640, 480);
    public boolean useFixedDownscale = true;
    protected String detectorName = "DogeCV Detector";

    public DogeCVDetector(){

    }

    public void setSpeed(DogeCV.DetectionSpeed speed){
        this.speed = speed;
    }

    public void addScorer(DogeCVScorer newScorer){
        //scorers.add(newScorer);
    }

    public double calculateScore(Mat input){
        double totalScore = 0;

//        for(DogeCVScorer scorer : scorers){
//            totalScore += scorer.calculateScore(input);
//        }

        return totalScore;
    }



    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        initSize = rgba.size();

        rgba.copyTo(workingMat);

        //process
        Core.rotate(workingMat, workingMat, Core.ROTATE_90_COUNTERCLOCKWISE);
        Core.flip(workingMat,workingMat,0);
        Core.flip(workingMat,workingMat,1);

        workingMat = process(workingMat);

        Core.rotate(workingMat, workingMat, Core.ROTATE_90_CLOCKWISE);
        Core.flip(workingMat,workingMat,1);
        Core.flip(workingMat,workingMat,0);
        //resize if not empty

//        if(!processOutput.empty()&!workingMat.empty()) {
//            // Process and scale back to original size for viewing
//            Imgproc.resize(processOutput, workingMat, getInitSize());
//        }


        return workingMat;
    }

    public Size getInitSize() {
        return initSize;
    }

    public Size getAdjustedSize() {
        return adjustedSize;
    }

    public void setAdjustedSize(Size size) { this.adjustedSize = size; }

}
