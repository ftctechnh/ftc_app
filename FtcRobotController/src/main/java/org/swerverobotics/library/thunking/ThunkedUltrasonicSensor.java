package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.*;

/**
 * An UltrasonicSensor that can be called on the main() thread.
 */
public class ThunkedUltrasonicSensor extends UltrasonicSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    UltrasonicSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedUltrasonicSensor(UltrasonicSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedUltrasonicSensor Create(UltrasonicSensor target)
        {
        return target instanceof ThunkedUltrasonicSensor ? (ThunkedUltrasonicSensor)target : new ThunkedUltrasonicSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // UltrasonicSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getUltrasonicLevel()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getUltrasonicLevel();
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


    }
