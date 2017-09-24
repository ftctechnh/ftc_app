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
     * @param angleDegrees the angle of which to drive at
     * @param speed speed at which to drive at
     * @param targetDistance the distance the robot should drive <b>in inches</b>
     */
    void directionalDrive(double angleDegrees, double speed, int targetDistance);

    /**
     * Drives the robot in the direction specified by {@code speedX} and {@code speedY}
     * indefinitely.
     *
     * @param speedX the axial movement speed
     * @param speedY the lateral movement speed
     */
    void drive(double speedX, double speedY);
}
