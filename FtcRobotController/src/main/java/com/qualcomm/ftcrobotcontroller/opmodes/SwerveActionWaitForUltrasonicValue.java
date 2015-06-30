package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 */
public class SwerveActionWaitForUltrasonicValue implements SwerveAction {

    static enum CompareType
    {
        equals,
        lessthan,
        greaterthan
    }

    UltrasonicSensor sensor;
    double endvalue;
    CompareType comparetype;

    Gamepad gamepad; //hack since we don't have real sensors yet

    //these are used in all SwerveActions...move them up?
    private double startTime;
    private boolean done = false;
    private boolean started = false;
    private String name = "";

    public SwerveActionWaitForUltrasonicValue(String debugName, UltrasonicSensor targetSensor, CompareType targetcompare, double targetValue, Gamepad g)
    {
        sensor = targetSensor;
        comparetype = targetcompare;
        name = debugName;

        //hack since we don't have real sensors yet
        gamepad = g;
    }

    public boolean IsDone()
    {
        return done;
    }

    public boolean IsStarted()
    {
        return started;
    }

    public void Start(ElapsedTime currentTime)
    {
        startTime = currentTime.time();
        started = true;
    }

    public void Update(ElapsedTime currentTime)
    {
        final double delta = 0.0001; //to do: decide what precision we need here.

        switch (comparetype)
        {
            case equals:
                //getting an exact value in a double may be sketchy, so look for a small delta from the endvalue
                //double currentValue = sensor.getUltrasonicLevel();
                //if (  ((currentValue - delta) < endvalue) && ((currentValue + delta) > endvalue) ) done = true;
                break;
            case lessthan:
                //if (sensor.getUltrasonicLevel() < endvalue) done = true;
                break;
            case greaterthan:
                //if (sensor.getUltrasonicLevel() > endvalue) done = true;
                break;
            default:
                //to do: throw exception here
                break;
        }

        //hack since we don't have sensors yet
        if (gamepad.dpad_up) done = true;
    }

    public String ToString()
    {
        String message  = "WaitUltra " + name;
        message += (started ? " started " : " not started ");
        if (started)
        {
            message += ("endvalue: " + String.format("%.2f", endvalue) /*+ " current: " + String.format("%.2f", sensor.getUltrasonicLevel())*/ );
        }
        return (message);
    }

}
