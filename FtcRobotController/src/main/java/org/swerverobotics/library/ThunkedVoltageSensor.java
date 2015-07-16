package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;

/**
 * A VoltageSensor that can be called on the main() thread.
 */
public class ThunkedVoltageSensor implements VoltageSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    VoltageSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedVoltageSensor(VoltageSensor target)
        {
        this.target = target;
        }

    static public ThunkedVoltageSensor Create(VoltageSensor target)
        {
        return target instanceof ThunkedVoltageSensor ? (ThunkedVoltageSensor)target : new ThunkedVoltageSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // VoltageSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getVoltage()
        {
        class Thunk extends ResultableThunk<Double>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getVoltage();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    }
