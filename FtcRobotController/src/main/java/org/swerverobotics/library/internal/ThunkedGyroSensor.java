package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;

/**
 * A GyroSensor that can be called on the main() thread.
 */
public class ThunkedGyroSensor extends  GyroSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public GyroSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedGyroSensor(GyroSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedGyroSensor create(GyroSensor target)
        {
        return target instanceof ThunkedGyroSensor ? (ThunkedGyroSensor)target : new ThunkedGyroSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ResultableThunk<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ResultableThunk<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doReadOperation();
        }

    @Override public String getDeviceName()
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
    // GyroSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getRotation()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getRotation();
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
