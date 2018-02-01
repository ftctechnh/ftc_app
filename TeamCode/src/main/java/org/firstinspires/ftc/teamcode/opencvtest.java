package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.GlyphDetector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Peter on 1/25/2018.
 */
@TeleOp(name = "cvtest", group = "linear OpMode")
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
        long precision = 10;
        while(!(-precision < glyphDetector.getChosenGlyphOffset()) && (glyphDetector.getChosenGlyphOffset() < precision)) {
            if(glyphDetector.getChosenGlyphOffset() > 0) {//if the glyph is too far to the right:
                drive.driveTranslateRotateNonstop(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED);
            }else {//if the glyph is too far to the left:
                drive.driveTranslateRotateNonstop(0, 0, -drive.SPIN_ON_BALANCE_BOARD_SPEED);
            }

            drive.stopMotors();

            telemetry.addData("Glyph Position", glyphDetector.getChosenGlyphPosition());
            telemetry.addData("Glyph Pos X", glyphDetector.getChosenGlyphOffset());
        }
        //grab the glyph:
        drive.init();
        drive.rightGyro(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED, -90);
        ForkLift.openClaw();
        drive.driveTranslateRotate(0.5, 0, 0, 36);
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

