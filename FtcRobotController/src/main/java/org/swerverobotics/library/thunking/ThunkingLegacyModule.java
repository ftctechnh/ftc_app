package org.swerverobotics.library.thunking;

import android.text.BoringLayout;

import com.qualcomm.robotcore.hardware.*;

public class ThunkingLegacyModule implements LegacyModule
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    LegacyModule target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkingLegacyModule(LegacyModule target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkingLegacyModule create(LegacyModule target)
        {
        return target instanceof ThunkingLegacyModule ? (ThunkingLegacyModule)target : new ThunkingLegacyModule(target);
        }

    //----------------------------------------------------------------------------------------------
    // LegacyModule interface
    //----------------------------------------------------------------------------------------------

    @Override public void enableNxtI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int memLength)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableNxtI2cReadMode(physicalPort, i2cAddress, memAddress, memLength);
                }
            }).doWriteOperation();
        }

    @Override public void enableNxtI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final byte[] initialValues)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableNxtI2cWriteMode(physicalPort, i2cAddress, memAddress, initialValues);
                }
            }).doWriteOperation();
        }

    @Override public void enableAnalogReadMode(final int physicalPort, final int i2cAddress)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableAnalogReadMode(physicalPort, i2cAddress);
                }
            }).doWriteOperation();
        }

    @Override public void enable9v(final int physicalPort, final boolean enable)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enable9v(physicalPort, enable);
                }
            }).doWriteOperation();
        }

    @Override public void setDigitalLine(final int physicalPort, final int line, final boolean set)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalLine(physicalPort, line, set);
                }
            }).doWriteOperation();
        }

    @Override public byte[] readLegacyModuleCache(final int physicalPort)
        {
        return (new ResultableThunk<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.readLegacyModuleCache(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void writeLegacyModuleCache(final int physicalPort, final byte[] data)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeLegacyModuleCache(physicalPort, data);
                }
            }).doWriteOperation();
        }

    @Override public byte[] readAnalog(final int physicalPort)
        {
        return (new ResultableThunk<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.readAnalog(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public boolean isPortReady(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isPortReady(physicalPort);
                }
            }).doReadOperation();
        }

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
    }
