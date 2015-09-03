package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;

/**
 * 
 */
public final class LegacyDcMotorControllerOnI2cDevice implements DcMotorController, IThunkWrapper<DcMotorController>, VoltageSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------
    
    /*
    Address     Type     Contents
    00 – 07H    chars    Sensor version number
    08 – 0FH    chars    Manufacturer
    10 – 17H    chars    Sensor type
    18 – 3DH    bytes    Not used
    3E, 3FH     chars    Reserved
    40H – 43H   s/int    Motor 1 target encoder value, high byte first == 64-67
    44H         byte     Motor 1 mode   == 68
    45H         s/byte   Motor 1 power  == 69
    46H         s/byte   Motor 2 power  == 70
    47H         byte     Motor 2 mode   == 71
    48 – 4BH    s/int    Motor 2 target encoder value, high byte first   == 72-75
    4C – 4FH    s/int    Motor 1 current encoder value, high byte first  == 76-79
    50 – 53H    s/int    Motor 2 current encoder value, high byte first  == 80-83
    54, 55H     word     Battery voltage 54H high byte, 55H low byte
    56H         S/byte   Motor 1 gear ratio
    57H         byte     Motor 1 P coefficient*
    58H         byte     Motor 1 I coefficient*
    59H         byte     Motor 1 D coefficient*
    5AH         s/byte   Motor 2 gear ratio
    5BH         byte     Motor 2 P coefficient*
    5CH         byte     Motor 2 I coefficient*
    5DH         byte     Motor 2 D coefficient*
     */
    
    // motor numbers are 1-based
    private static final byte[] mpMotorRegMotorPower          = new byte[]{(byte)-1, (byte)0x45, (byte)0x46};
    private static final byte[] mpMotorRegMotorMode           = new byte[]{(byte)-1, (byte)0x44, (byte)0x47};
    private static final byte[] mpMotorRegTargetEncoderValue  = new byte[]{(byte)-1, (byte)0x40, (byte)0x48};
    private static final byte[] mpMotorRegCurrentEncoderValue = new byte[]{(byte)-1, (byte)0x4c, (byte)0x50};
    private static final int iregFirstRead = 0x40;
    private static final int cregRead = 20;
    
    private static final byte cbEncoder = 4;

    private static final byte bPowerBrake = 0;
    private static final byte bPowerFloat = -128;
    private static final byte bPowerMax = 100;
    private static final byte bPowerMin = -100;
    
    private static final double powerMin = -1.0;
    private static final double powerMax = 1.0;
    
    private II2cDeviceClient i2cDeviceClient;
    private DcMotorController target;
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    public LegacyDcMotorControllerOnI2cDevice(II2cDeviceClient ii2cDeviceClient, DcMotorController target)
        {
        this.i2cDeviceClient = ii2cDeviceClient;
        this.target          = target;
        
        this.initPID();
        this.floatMotors();
        
        // Always read a certain set of registers
        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.RegWindow(iregFirstRead, cregRead));
        
        // Keep the motors from shutting off 
        this.i2cDeviceClient.setHeartbeatRead(2000);
        }

    //----------------------------------------------------------------------------------------------
    // IHardwareWrapper
    //----------------------------------------------------------------------------------------------
    
    @Override public DcMotorController getWrappedTarget()
        {
        return target;
        }

    //----------------------------------------------------------------------------------------------
    // VoltageSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getVoltage()
        {
        byte[] bytes = this.i2cDeviceClient.read(0x54, 2);
        
        // "The high byte is the upper 8 bits of a 10 bit value. It may be used as an 8 bit 
        // representation of the battery voltage in units of 80mV. This provides a measurement 
        // range of 0 – 20.4 volts. The low byte has the lower 2 bits at bit locations 0 and 1 
        // in the byte. This increases the measurement resolution to 20mV."
        return Util.unpack10BitAnalog(bytes,0) * 0.020;
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return "Swerve Robotics NXT DC Motor Controller";
        }

    @Override public String getConnectionInfo()
        {
        return this.i2cDeviceClient.getConnectionInfo();
        }

    @Override public int getVersion()
        {
        return 1;
        }

    @Override public void close()
        {
        this.floatMotors();
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController
    //----------------------------------------------------------------------------------------------

    @Override public void setMotorControllerDeviceMode(DcMotorController.DeviceMode port)
        {
        // ignored
        }

    @Override public DcMotorController.DeviceMode getMotorControllerDeviceMode()
        {
        return DeviceMode.READ_WRITE;
        }
    
    @Override public void setMotorChannelMode(int motor, DcMotorController.RunMode mode)
        {
        this.validateMotor(motor);
        byte b = modeToByte(mode);
        
        // We write the whole byte, but only the lower two bits are actually writable.
        // Thus, we don't need to read and OR in the other values
        this.i2cDeviceClient.write8(mpMotorRegMotorMode[motor], b);
        }

    @Override public DcMotorController.RunMode getMotorChannelMode(int motor)
        {
        this.validateMotor(motor);
        byte b = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        return modeFromByte(b);
        }
    
    @Override public boolean isBusy(int motor)
        {
        this.validateMotor(motor);
        byte b = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        return (b & 0x80) != 0;
        }

    @Override public void setMotorPower(int motor, double power)
        {
        this.validateMotor(motor);
        
        // Unlike the (beta) robot controller library, we just saturate the motor
        // power rather than making clients worry about that
        power = Range.clip(power, powerMin, powerMax);
        
        // The legacy values are -100 to 100
        byte bPower = (byte)Range.scale(power, powerMin, powerMax, bPowerMin, bPowerMax);
        
        // Write it on out
        this.i2cDeviceClient.write8(mpMotorRegMotorPower[motor], bPower);
        }

    @Override public double getMotorPower(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        
        // Float counts as zero power
        if (bPower == bPowerFloat)
            return 0.0;
        
        // Other values are just linear scaling. The clipping is just paranoia about 
        // numerical precision; it probably isn't necessary
        double power = Range.scale(bPower, bPowerMin, bPowerMax, powerMin, powerMax);
        return Range.clip(power, powerMin, powerMax);
        }

    @Override public void setMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = bPowerFloat;
        this.i2cDeviceClient.write8(mpMotorRegMotorPower[motor], bPower);
        }

    @Override public boolean getMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        return bPower == bPowerFloat;
        }

    @Override public void setMotorTargetPosition(int motor, int position)
        {
        this.validateMotor(motor);
        byte[] bytes = TypeConversion.intToByteArray(position);
        this.i2cDeviceClient.write(mpMotorRegTargetEncoderValue[motor], bytes);
        }

    @Override public int getMotorTargetPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegTargetEncoderValue[motor], cbEncoder);
        return TypeConversion.byteArrayToInt(bytes);
        }

    @Override public int getMotorCurrentPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegMotorPower[motor], cbEncoder);
        return TypeConversion.byteArrayToInt(bytes);
        }
    
    //----------------------------------------------------------------------------------------------
    // DcMotorController utility
    //----------------------------------------------------------------------------------------------

    private void initPID()
        {
        // is this necessary?
        }

    private void floatMotors()
        {
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
        }

    private void validateMotor(int motor)
        {
        if(motor < 1 || motor > 2)
            {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", motor, 2));
            }
        }

    public static byte modeToByte(DcMotorController.RunMode mode)
        {
        switch (mode)
            {
        default:
        case RUN_WITHOUT_ENCODERS:  return 0;
        case RUN_USING_ENCODERS:    return 1;
        case RUN_TO_POSITION:       return 2;
        case RESET_ENCODERS:        return 3;
            }
        }

    public static DcMotorController.RunMode modeFromByte(byte b)
        {
        switch (b & 3)
            {
        default:
        case 0:     return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
        case 1:     return DcMotorController.RunMode.RUN_USING_ENCODERS;
        case 2:     return DcMotorController.RunMode.RUN_TO_POSITION;
        case 3:     return DcMotorController.RunMode.RESET_ENCODERS;
            }
        }

    }
