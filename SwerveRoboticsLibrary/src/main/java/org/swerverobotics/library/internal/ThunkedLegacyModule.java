package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.SerialNumber;

import org.swerverobotics.library.interfaces.*;
import java.util.concurrent.locks.Lock;

public class ThunkedLegacyModule implements LegacyModule, IThunkWrapper<LegacyModule>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private LegacyModule target;   // can only talk to him on the loop thread

    @Override public LegacyModule getWrappedTarget() { return this.target; }

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
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doUntrackedWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doUntrackedReadOperation();
        }
    
    //----------------------------------------------------------------------------------------------
    // I2CController interface
    //----------------------------------------------------------------------------------------------

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

    @Override public void enableI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableI2cReadMode(physicalPort, i2cAddress, memAddress, length);
                }
            }).doWriteOperation();
        }

    @Override public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableI2cWriteMode(physicalPort, i2cAddress, memAddress, length);
                }
            }).doWriteOperation();
        }

    @Override public byte[] getCopyOfReadBuffer(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getCopyOfReadBuffer(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public byte[] getCopyOfWriteBuffer(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getCopyOfWriteBuffer(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void copyBufferIntoWriteBuffer(final int physicalPort, final byte[] data)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.copyBufferIntoWriteBuffer(physicalPort, data);
                }
            }).doWriteOperation();
        }

    @Override public void enableAnalogReadMode(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableAnalogReadMode(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void enable9v(final int physicalPort, final boolean enable)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enable9v(physicalPort, enable);
                }
            }).doWriteOperation();
        }

    @Override public void setDigitalLine(final int physicalPort, final int line, final boolean set)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalLine(physicalPort, line, set);
                }
            }).doWriteOperation();
        }

    @Override public byte[] readAnalog(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.readAnalog(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public Lock getI2cReadCacheLock(final int physicalPort)
        {
        return (new ThunkForReading<Lock>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cReadCacheLock(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public Lock getI2cWriteCacheLock(final int physicalPort)
        {
        return (new ThunkForReading<Lock>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cWriteCacheLock(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public byte[] getI2cReadCache(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cReadCache(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public byte[] getI2cWriteCache(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cWriteCache(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setI2cPortActionFlag(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setI2cPortActionFlag(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public boolean isI2cPortActionFlagSet(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isI2cPortActionFlagSet(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void readI2cCacheFromModule(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.readI2cCacheFromModule(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void readI2cCacheFromController(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.readI2cCacheFromController(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void writeI2cCacheToModule(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeI2cCacheToModule(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void writeI2cCacheToController(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeI2cCacheToController(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void writeI2cPortFlagOnlyToModule(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeI2cPortFlagOnlyToModule(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public void writeI2cPortFlagOnlyToController(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.writeI2cPortFlagOnlyToController(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public boolean isI2cPortInReadMode(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isI2cPortInReadMode(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public boolean isI2cPortInWriteMode(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortInWriteMode(physicalPort);
            }
        }).doReadOperation();
        }
    
    @Override public void registerForI2cPortReadyCallback(final LegacyModule.I2cPortReadyCallback callback, final int physicalPort)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
                {
                target.registerForI2cPortReadyCallback(callback, physicalPort);
                }
            }).doWriteOperation();
        }
        
    @Override public void deregisterForPortReadyCallback(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.deregisterForPortReadyCallback(physicalPort);
                }
            }).doWriteOperation();
        }
    
    @Override public boolean isI2cPortReady(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isI2cPortReady(physicalPort);
                }
            }).doReadOperation();
        }
    }
