package org.swerverobotics.library;

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
        class Thunk extends ResultableThunk<Double>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getUltrasonicLevel();
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
