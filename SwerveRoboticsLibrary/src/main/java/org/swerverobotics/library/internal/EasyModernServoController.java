package org.swerverobotics.library.internal;

import android.util.Log;
import com.qualcomm.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.SerialNumber;
import org.swerverobotics.library.BuildConfig;
import java.util.*;

import static junit.framework.Assert.*;


/**
 * An alternate implementation of the driver for a Modern Robotics Servo Controller.
 * This implementation doesn't use a blocking ReadWriteRunnable; that simplifies programming.
 */
public class EasyModernServoController extends EasyModernController implements ServoController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final byte[] ADDRESS_CHANNEL_MAP = new byte[]{(byte)-1, (byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47};
    public static final int SERVO_FIRST = 1;
    public static final int SERVO_LAST  = 6;
    public static final int ADDRESS_PWM = 0x48;
    public static final int MONITOR_LENGTH = 9;
    public static final byte PWM_DISABLE = -1;
    public static final byte PWM_ENABLE = 0;
    public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
    public static final byte START_ADDRESS = 0x40;

    public static final double positionMin = 0.0;
    public static final double positionMax = 1.0;
    public static final double regPositionMin = 0;
    public static final double regPositionMax = 255;

    private List<Servo>                              servos;
    private final double[]                           servoPositions;
    private final ModernRoboticsUsbServoController   target;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private EasyModernServoController(OpMode context, ModernRoboticsUsbServoController target) throws RobotCoreException, InterruptedException
        {
        super(context, target, newDummyReadWriteRunnable(target.getSerialNumber()));

        this.target  = target;
        this.servos  = new LinkedList<Servo>();
        this.servoPositions  = new double[ADDRESS_CHANNEL_MAP.length];
        this.findTargetNameAndMapping();
        }

    static ReadWriteRunnableHandy newDummyReadWriteRunnable(SerialNumber serialNumber)
        {
        RobotUsbDevice robotUsbDevice = new DummyRobotUsbDevice();
        return new ReadWriteRunnableHandy(serialNumber, robotUsbDevice, MONITOR_LENGTH, START_ADDRESS, false);
        }

    public static ServoController create(OpMode context, ServoController target, Collection<Servo> servos)
        {
        try {
            if (MemberUtil.isModernServoController(target))
                {
                EasyModernServoController controller = new EasyModernServoController(context, (ModernRoboticsUsbServoController) target);
                controller.setServos(servos);
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
    // Arming and disarming
    //----------------------------------------------------------------------------------------------

    private void setServos(Collection<Servo> servos)
        {
        assertTrue(!BuildConfig.DEBUG || !this.isArmed());

        for (Servo servo : servos)
            {
            if (servo.getController() != this.target)
                throw new IllegalArgumentException(String.format("servo has incorrect controller for usurpation: %s", servo.getConnectionInfo()));
            }

        this.servos = new LinkedList<Servo>(servos);
        }

    private void usurpDevices()
        {
        for (Servo servo : this.servos)
            {
            MemberUtil.setControllerOfServo(servo, this);
            }
        }

    private void deusurpDevices()
        {
        for (Servo servo : this.servos)
            {
            MemberUtil.setControllerOfServo(servo, this.target);
            }
        }

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
                }
            this.isArmed = true;

            // Turn on our usb stuff
            this.installReadWriteRunnable(this, MONITOR_LENGTH, START_ADDRESS);

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
        floatHardware();
        closeModernRoboticsUsbDevice(this);
        }

    @Override public String getConnectionInfo()
        {
        return "Swerve: USB " + this.getSerialNumber();
        }

    @Override public String getDeviceName()
        {
        return "Swerve: Modern Robotics USB Servo Controller";
        }

    //----------------------------------------------------------------------------------------------
    // ServoController
    //----------------------------------------------------------------------------------------------

    @Override
    public void pwmEnable()
        {
        this.write(ADDRESS_PWM, PWM_ENABLE);
        }

    @Override
    public void pwmDisable()
        {
        this.write(ADDRESS_PWM, PWM_DISABLE);
        }

    @Override
    public PwmStatus getPwmStatus()
        {
        return this.read(ADDRESS_PWM,1)[0] == PWM_DISABLE
                ? PwmStatus.DISABLED
                : PwmStatus.ENABLED;
        }

    @Override
    public void setServoPosition(int servo, double position)
        {
        validateServo(servo);
        position = Range.clip(position, positionMin, positionMax);  // note: runtime formerly threw on range error
        double bPosition = Range.scale(position, positionMin, positionMax, regPositionMin, regPositionMax);
        this.write(ADDRESS_CHANNEL_MAP[servo], bPosition);
        this.pwmEnable();

        // We remember the servo target positions so that getServoPosition can return something reasonable
        this.servoPositions[servo] = position;
        }

    @Override
    public double getServoPosition(int servo)
    // One would think we could just read the servo position registers. But they always report as zero
        {
        validateServo(servo);
        return this.servoPositions[servo];
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private void validateServo(int servo)
        {
        if (servo < SERVO_FIRST || servo > SERVO_LAST)
            {
            throw new IllegalArgumentException(String.format("servo %d is invalid; valid servos are %d..%d", servo, SERVO_FIRST, SERVO_LAST));
            }
        }

    void floatHardware(ServoController controller)
        {
        controller.pwmDisable();
        }
    void floatHardware()
        {
        floatHardware(this);
        }
    @Override public void stopHardware()
        {
        floatHardware();
        }
    }
