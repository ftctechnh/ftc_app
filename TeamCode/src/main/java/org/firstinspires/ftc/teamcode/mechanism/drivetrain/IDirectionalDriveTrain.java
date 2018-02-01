package org.firstinspires.ftc.teamcode.mechanism.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

/**
 * A directional drive train represents a drive train that implements
 * the ability to drive in any direction and can also pivot while driving.
 */
public interface IDirectionalDriveTrain extends IDriveTrain {

    /**
     * Drive in the direction specified as an angle in degrees at the specified speed for the set
     * distance. The sign of the target distance determines the direction of movement. For example,
     * a negative target distance results in backward movement. Depending on the op-mode implementation,
     * either the a blocking or non-blocking implementation will be called.
     * The latter indicates completion of the drive movement with {@link #isDriveTrainBusy()}.
     *
     * @param angleDegrees the angle to drive at in relation to the robot. This must be within
     *                     a range of +90 to -90 degrees, where 0 is right, 180 is left,
     *                     90 is forward, and 270 is backward. The convention is similar
     *                     to how angles are measured in math, counterclockwise from the right.
     * @param speed speed to drive within a range of 1.0 to 0
     * @param targetDistance the distance the robot should drive <b>in inches</b>.
     *                       The sign (+/-) of this parameter determines the direction to drive in.
     * @param nonBlocking whether this method should block. If this method is called by a
     *                    non-{@link LinearOpMode}, this method will always be non-blocking, regardless
     *                    of the value for this parameter, due to the nature of non-{@link LinearOpMode}.
     */
    void directionalDrive(double angleDegrees, double speed, int targetDistance, boolean nonBlocking);

    /**
     * Drives the robot in the direction specified by {@code speedX} and {@code speedY}
     * indefinitely unless overridden by some other command.
     *
     * @param speedX the axial movement speed
     * @param speedY the lateral movement speed
     */
    void drive(double speedX, double speedY);
}
