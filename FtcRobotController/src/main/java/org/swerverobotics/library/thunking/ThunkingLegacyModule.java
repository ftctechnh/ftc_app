package org.swerverobotics.library.thunking;

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
        this.target = target;
        }

    static public ThunkingLegacyModule Create(LegacyModule target)
        {
        return target instanceof ThunkingLegacyModule ? (ThunkingLegacyModule)target : new ThunkingLegacyModule(target);
        }

    //----------------------------------------------------------------------------------------------
    // LegacyModule interface
    //----------------------------------------------------------------------------------------------

    @Override public void enableNxtI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int memLength)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.enableNxtI2cReadMode(physicalPort, i2cAddress, memAddress, memLength);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void enableNxtI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final byte[] initialValues)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.enableNxtI2cWriteMode(physicalPort, i2cAddress, memAddress, initialValues);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void enableAnalogReadMode(final int physicalPort, final int i2cAddress)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.enableAnalogReadMode(physicalPort, i2cAddress);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void enable9v(final int physicalPort, final boolean enable)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.enable9v(physicalPort, enable);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void setDigitalLine(final int physicalPort, final int line, final boolean set)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.setDigitalLine(physicalPort, line, set);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public byte[] readLegacyModuleCache(final int physicalPort)
        {
        class Thunk extends ResultableThunk<byte[]>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.readLegacyModuleCache(physicalPort);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void writeLegacyModuleCache(final int physicalPort, final byte[] data)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.writeLegacyModuleCache(physicalPort, data);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public byte[] readAnalog(final int physicalPort)
        {
        class Thunk extends ResultableThunk<byte[]>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.readAnalog(physicalPort);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public boolean isPortReady(final int physicalPort)
        {
        class Thunk extends ResultableThunk<Boolean>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.isPortReady(physicalPort);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void close()
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.close();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }
    }
