package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.util.SerialNumber;

/**
 * Another in our series
 */
public class ThunkedAnalogInputController implements AnalogInputController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public AnalogInputController target;          // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedAnalogInputController(AnalogInputController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedAnalogInputController create(AnalogInputController target)
        {
        return target instanceof ThunkedAnalogInputController ? (ThunkedAnalogInputController)target : new ThunkedAnalogInputController(target);
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.close();
            }
        }).doWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ResultableThunk<Integer>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getVersion();
            }
        }).doReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ResultableThunk<String>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getConnectionInfo();
            }
        }).doReadOperation();
        }

    @Override public String getDeviceName()
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
    // AnalogInputController
    //----------------------------------------------------------------------------------------------

    @Override public SerialNumber getSerialNumber()
        {
        return (new ResultableThunk<SerialNumber>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getSerialNumber();
                }
            }).doReadOperation();
        }

    @Override public int getAnalogInputValue(final int channel)
        {
        return (new ResultableThunk<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAnalogInputValue(channel);
                }
            }).doReadOperation();
        }
    }
