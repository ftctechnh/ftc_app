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

    public AccelerationSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedAccelerationSensor(AccelerationSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedAccelerationSensor create(AccelerationSensor target)
        {
        return target instanceof ThunkedAccelerationSensor ? (ThunkedAccelerationSensor)target : new ThunkedAccelerationSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------
    
    @Override synchronized public void close()
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doWriteOperation();
        }
    
    @Override synchronized public int getVersion()
        {
        return (new ResultableThunk<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doReadOperation();
        }

    @Override synchronized public String getConnectionInfo()
        {
        return (new ResultableThunk<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doReadOperation();
        }

    @Override synchronized public String getDeviceName()
        {
        return (new ResultableThunk<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doReadOperation();
        }
    
    //----------------------------------------------------------------------------------------------
    // AccelerationSensor
    //----------------------------------------------------------------------------------------------

    @Override synchronized public AccelerationSensor.Acceleration getAcceleration()
        {
        return (new ResultableThunk<Acceleration>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAcceleration();
                }
            }).doReadOperation();
        }

    @Override synchronized public String status()
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
