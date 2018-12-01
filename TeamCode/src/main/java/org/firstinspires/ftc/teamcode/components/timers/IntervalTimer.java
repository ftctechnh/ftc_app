package org.firstinspires.ftc.teamcode.components.timers;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * The Interval Timer acts as a repeated timer that checks if the amount of time since the last time
 * it was called exceeds the interval designated by the constructor.
 */
public class IntervalTimer implements IClock
{
    private ElapsedTime elapsedTime;
    private long previousTime;
    private long interval;

    /**
     * Builds a new IntervalTimer
     * @param interval The time span that the IntervalTimer will check has been exceeded
     */
    public IntervalTimer(long interval)
    {
        previousTime = 0;
        elapsedTime = new ElapsedTime();
        this.interval = interval;
    }

    /**
     * Gets the current time in nanoseconds
     * @returns Returns the current time in nano seconds
     */
    @Override
    public long getCurrentTime()
    {
        return elapsedTime.nanoseconds();
    }

    /**
     * Gets the previous time the interval was called
     * @return Returns the previous time.
     */
    public long getPreviousTime()
    {
        return previousTime;
    }

    /**
     * Updates the previous time to be the current time
     */
    public void update()
    {
        previousTime = getCurrentTime();
    }

    /**
     * Checks if the current time interval has passed
     * @return Returns true if the current time interval has been exceeded
     */
    public boolean hasCurrentIntervalPassed()
    {
        return getCurrentTime() > previousTime + interval;
    }
}
