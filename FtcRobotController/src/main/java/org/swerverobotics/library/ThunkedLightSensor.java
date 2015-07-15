package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;

/**
 * A LightSensor that can be called on the main() thread.
 */
public class ThunkedLightSensor extends LightSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    LightSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedLightSensor(LightSensor target)
        {
        this.target = target;
        }

    static public ThunkedLightSensor Create(LightSensor target)
        {
        return target instanceof ThunkedLightSensor ? (ThunkedLightSensor)target : new ThunkedLightSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // LightSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getLightLevel()
        {
        class Thunk extends SynchronousOpMode.ResultableAction<Double>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getLightLevel();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void enableLed(final boolean enable)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            boolean enable;
            @Override public void actionOnLoopThread()
                {
                target.enableLed(enable);
                }
            }
        Thunk thunk = new Thunk();
        thunk.enable = enable;
        thunk.dispatch();
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
    }
