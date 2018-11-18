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

import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

public class SamplingDetector extends SamplingOrderDetector {

    private SamplingOrderDetector samplingDetector = new SamplingOrderDetector();
    HardwareMap hwmap;
    Robot robot = new Robot();

    public void initVision(HardwareMap ahwmap) {

//        samplingDetector = new SamplingOrderDetector();
        initVision(ahwmap, robot.constants.CAMERA_AIM_DIRECTION);

    }

    public void initVision(HardwareMap ahwmap, CameraCropAngle cropAngle) {
        hwmap = ahwmap;

        if (cropAngle == CameraCropAngle.LEFT) {
            samplingDetector.positionCamRight = false;
            samplingDetector.positionCamLeft = true;
        }
        else if (cropAngle == CameraCropAngle.RIGHT) {
            samplingDetector.positionCamRight = true;
            samplingDetector.positionCamLeft = false;
        }
        else if (cropAngle == CameraCropAngle.NO_CROP) {
            samplingDetector.positionCamRight = false;
            samplingDetector.positionCamLeft = false;
        }
        else {
            samplingDetector.positionCamRight = false;
            samplingDetector.positionCamLeft = false;
        }

        samplingDetector.init(hwmap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        samplingDetector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;
        samplingDetector.useDefaults(); // Set detector to use default settings

        samplingDetector.downscale = 0.6; // How much to downscale the input frames

        // Optional tuning
        samplingDetector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        samplingDetector.maxAreaScorer.weight = 0.001;

        samplingDetector.ratioScorer.weight = 10;
        samplingDetector.ratioScorer.perfectRatio = 1.0;
    }

    public void enableVision() {
        samplingDetector.enable();
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
