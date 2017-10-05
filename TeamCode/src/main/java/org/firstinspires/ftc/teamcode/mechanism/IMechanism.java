package org.firstinspires.ftc.teamcode.mechanism;

import org.firstinspires.ftc.teamcode.IRobot;

/**
 * A mechanism represents a discrete piece of hardware on the robot
 * whose operations can be encapsulated in an implementing class.
 */
public interface IMechanism {
    /**
     * Initialize this mechanism with a reference to the robot to provide
     * access to the current op-mode, telemetry, and also the hardware map for
     * mechanism-specific hardware initialization.
     *
     * @param robot the robot using this mechanism
     */
    void initialize(IRobot robot);
}
