package org.firstinspires.ftc.teamcode.mechanism;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * A mechanism represents a discrete piece of hardware on the robot
 * whose operations can be encapsulated in an implementing class.
 */
public interface IMechanism {
    /**
     * Initialize this mechanism with a reference to the calling op-mode
     * to provide access to telemetry and also the hardware map for
     * mechanism-specific hardware initialization.
     *
     * @param opMode the calling op-mode
     */
    void initialize(OpMode opMode);
}
