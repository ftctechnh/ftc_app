package org.firstinspires.ftc.teamcode.components.timers;

import com.qualcomm.robotcore.util.ElapsedTime;

public class IntervalTimer implements IClock
{
    private ElapsedTime elapsedTime;
    private long previousTime;
    private long interval;

    public IntervalTimer(long interval)
    {
        previousTime = 0;
        elapsedTime = new ElapsedTime();
        this.interval = interval;
    }

    @Override
    public long getCurrentTime()
    {
        return elapsedTime.nanoseconds();
    }

    public long getPreviousTime()
    {
        return previousTime;
    }

    public void update()
    {
        previousTime = getCurrentTime();
    }

    public boolean hasCurrentIntervalPassed()
    {
        return getCurrentTime() > previousTime + interval;
    }
}
