package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.GlyphDetector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Peter on 1/25/2018.
 */

public class opencvtest extends OpMode{

    private GlyphDetector glyphDetector;

    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private AutoDrive drive;
    private JewelArm jewelArm;
    private Systems Systems;

    public void init() {
        glyphDetector = new GlyphDetector();
        glyphDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        glyphDetector.minScore = 1;
        glyphDetector.downScaleFactor = 0.3;
        glyphDetector.speed = GlyphDetector.GlyphDetectionSpeed.SLOW;
        glyphDetector.enable();

        jewelArm = new JewelArm(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        RelicClaw = new RelicClaw(hardwareMap, telemetry);
        RelicClaw.init();
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init();

        //While the glyph is not within 10 pixels of the center:
        telemetry.addData("Glyph Pos X", glyphDetector.getChosenGlyphOffset());
        while(!(-10 < glyphDetector.getChosenGlyphOffset()) && (glyphDetector.getChosenGlyphOffset() < 10)) {
            if(glyphDetector.getChosenGlyphOffset() > 0) {//if the glyph is too far to the right:
                drive.driveTranslateRotate(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED, 1);
            }else {//if the glyph is too far to the left:
                drive.driveTranslateRotate(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED, -1);
            }
            telemetry.addData("Glyph Pos X", glyphDetector.getChosenGlyphOffset());
            sleep(1000);
        }
        //grab the glyph:
        drive.driveTranslateRotate(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED, 10);
        ForkLift.closeClaw();
        drive.driveTranslateRotate(1, 0, 0, 12);
        ForkLift.closeClaw();
    }
    public void loop() {
        sleep(1000);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }
}

