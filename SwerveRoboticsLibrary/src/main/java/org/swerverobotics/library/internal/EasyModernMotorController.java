package org.swerverobotics.library.internal;

import android.util.Log;
import com.qualcomm.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.*;
import com.qualcomm.robotcore.util.*;

import org.swerverobotics.library.BuildConfig;
import java.nio.*;
import java.util.*;

import static junit.framework.Assert.assertTrue;

/**
 * An alternate implementation of the driver for a Modern Robotics DC Motor Controller.
 * This implementation doesn't use a blocking ReadWriteRunnable; that greatly simplifies
 * programming.
 */
public class EasyModernMotorController extends EasyModernController implements DcMotorController, VoltageSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private DcMotor                                  motor1;
    private DcMotor                                  motor2;
    private final ModernRoboticsUsbDcMotorController target;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private EasyModernMotorController(OpMode context, ModernRoboticsUsbDcMotorController target) throws RobotCoreException, InterruptedException
        {
        super(context, target, newDummyReadWriteRunnable(target.getSerialNumber()));

        this.target  = target;
        this.findTargetNameAndMapping();
        }

    static NoErrorReportingReadWriteRunnableStandard newDummyReadWriteRunnable(SerialNumber serialNumber)
        {
        RobotUsbDevice robotUsbDevice = new DummyRobotUsbDevice();
        return new NoErrorReportingReadWriteRunnableStandard(serialNumber, robotUsbDevice, MONITOR_LENGTH, START_ADDRESS, false);
        }

    public static DcMotorController create(OpMode context, DcMotorController target, DcMotor motor1, DcMotor motor2)
        {
        try {
            if (MemberUtil.isModernMotorController(target))
                {
                EasyModernMotorController controller = new EasyModernMotorController(context, (ModernRoboticsUsbDcMotorController) target);
                controller.setMotors(motor1, motor2);
                controller.arm();
                return controller;
                }
            else
                {
                return target;
                }
            }
        catch (Exception e)
            {
            Util.handleCapturedException(e);
            return null; // not reached
            }
        }

    private void findTargetNameAndMapping()
        {
        for (HardwareMap.DeviceMapping<?> mapping : Util.deviceMappings(this.context.hardwareMap))
            {
            for (Map.Entry<String,?> pair : mapping.entrySet())
                {
                if (pair.getValue() == this.target)
                    {
                    this.targetName = pair.getKey();
                    this.targetDeviceMapping = mapping;
                    return;
                    }
                }
            }
        }

    //----------------------------------------------------------------------------------------------
    // Construction utility
    //----------------------------------------------------------------------------------------------

    private void setMotors(DcMotor motor1, DcMotor motor2)
        {
        assertTrue(!BuildConfig.DEBUG || !this.isArmed());

        if ((motor1 != null && motor1.getController() != this.target)
         || (motor2 != null && motor2.getController() != this.target))
            {
            throw new IllegalArgumentException("motors have incorrect controller for usurpation");
            }

        this.motor1 = motor1;
        this.motor2 = motor2;
        }

    private void usurpDevices()
        {
        if (this.motor1 != null) MemberUtil.setControllerOfMotor(this.motor1, this);
        if (this.motor2 != null) MemberUtil.setControllerOfMotor(this.motor2, this);
        }

    private void deusurpDevices()
        {
        if (this.motor1 != null) MemberUtil.setControllerOfMotor(this.motor1, this.target);
        if (this.motor2 != null) MemberUtil.setControllerOfMotor(this.motor2, this.target);
        }

    private static final String swerveVoltageSensorName = " |Swerve|Modern|VoltageSensor| ";

    @Override public void arm()
        {
        if (!this.isArmed())
            {
            Log.d(LOGGING_TAG, String.format("arming \"%s\"....", this.getConnectionInfo()));
            this.usurpDevices();

            // Turn off target's usb stuff
            this.eventLoopManager.unregisterSyncdDevice(MemberUtil.getReadWriteRunnableModernRoboticsUsbDevice(this.target));
            this.floatHardware(target);
            this.closeModernRoboticsUsbDevice(target);
            //
            if (this.targetName != null)
                {
                this.targetDeviceMapping.put(this.targetName, this);
                this.context.hardwareMap.voltageSensor.put(this.targetName, this);
                }
            this.isArmed = true;

            // Turn on our usb stuff
            this.installReadWriteRunnable(this, MONITOR_LENGTH, START_ADDRESS);

            this.initPID();
            this.floatHardware();
            Log.d(LOGGING_TAG, String.format("....armed \"%s\"", this.getConnectionInfo()));
            }
        }

    @Override public void disarm()
        {
        if (this.isArmed())
            {
            Log.d(LOGGING_TAG, String.format("disarming \"%s\"....", this.getConnectionInfo()));

            // Turn off our usb stuff
            this.eventLoopManager.unregisterSyncdDevice(this.readWriteRunnable);
            this.close();
            //
            this.isArmed = false;
            if (this.targetName != null)
                {
                this.targetDeviceMapping.put(this.targetName, this.target);
                this.context.hardwareMap.voltageSensor.put(this.targetName, this.target);
                }

            // Turn target's usb stuff back on
            this.installReadWriteRunnable(this.target, MONITOR_LENGTH, START_ADDRESS);

            this.deusurpDevices();
            Log.d(LOGGING_TAG, String.format("....disarmed \"%s\"", this.getConnectionInfo()));
            }
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        this.floatHardware();
        closeModernRoboticsUsbDevice(this);
        }

    @Override public String getConnectionInfo()
        {
        return "Swerve: USB " + this.getSerialNumber();
        }

    @Override public String getDeviceName()
        {
        return "Swerve: Modern Robotics USB DC Motor Controller";
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController
    //----------------------------------------------------------------------------------------------

    @Override public synchronized int getMotorCurrentPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4);
        return TypeConversion.byteArrayToInt(bytes, ByteOrder.BIG_ENDIAN);
        }

    @Override public synchronized int getMotorTargetPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] rgbPosition = this.read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], 4);
        return TypeConversion.byteArrayToInt(rgbPosition, ByteOrder.BIG_ENDIAN);
        }

    @Override public synchronized void setMotorTargetPosition(int motor, int position)
        {
        this.validateMotor(motor);
        this.write(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], TypeConversion.intToByteArray(position, ByteOrder.BIG_ENDIAN));
        }

    @Override public synchronized boolean getMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.read(ADDRESS_MOTOR_POWER_MAP[motor]);
        return bPower == bPowerFloat;
        }

    @Override public synchronized void setMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        this.write(ADDRESS_MOTOR_POWER_MAP[motor], new byte[]{(byte) bPowerFloat});
        }

    //----------------------------------------------------------------------------------------------
    // Support
    // http://ftcforum.usfirst.org/showthread.php?4639-New-release-of-FTC-SDK-and-FTC-apps-are-out-there&p=17430&viewfull=1#post17430
    // http://ftcforum.usfirst.org/showthread.php?5369-DC-motor-controller-run-mode-definitions&p=20960&viewfull=1#post20960
    //
    // From the HiTechnic Motor Controller specification (and we guess that the Modern Motor Controller is similar):
    //
    //      The Run to position command will cause the firmware to run the motor to make the current encoder
    //      value to become equal to the target encoder value. It will do this using a maximum rotation rate
    //      as defined by the motor power byte. It will hold this position in a servo like mode until the Run
    //      to position command is changed or the target encoder value is changed. While the Run to position
    //      command is executing, the Busy bit will be set. Once the target position is achieved, the Busy bit
    //      will be cleared. There may be a delay of up to 50mS after a Run to position command is initiated
    //      before the Busy bit will be set.
    //
    //----------------------------------------------------------------------------------------------

    static final int busyThreshold = 5;

    @Override public synchronized boolean isBusy(int motor)
        {
        this.validateMotor(motor);

        int cur = getMotorCurrentPosition(motor);
        int tar = getMotorTargetPosition(motor);
        RunMode mode = getMotorChannelMode(motor);

        return mode==RunMode.RUN_TO_POSITION && (Math.abs(cur - tar) > busyThreshold);
        }

    @Override public synchronized double getMotorPower(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.read(ADDRESS_MOTOR_POWER_MAP[motor]);

        // Float counts as zero power
        if (bPower == bPowerFloat)
            return 0.0;

        // Other values are just linear scaling. The clipping is just paranoia about
        // numerical precision; it probably isn't necessary
        double power = Range.scale(bPower, bPowerMin, bPowerMax, powerMin, powerMax);
        return Range.clip(power, powerMin, powerMax);
        }

    @Override public synchronized void setMotorPower(int motor, double power)
        {
        this.validateMotor(motor);
        power = Range.clip(power, powerMin, powerMax);   // NB: robot controller runtime previously threw on invalid range instead of clipping
        power = Range.scale(power, powerMin, powerMax, bPowerMin, bPowerMax);
        this.write(ADDRESS_MOTOR_POWER_MAP[motor], new byte[]{(byte)((int)(power))});
        }

    @Override public synchronized RunMode getMotorChannelMode(int motor)
        {
        this.validateMotor(motor);
        return byteToRunMode(this.read(ADDRESS_MOTOR_MODE_MAP[motor]));
        }

    @Override public synchronized void setMotorChannelMode(int motor, RunMode mode)
        {
        this.validateMotor(motor);
        byte bNewMode = runModeToByte(mode);

        this.write(ADDRESS_MOTOR_MODE_MAP[motor], bNewMode);

        // The mode switch doesn't happen instantaneously. Wait for it,
        // so that the programmer's model is that he just needs to set the
        // mode and be done.
        for (;;)
            {
            byte bCurrentMode = this.read(ADDRESS_MOTOR_MODE_MAP[motor]);
            if (bCurrentMode == bNewMode)
                break;

            // The above read() read from cache. To avoid flooding the system,
            // we wait for the next read cycle before we try again: the cache
            // isn't going to change until then.
            waitForNextReadComplete();
            }

        // If the mode is 'reset encoders', we don't want to return until the encoders have actually reset
        //      http://ftcforum.usfirst.org/showthread.php?4924-Use-of-RUN_TO_POSITION-in-LineraOpMode&highlight=reset+encoders
        //      http://ftcforum.usfirst.org/showthread.php?4567-Using-and-resetting-encoders-in-MIT-AI&p=19303&viewfull=1#post19303
        // For us, here, we believe we'll always *immediately* have that be true, as our writes
        // to the USB device actually happen when we issue them.
        if (mode == RunMode.RESET_ENCODERS)
            {
            assertTrue(!BuildConfig.DEBUG || this.getMotorCurrentPosition(motor)==0);
            }
        else if (mode == RunMode.RUN_TO_POSITION)
            {
            // Enforce that in RUN_TO_POSITION, we always need *positive* power. DCMotor will
            // take care of that if we set power *after* we set the mode, but not the other way
            // around. So we handle that here.
            double power = getMotorPower(motor);
            if (power < 0)
                setMotorPower(motor, Math.abs(power));
            }
        }

    @Override public synchronized DeviceMode getMotorControllerDeviceMode()
        {
        return DeviceMode.READ_WRITE;
        }

    @Override public synchronized void setMotorControllerDeviceMode(DeviceMode deviceMode)
        {
        // Nothing to do
        }

    //----------------------------------------------------------------------------------------------
    // VoltageSensor
    //----------------------------------------------------------------------------------------------

    @Override
    public double getVoltage()
        {
        byte[] bytes = this.read(ADDRESS_BATTERY_VOLTAGE, 2);

        // "The high byte is the upper 8 bits of a 10 bit value. It may be used as an 8 bit
        // representation of the battery voltage in units of 80mV. This provides a measurement
        // range of 0 – 20.4 volts. The low byte has the lower 2 bits at bit locations 0 and 1
        // in the byte. This increases the measurement resolution to 20mV."
        bytes[1]          = (byte)(bytes[1] << 6);
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
        int tenBits       = (buffer.getShort()>>6) & 0x3FF;
        double result     = ((double)tenBits) * 0.020;
        return result;
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private void floatHardware()
        {
        this.floatHardware(this);
        }

    private void floatHardware(DcMotorController controller)
        {
        controller.setMotorPowerFloat(1);
        controller.setMotorPowerFloat(2);
        }

    @Override public void stopHardware()
        {
        stopMotors();
        }

    private void stopMotors()
        {
        this.setMotorPower(1, 0);
        this.setMotorPower(2, 0);
        }
    
    private void initPID()
        {
        for (int motor = motorFirst; motor <= motorLast; ++motor)
            {
            this.write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[]{(byte)-128, (byte)64, (byte)-72});
            }
        }

    private void validateMotor(int motor)
        {
        if(motor < motorFirst || motor > motorLast)
            {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d",motor, motorFirst, motorLast));
            }
        }

    public static byte runModeToByte(RunMode mode)
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

    public static RunMode byteToRunMode(byte flag)
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

    //----------------------------------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------------------------------

    public static final int MONITOR_LENGTH = 30;
    public static final int motorFirst = 1;
    public static final int motorLast = 2;
    public static final double powerMin = -1.0;
    public static final double powerMax =  1.0;
    public static final byte bPowerMax = 100;
    public static final byte POWER_BRAKE = 0;
    public static final byte bPowerMin = -100;
    public static final byte bPowerFloat = -128;
    public static final byte RATIO_MIN = -128;
    public static final byte RATIO_MAX = 127;
    public static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
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
    public static final int[] ADDRESS_MOTOR_POWER_MAP                               = new int[]{ADDRESS_UNUSED, 69, 70};
    public static final int[] ADDRESS_MOTOR_MODE_MAP                                = new int[]{ADDRESS_UNUSED, 68, 71};
    public static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP                = new int[]{ADDRESS_UNUSED, 64, 72};
    public static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP               = new int[]{ADDRESS_UNUSED, 76, 80};
    public static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP                          = new int[]{ADDRESS_UNUSED, 86, 90};
    public static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = new int[]{ADDRESS_UNUSED, 87, 91};
    }
