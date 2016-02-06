package org.swerverobotics.library.internal;

import android.util.Log;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import java.nio.*;

import static junit.framework.Assert.*;

/**
 * An alternative implementation of a Legacy DC Motor controller.
 *
 * @see org.swerverobotics.library.ClassFactory#createEasyLegacyMotorController(OpMode, DcMotor, DcMotor)
 * @see org.swerverobotics.library.SynchronousOpMode#useExperimentalThunking
 */
public final class EasyLegacyMotorController extends I2cControllerPortDeviceImpl implements DcMotorController, IThunkWrapper<DcMotorController>, VoltageSensor, IOpModeStateTransitionEvents, Engagable
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public final String LOGGING_TAG = SynchronousOpMode.LOGGING_TAG;
    
    /* The NXT HiTechnic motor controller register layout is as follows:

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
    54, 55H     word     Battery voltage 54H high byte, 55H low byte     == 84-85
    56H         S/byte   Motor 1 gear ratio         == 86
    57H         byte     Motor 1 P coefficient*     == 87
    58H         byte     Motor 1 I coefficient*     == 88
    59H         byte     Motor 1 D coefficient*     == 89
    5AH         s/byte   Motor 2 gear ratio         == 90
    5BH         byte     Motor 2 P coefficient*     == 91
    5CH         byte     Motor 2 I coefficient*     == 92
    5DH         byte     Motor 2 D coefficient*     == 93
     */
    private static final int iRegWindowFirst = 0x40;
    private static final int iRegWindowMax   = 0x56;  // first register not included
    
    // motor numbers are 1-based
    private static final byte[] mpMotorRegMotorPower          = new byte[]{(byte)-1, (byte)0x45, (byte)0x46};
    private static final byte[] mpMotorRegMotorMode           = new byte[]{(byte)-1, (byte)0x44, (byte)0x47};
    private static final byte[] mpMotorRegTargetEncoderValue  = new byte[]{(byte)-1, (byte)0x40, (byte)0x48};
    private static final byte[] mpMotorRegCurrentEncoderValue = new byte[]{(byte)-1, (byte)0x4c, (byte)0x50};

    private static final int  motorFirst = 1;
    private static final int  motorLast  = 2;
    private static final byte cbEncoder  = 4;

    private static final byte bPowerBrake = 0;
    private static final byte bPowerFloat = -128;
    private static final byte bPowerMax = 100;
    private static final byte bPowerMin = -100;
    
    private static final double powerMin = -1.0;
    private static final double powerMax = 1.0;

    private final OpMode                    context;
    private final II2cDeviceClient          i2cDeviceClient;
    private final DcMotorController         target;
    I2cDeviceReplacementHelper<DcMotorController> helper;

    private       DcMotor                   motor1;
    private       DcMotor                   motor2;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private EasyLegacyMotorController(OpMode context, II2cDeviceClient ii2cDeviceClient, DcMotorController target, I2cController controller, int targetPort)
        {
        super(((I2cControllerPortDevice)target).getI2cController(), ((I2cControllerPortDevice)target).getPort());
        this.helper          = new I2cDeviceReplacementHelper<DcMotorController>(context, this, target, controller, targetPort);

        this.context         = context;
        this.i2cDeviceClient = ii2cDeviceClient;
        this.target          = target;
        this.motor1          = null;
        this.motor2          = null;

        RobotStateTransitionNotifier.register(context, this);

        // The NXT HiTechnic motor controller will time out if it doesn't receive any I2C communication for
        // 2.5 seconds. So we set up a heartbeat request to try to prevent that. We try to use
        // heartbeats which are as minimally disruptive as possible. Note as a matter of interest
        // that the heartbeat mechanism used by ModernRoboticsNxtDcMotorController is analogous to
        // 'rewriteLastWritten'.
        II2cDeviceClient.HeartbeatAction heartbeatAction = new II2cDeviceClient.HeartbeatAction();
        heartbeatAction.rereadLastRead      = true;
        heartbeatAction.rewriteLastWritten  = true;
        heartbeatAction.heartbeatReadWindow = new II2cDeviceClient.ReadWindow(mpMotorRegCurrentEncoderValue[1], 1, II2cDeviceClient.READ_MODE.ONLY_ONCE);

        this.i2cDeviceClient.setHeartbeatAction(heartbeatAction);
        this.i2cDeviceClient.setHeartbeatInterval(2000);

        // Also: set up a read-window. We make it BALANCED to avoid unnecessary ping-ponging
        // between read mode and write mode, since motors are read about as much as they are
        // written, but we make it relatively large so that least that when we DO go
        // into read mode and possibly do more than one read we will use this window
        // and won't have to fiddle with the 'switch to read mode' each and every time.
        // We include everything from the 'Motor 1 target encoder value' through the battery voltage.
        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.ReadWindow(iRegWindowFirst, iRegWindowMax-iRegWindowFirst, II2cDeviceClient.READ_MODE.BALANCED));
        }

    public static DcMotorController create(OpMode context, DcMotorController target, DcMotor motor1, DcMotor motor2)
        {
        if (MemberUtil.isLegacyMotorController(target))
            {
            HiTechnicNxtDcMotorController legacyTarget = (HiTechnicNxtDcMotorController)target;

            I2cController module      = legacyTarget.getI2cController();
            int          port         = legacyTarget.getPort();
            int          i2cAddr8Bit  = MemberUtil.i2cAddrOfLegacyMotorController(target);

            // Make a new legacy motor controller
            II2cDevice i2cDevice                 = new I2cDeviceOnI2cDeviceController(module, port);
            I2cDeviceClient i2cDeviceClient      = new I2cDeviceClient(context, i2cDevice, i2cAddr8Bit, false);
            EasyLegacyMotorController controller = new EasyLegacyMotorController(context, i2cDeviceClient, target, module, port);

            controller.setMotors(motor1, motor2);
            controller.engage();

            return controller;
            }
        else
            {
            // The target isn't a legacy motor controller, so we can't swap anything in for him.
            // Return the raw target (rather than, e.g., throwing) so that caller doesn't need to check
            // what kind of controller he has in hand.
            return target;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Construction utility
    //----------------------------------------------------------------------------------------------

    private void setMotors(DcMotor motor1, DcMotor motor2)
        {
        assertTrue(!BuildConfig.DEBUG || !this.isEngaged());

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

    private static final String swerveVoltageSensorName = " |Swerve|Legacy|VoltageSensor| ";

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

    synchronized public void engage()
    // Disarm the existing controller and arm us
        {
        if (!this.isEngaged())
            {
            this.usurpDevices();

            this.helper.engage();

            this.i2cDeviceClient.engage();
            this.registerVoltageSensor();
            this.initPID();
            this.floatMotors();
            }
        }

    synchronized public boolean isEngaged()
        {
        return this.helper.isEngaged();
        }

    synchronized public void disengage()
    // Disarm us and re-arm the target
        {
        if (this.isEngaged())
            {
            this.unregisterVoltageSensor();
            this.i2cDeviceClient.disengage();

            this.helper.disengage();

            this.deusurpDevices();
            }
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
        try {
            // Register is per the HiTechnic motor controller specification
            byte[] bytes = this.i2cDeviceClient.read(0x54, 2);

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
        catch (RuntimeInterruptedException e)
            {
            throw e;
            }
        catch (Exception e)
            {
            // Protect our clients from somehow getting an I2c related exception
            return 0;
            }
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return "Swerve EasyLegacyMotorController";
        }

    @Override public String getConnectionInfo()
        {
        return this.i2cDeviceClient.getConnectionInfo();
        }

    @Override public int getVersion()
        {
        return 1;
        }

    @Override public synchronized void close()
        {
        if (this.isEngaged())
            {
            this.floatMotors(); // mirrors robot controller runtime behavior
            this.disengage();
            }
        }

    //----------------------------------------------------------------------------------------------
    // IOpModeStateTransitionEvents
    //----------------------------------------------------------------------------------------------

    @Override synchronized public boolean onUserOpModeStop()
        {
        Log.d(LOGGING_TAG, "Easy legacy motor: auto-stopping...");
        if (this.isEngaged())
            {
            this.stopMotors();  // mirror StopRobotOpMode
            this.disengage();
            }
        Log.d(LOGGING_TAG, "Easy legacy motor: ... auto-stopping complete");
        return true;    // unregister us
        }

    @Override synchronized public boolean onRobotShutdown()
        {
        Log.d(LOGGING_TAG, "Easy legacy motor: auto-closing...");

        // We actually shouldn't be here by now, having received a onUserOpModeStop()
        // after which we should have been unregistered. But we close down anyway.
        this.close();

        Log.d(LOGGING_TAG, "Easy legacy motor: ... auto-closing complete");
        return true;    // unregister us
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController
    //----------------------------------------------------------------------------------------------

    private boolean isArmed()
        {
        return this.i2cDeviceClient.isArmed();
        }

    @Override public synchronized void setMotorControllerDeviceMode(DcMotorController.DeviceMode port)
        {
        // ignored
        }

    @Override public synchronized DcMotorController.DeviceMode getMotorControllerDeviceMode()
        {
        return DeviceMode.READ_WRITE;
        }
    
    @Override public synchronized void setMotorChannelMode(int motor, DcMotorController.RunMode mode)
        {
        this.validateMotor(motor);
        byte bNewMode = modeToByte(mode);

        // We write the whole byte, but only the lower five bits are actually writable
        // and we only ever use the lowest two as non zero.
        this.write8(mpMotorRegMotorMode[motor], bNewMode);

        // The mode switch doesn't happen instantaneously. Wait for it,
        // so that the programmer's model is that he just needs to set the
        // mode and be done.
        for (;;)
            {
            if (!this.isArmed()) break;

            byte bCurrentMode = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
            if (bCurrentMode == bNewMode)
                break;
            Thread.yield();;
            }

        // If the mode is 'reset encoders', we don't want to return until the encoders have actually reset
        //      http://ftcforum.usfirst.org/showthread.php?4924-Use-of-RUN_TO_POSITION-in-LineraOpMode&highlight=reset+encoders
        //      http://ftcforum.usfirst.org/showthread.php?4567-Using-and-resetting-encoders-in-MIT-AI&p=19303&viewfull=1#post19303
        // For us, here, we believe we'll always *immediately* have that be true, as our writes
        // to the I2C device actually happen when we issue them.
        //
        // Or, at least, insofar as anything is actually *observable*: the write will be issued
        // ahead of any subsequent reads or writes. Thus, the assertTrue here would never fire,
        // since the getMotorCurrentPosition() would follow the write and see its effect. However,
        // having the assert does unnecessarily slow things down. We'll keep it for a while, then
        // probably comment it out.
        //
        if (mode == RunMode.RESET_ENCODERS)
            {
            // Unclear if this is needed
            while (this.getMotorTargetPosition(motor) != 0)
                {
                if (!this.isArmed()) break;
                Thread.yield();
                }
            }
        else if (mode == RunMode.RUN_TO_POSITION)
            {
            // Enforce that in RUN_TO_POSITION, we always need *positive* power. DCMotor will
            // take care of that if we set power *after* we set the mode, but not the other way
            // around. So we handle that here.
            //
            // Unclear that this is needed. The motor controller might take the absolute value automatically
            double power = getMotorPower(motor);
            if (power < 0)
                setMotorPower(motor, Math.abs(power));
            }
        }

    @Override public synchronized DcMotorController.RunMode getMotorChannelMode(int motor)
        {
        this.validateMotor(motor);
        byte b = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        return modeFromByte(b);
        }

    // From the HiTechnic Motor Controller specification
    //
    //      The Run to position command will cause the firmware to run the motor to make the current encoder
    //      value to become equal to the target encoder value. It will do this using a maximum rotation rate
    //      as defined by the motor power byte. It will hold this position in a servo like mode until the Run
    //      to position command is changed or the target encoder value is changed. While the Run to position
    //      command is executing, the Busy bit will be set. Once the target position is achieved, the Busy bit
    //      will be cleared. There may be a delay of up to 50mS after a Run to position command is initiated
    //      before the Busy bit will be set.
    //
    // Our task here is to work around that 50ms issue

    static final int busyThreshold = 5;

    @Override public synchronized boolean isBusy(int motor)
        {
        this.validateMotor(motor);

        int cur = getMotorCurrentPosition(motor);
        int tar = getMotorTargetPosition(motor);
        RunMode mode = getMotorChannelMode(motor);

        return mode==RunMode.RUN_TO_POSITION && (Math.abs(cur - tar) > busyThreshold);
        }

    @Override public synchronized void setMotorPower(int motor, double power)
        {
        this.validateMotor(motor);
        
        // Unlike the (beta) robot controller library, we saturate the motor
        // power rather than making clients worry about doing that.
        power = Range.clip(power, powerMin, powerMax);
        
        // The legacy values are -100 to 100
        byte bPower = (byte)Range.scale(power, powerMin, powerMax, bPowerMin, bPowerMax);
        
        // Write it on out
        this.write8(mpMotorRegMotorPower[motor], bPower);
        }

    @Override public synchronized double getMotorPower(int motor)
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

    @Override public synchronized void setMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = bPowerFloat;
        this.write8(mpMotorRegMotorPower[motor], bPower);
        }

    @Override public synchronized boolean getMotorPowerFloat(int motor)
        {
        this.validateMotor(motor);
        byte bPower = this.i2cDeviceClient.read8(mpMotorRegMotorMode[motor]);
        return bPower == bPowerFloat;
        }

    @Override public synchronized void setMotorTargetPosition(int motor, int position)
        {
        this.validateMotor(motor);
        byte[] bytes = TypeConversion.intToByteArray(position, ByteOrder.BIG_ENDIAN);
        this.write(mpMotorRegTargetEncoderValue[motor], bytes);
        }

    @Override public synchronized int getMotorTargetPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegTargetEncoderValue[motor], cbEncoder);
        return TypeConversion.byteArrayToInt(bytes, ByteOrder.BIG_ENDIAN);
        }

    @Override public synchronized int getMotorCurrentPosition(int motor)
        {
        this.validateMotor(motor);
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegCurrentEncoderValue[motor], cbEncoder);
        return TypeConversion.byteArrayToInt(bytes, ByteOrder.BIG_ENDIAN);
        }
    
    //----------------------------------------------------------------------------------------------
    // DcMotorController utility
    //----------------------------------------------------------------------------------------------

    private void write8(int ireg, byte data)
        {
        if (this.isEngaged())
            this.i2cDeviceClient.write8(ireg, data, false);
        }

    private void write(int ireg, byte[] data)
        {
        if (this.isEngaged())
            this.i2cDeviceClient.write(ireg, data, false);
        }

    private void initPID()
        {
        // nothing to do here, it seems
        }

    private void floatMotors()
        {
        Log.d(LOGGING_TAG, "floating motors");
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
        i2cDeviceClient.waitForWriteCompletions();  // paranoia about safety
        }

    private void stopMotors()
        {
        Log.d(LOGGING_TAG, "stopping motors");
        this.setMotorPower(1, 0);
        this.setMotorPower(2, 0);
        i2cDeviceClient.waitForWriteCompletions();  // paranoia about safety
        }

    private void validateMotor(int motor)
        {
        if(motor < motorFirst || motor > motorLast)
            {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d", motor, motorFirst, motorLast));
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
