package org.swerverobotics.library;

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
        class Thunk extends SynchronousOpMode.ResultableAction<Double>
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
        class Thunk extends SynchronousOpMode.ResultableAction<String>
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
        class Thunk extends SynchronousOpMode.WaitableAction
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
        class Thunk extends SynchronousOpMode.ResultableAction<Boolean>
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
