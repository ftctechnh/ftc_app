package org.firstinspires.ftc.teamcode.mechanism.drivetrain;

/**
 * A directional drive train represents a drive train that implements
 * the ability to drive in any direction and can also pivot while driving.
 */
public interface IDirectionalDriveTrain extends IDriveTrain {

    /**
     * Drive in the direction as specified as a velocity vector composed of {@code speedX}
     * representing the axial speed and {@code speedY} representing the lateral speed.
     * <p>
     * The {@code pivotSpeed} parameter is used to pivot (or rotate) the robot while driving.
     * <p>
     * Setting {@code targetDistance} to zero will drive the robot indefinitely,
     * until {@link #stopDriveMotors()} has been called.
     *
     * @param speedX the axial robot speed
     * @param speedY the lateral robot speed
     * @param pivotSpeed speed at which to pivot while driving
     * @param targetDistance the distance the robot should drive
     */
    void directionalDrive(double speedX, double speedY, double pivotSpeed, int targetDistance);
}
