package org.swerverobotics.library.interfaces;

/**
 * {@link IAccelerationIntegrator} encapsulates an algorithm for integrating
 * acceleration information over time to produce velocity and position.
 *
 * @see IBNO055IMU
 */
public interface IAccelerationIntegrator
    {
    /**
     * Initializes the algorithm with a starting position and velocity. Any timestamps that
     * are present in these data are not to be considered as significant. The initial acceleration
     * should be taken as undefined; you should set it to null when this method is called.
     * @param initialPosition   the starting position
     * @param initialVelocity   the starting velocity
     *
     * @see #update(Acceleration)
     */
    void initialize(Position initialPosition, Velocity initialVelocity);

    /**
     * Returns the current position as calculated by the algorithm
     * @return  the current position
     */
    Position getPosition();

    /**
     * Returns the current velocity as calculated by the algorithm
     * @return  the current velocity
     */
    Velocity getVelocity();

    /**
     * Returns the current acceleration as understood by the algorithm. This is typically
     * just the value provided in the most recent call to {@link #update(Acceleration)}, if any.
     * @return  the current acceleration, or null if the current position is undefined
     */
    Acceleration getAcceleration();

    /**
     * Step the algorithm as a result of the stimulus of new acceleration data.
     * @param acceleration  the acceleration as just reported by the IMU
     */
    void update(Acceleration acceleration);
    }
