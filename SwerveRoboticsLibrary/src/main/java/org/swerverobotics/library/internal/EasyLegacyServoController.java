package org.swerverobotics.library.internal;

import android.util.Log;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.Range;
import org.swerverobotics.library.BuildConfig;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;
import java.util.*;

import static junit.framework.Assert.*;
import static org.swerverobotics.library.internal.EasyModernServoController.*;

/**
 * An alternative implementation of a Legacy Servo controller.
 */
public class EasyLegacyServoController extends I2cControllerPortDeviceImpl implements ServoController, IOpModeStateTransitionEvents, Engagable
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public final String LOGGING_TAG = SynchronousOpMode.LOGGING_TAG;

    /*
    Address         Type        Contents
    00 – 07H        chars       Sensor version number
    08 – 0FH        chars       Manufacturer
    10 – 17H        chars       Sensor type
    18 – 3DH        bytes       Not used
    3E, 3FH         chars       Reserved
    40H             byte        Status
    41H             byte        Step time
    42H             byte        Servo 1 position
    43H             byte        Servo 2 position
    44H             byte        Servo 3 position
    45H             byte        Servo 4 position
    46H             byte        Servo 5 position
    47H             byte        Servo 6 position
    48H             byte        PWM enable
     */

    public static final byte[] ADDRESS_CHANNEL_MAP = new byte[]{(byte)-1, (byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47};
    public static final int ADDRESS_PWM = 0x48;
    public static final byte PWM_DISABLE = -1;
    public static final byte PWM_ENABLE = 0;

    private static final int iRegWindowFirst = 0x40;
    private static final int iRegWindowMax   = 0x48+1;  // first register not included

    private final II2cDeviceClient              i2cDeviceClient;
    private List<Servo>                         servos;
    private final double[]                      servoPositions;
    private final ServoController               target;
    private I2cDeviceReplacementHelper<ServoController> helper;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private EasyLegacyServoController(OpMode context, II2cDeviceClient ii2cDeviceClient, ServoController target, I2cController controller, int targetPort)
        {
        super(((I2cControllerPortDevice)target).getI2cController(), ((I2cControllerPortDevice)target).getPort());
        this.helper          = new I2cDeviceReplacementHelper<ServoController>(context, this, target, controller, targetPort);
        this.i2cDeviceClient = ii2cDeviceClient;
        this.target          = target;
        this.servos          = new LinkedList<Servo>();
        this.servoPositions  = new double[ADDRESS_CHANNEL_MAP.length];

        RobotStateTransitionNotifier.register(context, this);

        // The NXT HiTechnic servo controller will time out if it doesn't receive any I2C communication for
        // 10.0 seconds. So we set up a heartbeat request to try to prevent that. We try to use
        // heartbeats which are as minimally disruptive as possible.
        II2cDeviceClient.HeartbeatAction heartbeatAction = new II2cDeviceClient.HeartbeatAction();
        heartbeatAction.rereadLastRead      = true;
        heartbeatAction.rewriteLastWritten  = true;
        heartbeatAction.heartbeatReadWindow = new II2cDeviceClient.ReadWindow(ADDRESS_CHANNEL_MAP[1], 1, II2cDeviceClient.READ_MODE.ONLY_ONCE);

        this.i2cDeviceClient.setHeartbeatAction(heartbeatAction);
        this.i2cDeviceClient.setHeartbeatInterval(9000);

        // Also: set up a read-window. We make it BALANCED to avoid unnecessary ping-ponging
        // between read mode and write mode, since motors are read about as much as they are
        // written, but we make it relatively large so that least that when we DO go
        // into read mode and possibly do more than one read we will use this window
        // and won't have to fiddle with the 'switch to read mode' each and every time.
        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.ReadWindow(iRegWindowFirst, iRegWindowMax-iRegWindowFirst, II2cDeviceClient.READ_MODE.BALANCED));
        }

    public static ServoController create(OpMode context, ServoController target, Collection<Servo> servos)
        {
        if (MemberUtil.isLegacyServoController(target))
            {
            HiTechnicNxtServoController legacyTarget = (HiTechnicNxtServoController)target;
            I2cController module      = legacyTarget.getI2cController();
            int          port         = legacyTarget.getPort();
            int          i2cAddr8Bit  = MemberUtil.i2cAddrOfLegacyServoController(target);

            // Make a new legacy servo controller
            II2cDevice i2cDevice                 = new I2cDeviceOnI2cDeviceController(module, port);
            I2cDeviceClient i2cDeviceClient      = new I2cDeviceClient(context, i2cDevice, i2cAddr8Bit, false);
            EasyLegacyServoController controller = new EasyLegacyServoController(context, i2cDeviceClient, target, module, port);

            controller.setServos(servos);
            controller.engage();

            return controller;
            }
        else
            {
            // The target isn't a legacy servo controller, so we can't swap anything in for him.
            // Return the raw target (rather than, e.g., throwing) so that caller doesn't need to check
            // what kind of controller he has in hand.
            return target;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Arming and disarming
    //----------------------------------------------------------------------------------------------

    private void setServos(Collection<Servo> servos)
        {
        assertTrue(!BuildConfig.DEBUG || !this.isEngaged());

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

    synchronized public void engage()
    // Disarm the existing controller and arm us
        {
        if (!this.isEngaged())
            {
            this.usurpDevices();

            this.helper.engage();

            this.i2cDeviceClient.engage();
            this.floatHardware();
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
            this.i2cDeviceClient.disengage();

            this.helper.disengage();

            this.deusurpDevices();
            }
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return "Swerve EasyLegacyServoController";
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
            this.floatHardware(); // mirrors robot controller runtime behavior
            this.disengage();
            }
        }

    //----------------------------------------------------------------------------------------------
    // IOpModeStateTransitionEvents
    //----------------------------------------------------------------------------------------------

    @Override synchronized public boolean onUserOpModeStop()
        {
        Log.d(LOGGING_TAG, "Easy: auto-stopping...");
        if (this.isEngaged())
            {
            this.stopHardware();  // mirror StopRobotOpMode
            this.disengage();
            }
        Log.d(LOGGING_TAG, "Easy: ... done");
        return true;    // unregister us
        }

    @Override synchronized public boolean onRobotShutdown()
        {
        Log.d(LOGGING_TAG, "Easy: auto-closing...");

        // We actually shouldn't be here by now, having received a onUserOpModeStop()
        // after which we should have been unregistered. But we close down anyway.
        this.close();

        Log.d(LOGGING_TAG, "Easy: ... done");
        return true;    // unregister us
        }


    //----------------------------------------------------------------------------------------------
    // ServoController
    //----------------------------------------------------------------------------------------------

    @Override
    public synchronized void pwmEnable()
        {
        this.write(ADDRESS_PWM, PWM_ENABLE);
        }

    @Override
    public synchronized void pwmDisable()
        {
        this.write(ADDRESS_PWM, PWM_DISABLE);
        }

    @Override
    public synchronized PwmStatus getPwmStatus()
        {
        return this.read(ADDRESS_PWM,1)[0] == PWM_DISABLE
                ? PwmStatus.DISABLED
                : PwmStatus.ENABLED;
        }

    @Override
    public synchronized void setServoPosition(int servo, double position)
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
    public synchronized double getServoPosition(int servo)
        {
        validateServo(servo);
        return this.servoPositions[servo];
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    synchronized void write(int ireg, byte bData)
        {
        if (this.isEngaged())
            this.i2cDeviceClient.write8(ireg, bData);
        }

    public void write(int ireg, double bData)
        {
        this.write(ireg, (byte)(int)bData);
        }

    synchronized byte[] read(int ireg, int cb)
        {
        return this.i2cDeviceClient.read(ireg, cb);
        }

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
    void stopHardware()
        {
        floatHardware();
        }
    }
