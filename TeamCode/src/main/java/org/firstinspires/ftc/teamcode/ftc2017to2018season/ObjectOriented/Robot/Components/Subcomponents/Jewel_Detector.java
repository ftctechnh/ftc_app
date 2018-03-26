
package org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot.Components.Subcomponents;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Jewel_Detector extends LinearOpMode {

    private JewelDetector jewelDetector = null;

    public Jewel_Detector() {
        this.jewelDetector = jewelDetector;
    }

    public void initialize() {
        jewelDetector = new JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        jewelDetector.areaWeight = 0.02;
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.MAX_AREA;
        jewelDetector.debugContours = true;
        jewelDetector.maxDiffrence = 15;
        jewelDetector.ratioWeight = 15;
        jewelDetector.minArea = 700;

        jewelDetector.enable();
    }

    public jewelOrder getOrder(){

        jewelOrder currentOrder;

        switch (jewelDetector.getCurrentOrder()) {
            case BLUE_RED:
                currentOrder = jewelOrder.BLUE_RED;
                break;
            case RED_BLUE:
                currentOrder = jewelOrder.RED_BLUE;
                break;
            case UNKNOWN:
                currentOrder = jewelOrder.UNKNOWN;
                break;
            default:
                currentOrder = jewelOrder.UNKNOWN;
                break;
        }
        return currentOrder;
    }

    public void runOpMode() {
    }


    private enum jewelOrder {
        RED_BLUE,
        BLUE_RED,
        UNKNOWN;
    }
}
