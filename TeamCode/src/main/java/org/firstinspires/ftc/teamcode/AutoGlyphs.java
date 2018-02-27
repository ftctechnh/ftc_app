package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.GlyphDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Aus on 2/20/18.
 */

public class AutoGlyphs extends GlyphDetector {
    static final double X_HIGH_POS = 240; //Austin test this number with GlyphOpMode. It puts a number on telemetry with the name "Glyph X Pos". This should be the highest possible value it outputs.
    static final double X_CENTER = X_HIGH_POS / 2;
    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry) {
        super();
        init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        minScore = 1;
        downScaleFactor = 0.3;
        speed = GlyphDetector.GlyphDetectionSpeed.SLOW;
    }
    public void enable() {
        enable();
    }
    public void disable() {
      disable();
    }
    public double getxOffset() {
        return getChosenGlyphOffset() - X_CENTER;
    }
    public double getYOffSet() {
        return getChosenGlyphPosition().x;
    }
}
