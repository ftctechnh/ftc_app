package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Another in our series
 */
public class ThunkedPWMOutputController implements PWMOutputController, IThunkWrapper<PWMOutputController>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private PWMOutputController target;          // can only talk to him on the loop thread

    @Override public PWMOutputController getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedPWMOutputController(PWMOutputController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedPWMOutputController create(PWMOutputController target)
        {
        return target instanceof ThunkedPWMOutputController ? (ThunkedPWMOutputController)target : new ThunkedPWMOutputController(target);
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
    // PWMOutputController
    //----------------------------------------------------------------------------------------------

    @Override public SerialNumber getSerialNumber()
        {
        return (new ThunkForReading<SerialNumber>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getSerialNumber();
                }
            }).doUntrackedReadOperation();
        }

    @Override public void setPulseWidthOutputTime(final int channel, final int time)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthOutputTime(channel, time);
                }
            }).doWriteOperation();
        }

    @Override public void setPulseWidthPeriod(final int channel, final int period)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthPeriod(channel, period);
                }
            }).doWriteOperation();
        }

    @Override public int getPulseWidthOutputTime(final int channel)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthOutputTime(channel);
                }
            }).doReadOperation();
        }

    @Override public int getPulseWidthPeriod(final int channel)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthPeriod(channel);
                }
            }).doReadOperation();
        }
    }
