package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.RelicRecoveryHardware;

/**
 * Created by lsatt on 11/25/2017.
 */

public class ReadColor {
    // -------------------------- Objects ---------------------------
    ColorSensor colorSensor;

    private static final int SENSOR_COMPARE_RED = 30;
    private static final int SENSOR_COMPARE_BLUE = 30;

    private boolean ifColorFound = false;

    public enum Color {
        NEITHER,
        RED,
        BLUE
    }

    private Color colorDetected = Color.NEITHER;
    // ------------------------ Constructor -------------------------
    public ReadColor(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }
    // ----------------------- Public Methods -----------------------
    public boolean readColor() {
        if (colorSensor.blue() > SENSOR_COMPARE_BLUE) {
            ifColorFound = true;
            colorDetected = Color.BLUE;
        } else if (colorSensor.red() > SENSOR_COMPARE_RED) {
            ifColorFound = true;
            colorDetected = Color.RED;
        }
        return ifColorFound;
    }

    public Color getColorDetected() {
        return colorDetected;
    }
}
