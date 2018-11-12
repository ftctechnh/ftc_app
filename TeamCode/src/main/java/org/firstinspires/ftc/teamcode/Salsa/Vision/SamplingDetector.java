package org.firstinspires.ftc.teamcode.Salsa.Vision;

/**
 * Created by adityamavalankar on 11/12/18.
 */

import com.disnodeteam.dogecv.*;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;

public class SamplingDetector {

    public SamplingOrderDetector samplingDetector = new SamplingOrderDetector();
    HardwareMap hwmap;

    public void initVision(HardwareMap ahwmap) {

        hwmap = ahwmap;

        samplingDetector.init(hwmap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        samplingDetector.useDefaults(); // Set detector to use default settings

        samplingDetector.downscale = 0.4; // How much to downscale the input frames

        // Optional tuning
        samplingDetector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        samplingDetector.maxAreaScorer.weight = 0.001;

        samplingDetector.ratioScorer.weight = 15;
        samplingDetector.ratioScorer.perfectRatio = 1.0;

        samplingDetector.enable(); // Start detector
    }

    public SamplingOrderDetector.GoldLocation getSamplingOrder() {

        SamplingOrderDetector.GoldLocation order;
        order = samplingDetector.getCurrentOrder();

        return order;
    }

    public SamplingOrderDetector.GoldLocation getLastOrder() {

        SamplingOrderDetector.GoldLocation lastOrder;
        lastOrder = samplingDetector.getLastOrder();

        return lastOrder;
    }

    public void disableVision() {
        samplingDetector.disable();
    }
}
