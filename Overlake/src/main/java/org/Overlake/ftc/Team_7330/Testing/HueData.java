package org.Overlake.ftc.Team_7330.Testing;

import com.google.gson.annotations.*;

/**
 * Created by leeac on 1/6/2016.
 */
public class HueData
{
    @Expose public double hueMin;
    @Expose public double hueMax;
    @Expose public double hueAverage;

    public boolean hasData;

    private double runningTotal;
    private int count;

    public HueData()
    {
        this.hueMin = 180.0;
        this.hueMax = -180.0;
        this.hueAverage = 0.0;
        this.runningTotal = 0.0;
        this.count = 0;
    }

    public boolean isHue(double value)
    {
        return value >= hueMin && value <= hueMax;
    }

    public void addSample(double value)
    {
        hasData = true;

        if (value < this.hueMin)
            this.hueMin = value;

        if (value > this.hueMax)
            this.hueMax = value;

        this.runningTotal += value;
        this.count++;
        this.hueAverage = this.runningTotal / this.count;
    }
}

