package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.util.SerialNumber;

/**
 * Another in our series
 */
public class ThunkedPWMOutputController implements PWMOutputController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public PWMOutputController target;          // can only talk to him on the loop thread

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
    // PWMOutputController
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void close()
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.close();
            }
        }).doWriteOperation();
        }

    @Override public synchronized int getVersion()
        {
        return (new ResultableThunk<Integer>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getVersion();
            }
        }).doReadOperation();
        }

    @Override public synchronized String getDeviceName()
        {
        return (new ResultableThunk<String>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getDeviceName();
            }
        }).doReadOperation();
        }

    @Override synchronized public SerialNumber getSerialNumber()
        {
        return (new ResultableThunk<SerialNumber>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getSerialNumber();
                }
            }).doReadOperation();
        }

    @Override synchronized public void setPulseWidthOutputTime(final int channel, final int time)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthOutputTime(channel, time);
                }
            }).doWriteOperation();
        }

    @Override synchronized public void setPulseWidthPeriod(final int channel, final int period)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthPeriod(channel, period);
                }
            }).doWriteOperation();
        }

    @Override synchronized public double getPulseWidthOutputTime(final int channel)
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthOutputTime(channel);
                }
            }).doReadOperation();
        }

    @Override synchronized public double getPulseWidthPeriod(final int channel)
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthPeriod(channel);
                }
            }).doReadOperation();
        }
    }
