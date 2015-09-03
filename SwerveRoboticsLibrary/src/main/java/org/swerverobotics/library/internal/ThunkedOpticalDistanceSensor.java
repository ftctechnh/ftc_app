package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Another in our story
 */
public class ThunkedOpticalDistanceSensor extends OpticalDistanceSensor implements IThunkWrapper<OpticalDistanceSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private OpticalDistanceSensor target;   // can only talk to him on the loop thread

    @Override public OpticalDistanceSensor getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    protected ThunkedOpticalDistanceSensor(OpticalDistanceSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedOpticalDistanceSensor create(OpticalDistanceSensor target)
        {
        return target instanceof ThunkedOpticalDistanceSensor ? (ThunkedOpticalDistanceSensor)target : new ThunkedOpticalDistanceSensor(target);
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
            }).doUntrackedWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doUntrackedReadOperation();
        }

    //----------------------------------------------------------------------------------------------
    // OpticalDistanceSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getLightDetected()
        {
        return (new ThunkForReading<Double>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getLightDetected();
            }
        }).doReadOperation();
        }

    @Override public int getLightDetectedRaw()
        {
        return (new ThunkForReading<Integer>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getLightDetectedRaw();
            }
        }).doReadOperation();
        }

    @Override public void enableLed(final boolean enable)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.enableLed(enable);
            }
        }).doWriteOperation();
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

    @Override public String toString()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.toString();
                }
            }).doReadOperation();
        }
    }
