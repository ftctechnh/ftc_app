package org.swerverobotics.library.internal;

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

    @Override public void close()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doReadOperation();
        }
    
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

    @Override public void setAnalogOutputVoltage(final int channel, final int voltage)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputVoltage(channel, voltage);
                }
            }).doWriteOperation();
        }

    @Override public void setAnalogOutputFrequency(final int channel, final int freq)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputFrequency(channel, freq);
                }
            }).doWriteOperation();
        }

    @Override public void setAnalogOutputMode(final int channel, final byte mode)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputMode(channel, mode);
                }
            }).doWriteOperation();
        }
    }
