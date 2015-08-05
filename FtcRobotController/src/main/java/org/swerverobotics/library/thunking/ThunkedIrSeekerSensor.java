package org.swerverobotics.library.thunking;

import android.text.BoringLayout;

import com.qualcomm.robotcore.hardware.*;

/**
 * An IrSeekerSensor that can be called on the main() thread.
 */
public class ThunkedIrSeekerSensor extends IrSeekerSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public IrSeekerSensor target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedIrSeekerSensor(IrSeekerSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedIrSeekerSensor create(IrSeekerSensor target)
        {
        return target instanceof ThunkedIrSeekerSensor ? (ThunkedIrSeekerSensor)target : new ThunkedIrSeekerSensor(target);
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
    // IrSeekerSensor
    //----------------------------------------------------------------------------------------------

    @Override public void setMode(final IrSeekerSensor.Mode mode)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMode(mode);
                }
            }).doWriteOperation();
        }

    @Override public IrSeekerSensor.Mode getMode()
        {
        return (new ResultableThunk<IrSeekerSensor.Mode>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMode();
                }
            }).doReadOperation();
        }

    @Override public boolean signalDetected()
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.signalDetected();
                }
            }).doReadOperation();
        }

    @Override public double getAngle()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAngle();
                }
            }).doReadOperation();
        }

    @Override public double getStrength()
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getStrength();
                }
            }).doReadOperation();
        }

    @Override public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors()
        {
        return (new ResultableThunk<IrSeekerSensor.IrSeekerIndividualSensor[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getIndividualSensors();
                }
            }).doReadOperation();
        }

    }
