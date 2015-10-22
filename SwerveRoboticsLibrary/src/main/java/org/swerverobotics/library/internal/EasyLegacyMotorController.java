package org.swerverobotics.library.internal;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import java.nio.*;
import java.util.*;
import static junit.framework.Assert.*;
import static org.swerverobotics.library.internal.ThunkingHardwareFactory.*;

/**
 * An alternative implementation of a Legacy DC Motor controller.
 *
 * @see org.swerverobotics.library.ClassFactory#createEasyLegacyMotorController(OpMode, DcMotor, DcMotor)
 * @see org.swerverobotics.library.SynchronousOpMode#useExperimentalThunking
 */
public final class EasyLegacyMotorController implements DcMotorController, IThunkWrapper<DcMotorController>, VoltageSensor, IOpModeStateTransitionEvents
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------
    
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
    56H         S/byte   Motor 1 gear ratio
    57H         byte     Motor 1 P coefficient*
    58H         byte     Motor 1 I coefficient*
    59H         byte     Motor 1 D coefficient*
    5AH         s/byte   Motor 2 gear ratio
    5BH         byte     Motor 2 P coefficient*
    5CH         byte     Motor 2 I coefficient*
    5DH         byte     Motor 2 D coefficient*
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

    private static final String             swerveVoltageSensorName = " |Swerve|VoltageSensor| ";
    private final OpMode                    context;
    private final II2cDeviceClient          i2cDeviceClient;
    private final DcMotorController         target;
    private       String                    targetName;
    private final LegacyModule              legacyModule;
    private final int                       targetPort;
    I2cController.I2cPortReadyCallback      targetCallback;
    private       boolean                   isArmed;
    private       DcMotor                   motor1;
    private       DcMotor                   motor2;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private EasyLegacyMotorController(OpMode context, II2cDeviceClient ii2cDeviceClient, DcMotorController target)
        {
        assertTrue(!BuildConfig.DEBUG || !ii2cDeviceClient.isArmed());
        this.context         = context;
        this.i2cDeviceClient = ii2cDeviceClient;
        this.target          = target;
        this.targetName      = findTargetName();
        this.legacyModule    = MemberUtil.legacyModuleOfLegacyMotorController(target);
        this.targetPort      = MemberUtil.portOfLegacyMotorController(target);
        this.targetCallback  = null;
        this.isArmed         = false;
        this.motor1          = null;
        this.motor2          = null;

        RobotStateTransitionNotifier.register(context, this);

        this.initPID();

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

        // Also: set up a read-window. We make it 'ONLY_ONCE' to avoid unnecessary ping-ponging
        // between read mode and write mode, since motors are read about as much as they are
        // written, but we make it relatively large so that least that when we DO go
        // into read mode and possibly do more than one read we will use this window
        // and won't have to fiddle with the 'switch to read mode' each and every time.
        // We include everything from the 'Motor 1 target encoder value' through the battery voltage.
        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.ReadWindow(iRegWindowFirst, iRegWindowMax-iRegWindowFirst, II2cDeviceClient.READ_MODE.ONLY_ONCE));
        }

    public static DcMotorController create(OpMode context, DcMotorController target, DcMotor motor1, DcMotor motor2)
        {
        if (MemberUtil.isLegacyMotorController(target))
            {
            LegacyModule legacyModule = MemberUtil.legacyModuleOfLegacyMotorController(target);
            int          port         = MemberUtil.portOfLegacyMotorController(target);
            int          i2cAddr8Bit  = i2cAddrOfLegacyMotorController(target);

            // Make a new legacy motor controller
            II2cDevice i2cDevice                 = new I2cDeviceOnI2cDeviceController(legacyModule, port);
            I2cDeviceClient i2cDeviceClient      = new I2cDeviceClient(context, i2cDevice, i2cAddr8Bit, false);
            EasyLegacyMotorController controller = new EasyLegacyMotorController(context, i2cDeviceClient, target);

            controller.setMotors(motor1, motor2);
            controller.arm();

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
        assertTrue(!BuildConfig.DEBUG || !this.isArmed);

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
        if (this.motor1 != null)    setController(this.motor1, this);
        if (this.motor2 != null)    setController(this.motor2, this);
        }

    private void deusurpMotors()
        {
        if (this.motor1 != null) setController(this.motor1, this.target);
        if (this.motor2 != null) setController(this.motor2, this.target);
        }


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
            if (ThunkingHardwareFactory.contains(this.context.hardwareMap.voltageSensor, swerveVoltageSensorName))
                {
                VoltageSensor voltageSensor = this.context.hardwareMap.voltageSensor.get(swerveVoltageSensorName);
                if (voltageSensor == (VoltageSensor)this)
                    {
                    ThunkingHardwareFactory.removeName(this.context.hardwareMap.voltageSensor, swerveVoltageSensorName);
                    }
                }
            }
        }

    private String findTargetName()
        {
        if (this.context != null)
            {
            for (Map.Entry<String,DcMotorController> pair : this.context.hardwareMap.dcMotorController.entrySet())
                {
                if (pair.getValue() == this.target)
                    return pair.getKey();
                }
            }
        return null;
        }

    private void arm()
    // Disarm the existing controller and arm us
        {
        if (!this.isArmed)
            {
            this.usurpMotors();
            this.targetCallback = MemberUtil.callbacksOfLegacyModule(this.legacyModule)[this.targetPort];
            this.legacyModule.deregisterForPortReadyCallback(this.targetPort);
            if (this.targetName != null) this.context.hardwareMap.dcMotorController.put(this.targetName, this);
            this.i2cDeviceClient.arm();
            this.registerVoltageSensor();
            this.isArmed = true;

            this.floatMotors();
            }
        }
    private synchronized void disarm()
    // Disarm us and re-arm the target
        {
        if (this.isArmed)
            {
            this.isArmed = false;
            this.unregisterVoltageSensor();
            this.i2cDeviceClient.disarm();
            if (this.targetName != null) this.context.hardwareMap.dcMotorController.put(this.targetName, this.target);
            this.legacyModule.registerForI2cPortReadyCallback(this.targetCallback, this.targetPort);
            this.deusurpMotors();
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
            double result     = ((double)tenBits) / 4 * 0.080;
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
        if (this.isArmed)
            {
            this.floatMotors(); // mirrors robot controller runtime behavior
            this.disarm();
            }
        }

    //----------------------------------------------------------------------------------------------
    // IOpModeStateTransitionEvents
    //----------------------------------------------------------------------------------------------

    @Override synchronized public boolean onUserOpModeStop()
        {
        Log.d(SynchronousOpMode.LOGGING_TAG, "Easy: auto-stopping...");
        if (this.isArmed)
            {
            this.stopMotors();  // mirror StopRobotOpMode
            this.disarm();
            }
        Log.d(SynchronousOpMode.LOGGING_TAG, "Easy: ... done");
        return true;    // unregister us
        }

    @Override synchronized public boolean onRobotShutdown()
        {
        Log.d(SynchronousOpMode.LOGGING_TAG, "Easy: auto-closing...");

        // We actually shouldn't be here by now, having received a onUserOpModeStop()
        // after which we should have been unregistered. But we close down anyway.
        this.close();

        Log.d(SynchronousOpMode.LOGGING_TAG, "Easy: ... done");
        return true;    // unregister us
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
        
        // We write the whole byte, but only the lower five bits are actually writable
        // and we only ever use the lowest two as non zero.
        this.write8(mpMotorRegMotorMode[motor], b);
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
        
        // Unlike the (beta) robot controller library, we saturate the motor
        // power rather than making clients worry about doing that.
        power = Range.clip(power, powerMin, powerMax);
        
        // The legacy values are -100 to 100
        byte bPower = (byte)Range.scale(power, powerMin, powerMax, bPowerMin, bPowerMax);
        
        // Write it on out
        this.write8(mpMotorRegMotorPower[motor], bPower);
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
        this.write8(mpMotorRegMotorPower[motor], bPower);
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
        this.write(mpMotorRegTargetEncoderValue[motor], bytes);
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
        byte[] bytes = this.i2cDeviceClient.read(mpMotorRegCurrentEncoderValue[motor], cbEncoder);
        return TypeConversion.byteArrayToInt(bytes);
        }
    
    //----------------------------------------------------------------------------------------------
    // DcMotorController utility
    //----------------------------------------------------------------------------------------------

    private void write8(int ireg, byte data)
        {
        if (this.isArmed)
            this.i2cDeviceClient.write8(ireg, data, false);
        }

    private void write(int ireg, byte[] data)
        {
        if (this.isArmed)
            this.i2cDeviceClient.write(ireg, data, false);
        }

    private void initPID()
        {
        // nothing to do here, it seems
        }

    private void floatMotors()
        {
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
        }

    private void stopMotors()
        {
        this.setMotorPower(1, 0);
        this.setMotorPower(2, 0);
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
