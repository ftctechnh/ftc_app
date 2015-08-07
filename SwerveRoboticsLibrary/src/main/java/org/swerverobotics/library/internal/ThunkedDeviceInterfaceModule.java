package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import java.util.concurrent.locks.Lock;

public class ThunkedDeviceInterfaceModule implements DeviceInterfaceModule
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public DeviceInterfaceModule target;
    
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
    // DeviceInterfaceModule
    //----------------------------------------------------------------------------------------------

    @Override public int getDigitalInputStateByte()
        {
        return (new ResultableThunk<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalInputStateByte();
                }
            }).doReadOperation();
        }

    @Override public void setDigitalIOControlByte(final byte physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalIOControlByte(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public byte getDigitalIOControlByte()
        {
        return (new ResultableThunk<Byte>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalIOControlByte();
                }
            }).doReadOperation();
        }

    @Override public void setDigitalOutputByte(final byte physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalOutputByte(physicalPort);
                }
            }).doWriteOperation();
        }

    @Override public byte getDigitalOutputStateByte()
        {
        return (new ResultableThunk<Byte>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalOutputStateByte();
                }
            }).doReadOperation();
        }

    @Override public boolean getLEDState(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getLEDState(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setLED(final int physicalPort, final boolean var2)
        {
        (new NonwaitingThunk()
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
        return (new ResultableThunk<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAnalogInputValue(physicalPort);
                }
            }).doReadOperation();
        }

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

    //----------------------------------------------------------------------------------------------
    // AnalogOutputController
    //----------------------------------------------------------------------------------------------

    // String getDeviceName();

    // SerialNumber getSerialNumber();

    // int getVersion();

    // void close();

    @Override public void setAnalogOutputVoltage(final int physicalPort, final int voltage)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputVoltage(physicalPort, voltage);
                }
            }).doWriteOperation();
        }
        
    @Override public void setAnalogOutputFrequency(final int physicalPort, final int frequency)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setAnalogOutputFrequency(physicalPort, frequency);
                }
            }).doWriteOperation();
        }
        
    @Override public void setAnalogOutputMode(final int physicalPort, final byte mode)
        {
        (new NonwaitingThunk()
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
        return (new ResultableThunk<DigitalChannelController.Mode>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalChannelMode(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setDigitalChannelMode(final int physicalPort, final DigitalChannelController.Mode mode)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setDigitalChannelMode(physicalPort, mode);
                }
            }).doWriteOperation();
        }

    @Override public boolean getDigitalChannelState(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDigitalChannelState(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void setDigitalChannelState(final int physicalPort, final boolean state)
        {
        (new NonwaitingThunk()
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
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableI2cReadMode(physicalPort, i2cAddress, memAddress, length);
                }
            }).doWriteOperation();
        }

    @Override public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableI2cWriteMode(physicalPort, i2cAddress, memAddress, length);
                }
            }).doWriteOperation();
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

    @Override public void setI2cPortActionFlag(final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.setI2cPortActionFlag(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override public boolean isI2cPortActionFlagSet(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isI2cPortActionFlagSet(physicalPort);
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

    @Override public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int physicalPort)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.registerForI2cPortReadyCallback(callback, physicalPort);
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
    
    //----------------------------------------------------------------------------------------------
    // PWMOutputController
    //----------------------------------------------------------------------------------------------

    // @Override public String getDeviceName();

    // @Override public SerialNumber getSerialNumber();

    // int getVersion();

    // void close();

    @Override public void setPulseWidthOutputTime(final int physicalPort, final int var2)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthOutputTime(physicalPort, var2);
                }
            }).doWriteOperation();
        }

    @Override public void setPulseWidthPeriod(final int physicalPort, final int var2)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setPulseWidthPeriod(physicalPort, var2);
                }
            }).doWriteOperation();
        }


    @Override public double getPulseWidthOutputTime(final int physicalPort)
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthOutputTime(physicalPort);
                }
            }).doReadOperation();
        }
        

    @Override public double getPulseWidthPeriod(final int physicalPort)
        {
        return (new ResultableThunk<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getPulseWidthPeriod(physicalPort);
                }
            }).doReadOperation();
        }
    }
