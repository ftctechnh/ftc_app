package org.swerverobotics.library;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * SynchronousOpMode is a base class that can be derived from in order to
 * write op modes that can be coded in a linear, synchronous programming style.
 *
 * Extend this class and implement the main() method to add your own code.
 */
public abstract class SynchronousOpMode extends OpMode implements Runnable
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    Thread loopThread;
    Thread mainThread;
    private static final ThreadLocal<SynchronousOpMode> synchronousOpContext
            = new ThreadLocal<SynchronousOpMode>()
                {
                @Override protected SynchronousOpMode initialValue() { return null; }
                };

    ConcurrentLinkedQueue queue;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousOpMode()
        {
        this.queue = new ConcurrentLinkedQueue();
        }

    /**
     * If we are running on a main() thread, then return the SynchronousOpMode
     * which is managing that thread.
     */
    public static SynchronousOpMode getThreadThunker()
        {
        return synchronousOpContext.get();
        }

    //----------------------------------------------------------------------------------------------
    // User code
    //----------------------------------------------------------------------------------------------

    /**
     * Implement main() (in a derived class) to contain your robot logic.
     *
     * Note that ideally your code will be interruption-aware, but that is not
     * strictly necessary.
     *
     * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#interrupt()">Thread.interrupt()</a>
     * @see <a href="http://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html">Interrupts</a>
     */
    public abstract void main() throws InterruptedException;

    //----------------------------------------------------------------------------------------------
    // Startup and shutdown
    //----------------------------------------------------------------------------------------------

    @Override
    public void start()
        {
        this.loopThread = Thread.currentThread();
        //
        this.mainThread = new Thread(this);     // REVIEW: would this be better with an inner class?
        this.mainThread.start();
        }

    /**
     * Shut down this op mode.
     *
     * It will in particular ALWAYS be the case that by the  time the stop() method returns that
     * the thread on which MainThread() is executed will have been terminated.
     */
    @Override
    public final void stop()
        {
        // Notify the MainThread() method that we wish it to stop what it's doing, clean
        // up, and return.
        this.mainThread.interrupt();
        //
        try {
            // Wait, briefly, to give the thread a chance to handle the interruption and complete
            this.mainThread.wait(100);
            }
        catch (InterruptedException e) { }
        finally
            {
            // Under all circumstances, make sure the thread shuts down, even if the
            // programmer hasn't handled interruption (for example, he might have entered
            // an infinite loop, or a very long one at least).
            this.mainThread.stop();
            }
        try {
            // Wait until the thread terminates
            this.mainThread.join();
            }
        catch (InterruptedException e) { }
        }

    /**
     * Our run method here calls the synchronous main() method.
     */
    public final void run()
        {
        // Note that this op mode is the thing on this thread that can hop one back to the loop thread
        synchronousOpContext.set(this);

        try
            {
            this.main();
            }
        catch (InterruptedException e)
            {
            return;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    public boolean isLoopThread()
        {
        return this.loopThread.getId() == Thread.currentThread().getId();
        }
    public boolean isMainThread()
        {
        return this.mainThread.getId() == Thread.currentThread().getId();
        }

    //----------------------------------------------------------------------------------------------
    // Thunking
    //----------------------------------------------------------------------------------------------

    @Override
    public void loop()
        {
        // Run any actions we've been asked to execute
        for (;;)
            {
            Object o = this.queue.poll();
            if (null == o)
                break;

            IAction action = (IAction)(o);
            if (null != action)
                action.doAction();
            }
        }

    /**
     * Execute the indicated action on the loop thread.
     */
    public void executeOnLoopThread(IAction action)
        {
        assert this.isMainThread();
        this.queue.add(action);
        }

    //----------------------------------------------------------------------------------------------
    // Thunking method helpers
    //----------------------------------------------------------------------------------------------

    public interface IAction
        {
        void doAction();
        }

    public abstract static class WaitableAction implements IAction
        {
        Semaphore semaphore = new Semaphore(0);

        public void doAction()
            {
            this.actionOnLoopThread();
            this.semaphore.release();
            }

        protected abstract void actionOnLoopThread();

        void dispatch()
            {
            SynchronousOpMode.getThreadThunker().executeOnLoopThread(this);
            try
                {
                this.semaphore.acquire();
                }
            catch (InterruptedException e)
                {
                Thread.currentThread().interrupt();
                }
            }
        }

    public abstract static class ResultableAction<T> extends WaitableAction
        {
        T result;
        }

    //----------------------------------------------------------------------------------------------
    // Thunking hardware map
    //----------------------------------------------------------------------------------------------

    /**
     * Given a non-thunking hardware map, create a new hardware map containing
     * all the same devices but in a form that their methods thunk from the main()
     * thread to the loop() thread.
     */
    public static HardwareMap CreateThunkedHardwareMap(HardwareMap hwmap)
        {
        HardwareMap result = new HardwareMap();

        CreateThunks(hwmap.dcMotorController, result.dcMotorController,
            new IThunkFactory<DcMotorController>()
                {
                @Override public DcMotorController Create(DcMotorController target)
                    {
                    return ThunkingMotorController.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.servoController, result.servoController,
            new IThunkFactory<ServoController>()
                {
                @Override public ServoController Create(ServoController target)
                    {
                    return ThunkingServoController.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.legacyModule, result.legacyModule,
            new IThunkFactory<LegacyModule>()
                {
                @Override public LegacyModule Create(LegacyModule target)
                    {
                    return ThunkingLegacyModule.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.dcMotor, result.dcMotor,
            new IThunkFactory<DcMotor>()
                {
                @Override public DcMotor Create(DcMotor target)
                    {
                    return new DcMotor(
                            ThunkingMotorController.Create(target.getController()),
                            target.getPortNumber(),
                            target.getDirection()
                            );
                    }
                }
            );

        CreateThunks(hwmap.servo, result.servo,
            new IThunkFactory<com.qualcomm.robotcore.hardware.Servo>()
                {
                @Override public com.qualcomm.robotcore.hardware.Servo Create(com.qualcomm.robotcore.hardware.Servo target)
                    {
                    return new com.qualcomm.robotcore.hardware.Servo(
                            ThunkingServoController.Create(target.getController()),
                            target.getPortNumber(),
                            target.getDirection()
                            );
                    }
                }
            );

        /*
        CreateThunks(hwmap.accelerationSensor, result.accelerationSensor);
        CreateThunks(hwmap.compassSensor, result.compassSensor);
        CreateThunks(hwmap.gyroSensor, result.gyroSensor);
        CreateThunks(hwmap.irSeekerSensor, result.irSeekerSensor);
        CreateThunks(hwmap.lightSensor, result.lightSensor);
        CreateThunks(hwmap.ultrasonicSensor, result.ultrasonicSensor);
        CreateThunks(hwmap.voltageSensor, result.voltageSensor);
        */

        result.appContext = hwmap.appContext;

        return result;
        }

    private interface IThunkFactory<T>
        {
        T Create(T t);
        }

    private static <T> void CreateThunks(HardwareMap.DeviceMapping<T> from, HardwareMap.DeviceMapping<T> to, IThunkFactory<T> thunkFactory)
        {
        for (Map.Entry<String,T> pair : from.entrySet())
            {
            T thunked = thunkFactory.Create(pair.getValue());
            to.put(pair.getKey(), thunked);
            }
        }
    }









































