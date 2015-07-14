package org.swerverobotics.library;

// Issues & Questions
//      * How do we configure a motor as having an encoder or not
//      * Separately, how do we 'mode' a motor to USE its controller if it has one?

/**
 * Interface to continuous rotation motors
 */
public interface IMotor
    {
    /**
     * The level of exertion to be used by this motor, expressed as a (positive or negative)
     * fraction of its capability, that is, as a value between -1 and 1 (inclusively). Setting
     * power to zero will stop the motor.
     */
    double power();
    void setPower(double power);

    /**
     * Control the sense with respect to which the motor's power and encoders are
     * interpreted.
     *
     * Setting reflected to true will cause all power and encoder values to be
     * inverted, that is, to be multiplied by minus one. This capability facilitates the ability
     * to group several motors together such that they can act as one.
     */
    boolean reflected();
    void setReflected(boolean reflected);

    /**
     * Retrieve the encoder associated with this motor, if any exists.
     *
     * There is no corresponding 'set' method for encoders, as the connection
     * between a motor and its encoder is, generally, something defined by the
     * hardware and cannot be controlled by software.
     */
    IMotorEncoder encoder();

    // more to come
    }
