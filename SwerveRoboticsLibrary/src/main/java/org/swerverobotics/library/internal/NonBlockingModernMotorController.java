package org.swerverobotics.library.internal;

import com.qualcomm.hardware.*;
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.exception.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.*;
import com.qualcomm.robotcore.util.*;

/**
 * Created by Bob on 2015-11-11.
 */
public class NonBlockingModernMotorController extends ModernRoboticsUsbDevice implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------------------------------

    public static final int MONITOR_LENGTH = 30;
    public static final int MIN_MOTOR = 1;
    public static final int MAX_MOTOR = 2;
    public static final byte POWER_MAX = 100;
    public static final byte POWER_BRAKE = 0;
    public static final byte POWER_MIN = -100;
    public static final byte POWER_FLOAT = -128;
    public static final byte RATIO_MIN = -128;
    public static final byte RATIO_MAX = 127;
    public static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
    public static final int BATTERY_MAX_MEASURABLE_VOLTAGE_INT = 1023;
    public static final double BATTERY_MAX_MEASURABLE_VOLTAGE = 20.4D;
    public static final byte DEFAULT_P_COEFFICIENT = -128;
    public static final byte DEFAULT_I_COEFFICIENT = 64;
    public static final byte DEFAULT_D_COEFFICIENT = -72;
    public static final byte START_ADDRESS = 64;
    public static final int CHANNEL_MODE_MASK_SELECTION = 3;
    public static final int CHANNEL_MODE_MASK_LOCK = 4;
    public static final int CHANNEL_MODE_MASK_REVERSE = 8;
    public static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
    public static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
    public static final int CHANNEL_MODE_MASK_ERROR = 64;
    public static final int CHANNEL_MODE_MASK_BUSY = 128;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY = 0;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED = 1;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
    public static final byte CHANNEL_MODE_FLAG_LOCK = 4;
    public static final byte CHANNEL_MODE_FLAG_REVERSE = 8;
    public static final byte CHANNEL_MODE_FLAG_NO_TIMEOUT = 16;
    public static final byte CHANNEL_MODE_FLAG_UNUSED = 32;
    public static final byte CHANNEL_MODE_FLAG_ERROR = 64;
    public static final byte CHANNEL_MODE_FLAG_BUSY = -128;
    public static final int ADDRESS_MOTOR1_TARGET_ENCODER_VALUE = 64;
    public static final int ADDRESS_MOTOR1_MODE = 68;
    public static final int ADDRESS_MOTOR1_POWER = 69;
    public static final int ADDRESS_MOTOR2_POWER = 70;
    public static final int ADDRESS_MOTOR2_MODE = 71;
    public static final int ADDRESS_MOTOR2_TARGET_ENCODER_VALUE = 72;
    public static final int ADDRESS_MOTOR1_CURRENT_ENCODER_VALUE = 76;
    public static final int ADDRESS_MOTOR2_CURRENT_ENCODER_VALUE = 80;
    public static final int ADDRESS_BATTERY_VOLTAGE = 84;
    public static final int ADDRESS_MOTOR1_GEAR_RATIO = 86;
    public static final int ADDRESS_MOTOR1_P_COEFFICIENT = 87;
    public static final int ADDRESS_MOTOR1_I_COEFFICIENT = 88;
    public static final int ADDRESS_MOTOR1_D_COEFFICIENT = 89;
    public static final int ADDRESS_MOTOR2_GEAR_RATIO = 90;
    public static final int ADDRESS_MOTOR2_P_COEFFICIENT = 91;
    public static final int ADDRESS_MOTOR2_I_COEFFICIENT = 92;
    public static final int ADDRESS_MOTOR2_D_COEFFICIENT = 93;
    public static final int ADDRESS_UNUSED = 255;
    public static final int[] ADDRESS_MOTOR_POWER_MAP = new int[]{255, 69, 70};
    public static final int[] ADDRESS_MOTOR_MODE_MAP = new int[]{255, 68, 71};
    public static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = new int[]{255, 64, 72};
    public static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = new int[]{255, 76, 80};
    public static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP = new int[]{255, 86, 90};
    public static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = new int[]{255, 87, 91};

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public NonBlockingModernMotorController(SerialNumber serialNumber, RobotUsbDevice device, EventLoopManager manager) throws RobotCoreException, InterruptedException
        {
        super(serialNumber, manager, new ReadWriteRunnableStandard(serialNumber, device, MONITOR_LENGTH, START_ADDRESS, false));

        this.floatMotors();
        this.initializePID();
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        this.floatMotors();
        super.close();
        }

    @Override public String getConnectionInfo()
        {
        return "USB " + this.getSerialNumber();
        }

    @Override public String getDeviceName()
        {
        return "Swerve Modern Motor Controller";
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController
    //----------------------------------------------------------------------------------------------

    @Override public int getMotorCurrentPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] var2 = this.read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4);
        return TypeConversion.byteArrayToInt(var2);
        }

    @Override public int getMotorTargetPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] rgbPosition = this.read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], 4);
        return TypeConversion.byteArrayToInt(rgbPosition);
        }

    @Override public void setMotorTargetPosition(int motor, int position)
        {
        this.validateMotor(motor);
        Range.throwIfRangeIsInvalid((double)position, -2.147483648E9D, 2.147483647E9D);
        this.write(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], TypeConversion.intToByteArray(position));
        }

    @Override public boolean getMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.read(ADDRESS_MOTOR_POWER_MAP[motor]);
        return bPower == POWER_FLOAT;
        }

    @Override public void setMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        this.write(ADDRESS_MOTOR_POWER_MAP[motor], new byte[]{(byte) POWER_FLOAT});
        }

    @Override public boolean isBusy(int i)
        {
        return false; // WRONG
        }

    @Override public double getMotorPower(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.read(ADDRESS_MOTOR_POWER_MAP[motor]);

        if (bPower == POWER_FLOAT)
            return 0;
        else
            return Range.scale(bPower, POWER_MIN, POWER_MAX, -1.0, 1.0);
        }

    @Override public void setMotorPower(int motor, double power)
        {
        this.validateMotor(motor);
        power = Range.clip(power, -1.0, 1.0);   // NB: ftc runtime threw on invalid range instead of clipping
        power = Range.scale(power, -1.0, 1.0, POWER_MIN, POWER_MAX);
        this.write(ADDRESS_MOTOR_POWER_MAP[motor], new byte[]{(byte)((int)(power))});
        }

    @Override public RunMode getMotorChannelMode(int motor)
        {
        this.validateMotor(motor);
        return flagToRunMode(this.read(ADDRESS_MOTOR_MODE_MAP[motor]));
        }

    @Override public void setMotorChannelMode(int motor, RunMode mode)
        {
        this.validateMotor(motor);
        byte bMode = runModeToFlag(mode);
        this.write(ADDRESS_MOTOR_MODE_MAP[motor], bMode);
        }

    @Override public DeviceMode getMotorControllerDeviceMode()
        {
        return DeviceMode.READ_WRITE;
        }

    @Override public void setMotorControllerDeviceMode(DeviceMode deviceMode)
        {
        // Nothing to do
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private void floatMotors()
        {
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
        }
    
    private void initializePID() 
        {
        for (int motor = MIN_MOTOR; motor <= MAX_MOTOR; ++motor)
            {
            this.write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[]{(byte)-128, (byte)64, (byte)-72});
            }
        }

    private void validateMotor(int motor)
        {
        if(motor < MIN_MOTOR || motor > MAX_MOTOR)
            {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d",motor, MIN_MOTOR, MAX_MOTOR));
            }
        }

    public static byte runModeToFlag(RunMode mode)
        {
        switch(mode)
            {
        default:
        case RUN_WITHOUT_ENCODERS:  return (byte)0;
        case RUN_USING_ENCODERS:    return (byte)1;
        case RUN_TO_POSITION:       return (byte)2;
        case RESET_ENCODERS:        return (byte)3;
            }
        }

    public static RunMode flagToRunMode(byte flag)
        {
        switch(flag & 3)
            {
        default:
        case 0: return RunMode.RUN_WITHOUT_ENCODERS;
        case 1: return RunMode.RUN_USING_ENCODERS;
        case 2: return RunMode.RUN_TO_POSITION;
        case 3: return RunMode.RESET_ENCODERS;
            }
        }
    }
