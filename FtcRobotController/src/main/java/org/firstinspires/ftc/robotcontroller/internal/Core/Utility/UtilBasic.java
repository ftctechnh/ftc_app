/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;


/**
 * Basic utility functions (mostly mathematical) that are sprinkled across the code base.
 */
@SuppressWarnings({"unused" , "WeakerAccess"})
public final class UtilBasic
{
    /**
     * Scales a value. Useful for joysticks so that a "soft zone" is created for more accurate
     * movement.
     *
     * @param VALUE The value to be scaled
     * @return Returns the scaled value
     */
    public static double scaleValue(final double VALUE)
    {
        return Math.pow(VALUE , 3);
    }


    /**
     * Trims an angle to between 0 and 360 degrees
     *
     * @param angle The angle to trim
     * @return Returns the trimmed angle
     */
    public static int trimAngle(int angle)
    {
        angle %= 360;

        if(angle < 0)
            angle += 360;

        return angle;
    }


    /**
     * Returns the smallest angle error.
     *
     * @param INITIAL Initial angle
     * @param target Target angle
     * @return Returns the smallest error from the initial to the target
     */
    public static int angleError(final int INITIAL , int target)
    {
        int initialTarget = target;         // Hold the initial target for use later
        int error;                          // The error between the initial angle and the target

        if(target < INITIAL)
            target += 360;

        error = target - INITIAL;

        if(Math.abs(error) > 180)
            error = Math.abs(360 - error);

        if(trimAngle(error + INITIAL) != initialTarget)
            error *= -1;

        return error;
    }
}