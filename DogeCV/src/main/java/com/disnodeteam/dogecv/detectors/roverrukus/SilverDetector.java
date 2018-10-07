package com.disnodeteam.dogecv.detectors.roverrukus;

import android.util.Log;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.HSVColorFilter;
import com.disnodeteam.dogecv.filters.HSVRangeFilter;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;
import com.disnodeteam.dogecv.scoring.PerfectAreaScorer;
import com.disnodeteam.dogecv.scoring.RatioScorer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public class SilverDetector extends DogeCVDetector {

    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;

    public RatioScorer ratioScorer = new RatioScorer(1.0,1);
    public MaxAreaScorer maxAreaScorer = new MaxAreaScorer(0.005);
    public PerfectAreaScorer perfectAreaScorer = new PerfectAreaScorer(5000,0.05);
    public DogeCVColorFilter whiteFilter  = new HSVRangeFilter(new Scalar(0,0,200), new Scalar(50,40,255));


    private Mat whiteMask = new Mat();
    private Mat workingMat = new Mat();
    private Mat hiarchy    = new Mat();
    private int results;
    private Rect foundRect;
    private boolean isFound = false;

    public SilverDetector() {
        super();
        this.detectorName = "Silver Detector";
    }

    @Override
    public Mat process(Mat input) {
        if(input.channels() < 0 || input.cols() <= 0){
            Log.e("DogeCV", "Bad INPUT MAT!");
        }
        input.copyTo(workingMat);
        Imgproc.GaussianBlur(workingMat,workingMat,new Size(5,5),0);
        whiteFilter.process(workingMat,whiteMask);

        List<MatOfPoint> contoursYellow = new ArrayList<>();

        Imgproc.findContours(whiteMask, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursYellow,-1,new Scalar(230,70,70),2);
        results = 0;

        Rect bestRect = null;
        double bestDiffrence = Double.MAX_VALUE;

        for(MatOfPoint cont : contoursYellow){
            double score = calculateScore(cont);

            results++;

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);
            Imgproc.rectangle(workingMat, rect.tl(), rect.br(), new Scalar(0,0,255),2);

            if(score < bestDiffrence){
                bestDiffrence = score;
                bestRect = rect;
            }
        }

        if(bestRect != null){
            Imgproc.rectangle(workingMat, bestRect.tl(), bestRect.br(), new Scalar(255,0,0),4);
            Imgproc.putText(workingMat, "Chosen", bestRect.tl(),0,2,new Scalar(255,255,255));
            foundRect = bestRect;
            isFound = true;
        }else{
            isFound = false;
            foundRect = null;
        }



        return workingMat;
    }

    @Override
    public void useDefaults() {
        if(areaScoringMethod == DogeCV.AreaScoringMethod.MAX_AREA){
            addScorer(maxAreaScorer);
        }

        if (areaScoringMethod == DogeCV.AreaScoringMethod.PERFECT_AREA){
            addScorer(perfectAreaScorer);
        }
        addScorer(ratioScorer);
    }

    public boolean isFound() {
        return isFound;
    }

    public Rect getFoundRect() {
        return foundRect;
    }
}
