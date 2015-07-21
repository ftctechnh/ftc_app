package org.swerverobotics.library.thunking;

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
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVoltage();
                }
            }).doReadOperation();
        }

    }
