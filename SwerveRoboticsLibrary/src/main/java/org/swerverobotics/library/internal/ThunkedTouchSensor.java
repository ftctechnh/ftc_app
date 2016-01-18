package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Another in our series
 */
public class ThunkedTouchSensor implements TouchSensor, IThunkWrapper<TouchSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private TouchSensor target;   // can only talk to him on the loop thread

    @Override public TouchSensor getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    protected ThunkedTouchSensor(TouchSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedTouchSensor create(TouchSensor target)
        {
        return target instanceof ThunkedTouchSensor ? (ThunkedTouchSensor)target : new ThunkedTouchSensor(target);
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
    // TouchSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getValue()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getValue();
                }
            }).doReadOperation();
        }

    @Override public boolean isPressed()
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isPressed();
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
