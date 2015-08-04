package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Another in our series
 */
public class ThunkedTouchSensor extends TouchSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public TouchSensor target;   // can only talk to him on the loop thread

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
    // TouchSensor
    //----------------------------------------------------------------------------------------------

    @Override synchronized public double getValue()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getValue();
                }
            }).doReadOperation();
        }

    @Override synchronized public boolean isPressed()
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isPressed();
                }
            }).doReadOperation();
        }

    @Override synchronized public String toString()
        {
        return (new ResultableThunk<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.toString();
                }
            }).doReadOperation();
        }
   
    }
