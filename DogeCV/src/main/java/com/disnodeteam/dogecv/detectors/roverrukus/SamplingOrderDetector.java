package com.disnodeteam.dogecv.detectors.roverrukus;

import android.util.Log;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.HSVRangeFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;
import com.disnodeteam.dogecv.scoring.PerfectAreaScorer;
import com.disnodeteam.dogecv.scoring.RatioScorer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 *
 * Basic sampling order detector - users are encouraged to tune it to their own needs.
 */

public class SamplingOrderDetector extends DogeCVDetector {

    // Enum to describe gold location
    public enum GoldLocation {
        UNKNOWN,
        LEFT,
        CENTER,
        RIGHT
    }

    // Which area scoring method to use
    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;

    //Create the scorers used for the detector
    public RatioScorer ratioScorer             = new RatioScorer(1.0,5);
    public MaxAreaScorer maxAreaScorer         = new MaxAreaScorer(0.01);
    public PerfectAreaScorer perfectAreaScorer = new PerfectAreaScorer(5000,0.05);

    //Create the filters used
    public DogeCVColorFilter yellowFilter = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW,100);
    public DogeCVColorFilter whiteFilter  = new HSVRangeFilter(new Scalar(0,0,200), new Scalar(50,40,255));


    // Results for the detector
    private GoldLocation currentOrder = GoldLocation.UNKNOWN;
    private GoldLocation lastOrder    = GoldLocation.UNKNOWN;
    private boolean      isFound      = false;

    // Create the mats used
    private Mat workingMat  = new Mat();
    private Mat displayMat  = new Mat();
    private Mat yellowMask  = new Mat();
    private Mat whiteMask   = new Mat();
    private Mat hiarchy     = new Mat();

    public SamplingOrderDetector() {
        super();
        this.detectorName = "Sampling Order Detector";
    }

    @Override
    public Mat process(Mat input) {

        // Copy input mat to working/display mats
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();

        // Generate Masks
        yellowFilter.process(workingMat.clone(), yellowMask);
        whiteFilter.process(workingMat.clone(), whiteMask);


        // Blur and find the countours in the masks
        List<MatOfPoint> contoursYellow = new ArrayList<>();
        List<MatOfPoint> contoursWhite = new ArrayList<>();

        Imgproc.blur(whiteMask,whiteMask,new Size(2,2));
        Imgproc.blur(yellowMask,yellowMask,new Size(2,2));

        Imgproc.findContours(yellowMask, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursYellow,-1,new Scalar(230,70,70),2);

        Imgproc.findContours(whiteMask, contoursWhite, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursWhite,-1,new Scalar(230,70,70),2);


        // Prepare to find best yellow (gold) results
        Rect   chosenYellowRect  = null;
        double chosenYellowScore = Integer.MAX_VALUE;

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for(MatOfPoint c : contoursYellow){
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            double diffrenceScore = calculateScore(points);

            if(diffrenceScore < chosenYellowScore && diffrenceScore < maxDifference){
                chosenYellowScore = diffrenceScore;
                chosenYellowRect = rect;
            }

            double area = Imgproc.contourArea(c);
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));
            if( area > 500){
                Imgproc.circle(displayMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(displayMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
            }
        }

        // Prepare to find best white (silver) results
        List<Rect>   choosenWhiteRect  = new ArrayList<>(2);
        List<Double> chosenWhiteScore  = new ArrayList<>(2);
        chosenWhiteScore.add(0, Double.MAX_VALUE);
        chosenWhiteScore.add(1, Double.MAX_VALUE);
        choosenWhiteRect.add(0, null);
        choosenWhiteRect.add(1, null);


        for(MatOfPoint c : contoursWhite){
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            double diffrenceScore = calculateScore(points);

            double area = Imgproc.contourArea(c);
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));
            if( area > 1000){
                Imgproc.circle(displayMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(displayMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
                Imgproc.putText(displayMat,"Diff: " + diffrenceScore,new Point(centerPoint.x, centerPoint.y + 20),0,0.5,new Scalar(0,255,255));
            }

            boolean good = true;
            if(diffrenceScore < maxDifference && area > 1000){

                if(diffrenceScore < chosenWhiteScore.get(0)){
                    choosenWhiteRect.set(0,rect);
                    chosenWhiteScore.set(0,diffrenceScore);
                }
                else if(diffrenceScore < chosenWhiteScore.get(1) && diffrenceScore > chosenWhiteScore.get(0)){
                    choosenWhiteRect.set(1,rect);
                    chosenWhiteScore.set(1, diffrenceScore);
                }
            }


        }

        //Draw found gold element
        if(chosenYellowRect != null){
            Imgproc.rectangle(displayMat,
                    new Point(chosenYellowRect.x, chosenYellowRect.y),
                    new Point(chosenYellowRect.x + chosenYellowRect.width, chosenYellowRect.y + chosenYellowRect.height),
                    new Scalar(255, 0, 0), 2);

            Imgproc.putText(displayMat,
                    "Gold: " + String.format("%.2f X=%.2f", chosenYellowScore, (double)chosenYellowRect.x),
                    new Point(chosenYellowRect.x - 5, chosenYellowRect.y - 10),
                    Core.FONT_HERSHEY_PLAIN,
                    1.3,
                    new Scalar(0, 255, 255),
                    2);

        }
        //Draw found white elements
        for(int i=0;i<choosenWhiteRect.size();i++){
            Rect rect = choosenWhiteRect.get(i);
            if(rect != null){
                double score = chosenWhiteScore.get(i);
                Imgproc.rectangle(displayMat,
                        new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(255, 255, 255), 2);
                Imgproc.putText(displayMat,
                        "Silver: " + String.format("Score %.2f ", score) ,
                        new Point(rect.x - 5, rect.y - 10),
                        Core.FONT_HERSHEY_PLAIN,
                        1.3,
                        new Scalar(255, 255, 255),
                        2);
            }


        }

        // If enough elements are found, compute gold position
        if(choosenWhiteRect.get(0) != null && choosenWhiteRect.get(1) != null  && chosenYellowRect != null){
            int leftCount = 0;
            for(int i=0;i<choosenWhiteRect.size();i++){
                Rect rect = choosenWhiteRect.get(i);
                if(chosenYellowRect.x > rect.x){
                    leftCount++;
                }
            }
            if(leftCount == 0){
                currentOrder = SamplingOrderDetector.GoldLocation.LEFT;
            }

            if(leftCount == 1){
                currentOrder = SamplingOrderDetector.GoldLocation.CENTER;
            }

            if(leftCount >= 2){
                currentOrder = SamplingOrderDetector.GoldLocation.RIGHT;
            }
            isFound = true;
            lastOrder = currentOrder;

        }else{
            currentOrder = SamplingOrderDetector.GoldLocation.UNKNOWN;
            isFound = false;
        }

        //Display Debug Information
        Imgproc.putText(displayMat,"Gold Position: " + lastOrder.toString(),new Point(10,getAdjustedSize().height - 30),0,1, new Scalar(255,255,0),1);
        Imgproc.putText(displayMat,"Current Track: " + currentOrder.toString(),new Point(10,getAdjustedSize().height - 10),0,0.5, new Scalar(255,255,255),1);

        return displayMat;
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

    /**
     * Is both elements found?
     * @return if the elements are found
     */
    public boolean isFound() {
        return isFound;
    }

    /**
     * Returns the current gold pos
     * @return current gold pos (UNKNOWN, LEFT, CENTER, RIGHT)
     */
    public GoldLocation getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Returns the last known gold pos
     * @return last known gold pos (UNKNOWN, LEFT, CENTER, RIGHT)
     */
    public GoldLocation getLastOrder() {
        return lastOrder;
    }
}
