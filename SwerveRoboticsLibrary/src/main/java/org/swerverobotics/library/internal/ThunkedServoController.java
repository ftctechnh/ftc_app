package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An implementation of ServoController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain.
 */
public class ThunkedServoController implements ServoController,IThunkWrapper<ServoController>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private ServoController target;   // can only talk to him on the loop thread

    @Override public ServoController getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedServoController(ServoController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedServoController create(ServoController target)
        {
        return target instanceof ThunkedServoController ? (ThunkedServoController)target : new ThunkedServoController(target);
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
    // ServoController interface
    //----------------------------------------------------------------------------------------------

    @Override public void pwmEnable()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.pwmEnable();
                }
            }).doWriteOperation();
        }

    @Override public void pwmDisable()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.pwmDisable();
                }
            }).doWriteOperation();
        }

    @Override public ServoController.PwmStatus getPwmStatus()
        {
        return (new ThunkForReading<PwmStatus>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPwmStatus();
                }
            }).doReadOperation();
        }

    @Override public void setServoPosition(final int channel, final double position)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setServoPosition(channel, position);
                }
            }).doWriteOperation();
        }

    @Override public double getServoPosition(final int channel)
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getServoPosition(channel);
                }
            }).doReadOperation();
        }
    }