package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;

/**
 * A CompassSensor that can be called on the main() thread.
 */
public class ThunkedCompassSensor extends CompassSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public CompassSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedCompassSensor(CompassSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedCompassSensor create(CompassSensor target)
        {
        return target instanceof ThunkedCompassSensor ? (ThunkedCompassSensor)target : new ThunkedCompassSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ThunkForReading<String>()
        {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
        {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doReadOperation();
        }
    
    //----------------------------------------------------------------------------------------------
    // CompassSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getDirection()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDirection();
                }
            }).doReadOperation();
        }

    @Override public String status()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.status();
                }
            }).doReadOperation();
        }

    @Override public void setMode(final CompassSensor.CompassMode mode)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMode(mode);
                }
            }).doWriteOperation();
        }

    @Override public boolean calibrationFailed()
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.calibrationFailed();
                }
            }).doReadOperation();
        }
    }
