package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;

/**
 * A LightSensor that can be called on a synchronous thread.
 */
public class ThunkedLightSensor extends LightSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public LightSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedLightSensor(LightSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedLightSensor create(LightSensor target)
        {
        return target instanceof ThunkedLightSensor ? (ThunkedLightSensor)target : new ThunkedLightSensor(target);
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
    // LightSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getLightDetected()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getLightDetected();
                }
            }).doReadOperation();
        }
    
    @Override public int getLightDetectedRaw()
        {
        return (new ResultableThunk<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getLightDetectedRaw();
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
