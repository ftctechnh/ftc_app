package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

import java.util.LinkedList;
import java.util.List;

/**
 * The point of all this is to hook into the robot controller runtime in order to
 * get access to their motor controller shutdown and stop logic. They expect *all*
 * the motors and motor controllers to live in the hardware map that they know about,
 * and they run over the collections of those at times in order to shutdown motors.
 * Ours don't always do that, so we need to compensate.
 *
 * Looked at more generally, what we're doing here is providing a general purpose
 * notification mechanism that any component can use to register for end-of-user-OpMode
 * callbacks.
 *
 * The runtime runs down it's hardware map at two distinct times:
 *
 *      1) when the OpModeManager.'StopRobot' OpMode starts up, which is the OpMode which is run
 *         whenever there's not a user's OpMode running, it
 *          a) runs through all the servo controllers and disables their PWM
 *          b) runs through all the motor controllers and sets them to write mode
 *          c) runs through all the motors and sets them to zero power and run w/o encoders
 *          d) runs through all the light sensors and turns of the LED
 *
 *      2) FtcEventLoopHandler.shutdownMotorControllers() runs through all the motor
 *         controllers and calls 'close()'.
 *          a) This is called from FtcEventLoop.teardown(), which also close()s the
 *             servo controllers, legacy modules, and CDIMs.
 *          b) This is called from EventLoopManager.stopEventLoopRunnable()
 *          c) which is called from EventLoopManager.shutdown() (also from setEventLoop())
 *          d) which is called from Robot.shutdown()
 *          e) which is called from FtcRobotControllerService.shutdownRobot()
 *         etc. It's part of robot restart / app closing sequence.
 *
 * Our immediate goal here is to build a notification mechanism for both these situations.
 * So we build this weird beast that is both a motor and its own controller as that's the
 * most efficient way to accomplish the teast.
 */
class OpModeShutdownNotifier extends DcMotor implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    static final String shutdownHookName = " |Swerve|ShutdownHook| ";

    public  final HardwareMap                   hardwareMap;
    private final List<IOpModeShutdownNotify>   registrants;
                  boolean                       shutdownProcessed;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    OpModeShutdownNotifier(HardwareMap hardwareMap)
        {
        super(null, 0);
        this.controller        = this;
        this.hardwareMap       = hardwareMap;
        this.registrants       = new LinkedList<IOpModeShutdownNotify>();
        this.shutdownProcessed = false;
        }

    public static void register(OpMode context, IOpModeShutdownNotify registrant)
        {
        if (context != null)
            {
            create(context.hardwareMap).register(registrant);
            }
        }

    private static OpModeShutdownNotifier create(HardwareMap map)
        {
        if (!ThunkingHardwareFactory.contains(map.dcMotorController, shutdownHookName))
            {
            OpModeShutdownNotifier hook = new OpModeShutdownNotifier(map);
            map.dcMotorController.put(shutdownHookName, hook);
            map.dcMotor.put(shutdownHookName, hook);
            }
        return (OpModeShutdownNotifier)map.dcMotorController.get(shutdownHookName);
        }

    //----------------------------------------------------------------------------------------------
    // State transitions
    //----------------------------------------------------------------------------------------------

    synchronized void register(IOpModeShutdownNotify him)
        {
        this.registrants.add(him);
        }
    synchronized boolean unregister(IOpModeShutdownNotify him)
        {
        for (int i = 0; i < this.registrants.size(); i++)
            {
            if (this.registrants.get(i) == him)
                {
                this.registrants.remove(i);
                return true;
                }
            }
        return false;
        }

    synchronized void onUserOpModeStop()
        {
        List<IOpModeShutdownNotify> toRemove = new LinkedList<IOpModeShutdownNotify>();
        for (IOpModeShutdownNotify registrant : this.registrants)
            {
            if (registrant.onUserOpModeStop())
                toRemove.add(registrant);
            }

        for (IOpModeShutdownNotify registrant : toRemove)
            unregister(registrant);
        }

    synchronized void onRobotShutdown()
        {
        if (!this.shutdownProcessed)
            {
            this.shutdownProcessed = true;

            List<IOpModeShutdownNotify> toRemove = new LinkedList<IOpModeShutdownNotify>();
            for (IOpModeShutdownNotify registrant : this.registrants)
                {
                if (registrant.onRobotShutdown())
                    toRemove.add(registrant);
                }

            for (IOpModeShutdownNotify registrant : toRemove)
                unregister(registrant);
            }
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void close()
        {
        this.onRobotShutdown();
        }

    @Override public synchronized int getVersion()
        {
        return 1;
        }

    @Override public synchronized String getConnectionInfo()
        {
        return "unconnected";
        }

    @Override public synchronized String getDeviceName()
        {
        return "Swerve Shutdown Hook Monitor";
        }


    //----------------------------------------------------------------------------------------------
    // DCMotorController
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void setMotorPower(final int channel, final double power)
        {
        this.onUserOpModeStop();
        }

    @Override public synchronized void setMotorControllerDeviceMode(final DeviceMode mode)
        {
        }

    @Override public synchronized DeviceMode getMotorControllerDeviceMode()
        {
        return DeviceMode.READ_WRITE;
        }

    @Override public synchronized void setMotorChannelMode(final int channel, final DcMotorController.RunMode mode)
        {
        }

    @Override public synchronized DcMotorController.RunMode getMotorChannelMode(final int channel)
        {
        return RunMode.RUN_WITHOUT_ENCODERS;
        }

    @Override public synchronized double getMotorPower(final int channel)
        {
        return 0;
        }

    @Override public synchronized boolean isBusy(final int channel)
        {
        return false;
        }

    @Override public synchronized void setMotorPowerFloat(final int channel)
        {
        }

    @Override public synchronized boolean getMotorPowerFloat(final int channel)
        {
        return false;
        }

    @Override public synchronized void setMotorTargetPosition(final int channel, final int position)
        {
        }

    @Override public synchronized int getMotorTargetPosition(final int channel)
        {
        return 0;
        }

    @Override public synchronized int getMotorCurrentPosition(final int channel)
        {
        return 0;
        }
    }
