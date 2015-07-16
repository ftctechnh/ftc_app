package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;

/**
 * A GyroSensor that can be called on the main() thread.
 */
public class ThunkedGyroSensor extends  GyroSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    GyroSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedGyroSensor(GyroSensor target)
        {
        this.target = target;
        }

    static public ThunkedGyroSensor Create(GyroSensor target)
        {
        return target instanceof ThunkedGyroSensor ? (ThunkedGyroSensor)target : new ThunkedGyroSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // GyroSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getRotation()
        {
        class Thunk extends ResultableThunk<Double>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getRotation();
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

    }
