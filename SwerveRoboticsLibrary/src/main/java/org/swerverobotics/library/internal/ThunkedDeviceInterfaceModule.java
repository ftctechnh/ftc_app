package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;
import java.util.concurrent.locks.Lock;


public class ThunkedDeviceInterfaceModule implements DeviceInterfaceModule, IThunkWrapper<DeviceInterfaceModule>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private DeviceInterfaceModule target;

    @Override public DeviceInterfaceModule getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedDeviceInterfaceModule(DeviceInterfaceModule target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedDeviceInterfaceModule create(DeviceInterfaceModule target)
        {
        return target instanceof ThunkedDeviceInterfaceModule ? (ThunkedDeviceInterfaceModule)target : new ThunkedDeviceInterfaceModule(target);
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
    // DeviceInterfaceModule
    //----------------------------------------------------------------------------------------------

    @Override public int getDigitalInputStateByte()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalInputStateByte();
                }
            }).doReadOperation();
        }

    @Override public void setDigitalIOControlByte(final byte physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalIOControlByte(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public byte getDigitalIOControlByte()
        {
        return (new ThunkForReading<Byte>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalIOControlByte();
                }
            }).doReadOperation();
        }

    @Override public void setDigitalOutputByte(final byte physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalOutputByte(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public byte getDigitalOutputStateByte()
        {
        return (new ThunkForReading<Byte>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalOutputStateByte();
                }
            }).doReadOperation();
        }

    @Override public boolean getLEDState(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getLEDState(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setLED(final int physicalPort, final boolean var2)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setLED(physicalPort, var2);
                }
            }).doWriteOperation();
        }

    //----------------------------------------------------------------------------------------------
    // AnalogInputController
    //----------------------------------------------------------------------------------------------

    @Override public int getAnalogInputValue(final int physicalPort)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAnalogInputValue(physicalPort);
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

    //----------------------------------------------------------------------------------------------
    // AnalogOutputController
    //----------------------------------------------------------------------------------------------

    // String getDeviceName();

    // SerialNumber getSerialNumber();

    // int getVersion();

    // void close();

    @Override public void setAnalogOutputVoltage(final int physicalPort, final int voltage)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputVoltage(physicalPort, voltage);
                }
            }).doWriteOperation();
        }
        
    @Override public void setAnalogOutputFrequency(final int physicalPort, final int frequency)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputFrequency(physicalPort, frequency);
                }
            }).doWriteOperation();
        }
        
    @Override public void setAnalogOutputMode(final int physicalPort, final byte mode)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputMode(physicalPort, mode);
                }
            }).doWriteOperation();
        }
    
    //----------------------------------------------------------------------------------------------
    // DigitalChannelController
    //----------------------------------------------------------------------------------------------

    // @Override public SerialNumber getSerialNumber();

    @Override public DigitalChannelController.Mode getDigitalChannelMode(final int physicalPort)
        {
        return (new ThunkForReading<Mode>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalChannelMode(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setDigitalChannelMode(final int physicalPort, final DigitalChannelController.Mode mode)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalChannelMode(physicalPort, mode);
                }
            }).doWriteOperation();
        }

    @Override public boolean getDigitalChannelState(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalChannelState(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setDigitalChannelState(final int physicalPort, final boolean state)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalChannelState(physicalPort, state);
                }
            }).doWriteOperation();
        }

    //----------------------------------------------------------------------------------------------
    // I2CController
    //----------------------------------------------------------------------------------------------

    // @Override public String getDeviceName();

    // @Override public SerialNumber getSerialNumber();

    // @Override public int getVersion(); 

    // @Override public void close()

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

    @Override public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int physicalPort)
        {
        target.registerForI2cPortReadyCallback(callback, physicalPort);
        }

    @Override public I2cPortReadyCallback getI2cPortReadyCallback(int i)
        {
        return target.getI2cPortReadyCallback(i);
        }

    @Override public void deregisterForPortReadyCallback(final int physicalPort)
        {
        target.deregisterForPortReadyCallback(physicalPort);
        }

    @Override public void registerForPortReadyBeginEndCallback(I2cPortReadyBeginEndNotifications i2cNotificationsCallback, int i)
        {
        target.registerForPortReadyBeginEndCallback(i2cNotificationsCallback, i);
        }

    @Override public I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback(int i)
        {
        return target.getPortReadyBeginEndCallback(i);
        }

    @Override public void deregisterForPortReadyBeginEndCallback(int i)
        {
        target.deregisterForPortReadyBeginEndCallback(i);
        }

    //----------------------------------------------------------------------------------------------
    // PWMOutputController
    //----------------------------------------------------------------------------------------------

    // @Override public String getDeviceName();

    // @Override public SerialNumber getSerialNumber();

    // int getVersion();

    // void close();

    @Override public void setPulseWidthOutputTime(final int physicalPort, final int var2)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthOutputTime(physicalPort, var2);
                }
            }).doWriteOperation();
        }

    @Override public void setPulseWidthPeriod(final int physicalPort, final int var2)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthPeriod(physicalPort, var2);
                }
            }).doWriteOperation();
        }


    @Override public int getPulseWidthOutputTime(final int physicalPort)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthOutputTime(physicalPort);
                }
            }).doReadOperation();
        }
        

    @Override public int getPulseWidthPeriod(final int physicalPort)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthPeriod(physicalPort);
                }
            }).doReadOperation();
        }
    }
