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
        class Thunk extends ResultableThunk<Double>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getDirection();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public String status()
        {
        class Thunk extends ResultableThunk<String>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.status();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setMode(CompassSensor.CompassMode mode)
        {
        class Thunk extends NonwaitingThunk
            {
            CompassSensor.CompassMode mode;
            @Override public void actionOnLoopThread()
                {
                target.setMode(mode);
                }
            }
        Thunk thunk = new Thunk();
        thunk.mode = mode;
        thunk.dispatch();
        }

    @Override public boolean calibrationFailed()
        {
        class Thunk extends ResultableThunk<Boolean>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.calibrationFailed();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    }
