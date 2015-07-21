package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.*;

/**
 * A CompassSensor that can be called on the main() thread.
 */
public class ThunkedCompassSensor extends CompassSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    CompassSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedCompassSensor(CompassSensor target)
        {
        this.target = target;
        }

    static public ThunkedCompassSensor Create(CompassSensor target)
        {
        return target instanceof ThunkedCompassSensor ? (ThunkedCompassSensor)target : new ThunkedCompassSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // CompassSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getDirection()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDirection();
                }
            }).doReadOperation();
        }

    @Override public String status()
        {
        return (new ResultableThunk<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.status();
                }
            }).doReadOperation();
        }

    @Override public void setMode(final CompassSensor.CompassMode mode)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMode(mode);
                }
            }).doWriteOperation();
        }

    @Override public boolean calibrationFailed()
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.calibrationFailed();
                }
            }).doReadOperation();
        }
    }
