package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by guberti on 10/6/2017.
 */

public class ManualHeadingAdjustmentController {
    double ADJUSTMENT_INTERVAL = (1.5 / 360) * Math.PI;

    Gamepad adjuster;
    boolean prevLeftBumper;
    boolean prevRightBumper;

    public ManualHeadingAdjustmentController(Gamepad gp2) {
        adjuster = gp2;
        prevLeftBumper = false;
        prevRightBumper = false;
    }

    public double getHeadingAdjustments() {
        double adjustment = 0;
        if (adjuster.left_bumper && !prevLeftBumper) {
            adjustment += ADJUSTMENT_INTERVAL;
        } else if (adjuster.right_bumper && !prevRightBumper) {
            adjustment -= ADJUSTMENT_INTERVAL;
        }
        return adjustment;
    }
}
