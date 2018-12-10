package org.firstinspires.ftc.teamcode.SubAssembly.Leds;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

/* Sub Assembly Class
 */
public class LedControl {
    /* Declare private class object */
    private LinearOpMode opmode = null; /* local copy of HardwareMap object from opmode class */

    //initializing motors
    private RevBlinkinLedDriver LedDriver = null;

    /* Subassembly constructor */
    public LedControl() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opMode.telemetry.addLine("LED Control" + " initialize");
        opMode.telemetry.update();

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        /* Map hardware devices */
        LedDriver = hwMap.get(RevBlinkinLedDriver.class, "LedDriver");
    }

    public void orange() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.ORANGE);
    }

    public void darkRed() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.DARK_RED);
    }

    public void red() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
    }

    public void white() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
    }

    public void darkBlue() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.DARK_BLUE);
    }

    public void skyBlue() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);
    }

    public void darkGreen() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.DARK_GREEN);
    }

    public void lawnGreen() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.LAWN_GREEN);
    }

    public void rainbowRainbowPalette() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
    }

    public void yellow() {
        LedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
    }
}
