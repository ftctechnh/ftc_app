package org.firstinspires.ftc.teamcode.core;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * API for accessing detection state results from the {@link SamplingOrderDetector} routine.
*/
public class ResourceAPI
{
    private SamplingOrderDetector detector;

    public ResourceAPI(HardwareMap hwMap)
    {
        // Setup detector
        detector = new SamplingOrderDetector(); // Create the detector
        detector.init(hwMap.appContext, CameraViewDisplay.getInstance()); // Initialize detector with app context and camera
        detector.useDefaults(); // Set detector to use default settings

        detector.downscale = 0.4; // How much to downscale the input frames

        // Optional tuning
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;

        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;
    }

    public void enableDetection(){
        detector.enable();
    }

    public void disableDetector(){
        detector.disable();
    }

    public SamplingOrderDetector.GoldLocation getCurrentOrder() {
        return detector.getCurrentOrder();
    }

    public SamplingOrderDetector getDetectorReference(){
        return detector;
    }
}