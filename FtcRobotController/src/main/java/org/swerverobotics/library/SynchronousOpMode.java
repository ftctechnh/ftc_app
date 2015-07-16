package org.swerverobotics.library;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

// Work items:
//      * telemetry: dashboard + log; throttling
//      * maybe 'getPower on legacy NXT-compatible motor controller returns a null value' (eh?)
//      * a big once-over for (default)/public/private/protected and/or final on methods and classes
//      * a once-over thinking about concurrent multiple synchronous threads (main() + workers)
//      * wait() instead of semaphore in thunking logic
//      * allowing setters to return once thunk initiated but not necessarily completed
//          * beware of loop() being overwhelmed
//          * this.waitForThunkCompletions() : waits for THIS THREAD's completions

/**
 * SynchronousOpMode is a base class that can be derived from in order to
 * write op modes that can be coded in a linear, synchronous programming style.
 *
 * Extend this class and implement the main() method to add your own code.
 */
public abstract class SynchronousOpMode extends OpMode implements IThunker
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private Thread loopThread;
    private Thread mainThread;
    private ConcurrentLinkedQueue loopThreadActionQueue = new ConcurrentLinkedQueue();
    private AtomicBoolean gamePadStateChanged = new AtomicBoolean(false);

    /**
     * Advanced: unthunkedHardwareMap contains the original hardware map provided
     * in OpMode before it was replaced with a version that does thunking.
     */
    public HardwareMap unthunkedHardwareMap = null;

    /**
     * The game pad variables are redeclared here so as to hide those in our OpMode superclass
     * as the latter may be updated by the loop() thread at arbitrary times and in a manner which
     * is not synchronized with processing on the main() thread. We take pains to ensure that
     * the variables declared here do not suffer from that problem.
     */
    public Gamepad gamepad1 = null;
    public Gamepad gamepad2 = null;

    /**
     * Advanced: 'nanotimeLoopDwellMax' is the (soft) maximum number of nanoseconds that
     * our loop() implementation will spend in any one call before returning.
     */
    public long nanotimeLoopDwellMax = 3000000;     // == 3ms

    /**
     * Advanced: loopDwellCheckInterval is the number of thunks we will execute in loop()
     * before checking whether we've exceeded nanotimeLoopDwellMax
     */
    public int loopDwellCheckInterval = 10;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousOpMode()
        {
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
    protected abstract void main() throws InterruptedException;

    //----------------------------------------------------------------------------------------------
    // Startup and shutdown
    //----------------------------------------------------------------------------------------------

    @Override
    public final void start()
        {
        // Replace the op mode's hardware map variable with one whose contained
        // object implementations will thunk over to the loop thread as they need to.
        this.unthunkedHardwareMap = this.hardwareMap;
        this.hardwareMap = CreateThunkedHardwareMap(this.unthunkedHardwareMap);

        // Remember who the loop thread is so that we know whom to communicate
        // with from the main() thread.
        this.loopThread = Thread.currentThread();

        // Create the main thread and start it up and going!
        // REVIEW: should we defer the start() until the first loop() call?
        this.mainThread = new Thread(new Runner());
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
        //
        try {
            // Wait until the thread terminates
            this.mainThread.join();
            }
        catch (InterruptedException e) { }
        }

    private class Runner implements Runnable
        {
        /**
         * Our run method here calls the synchronous main() method.
         */
        public final void run()
            {
            // Note that this op mode is the thing on this thread that can thunk back to the loop thread
            SynchronousOpMode.this.setThreadThunker();

            try
                {
                SynchronousOpMode.this.main();
                }
            catch (InterruptedException e)
                {
                return;
                }
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    public final boolean isLoopThread()
        {
        return this.loopThread.getId() == Thread.currentThread().getId();
        }
    public final boolean isMainThread()
        {
        return this.mainThread.getId() == Thread.currentThread().getId();
        }

    /**
     * Answer as to whether there's (probably) any state different in any of the game pads
     * since the last time that this method was called
     */
    public final boolean newGamePadInputAvailable()
        {
        // We *wish* there was a way that we could hook or get a callback from the
        // incoming gamepad change messages, but, alas, at present we can find no
        // way of doing that.
        //
        return this.gamePadStateChanged.getAndSet(false);
        }


    //----------------------------------------------------------------------------------------------
    // Thunking
    //----------------------------------------------------------------------------------------------

    /**
     * Note that the receiver is the party which should handle thunking requests for the
     * current thread.
     *
     * Advanced: this is called automatically for the main() thread. If you choose in your
     * code to spawn additional worker synchronous threads, each of those threads should call
     * this method near the thread's beginning in order that access to the (thunked) hardware
     * objects will function correctly from that thread.
     */
    public void setThreadThunker()
        {
        ThreadThunkContext.setThreadThunker(this);
        }

    /**
     * If we are running on a synchronous thread, then return the SynchronousOpMode
     * which is managing the thunking from the current thread to the loop() thread.
     */
    public static IThunker getThreadThunker()
        {
        return ThreadThunkContext.getThreadContext().getThunker();
        }

    /**
     * Wait until all any thunks that are currently in flight and have been dispatched
     * from the current thread have completed
     */
    public void waitForThreadThunkCompletions() throws InterruptedException
        {
        ThreadThunkContext.getThreadContext().waitForThreadThunkCompletions();
        }

    /**
     * Our implementation of the loop() callback
     */
    public final void loop()
        {
        // Capture the gamepad states safely so that in the main() thread we
        // don't see torn writes
        boolean diff1 = this.gamepad1.update(super.gamepad1);
        boolean diff2 = this.gamepad2.update(super.gamepad2);
        this.gamePadStateChanged.compareAndSet(false, diff1 || diff2);

        // Call the subclass hook in case they might want to do something interesting
        this.preLoop();

        long nanotimeStart = System.nanoTime();

        // Execute any thunks we've been asked to execute here on the loop thread
        for (int i = 0; ; i++)
            {
            // Get the next work item in the queue. Get out of here if there isn't
            // any more work.
            Object o = this.loopThreadActionQueue.poll();
            if (null == o)
                break;

            // That should be a thunk. If it is, then execute it; if not, who cares.
            IThunk thunk = (IThunk)(o);
            if (null != thunk)
                thunk.doThunk();

            // Periodically check whether we've run long enough for this
            // loop() call.
            if ((i+1) % this.loopDwellCheckInterval == 0)
                {
                long nanoTimeNow = System.nanoTime();
                if (nanoTimeNow - nanotimeStart > this.nanotimeLoopDwellMax)
                    break;
                }
            }

        // Call the subclass hook in case they might want to do something interesting
        this.postLoop();
        }

    /**
     * preLoop() and postLoop() are hooks that advanced users might want to override in their
     * subclasses. The implementations of those function here do nothing. preLoop() is called
     * early on the loop() thread, just after variable state has been stabilized (e.g. gamepads),
     * and postLoop() is called just before the loop() method returns.
     */
    protected void preLoop() { /* hook for subclasses */ }

    /**
     * @see #preLoop
     */
    protected void postLoop() { /* hook for subclasses */ }

    //----------------------------------------------------------------------------------------------
    // IThunker
    //----------------------------------------------------------------------------------------------

    /**
     * Execute the indicated action on the loop thread.
     */
    public void executeOnLoopThread(IThunk thunk)
        {
        this.loopThreadActionQueue.add(thunk);
        }

    //----------------------------------------------------------------------------------------------
    // Thunking hardware map
    //----------------------------------------------------------------------------------------------

    /**
     * Given a (non-thunking) hardware map, create a new hardware map containing
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

        CreateThunks(hwmap.accelerationSensor, result.accelerationSensor,
            new IThunkFactory<AccelerationSensor>()
                {
                @Override public AccelerationSensor Create(AccelerationSensor target)
                    {
                    return ThunkedAccelerationSensor.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.compassSensor, result.compassSensor,
            new IThunkFactory<CompassSensor>()
                {
                @Override public CompassSensor Create(CompassSensor target)
                    {
                    return ThunkedCompassSensor.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.gyroSensor, result.gyroSensor,
            new IThunkFactory<GyroSensor>()
                {
                @Override public GyroSensor Create(GyroSensor target)
                    {
                    return ThunkedGyroSensor.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.irSeekerSensor, result.irSeekerSensor,
            new IThunkFactory<IrSeekerSensor>()
                {
                @Override public IrSeekerSensor Create(IrSeekerSensor target)
                    {
                    return ThunkedIrSeekerSensor.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.lightSensor, result.lightSensor,
            new IThunkFactory<LightSensor>()
                {
                @Override public LightSensor Create(LightSensor target)
                    {
                    return ThunkedLightSensor.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.ultrasonicSensor, result.ultrasonicSensor,
            new IThunkFactory<UltrasonicSensor>()
                {
                @Override public UltrasonicSensor Create(UltrasonicSensor target)
                    {
                    return ThunkedUltrasonicSensor.Create(target);
                    }
                }
            );

        CreateThunks(hwmap.voltageSensor, result.voltageSensor,
            new IThunkFactory<VoltageSensor>()
                {
                @Override public VoltageSensor Create(VoltageSensor target)
                    {
                    return ThunkedVoltageSensor.Create(target);
                    }
                }
            );

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









































