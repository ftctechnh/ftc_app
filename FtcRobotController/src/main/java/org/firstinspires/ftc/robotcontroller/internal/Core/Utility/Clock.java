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
 * Utility class used to manage pulses in a continuous loop. A tick is defined by a single true
 * value emitted during a loop per set period of time- think of this as a timer.
 */
public final class Clock
{
    private long startTime = System.currentTimeMillis();            // Start time of the tick


    /**
     * Pulsing function that returns true on certain intervals of time and false every other time.
     * Function should be called in a loop, where it is run an checked every iteration.
     *
     *
     * @param period The length of time between each tick in milliseconds
     *
     * @return True or false depending on system time. True is returned if a "tick" is emitted.
     *         False is returned every other time.
     */
    public boolean tick(int period)
    {
        boolean pulsed = false;     // Whether the function pulses or not

        if(System.currentTimeMillis() >= startTime + period)
        {
            startTime = System.currentTimeMillis();
            pulsed = true;
        }

        return pulsed;
    }
}
