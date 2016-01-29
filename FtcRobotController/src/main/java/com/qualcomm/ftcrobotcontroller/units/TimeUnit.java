package com.qualcomm.ftcrobotcontroller.units;

import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;

/**
 * A {@link Unit} for the {@link MotorRunner}, using a time value.
 */
public class TimeUnit extends Unit {

    /**
     * Creates a new {@link TimeUnit}, based on a value in milliseconds.
     *
     * @param msecs Number of milliseconds to run.
     * @see MotorRunner
     */
    public TimeUnit(long msecs) {
        setValue(msecs);
    }

    /**
     * Creates a new {@link TimeUnit}, based on a value in seconds.
     *
     * @param seconds Number of seconds to run.
     * @see MotorRunner
     */
    public TimeUnit(float seconds) {
        setValue((long) (seconds * 1000));
    }

}
