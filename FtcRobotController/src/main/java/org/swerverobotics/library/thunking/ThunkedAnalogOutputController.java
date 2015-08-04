package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.util.SerialNumber;

/**
 * Another in our series
 */
public class ThunkedAnalogOutputController implements AnalogOutputController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public AnalogOutputController target;          // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedAnalogOutputController(AnalogOutputController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedAnalogOutputController create(AnalogOutputController target)
        {
        return target instanceof ThunkedAnalogOutputController ? (ThunkedAnalogOutputController)target : new ThunkedAnalogOutputController(target);
        }

    //----------------------------------------------------------------------------------------------
    // AnalogOutputController
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

    @Override synchronized public void setAnalogOutputVoltage(final int channel, final int voltage)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputVoltage(channel, voltage);
                }
            }).doWriteOperation();
        }

    @Override synchronized public  void setAnalogOutputFrequency(final int channel, final int freq)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputFrequency(channel, freq);
                }
            }).doWriteOperation();
        }

    @Override synchronized public  void setAnalogOutputMode(final int channel, final byte mode)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputMode(channel, mode);
                }
            }).doWriteOperation();
        }
    }
