package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDriveTrain;

/**
 * The IRobot interface represents a specific robot implementation.
 * Methods common to all robots, such as {@link #getDriveTrain()}, are provided in this interface.
 * This allows robot algorithms instances, for example, to easily access objects generic to all robots.
 */

public interface IRobot {
    /**
     * Returns the drive train being used by the robot.
     *
     * @return the drive train in use by the robot
     */
    IDriveTrain getDriveTrain();

    /**
     * Returns the op-mode currently using this robot.
     *
     * @return the op-mode that is currently utilizing this robot instance
     */
    OpMode getCurrentOpMode();
}
