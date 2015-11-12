package org.swerverobotics.library.internal;

import android.content.Context;
import android.util.Log;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robot.RobotState;

import org.swerverobotics.library.SynchronousOpMode;

import java.lang.reflect.*;
import java.util.*;

/**
 * The point of all this is to hook into the robot controller runtime in order to
 * get access to their motor controller shutdown and stop logic. The runtime expects *all*
 * the motors and motor controllers to live in the hardware map that it knows about,
 * and it runs over the collections of those at times in order to shutdown motors.
 * Ours motor controllers etc don't always do that, so we need to compensate.
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
 * most efficient way to accomplish the test.
 */
public class RobotStateTransitionNotifier extends DcMotor implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    static final String       shutdownHookName    = " |Swerve|ShutdownHook| ";
    static       Context      applicationContext  = null;
    static       List<Method> onRobotRunningMethods = new LinkedList<Method>();
    static       List<Method> onRobotStartupFailureMethods = new LinkedList<Method>();
    static       List<Runnable> onRobotUpdateActions = new LinkedList<Runnable>();

    public  final HardwareMap                           hardwareMap;
    private final List<IOpModeStateTransitionEvents>    registrants;
                  boolean                               shutdownProcessed;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    RobotStateTransitionNotifier(HardwareMap hardwareMap)
        {
        super(null, 0);
        this.controller        = this;
        this.hardwareMap       = hardwareMap;
        this.registrants       = new LinkedList<IOpModeStateTransitionEvents>();
        this.shutdownProcessed = false;
        }

    public static void register(OpMode context, IOpModeStateTransitionEvents registrant)
        {
        if (context != null)
            {
            create(context.hardwareMap).register(registrant);
            }
        }

    private static RobotStateTransitionNotifier create(HardwareMap map)
        {
        // Only need to create one instance per hardwareMap.
        if (!ThunkingHardwareFactory.contains(map.dcMotorController, shutdownHookName))
            {
            RobotStateTransitionNotifier hook = new RobotStateTransitionNotifier(map);
            map.dcMotorController.put(shutdownHookName, hook);
            map.dcMotor.put(shutdownHookName, hook);
            }
        return (RobotStateTransitionNotifier)map.dcMotorController.get(shutdownHookName);
        }

    //----------------------------------------------------------------------------------------------
    // Registration
    //----------------------------------------------------------------------------------------------

    synchronized void register(IOpModeStateTransitionEvents him)
        {
        this.registrants.add(him);
        }
    synchronized boolean unregister(IOpModeStateTransitionEvents him)
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

    public synchronized static void setStateTransitionCallbacks(Context applicationContext, Collection<Method> onRunningMethods, Collection<Method> onStartupFailureMethods)
        {
        RobotStateTransitionNotifier.applicationContext  = applicationContext;
        RobotStateTransitionNotifier.onRobotRunningMethods = new LinkedList<Method>(onRunningMethods);
        RobotStateTransitionNotifier.onRobotStartupFailureMethods = new LinkedList<Method>(onStartupFailureMethods);
        }

    public synchronized static void registerRobotUpdateAction(Runnable runnable)
        {
        onRobotUpdateActions.add(runnable);
        }

    public synchronized static void unregisterRobotUpdateAction(Runnable runnable)
        {
        onRobotUpdateActions.remove(runnable);
        }

    //----------------------------------------------------------------------------------------------
    // Notifications
    //----------------------------------------------------------------------------------------------

    synchronized void onUserOpModeStop()
        {
        List<IOpModeStateTransitionEvents> toRemove = new LinkedList<IOpModeStateTransitionEvents>();
        for (IOpModeStateTransitionEvents registrant : this.registrants)
            {
            if (registrant.onUserOpModeStop())
                toRemove.add(registrant);
            }

        for (IOpModeStateTransitionEvents registrant : toRemove)
            unregister(registrant);
        }

    synchronized void onRobotShutdown()
        {
        Log.d(SynchronousOpMode.LOGGING_TAG, "state xtion: robot shutdown");
        if (!this.shutdownProcessed)
            {
            this.shutdownProcessed = true;

            List<IOpModeStateTransitionEvents> toRemove = new LinkedList<IOpModeStateTransitionEvents>();
            for (IOpModeStateTransitionEvents registrant : this.registrants)
                {
                if (registrant.onRobotShutdown())
                    toRemove.add(registrant);
                }

            for (IOpModeStateTransitionEvents registrant : toRemove)
                unregister(registrant);
            }
        }

    public static synchronized void onRobotUpdate(final String status)
        {
        List<Runnable> copiedList = new LinkedList<Runnable>(onRobotUpdateActions);
        for (Runnable runnable : copiedList)
            {
            runnable.run();
            }
        }

    /**
     * Called by the FtcRobotControllerActivity hooking infrastructure when the event loop
     * changes state.
     *
     * @param newState the new state into which the event loop is transitioning.
     */
    public static synchronized void onRobotStateChange(RobotState newState)
        {
        Log.d(SynchronousOpMode.LOGGING_TAG, String.format("state xtion: state=%s", newState.toString()));
        switch (newState)
            {
            case RUNNING:
                for (Method method : onRobotRunningMethods)
                    {
                    try {
                        method.invoke(null, applicationContext);
                        }
                    catch (Exception e)
                        { /* ignored */ }
                    }
                break;

            case EMERGENCY_STOP:
                for (Method method : onRobotStartupFailureMethods)
                    {
                    try {
                        method.invoke(null, applicationContext);
                        }
                    catch (Exception e)
                        { /* ignored */ }
                    }
                break;
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
