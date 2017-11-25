package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


/**
 * <p>
 *      Utility class used to manage pulses in a continuous loop.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
final class UtilPulsar
{
    private long startTime = System.currentTimeMillis();            // Start time of the pulse


    /**
     * Pulsing function that returns true on certain intervals of time and false every other time.
     * Function should be called in a loop, where it is run an checked every iteration.
     *
     *
     * @param period The length of time between each pulse in milliseconds
     *
     * @return True or false depending on system time. True is returned if a "pulse" is emitted.
     *         False is returned every other time.
     */
    boolean pulse(int period)
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