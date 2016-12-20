package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Map;

/**
 * a basic teleop template
 */

public abstract class TeleOpMode extends OpMode {
    // State
    public OmniWheelDriver driver;

    /*
     * Initializes robot
     */
    public void init() {
        driver = OmniWheelDriver.build(this);
    }

    /*
     * called on stop
     */
    public void stop() {
        OpModeTools.stop(this);
    }

    /*
     * Updates telemetry readings
     */
    public void debug() {
        OpModeTools.debug(this);
    }
}
