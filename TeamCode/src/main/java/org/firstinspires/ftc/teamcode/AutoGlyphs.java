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
    double xPos = DEFAULT_X_POS_VALUE;
    static final double X_HIGH_POS = 384;
    static final double X_CENTER = X_HIGH_POS / 2;
    static final double Y_HIGH_POS = 216;
    static final double Y_CENTER = Y_HIGH_POS / 2;
    static final double X_POSITION_OFFSET = 31.5;
    static final double DEFAULT_X_POS_VALUE = 1000;

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry) {
        this(opMode.hardwareMap, opMode.elemetry, GlyphDetectionSpeed.VERY_FAST, 0);
    }

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry, GlyphDetectionSpeed speed, CameraDirection cameraDirection) {
        this(hardwareMap, telemetry, speed, cameraDirection.direction);
    }

    public AutoGlyphs(HardwareMap hardwareMap, Telemetry telemetry, GlyphDetectionSpeed speed, int cameraDirection) {
        super();
        super.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), cameraDirection);
        this.minScore = 0.5;
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
        return getXPos() + X_POSITION_OFFSET;
    }

    public double getXPos() {
        return super.getChosenGlyphPosition().y - Y_CENTER;
    }

    public double getYPos() {
        return super.getChosenGlyphPosition().x - X_CENTER;
    }

    public double getSize() {return super.getSize();}

    public Point getPoint() {
        return new Point(getXOffset(), getYPos());
    }
}
