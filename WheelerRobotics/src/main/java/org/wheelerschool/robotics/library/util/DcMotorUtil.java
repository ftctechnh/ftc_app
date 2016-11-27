package org.wheelerschool.robotics.library.util;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

/**
 * Helpful functions for "DcMotor"s.
 *
 * @see com.qualcomm.robotcore.hardware.DcMotor
 *
 * @author luciengaitskell
 * @version 1.0
 * @since 161127
 */

public class DcMotorUtil {
    private DcMotorUtil() {}  // Prevent instantiation

    public static void setMotorsPower(List<DcMotor> motors, double power) {
        /**
         * Set the power of all motors in a list.
         */
        for (DcMotor mtr : motors) {
            mtr.setPower(power);
        }
    }
}
