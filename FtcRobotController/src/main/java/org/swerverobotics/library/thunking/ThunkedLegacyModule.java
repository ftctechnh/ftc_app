package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.*;

import java.util.concurrent.locks.Lock;

public class ThunkedLegacyModule implements LegacyModule
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public LegacyModule target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedLegacyModule(LegacyModule target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedLegacyModule create(LegacyModule target)
        {
        return target instanceof ThunkedLegacyModule ? (ThunkedLegacyModule)target : new ThunkedLegacyModule(target);
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

    @Override public void enableNxtI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int var4)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableNxtI2cWriteMode(physicalPort, i2cAddress, memAddress, var4);
                }
            }).doWriteOperation();
        }

    @Override public void enableAnalogReadMode(final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableAnalogReadMode(physicalPort);
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

    @Override public Lock getI2cReadCacheLock(final int physicalPort)
        {
        return (new ResultableThunk<Lock>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cReadCacheLock(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public Lock getI2cWriteCacheLock(final int physicalPort)
        {
        return (new ResultableThunk<Lock>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cWriteCacheLock(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public byte[] getI2cReadCache(final int physicalPort)
        {
        return (new ResultableThunk<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cReadCache(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public byte[] getI2cWriteCache(final int physicalPort)
        {
        return (new ResultableThunk<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cWriteCache(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setNxtI2cPortActionFlag(final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setNxtI2cPortActionFlag(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public boolean isNxtI2cPortActionFlagSet(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isNxtI2cPortActionFlagSet(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void readI2cCacheFromModule(final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.readI2cCacheFromModule(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void writeI2cCacheToModule(final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeI2cCacheToModule(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void writeI2cPortFlagOnlyToModule(final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeI2cPortFlagOnlyToModule(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public boolean isI2cPortInReadMode(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isI2cPortInReadMode(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public boolean isI2cPortInWriteMode(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortInWriteMode(physicalPort);
            }
        }).doReadOperation();
        }
    
    @Override public void registerForPortReadyCallback(final LegacyModule.PortReadyCallback callback, final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.registerForPortReadyCallback(callback, physicalPort);
                }
            }).doWriteOperation();
        }
        
    @Override public void deregisterForPortReadyCallback(final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.deregisterForPortReadyCallback(physicalPort);
                }
            }).doWriteOperation();
        }
    
    @Override public boolean isI2cPortReady(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isI2cPortReady(physicalPort);
                }
            }).doReadOperation();
        }
    }
