package org.firstinspires.ftc.teamcode.mechanism.drivetrain;

/**
 * A directional drive train represents a drive train that implements
 * the ability to drive in any direction and can also pivot while driving.
 */
public interface IDirectionalDriveTrain extends IDriveTrain {

    /**
     * Drive in the direction specified as an angle in degrees at the specified speed for the set
     * distance
     *
     * @param angleDegrees the angle to drive at in relation to the robot. <br>
     *                     0 deg = Forward <br>
     *                     90 deg = Right <br>
     *                     -90 deg = Left <br>
     *                     180 deg = Backwards <br>
     * @param speed speed at which to drive at
     * @param targetDistance the distance the robot should drive <b>in inches</b>
     */
    void directionalDrive(double angleDegrees, double speed, int targetDistance);

    /**
     * Drives the robot in the direction specified by {@code speedX} and {@code speedY}
     * indefinitely unless overridden by some other command.
     *
     * @param speedX the axial movement speed
     * @param speedY the lateral movement speed
     */
    void drive(double speedX, double speedY);
}
