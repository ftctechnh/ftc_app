package org.swerverobotics.library;

/**
 *
 */
public interface IMotorEncoder
    {
    /**
     * Retrieve's the encoder's position. The position advances by positionsPerRevolution() for
     * each rotation of the associated motor.
     */
    double position();
    void setPosition(double position);

    /**
     * Controls how much the encoder position advances for each revolution of the associated motor.
     *
     * Default positionsPerRevolution() is 1.0, which has the effect of the encoder simply counting
     * full and partial revolutions.
     *
     * Changing positionsPerRevolution() will maintain the value of the current position as
     * manifest through position().
     */
    double positionsPerRevolution();
    void setPositionsPerRevolution(double positionsPerRevolution);
    }
