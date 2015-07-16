package org.swerverobotics.library;

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

    private com.qualcomm.robotcore.hardware.Gamepad hwPad;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeGamepad(com.qualcomm.robotcore.hardware.Gamepad hwPad)
        {
        this.hwPad = hwPad;
        }

    //----------------------------------------------------------------------------------------------
    // Gamepad access
    //----------------------------------------------------------------------------------------------

    public synchronized float left_stick_x()
        {
        return hwPad.left_stick_x;
        }
    public synchronized float left_stick_y()
        {
        return hwPad.left_stick_y;
        }
    public synchronized float right_stick_x()
        {
        return hwPad.right_stick_x;
        }
    public synchronized float right_stick_y()
        {
        return hwPad.right_stick_y;
        }
    public synchronized boolean dpad_up()
        {
        return hwPad.dpad_up;
        }
    public synchronized boolean dpad_down()
        {
        return hwPad.dpad_down;
        }
    public synchronized boolean dpad_left()
        {
        return hwPad.dpad_left;
        }
    public synchronized boolean dpad_right()
        {
        return hwPad.dpad_right;
        }
    public synchronized boolean a()
        {
        return hwPad.a;
        }
    public synchronized boolean b()
        {
        return hwPad.b;
        }
    public synchronized boolean x()
        {
        return hwPad.x;
        }
    public synchronized boolean y()
        {
        return hwPad.y;
        }
    public synchronized boolean guide()
        {
        return hwPad.guide;
        }
    public synchronized boolean start()
        {
        return hwPad.start;
        }
    public synchronized boolean back()
        {
        return hwPad.back;
        }
    public synchronized boolean left_bumper()
        {
        return hwPad.left_bumper;
        }
    public synchronized boolean right_bumper()
        {
        return hwPad.right_bumper;
        }
    public synchronized float left_trigger()
        {
        return hwPad.left_trigger;
        }
    public synchronized float right_trigger()
        {
        return hwPad.right_trigger;
        }
    public synchronized byte user()
        {
        return hwPad.user;
        }
    public synchronized int id()
        {
        return hwPad.id;
        }
    public synchronized long timestamp()
        {
        return hwPad.timestamp;
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
        boolean result = !equals(this.hwPad, hwPad);
        this.hwPad = hwPad;
        return result;
        }

    /**
     * Are the states of the two gamepads equivalent?
     */
    private static boolean equals(com.qualcomm.robotcore.hardware.Gamepad p1, com.qualcomm.robotcore.hardware.Gamepad p2)
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
