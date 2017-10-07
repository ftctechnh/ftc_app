package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Robot is an abstract class that represents a season-specific robot implementation.
 * This class is meant to provide methods that allow robot algorithm
 * instances, for example, to easily access objects generic to all robots.
 * Currently, this class only provides access to the op-mode using this robot.
 */

public abstract class Robot {
    private OpMode opMode;

    /**
     * Create a new robot with a reference to the op-mode using this robot.
     * This is the op-mode to be returned by {@link #getCurrentOpMode()}.
     *
     * @param opMode the op-mode using this robot
     */
    public Robot(OpMode opMode) {
        this.opMode = opMode;
    }

    /**
     * Returns the op-mode currently using this robot.
     *
     * @return the op-mode that is currently utilizing this robot instance
     */
    public OpMode getCurrentOpMode() {
        return opMode;
    }
}
