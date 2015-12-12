package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
/**
 * Yet another!
 */
public class ThunkedTouchSensorMultiplexer extends TouchSensorMultiplexer implements IThunkWrapper<TouchSensorMultiplexer>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private TouchSensorMultiplexer target;   // can only talk to him on the loop thread

    @Override public TouchSensorMultiplexer getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    protected ThunkedTouchSensorMultiplexer(TouchSensorMultiplexer target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedTouchSensorMultiplexer create(TouchSensorMultiplexer target)
        {
        return target instanceof ThunkedTouchSensorMultiplexer ? (ThunkedTouchSensorMultiplexer)target : new ThunkedTouchSensorMultiplexer(target);
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
    // TouchSensorMultiplexer
    //----------------------------------------------------------------------------------------------

    @Override public boolean isTouchSensorPressed(final int sensor)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isTouchSensorPressed(sensor);
                }
            }).doUntrackedReadOperation();
        }

    @Override public int getSwitches()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getSwitches();
                }
            }).doUntrackedReadOperation();
        }

    }
