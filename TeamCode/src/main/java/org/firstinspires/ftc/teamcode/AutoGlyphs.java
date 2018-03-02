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
    static final double X_HIGH_POS = 384;
    static final double X_CENTER = X_HIGH_POS / 2;
    static final double Y_HIGH_POS = 216; //Austin test this number with GlyphOpMode.
    static final double Y_CENTER = Y_HIGH_POS / 2;
    static final double X_POSITION_OFFSET = 38;

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry) {
        this(hardwareMap, telemetry, GlyphDetectionSpeed.VERY_SLOW, 0);
    }

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry, GlyphDetectionSpeed speed, CameraDirection cameraDirection) {
        this(hardwareMap, telemetry, speed, cameraDirection.direction);
    }
    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry, GlyphDetectionSpeed speed, int cameraDirection) {
        super();
        super.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), cameraDirection);
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
        return super.getChosenGlyphPosition().y - Y_CENTER + X_POSITION_OFFSET;
    }

    public double getYOffset() {
        return super.getChosenGlyphPosition().x - X_CENTER;
    }

    public Point getPoint() {
        return new Point(getXOffset(), getYOffset());
    }
}
