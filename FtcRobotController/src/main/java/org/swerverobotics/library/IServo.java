package org.swerverobotics.library;

/**
 * Interface to non-continuous-rotation servos
 */
public interface IServo
    {
    /**
     * Set the desired position of this servo. Then, optionally, initiate movement
     * towards that position.
     *
     * @param newPosition       The new desired position of the servo
     * @param moveImmediately   If true, start moving immediate; if false, defer movement
     *                          until a wait() is requested on this servo. In this way, the
     *                          desired positions of several servos can be indicated, then
     *                          one wait() issued to initiate simultaneous movement and await
     *                          completion.
     */
    void setPosition(double newPosition, boolean moveImmediately);
    /**
     * Start the servo moving from its current position towards a new desired position. The
     * servo is NOT guaranteed to reach the new position before this method returns. This method
     * is equivalent to calling setPosition(newPosition, true).
     *
     * @param newPosition The new desired position of the servo
     */
    void setPosition(double newPosition);

    /**
     * positionsPerRevolution() indicates the amount the servo position will move
     * as the servo (if it could) completed one full revolution. Default is 1.0
     */
    double positionsPerRevolution();
    void setPositionsPerRevolution(double positionsPerRevolution);

    /**
     * The smallest legal position value for this servo. setPosition() requests will be clamped
     * to be >= lowerPosition(). Default is 0.0
     */
    double lowerPosition();
    /**
     * The largest legal position value for this servo. setPosition() requests will be clamped
     * to be <= upperPosition(). Default is 1.0
     */
    double upperPosition();
    /**
     * (Optional) The position at which the object being manipulated reaches its highest
     * physical altitude. This is used to help calculate servo wait durations.
     */
    double apogeePosition();

    void setLowerPosition(double position);
    void setApogeePosition(double position);
    void setUpperPosition(double position);

    /**
     * If reflected() is true, then all the position values of the servo are inverted
     * within the legal servo positioning range.
     */
    boolean reflected();
    void setReflected(boolean reflected);

    // More to come
    }
