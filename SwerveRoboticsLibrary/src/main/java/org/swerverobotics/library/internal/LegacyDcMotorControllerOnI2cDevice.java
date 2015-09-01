package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;

/**
 * 
 */
public final class LegacyDcMotorControllerOnI2cDevice implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------
    
    // motor numbers are 1-based
    private static final byte[] mpMotorRegMotorPower          = new byte[]{(byte)-1, (byte)9, (byte)10};
    private static final byte[] mpMotorRegMotorMode           = new byte[]{(byte)-1, (byte)8, (byte)11};
    private static final byte[] mpMotorRegTargetEncoderValue  = new byte[]{(byte)-1, (byte)4, (byte)12};
    private static final byte[] mpMotorRegCurrentEncoderValue = new byte[]{(byte)-1, (byte)16, (byte)20};
    
    private II2cDeviceClient i2cDeviceClient;
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    public LegacyDcMotorControllerOnI2cDevice()
        {
        this.initPID();
        this.floatMotors();
        // TODO: set the registerWindow
        // TODO: rename register window to read window
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
        return "";  // TODO
        }

    @Override public int getVersion()
        {
        return 1;
        }

    @Override public void close()
        {
        this.floatMotors();
        // super.close();   // TODO: close the device?
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
        power = Range.clip(power, -1.0, 1.0);
        
        // The legacy values are -100 to 100
        byte bPower = (byte)(power * 100.0);
        
        // Write it on out
        this.i2cDeviceClient.write8(mpMotorRegMotorPower[motor], bPower);
        }

    @Override public double getMotorPower(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        
        // Float counts as zero power
        if (bPower == -128)
            return 0.0;
        
        // Other values are just linear scaling. The clipping is just paranoia about 
        // numerical precision; it probably isn't necessary
        double power = (double)bPower / 100.0;
        return Range.clip(power, -1.0, 1.0);
        }

    @Override public void setMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = -128;
        this.i2cDeviceClient.write8(mpMotorRegMotorPower[motor], bPower);
        }

    @Override public boolean getMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        return bPower == -128;
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
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegTargetEncoderValue[motor], 2);
        return TypeConversion.byteArrayToInt(bytes);
        }

    @Override public int getMotorCurrentPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegMotorPower[motor], 2);
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
