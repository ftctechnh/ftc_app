package org.swerverobotics.library.interfaces;

/**
 * The public interface to a gamepad object
 */
public interface IGamepad
    {
    //----------------------------------------------------------------------------------------------
    // Basic state access
    //----------------------------------------------------------------------------------------------

    float left_stick_x();
    float left_stick_y();
    float right_stick_x();
    float right_stick_y();
    boolean dpad_up();
    boolean dpad_down();
    boolean dpad_left();
    boolean dpad_right();
    boolean a();
    boolean b();
    boolean x();
    boolean y();
    boolean guide();
    boolean start();
    boolean back();
    boolean left_bumper();
    boolean right_bumper();
    float left_trigger();
    float right_trigger();
    byte user();
    int id();
    long timestamp();

    //----------------------------------------------------------------------------------------------
    // Methods
    //----------------------------------------------------------------------------------------------

    boolean atRest();
    void    setJoystickDeadzone(float deadzone);
    float   getJoystickDeadzone();
    }
