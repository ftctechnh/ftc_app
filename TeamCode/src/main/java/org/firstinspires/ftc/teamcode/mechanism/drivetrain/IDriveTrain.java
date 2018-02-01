package org.firstinspires.ftc.teamcode.mechanism.drivetrain;

import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

/**
 * A representation of a hardware-specific drive train implementation.
 * This interface provides an abstraction for basic drive controls.
 */
public interface IDriveTrain extends IMechanism {

    /**
     * Pivot (rotate) the robot at the given {@code pivotSpeed}.
     *
     * @param pivotSpeed the speed at which to pivot the robot
     */
    void pivot(double pivotSpeed);

    /**
     * Drive forward/backward at the speed of {@code speedY} for {@code targetDistance} inches.
     * The sign (positive or negative) of speedY determines the robot's direction.
     *
     * A negative value corresponds to backwards and a positive value for forward.
     * Setting {@code targetDistance} to zero will drive the robot indefinitely,
     * until {@link #stopDriveMotors()} has been called.
     * <p>
     * Implementations of this method should only implement forward and backward driving as
     * this is the only directional drive control all drive trains provide.
     * For additionally strafing left and right, {@link IDirectionalDriveTrain} may be used.
     *
     * @param speedY the speed at which to drive: negative for backward and positive for forward
     * @param targetDistance the distance the robot should drive or zero to drive indefinitely
     */
    void drive(double speedY, int targetDistance);

    /**
     * Returns whether the drive train is currently in use (i.e. running).
     * A drive movement initiated by any drive method defined in this class
     * will result in this method returning {@code true}, given that it is called
     * during the course of the robot's movement.
     *
     * @return whether the drive train is currently running.
     */
    boolean isDriveTrainBusy();

    /**
     * Stop every motor utilized as part of this drive train.
     * This will cause the robot to come to a complete stop.
     */
    void stopDriveMotors();
}
