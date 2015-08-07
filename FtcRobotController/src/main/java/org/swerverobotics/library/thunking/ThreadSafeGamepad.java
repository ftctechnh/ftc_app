package org.swerverobotics.library.thunking;

/**
 * A form of GamePad that guards against torn writes to its state caused by concurrency.
 *
 * This is *maybe* being overly paranoid, depending on the atomicity of member variable reads
 * and writes in Java (the 8-byte 'timestamp' member in particular offends), but being careful
 * will with certainty avoid any latent bugs that might just happen to be there.
 */
public class ThreadSafeGamepad
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private float joystickDeadzone = 0.2F;
    public com.qualcomm.robotcore.hardware.Gamepad target;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeGamepad(com.qualcomm.robotcore.hardware.Gamepad target)
        {
        this.target = target;
        }

    //----------------------------------------------------------------------------------------------
    // Basic state access
    //----------------------------------------------------------------------------------------------

    public synchronized float left_stick_x()
        {
        return target.left_stick_x;
        }
    public synchronized float left_stick_y()
        {
        return target.left_stick_y;
        }
    public synchronized float right_stick_x()
        {
        return target.right_stick_x;
        }
    public synchronized float right_stick_y()
        {
        return target.right_stick_y;
        }
    public synchronized boolean dpad_up()
        {
        return target.dpad_up;
        }
    public synchronized boolean dpad_down()
        {
        return target.dpad_down;
        }
    public synchronized boolean dpad_left()
        {
        return target.dpad_left;
        }
    public synchronized boolean dpad_right()
        {
        return target.dpad_right;
        }
    public synchronized boolean a()
        {
        return target.a;
        }
    public synchronized boolean b()
        {
        return target.b;
        }
    public synchronized boolean x()
        {
        return target.x;
        }
    public synchronized boolean y()
        {
        return target.y;
        }
    public synchronized boolean guide()
        {
        return target.guide;
        }
    public synchronized boolean start()
        {
        return target.start;
        }
    public synchronized boolean back()
        {
        return target.back;
        }
    public synchronized boolean left_bumper()
        {
        return target.left_bumper;
        }
    public synchronized boolean right_bumper()
        {
        return target.right_bumper;
        }
    public synchronized float left_trigger()
        {
        return target.left_trigger;
        }
    public synchronized float right_trigger()
        {
        return target.right_trigger;
        }
    public synchronized byte user()
        {
        return target.user;
        }
    public synchronized int id()
        {
        return target.id;
        }
    public synchronized long timestamp()
        {
        return target.timestamp;
        }

    //----------------------------------------------------------------------------------------------
    // Methods
    //----------------------------------------------------------------------------------------------

    public synchronized boolean atRest() 
        {
        return target.atRest();
        }
    public synchronized void setJoystickDeadzone(float deadzone)
        {
        this.joystickDeadzone = deadzone;
        this.target.setJoystickDeadzone(deadzone);
        }
    public synchronized float getJoystickDeadzone()
        {
        return this.joystickDeadzone;
        }
    
    //----------------------------------------------------------------------------------------------
    // Updates
    //----------------------------------------------------------------------------------------------

    /**
     * Update our state to be that of the indicated hw pad. Answer as to whether any
     * of that state was different from what we already had.
     */
    public synchronized boolean update(com.qualcomm.robotcore.hardware.Gamepad hwPad)
        {
        boolean result = !sameHardwareState(this.target, hwPad);
        
        // Transfer settings from old to new guy
        hwPad.setJoystickDeadzone(this.joystickDeadzone);
        
        this.target = hwPad;
        return result;
        }

    /**
     * Are the states of the two gamepads equivalent?
     */
    private static boolean sameHardwareState(com.qualcomm.robotcore.hardware.Gamepad p1, com.qualcomm.robotcore.hardware.Gamepad p2)
        {
        if (p1.left_stick_x != p2.left_stick_x) return false;
        if (p1.left_stick_y != p2.left_stick_y) return false;
        if (p1.right_stick_x != p2.right_stick_x) return false;
        if (p1.right_stick_y != p2.right_stick_y) return false;
        if (p1.dpad_up != p2.dpad_up) return false;
        if (p1.dpad_down != p2.dpad_down) return false;
        if (p1.dpad_left != p2.dpad_left) return false;
        if (p1.dpad_right != p2.dpad_right) return false;
        if (p1.a != p2.a) return false;
        if (p1.b != p2.b) return false;
        if (p1.x != p2.x) return false;
        if (p1.y != p2.y) return false;
        if (p1.guide != p2.guide) return false;
        if (p1.start != p2.start) return false;
        if (p1.back != p2.back) return false;
        if (p1.left_bumper != p2.left_bumper) return false;
        if (p1.right_bumper != p2.right_bumper) return false;
        if (p1.left_trigger != p2.left_trigger) return false;
        if (p1.right_trigger != p2.right_trigger) return false;
        if (p1.user != p2.user) return false;
        if (p1.id != p2.id) return false;
        if (p1.timestamp != p2.timestamp) return false;
        //
        return true;
        }
    }
