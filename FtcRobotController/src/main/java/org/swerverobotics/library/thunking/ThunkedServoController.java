package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.*;

/**
 * An implementation of ServoController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain.
 */
public class ThunkedServoController implements ServoController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public ServoController target;   // can only talk to him on the loop thread

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
    // ServoController interface
    //----------------------------------------------------------------------------------------------

    @Override synchronized public void pwmEnable()
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.pwmEnable();
                }
            }).doWriteOperation();
        }

    @Override synchronized public void pwmDisable()
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.pwmDisable();
                }
            }).doWriteOperation();
        }

    @Override synchronized public ServoController.PwmStatus getPwmStatus()
        {
        return (new ResultableThunk<ServoController.PwmStatus>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPwmStatus();
                }
            }).doReadOperation();
        }

    @Override synchronized public void setServoPosition(final int channel, final double position)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setServoPosition(channel, position);
                }
            }).doWriteOperation();
        }

    @Override synchronized public double getServoPosition(final int channel)
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getServoPosition(channel);
                }
            }).doReadOperation();
        }
    }