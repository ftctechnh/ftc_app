package org.swerverobotics.library.thunking;

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
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
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
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getLightLevel();
                }
            }).doReadOperation();
        }

    @Override public void enableLed(final boolean enable)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableLed(enable);
                }
            }).doWriteOperation();
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
    }
