package org.swerverobotics.library.internal;

import android.util.Log;
import com.qualcomm.hardware.*;
import com.qualcomm.modernrobotics.*;
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.BuildConfig;

import java.nio.*;
import java.util.*;
import java.util.concurrent.*;

import static junit.framework.Assert.assertTrue;

/**
 * An alternate implementation of the driver for a Modern Robotics DC Motor Controller.
 * Not yet used.
 */
public class NonBlockingModernMotorController extends ModernRoboticsUsbDevice implements DcMotorController, VoltageSensor, IOpModeStateTransitionEvents
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String LOGGING_TAG = SynchronousOpMode.LOGGING_TAG;

    private final OpMode                             context;
    private final EventLoopManager                   eventLoopManager;
    private final IsBusyHelper[]                     isBusyHelpers;
    private boolean                                  isArmed;
    private DcMotor                                  motor1;
    private DcMotor                                  motor2;
    private final ModernRoboticsUsbDcMotorController target;
    private final RobotUsbDevice                     robotUsbDevice;
    private String                                   targetName;
    private HardwareMap.DeviceMapping                targetDeviceMapping;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private NonBlockingModernMotorController(OpMode context, ModernRoboticsUsbDcMotorController target) throws RobotCoreException, InterruptedException
        {
        // We *have*to* construct with something live
        super(target.getSerialNumber(), SwerveThreadContext.getEventLoopManager(), newDummyReadWriteRunnable(target.getSerialNumber()));
        // But we shut it down right away, because we want to start disarmed until we fully configure
        Util.shutdownAndAwaitTermination(this.readWriteService, new Runnable() {
            @Override public void run() {
                ((ReadWriteRunnableStandard)NonBlockingModernMotorController.this.readWriteRunnable).close();
                }
            });

        this.context = context;
        this.eventLoopManager = SwerveThreadContext.getEventLoopManager();
        this.isArmed = false;
        this.target  = target;
        this.findTargetNameAndMapping();

        ReadWriteRunnableStandard readWriteRunnableStandard = MemberUtil.getReadWriteRunnableModernRoboticsUsbDevice(target);
        ReadWriteRunnableUsbHandler handler                 = MemberUtil.getHandlerOfReadWriteRunnableStandard(readWriteRunnableStandard);
        this.robotUsbDevice                                 = MemberUtil.getRobotUsbDeviceOfReadWriteRunnableUsbHandler(handler);

        RobotStateTransitionNotifier.register(context, this);

        this.isBusyHelpers = new IsBusyHelper[3];
        for (int i = 0; i < this.isBusyHelpers.length; i++)
            this.isBusyHelpers[i] = new IsBusyHelper();
        }

    public static DcMotorController create(OpMode context, DcMotorController target, DcMotor motor1, DcMotor motor2)
        {
        try {
            if (MemberUtil.isModernMotorController(target))
                {
                NonBlockingModernMotorController controller = new NonBlockingModernMotorController(context, (ModernRoboticsUsbDcMotorController) target);
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

    private void usurpMotors()
        {
        if (this.motor1 != null) MemberUtil.setControllerOfMotor(this.motor1, this);
        if (this.motor2 != null) MemberUtil.setControllerOfMotor(this.motor2, this);
        }

    private void deusurpMotors()
        {
        if (this.motor1 != null) MemberUtil.setControllerOfMotor(this.motor1, this.target);
        if (this.motor2 != null) MemberUtil.setControllerOfMotor(this.motor2, this.target);
        }

    private static final String swerveVoltageSensorName = " |Swerve|Modern|VoltageSensor| ";

    private void registerVoltageSensor()
        {
        if (this.context != null)
            {
            // Are there any voltage sensors there in the map the robot controller runtime made?
            if (this.context.hardwareMap.voltageSensor.size() == 0)
                {
                // No, there isn't. Well, we're one. We'll take up the challenge!
                this.context.hardwareMap.voltageSensor.put(swerveVoltageSensorName, this);
                }
            }
        }

    private void unregisterVoltageSensor()
        {
        if (this.context != null)
            {
            if (Util.contains(this.context.hardwareMap.voltageSensor, swerveVoltageSensorName))
                {
                VoltageSensor voltageSensor = this.context.hardwareMap.voltageSensor.get(swerveVoltageSensorName);
                if (voltageSensor == (VoltageSensor)this)
                    {
                    Util.removeName(this.context.hardwareMap.voltageSensor, swerveVoltageSensorName);
                    }
                }
            }
        }

    private boolean isArmed()
        {
        return this.isArmed;
        }

    private void arm()
        {
        if (!this.isArmed())
            {
            Log.d(LOGGING_TAG, String.format("arming %s", this.getConnectionInfo()));
            this.usurpMotors();
            //
            // Turn off target's usb stuff
            this.eventLoopManager.unregisterSyncdDevice(MemberUtil.getReadWriteRunnableModernRoboticsUsbDevice(this.target));
            this.floatMotors(target);
            this.closeModernRoboticsUsbDevice(target);
            //
            if (this.targetName != null) this.targetDeviceMapping.put(this.targetName, this);
            this.isArmed = true;
            //
            // Turn on our usb stuff
            this.installReadWriteRunnable(this);
            //
            this.registerVoltageSensor();
            this.initPID();
            this.floatMotors();
            }
        }

    private void disarm()
        {
        if (this.isArmed())
            {
            Log.d(LOGGING_TAG, String.format("disarming %s", this.getConnectionInfo()));
            this.unregisterVoltageSensor();
            //
            // Turn off our usb stuff
            this.eventLoopManager.unregisterSyncdDevice(this.readWriteRunnable);
            this.close();
            //
            this.isArmed = false;
            if (this.targetName != null) this.targetDeviceMapping.put(this.targetName, this.target);
            //
            // Turn target's usb stuff back on
            this.installReadWriteRunnable(this.target);
            //
            this.deusurpMotors();
            }
        }

    void closeModernRoboticsUsbDevice(ModernRoboticsUsbDevice usbDevice)
    // Close down the usbDevice in a robust and reliable way
        {
        // Get access to the state
        ExecutorService service = MemberUtil.getExecutorServiceModernRoboticsUsbDevice(usbDevice);
        ReadWriteRunnableStandard readWriteRunnableStandard = MemberUtil.getReadWriteRunnableModernRoboticsUsbDevice(usbDevice);

        // Stop accepting new work
        service.shutdown();

        // Set a dummy handler so that we don't end up closing the actual FT_device
        RobotUsbDevice robotUsbDevice = newDummyRobotUsbDevice();
        ReadWriteRunnableUsbHandler dummyHandler = new ReadWriteRunnableUsbHandler(robotUsbDevice);
        MemberUtil.setHandlerOfReadWriteRunnableStandard(readWriteRunnableStandard, dummyHandler);

        // Set 'running' to false; this fixes a race condition
        MemberUtil.setRunningReadWriteRunnableStandard(readWriteRunnableStandard, false);

        // Ok: actually carry out the close
        readWriteRunnableStandard.close();

        // Wait until the thread terminates
        Util.awaitTermination(service);
        }

    void installReadWriteRunnable(ModernRoboticsUsbDevice usbDevice)
        {
        try
            {
            ExecutorService service = Executors.newSingleThreadScheduledExecutor();
            ReadWriteRunnableStandard rwRunnable = new OurReadWriteRunnable(usbDevice.getSerialNumber(), this.robotUsbDevice, MONITOR_LENGTH, START_ADDRESS, false);
            //
            MemberUtil.setExecutorServiceModernRoboticsUsbDevice(usbDevice, service);
            MemberUtil.setReadWriteRunnableModernRoboticsUsbDevice(usbDevice, rwRunnable);
            service.execute(rwRunnable);
            rwRunnable.blockUntilReady();
            rwRunnable.setCallback(usbDevice);
            this.eventLoopManager.registerSyncdDevice(rwRunnable);
            }
        catch (Exception e)
            {
            Util.handleCapturedException(e);
            }
        }

    static ReadWriteRunnableStandard newDummyReadWriteRunnable(SerialNumber serialNumber)
        {
        RobotUsbDevice robotUsbDevice = new RobotUsbDevice()
            {
            @Override public void setBaudRate(int i) throws RobotCoreException {}
            @Override public void setDataCharacteristics(byte b, byte b1, byte b2) throws RobotCoreException  {}
            @Override public void setLatencyTimer(int i) throws RobotCoreException {}
            @Override public void purge(Channel channel) throws RobotCoreException {}
            @Override public void write(byte[] bytes) throws RobotCoreException {}
            @Override public int read(byte[] bytes) throws RobotCoreException { return 0; }
            @Override public int read(byte[] bytes, int i, int i1) throws RobotCoreException { return 0; }
            @Override public void close()  {}
            };
        return new OurReadWriteRunnable(serialNumber, robotUsbDevice, MONITOR_LENGTH, START_ADDRESS, false);
        }

    static RobotUsbDevice newDummyRobotUsbDevice()
        {
        return new RobotUsbDevice()
            {
            @Override public void setBaudRate(int i) throws RobotCoreException {}
            @Override public void setDataCharacteristics(byte b, byte b1, byte b2) throws RobotCoreException {}
            @Override public void setLatencyTimer(int i) throws RobotCoreException {}
            @Override public void purge(Channel channel) throws RobotCoreException {}
            @Override public void write(byte[] bytes) throws RobotCoreException {}
            @Override public int read(byte[] bytes) throws RobotCoreException { return 0; }
            @Override public int read(byte[] bytes, int i, int i1) throws RobotCoreException { return 0; }
            @Override public void close() { }
            };
        }

    static class OurReadWriteRunnable extends ReadWriteRunnableStandard
        {
        public OurReadWriteRunnable(SerialNumber serialNumber, RobotUsbDevice device, int monitorLength, int startAddress, boolean debug)
            {
            super(serialNumber, device, monitorLength, startAddress, debug);
            }

        @Override
        public void blockUntilReady() throws RobotCoreException, InterruptedException
            {
            // Do nothing. In particular, don't report any errors
            }
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        this.floatMotors();
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

    @Override public int getMotorCurrentPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4);
        return TypeConversion.byteArrayToInt(bytes);
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
        synchronized (this.isBusyHelpers[motor])
            {
            this.isBusyHelpers[motor].noteTargetPositionSet(position);
            this.write(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], TypeConversion.intToByteArray(position));
            }
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

    @Override public boolean isBusy(int motor)
        {
        this.validateMotor(motor);
        return this.isBusyHelpers[motor].isBusy();
        }

    @Override public double getMotorPower(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.read(ADDRESS_MOTOR_POWER_MAP[motor]);

        if (bPower == POWER_FLOAT)
            return 0;
        else
            return Range.scale(bPower, POWER_MIN, POWER_MAX, API_POWER_MIN, API_POWER_MAX);
        }

    @Override public void setMotorPower(int motor, double power)
        {
        this.validateMotor(motor);
        power = Range.clip(power, API_POWER_MIN, API_POWER_MAX);   // NB: runtime previously threw on invalid range instead of clipping
        power = Range.scale(power, API_POWER_MIN, API_POWER_MAX, POWER_MIN, POWER_MAX);
        this.write(ADDRESS_MOTOR_POWER_MAP[motor], new byte[]{(byte)((int)(power))});
        }

    @Override public RunMode getMotorChannelMode(int motor)
        {
        this.validateMotor(motor);
        return byteToRunMode(this.read(ADDRESS_MOTOR_MODE_MAP[motor]));
        }

    @Override public void setMotorChannelMode(int motor, RunMode mode)
        {
        this.validateMotor(motor);
        byte bMode = runModeToByte(mode);
        synchronized (this.isBusyHelpers[motor])
            {
            this.write(ADDRESS_MOTOR_MODE_MAP[motor], bMode);
            this.isBusyHelpers[motor].noteMotorMode(mode);
            }
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
    // Support
    // http://ftcforum.usfirst.org/showthread.php?4639-New-release-of-FTC-SDK-and-FTC-apps-are-out-there&p=17430&viewfull=1#post17430
    // http://ftcforum.usfirst.org/showthread.php?5369-DC-motor-controller-run-mode-definitions&p=20960&viewfull=1#post20960
    //
    // From the HiTechnic Motor Controller specification:
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

    @Override
    public void readComplete() throws InterruptedException
        {
        if (this.isArmed())
            {
            for (int motor = FIRST_MOTOR; motor <= LAST_MOTOR; motor++)
                {
                this.isBusyHelpers[motor].noteMotorPosition(this.getMotorCurrentPosition(motor));
                }
            }
        }

    class IsBusyHelper
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        static final int positionTolerance = 10;

        int     targetPosition;
        int     currentPosition;
        boolean targetPositionSet;
        boolean currentPositionSet;
        RunMode motorMode;

        //------------------------------------------------------------------------------------------
        // Construction
        //------------------------------------------------------------------------------------------

        public IsBusyHelper()
            {
            this.targetPosition      = 0;
            this.currentPosition     = 0;
            this.targetPositionSet   = false;
            this.currentPositionSet  = false;
            this.motorMode           = null;
            }

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        public synchronized boolean isBusy()
            {
            if (this.motorMode == RunMode.RUN_TO_POSITION)
                {
                if (this.currentPositionSet && this.targetPositionSet)
                    {
                    return Math.abs(this.currentPosition - this.targetPosition) <= positionTolerance;
                    }
                }
            return false;
            }

        public synchronized void noteMotorMode(RunMode motorMode)
            {
            this.motorMode = motorMode;
            if (this.motorMode == RunMode.RESET_ENCODERS)
                {
                this.currentPosition = 0;
                this.currentPositionSet = true;
                }
            }

        public synchronized void noteTargetPositionSet(int position)
            {
            this.targetPosition = position;
            this.targetPositionSet = true;
            }

        public synchronized void noteMotorPosition(int position)
            {
            this.currentPosition = position;
            this.currentPositionSet = true;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private void floatMotors()
        {
        this.floatMotors(this);
        }

    private void floatMotors(DcMotorController controller)
        {
        controller.setMotorPowerFloat(1);
        controller.setMotorPowerFloat(2);
        }

    private void stopMotors()
        {
        this.setMotorPower(1, 0);
        this.setMotorPower(2, 0);
        }
    
    private void initPID()
        {
        for (int motor = FIRST_MOTOR; motor <= LAST_MOTOR; ++motor)
            {
            this.write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[]{(byte)-128, (byte)64, (byte)-72});
            }
        }

    private void validateMotor(int motor)
        {
        if(motor < FIRST_MOTOR || motor > LAST_MOTOR)
            {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d",motor, FIRST_MOTOR, LAST_MOTOR));
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
    // IOpModeStateTransitionEvents
    //----------------------------------------------------------------------------------------------

    @Override synchronized public boolean onUserOpModeStop()
        {
        Log.d(LOGGING_TAG, "EasyModern: auto-stopping...");
        if (this.isArmed())
            {
            this.stopMotors();  // mirror StopRobotOpMode
            this.disarm();
            }
        Log.d(LOGGING_TAG, "EasyModern: ... done");
        return true;    // unregister us
        }

    @Override synchronized public boolean onRobotShutdown()
        {
        Log.d(LOGGING_TAG, "EasyModern: auto-closing...");

        // We actually shouldn't be here by now, having received a onUserOpModeStop()
        // after which we should have been unregistered. But we close down anyway.
        this.close();

        Log.d(LOGGING_TAG, "EasyModern: ... done");
        return true;    // unregister us
        }

    //----------------------------------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------------------------------

    public static final int MONITOR_LENGTH = 30;
    public static final int FIRST_MOTOR = 1;
    public static final int LAST_MOTOR = 2;
    public static final double API_POWER_MIN = -1.0;
    public static final double API_POWER_MAX =  1.0;
    public static final byte POWER_MAX = 100;
    public static final byte POWER_BRAKE = 0;
    public static final byte POWER_MIN = -100;
    public static final byte POWER_FLOAT = -128;
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
