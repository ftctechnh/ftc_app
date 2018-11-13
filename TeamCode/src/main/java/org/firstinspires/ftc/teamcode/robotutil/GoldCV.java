package org.firstinspires.ftc.teamcode.robotutil;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class GoldCV {

    private GoldAlignDetector goldAlignDetector;
    private LinearOpMode opMode;

    public GoldCV(LinearOpMode opMode) {
        this.opMode = opMode;
        initDetector();
    }

    private void initDetector() {
        goldAlignDetector = new GoldAlignDetector();
        goldAlignDetector.init(opMode.hardwareMap.appContext, CameraViewDisplay.getInstance());
        goldAlignDetector.useDefaults();

        /* DETECTOR PARAMETERS */
        goldAlignDetector.alignSize = 100;
        goldAlignDetector.alignPosOffset = 0;
        goldAlignDetector.downscale = 0.4;

        goldAlignDetector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;
        goldAlignDetector.maxAreaScorer.weight = 0.005;

        goldAlignDetector.ratioScorer.weight = 5;
        goldAlignDetector.ratioScorer.perfectRatio = 1.0;
        /* END OF DETECTOR PARAMETERS */

        goldAlignDetector.enable();
    }

    /**
     * Returns if the gold element is aligned
     * @return if the gold element is alined
     */
    public boolean getAligned() {
        return goldAlignDetector.getAligned();
    }

    /**
     * Returns gold element last x-position
     * @return last x-position in screen pixels of gold element
     */
    public double getXPosition() {
        return goldAlignDetector.getXPosition();
    }

    /**
     * Returns if a gold mineral is being tracked/detected
     * @return if a gold mineral is being tracked/detected
     */
    public boolean isFound() {
        return goldAlignDetector.isFound();
    }
}
