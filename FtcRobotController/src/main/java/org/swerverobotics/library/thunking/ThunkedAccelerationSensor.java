package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.*;

/**
 * An AccelerationSensor that can be called on the main() thread.
 */
public class ThunkedAccelerationSensor extends AccelerationSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    AccelerationSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedAccelerationSensor(AccelerationSensor target)
        {
        this.target = target;
        }

    static public ThunkedAccelerationSensor Create(AccelerationSensor target)
        {
        return target instanceof ThunkedAccelerationSensor ? (ThunkedAccelerationSensor)target : new ThunkedAccelerationSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // AccelerationSensor
    //----------------------------------------------------------------------------------------------

    @Override public AccelerationSensor.Acceleration getAcceleration()
        {
        return (new ResultableThunk<Acceleration>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAcceleration();
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
