package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Another in our series of Thunked objects
 */
public class ThunkedDigitalChannelController implements DigitalChannelController, IThunkWrapper<DigitalChannelController>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private DigitalChannelController target;          // can only talk to him on the loop thread

    @Override public DigitalChannelController getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedDigitalChannelController(DigitalChannelController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedDigitalChannelController create(DigitalChannelController target)
        {
        return target instanceof ThunkedDigitalChannelController ? (ThunkedDigitalChannelController)target : new ThunkedDigitalChannelController(target);
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
    // DigitalChannelController
    //----------------------------------------------------------------------------------------------

    @Override public SerialNumber getSerialNumber()
        {
        return (new ThunkForReading<SerialNumber>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getSerialNumber();
                }
            }).doReadOperation();
        }
    
    @Override public DigitalChannelController.Mode getDigitalChannelMode(final int channel)
        {
        return (new ThunkForReading<Mode>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalChannelMode(channel);
                }
            }).doReadOperation();
        }

    @Override public void setDigitalChannelMode(final int channel, final DigitalChannelController.Mode mode)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalChannelMode(channel, mode);
                }
            }).doWriteOperation();
        }
        
    @Override public boolean getDigitalChannelState(final int channel)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalChannelState(channel);
                }
            }).doReadOperation();
        }

    @Override public void setDigitalChannelState(final int channel, final boolean state)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalChannelState(channel, state);
                }
            }).doWriteOperation();
        }
    
    }
