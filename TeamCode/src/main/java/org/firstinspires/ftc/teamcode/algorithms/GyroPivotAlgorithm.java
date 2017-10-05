package org.firstinspires.ftc.teamcode.algorithms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by daniel on 10/3/17.
 */

public interface GyroPivotAlgorithm {
    /**
     * Pivot the robot to the specified degree angle using a gyroscopic sensor.
     *
     * @param speed the speed of the drive motors while pivoting
     * @param angle the degree angle to pivot to; a negative value is
     *              counter clockwise and a positive value is clockwise
     * @param absolute whether or not the angle should be relative to where the
     *                 gyro sensor last calibrated or the robot's current rotational position
     * @param nonBlocking whether this method should block. If this method is called by a
     *                    non-{@link LinearOpMode}, this method will always be non-blocking, regardless
     *                    of the value for this parameter, due to the nature of non-{@link LinearOpMode}.
     */
    void pivot(double speed, double angle, boolean absolute, boolean nonBlocking);
}