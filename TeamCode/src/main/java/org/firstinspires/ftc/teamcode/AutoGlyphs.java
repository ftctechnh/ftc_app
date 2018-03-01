package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.GlyphDetector;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.opencv.core.Point;

/**
 * Created by Aus on 2/20/18.
 */

public class AutoGlyphs extends GlyphDetector {
    static final double X_HIGH_POS = 240;
    static final double X_CENTER = X_HIGH_POS / 2;
    static final double Y_HIGH_POS = 200; //Austin test this number with GlyphOpMode.
    static final double Y_CENTER = Y_HIGH_POS / 2;

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry) {
        this(hardwareMap, telemetry, GlyphDetectionSpeed.VERY_SLOW, CameraDirection.BACK);
    }

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry, GlyphDetectionSpeed speed, CameraDirection cameraDirection) {
        super();
        init(hardwareMap.appContext, CameraViewDisplay.getInstance(), cameraDirection.direction);
        this.minScore = 1;
        this.downScaleFactor = 0.3;
        this.speed = speed;
    }

    public void enable() {
        super.enable();
    }

    public void disable() {
        super.disable();
    }

    public double getXOffset() {
        return super.getChosenGlyphOffset() - X_CENTER;
    }

    public double getYOffset() {
        return super.getChosenGlyphPosition().x - Y_CENTER;
    }

    public Point getPoint() {
        return new Point(getXOffset(), getYOffset());
    }
}
