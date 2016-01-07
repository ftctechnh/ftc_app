package org.Overlake.ftc.Team_7330.Testing;

import com.google.gson.annotations.*;

/**
 * Created by leeac on 1/6/2016.
 */
public class HueCalibration
{
    @Expose public double hueMin;
    @Expose public double hueMax;
    @Expose public double hueAverage;

    private double runningTotal;
    private int count;

    public HueCalibration()
    {
        this.hueMin = 180.0;
        this.hueMax = -180.0;
        this.hueAverage = 0.0;
        this.runningTotal = 0.0;
        this.count = 0;
    }

    public void AddSample(double value)
    {
        if (value < this.hueMin)
            this.hueMin = value;

        if (value > this.hueMax)
            this.hueMax = value;

        this.runningTotal += value;
        this.count++;
        this.hueAverage = this.runningTotal / this.count;
    }
}

