package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Map;

/**
 * Common tools for handling op modes
 */

public final class OpModeTools {

    /*
     * updates telemetry values
     */
    public static void debug(OpMode opMode) {
        debug(opMode, true);
    }
    public static void debug(OpMode opMode, boolean update) {
        // For each motor
        for (Map.Entry<String, DcMotor> entry : opMode.hardwareMap.dcMotor.entrySet()) {
            opMode.telemetry.addData("Motor Power", entry.getKey() + "="
                    + entry.getValue().getController().getMotorPower(entry.getValue().getPortNumber()));
        }

        if(update) {
            opMode.telemetry.update();
        }
    }

    /*
     * Stops all motors
     */
    public static void stop(OpMode opMode) {
        for (Map.Entry<String, DcMotor> entry : opMode.hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
    }
}
