package org.swerverobotics.library;

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
        class Thunk extends SynchronousOpMode.ResultableAction<AccelerationSensor.Acceleration>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getAcceleration();
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
    }
