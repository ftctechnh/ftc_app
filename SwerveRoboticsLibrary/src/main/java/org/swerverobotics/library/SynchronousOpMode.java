package org.swerverobotics.library;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import android.util.*;
import static junit.framework.Assert.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ThreadPool;

import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;

/**
 * SynchronousOpMode is a base class that can be inherited from in order to
 * write op modes that can be coded in a traditional programming style.
 *
 * Extend this class and implement the {@link #main()} method to add your own code.
 */
public abstract class SynchronousOpMode extends OpMode
    {
    //----------------------------------------------------------------------------------------------
    // Public State
    //----------------------------------------------------------------------------------------------

    /**
     * The logging tag we use in LogCat output in this general vicinity.
     */
    public static final String LOGGING_TAG = "Swerve";

    /**
     * Provides access to the first gamepad controller. Only changes as a result of calling
     * {@linkplain #updateGamepads()}.
     * 
     * The game pad variables are redeclared here so as to hide those in our OpMode superclass
     * as the latter may be updated by robot controller runtime at arbitrary times in a manner
     * which is not synchronized with processing on a synchronous thread.
     *
     * @see #gamepad2
     */
    public final Gamepad gamepad1 = new Gamepad();
    /** 
     * Provides access to the second gamepad controller. Only changes as a result of calling
     * {@linkplain #updateGamepads()}.
     *
     * @see #gamepad1
     */
    public final Gamepad gamepad2 = new Gamepad();

    /**
     * provides access to an object by which telemetry information can be transmitted
     * from the robot controller to the driver station.
     * 
     * As with game pads, we hid the 'telemetry' variable of the super class and replace it
     * with one that can work on synchronous threads.
     */
    public TelemetryDashboardAndLog telemetry;

    /** Advanced: the number of nanoseconds in a millisecond.
     * @deprecated use ElapsedTime.MILLIS_IN_NANO instead. */
    @Deprecated
    public static final long NANO_TO_MILLI = ElapsedTime.MILLIS_IN_NANO;

    /**
     * Advanced: the number of times loop() has been called on the loop thread.
     * @return the number of times loop() has been called.
     */
    public int getLoopCount() { return this.loopCount.get(); }
    private final AtomicInteger loopCount = new AtomicInteger(0);

    /**
     * We define a *local* hardwareMap variable here to hide the one in our base
     * class as the one we want user code to see is the one with the processing in it.
     * The original map is still available with {@link #getUnprocessedHardwareMap()}.
     */
    public HardwareMap hardwareMap = null;

    /**
     * Advanced: unthunkedHardwareMap contains the original hardware map provided
     * in OpMode before it was replaced with a version that does thunking.
     * @deprecated This member variable is redundant; use {@link #getUnprocessedHardwareMap()} instead.
     */
    @Deprecated
    public HardwareMap unthunkedHardwareMap = null;

    /**
     * Returns the hardware map as originally created by the robot controller runtime, unoptimized
     * and unprocessed by the SynchronousOpMode.
     * @return the hardware map as originally created by the robot controller runtime
     */
    public HardwareMap getUnprocessedHardwareMap()
        {
        return super.hardwareMap;
        }

    /**
     * Advanced: use experimental approaches to processing hardware devices
     */
    protected boolean useExperimentalHardwareMap = false;

    //----------------------------------------------------------------------------------------------
    // Key threading-related methods
    //----------------------------------------------------------------------------------------------

    /**
     * Implement main() (in a subclass) to contain your robot logic. Your code will
     * execute on its own thread, which will be started and stopped automatically. A typical
     * skeleton of a main() method looks like the following:
     * <pre>
        // Initialize stuff (not shown)

        // Wait for the game to start
        this.waitForStart();

        while (this.opModeIsActive()) {
            if (this.updateGamePads()) {
                // Do something interesting
                }
            this.telemetry.update();
            this.idle();
            }
     * </pre>
     *
     * @throws InterruptedException thrown if the thread is interrupted
     * @see #waitForStart()
     * @see #opModeIsActive()
     * @see #updateGamepads()
     * @see TelemetryDashboardAndLog#update()
     * @see #idle()
     */
    protected abstract void main() throws InterruptedException;

    /**
     * In your {@link #main()} method, first perform any necessary data and hardware initialization,
     * then call waitForStart() to await the commencement of the game.
     * @throws InterruptedException thrown if the thread is interrupted
     */
    public final void waitForStart() throws InterruptedException
        {
        synchronized (this.loopLock)
            {
            while (!this.isStarted())  // avoid spurious wakeups
                {
                this.loopLock.wait();
                }
            }
        }

    /**
     * Answer as to whether this opMode is active and the robot should continue onwards. If the
     * opMode is not active, synchronous threads should terminate at their earliest convenience.
     *
     * @return whether the OpMode is currently active. If this returns false, you should
     *         break out of the loop in your {@link #main()} method and return to its caller.
     * @see #main()
     * @see #isStarted()
     * @see #isStopRequested()
     */
    public final boolean opModeIsActive()
        {
        return !this.isStopRequested() && this.isStarted();
        }

    /**
     * Has the opMode been started?
     *
     * @return whether this opMode has been started or not
     * @see #opModeIsActive()
     * @see #isStopRequested()
     */
    public final boolean isStarted()
        {
        return this.started;
        }

    /**
     * Has the the stopping of the opMode been requested?
     * 
     * @return whether stopping opMode has been requested or not
     * @see #opModeIsActive()
     * @see #isStarted()
     */
    public final boolean isStopRequested()
        {
        return this.stopRequested || Thread.currentThread().isInterrupted();
        }


    /**
     * Puts the current thread to sleep for a bit as it has nothing better to do.
     *
     * idle(), which must be called on a synchronous thread, never on the loop() thread, causes the
     * synchronous thread to go to sleep until it is likely that there's something useful to do.
     *
     * One should use this method when you have nothing better to do in your code, usually
     * at the very end of your while(opModeIsActive()) loop in TeleOp. Calling idle()
     * is entirely optional: it just helps make the system a little more responsive and a
     * little more efficient.
     *
     * {@link #idle()} is conceptually related to waitOneFullHardwareCycle(), but makes no
     * guarantees as to completing any particular number of hardware cycles, if any.
     *
     * @throws InterruptedException thrown if the thread is interrupted
     * @see #main()
     * @see #synchronousThreadIdle()
     * @see #waitOneFullHardwareCycle()
     */
    public final void idle() throws InterruptedException
        {
        // Abort the world if the OpMode has been asked to stop
        if (this.isStopRequested())
            throw new InterruptedException();

        // Otherwise, yield back our thread scheduling quantum and give other threads at
        // our priority level a chance to run
        Thread.yield();
        }

    /**
     * Waits until the rest of the event loop code that runs on the loop() thread but outside
     * of the actual body of the loop() method itself has had a chance to run at least once. In
     * practice, up to two such 'hardware cycles' are sometimes used. Provided only for compatibility
     * with LinearOpMode, as it is unnecessary here: {@link #idle()} is a better choice.
     * @throws InterruptedException thrown if the thread is interrupted
     * @see #idle()
     */
    @Deprecated
    public void waitOneFullHardwareCycle() throws InterruptedException
        {
        synchronized (this.loopLock)
            {
            this.loopLock.wait();
            }
        Thread.sleep(1);
        synchronized (this.loopLock)
            {
            this.loopLock.wait();
            }
        }

    /**
     * Idles the current thread until stimulated by the robot controller runtime.
     * The current thread must be a synchronous thread.
     * @throws InterruptedException thrown if the thread is interrupted
     * @see #idle()
     */
    public static void synchronousThreadIdle() throws InterruptedException
        {
        getThreadSynchronousOpMode().idle();
        }

    /**
     * Advanced: createSynchronousWorkerThread() is used to create secondary worker threads
     * from your main thread, should you wish to do so. 
     * <p>
     * Warning: multithreaded programming <em>is</em> rocket science!
     * <p>
     * Like the {@link #main()} thread, synchronous worker threads should frequently call
     * {@link #opModeIsActive()} and return from their loop body if the opMode has stopped.
     *
     * @param threadBody the code to execute on the newly created thread
     * @see #main()
     * @see #opModeIsActive()
     */
    public void createSynchronousWorkerThread(IInterruptableRunnable threadBody)
        {
        this.createSynchronousWorkerThread(threadBody, false);
        }

    /**
     * Advanced: wait until all thunks that have been dispatched from the current (synchronous)
     * thread have completed their execution over on the loop() thread and their effects
     * to have reached the hardware.
     *
     * In general, thunked methods that don't return any information to the caller
     * (that is, the majority of setXXX() calls) only *initiate* their work on the loop()
     * thread before returning to their caller; the work may or may not have been completed
     * by the time the setXXX() call returns. Calling waitForThreadsWritesToReachHardware()
     * allows one to wait later for these calls to have been dispatched. It waits further
     * until it is known that the effect of those calls has been propagated to the hardware.
     *
     * Note that waitForThreadsWritesToReachHardware() only deals with work that has been issued
     * by the current thread. Work dispatched from *other* (synchronous) threads may not yet have
     * completed when waitForThreadsWritesToReachHardware() returns.
     *
     * @throws InterruptedException thrown if the thread is interrupted
     * @deprecated Thunking support is being removed
     */
    @Deprecated
    public void waitForThreadsWritesToReachHardware() throws InterruptedException
        {
        // Nothing here any more
        }

    //----------------------------------------------------------------------------------------------
    // Gamepad management
    //----------------------------------------------------------------------------------------------

    /**
     * Captures any new state available from the game pads, and answers as to whether
     * anything is different from the previous state.
     *
     * Between calls to updateGamepads(), the visible gamepad state is guaranteed
     * not to change. This permits you to consistently reason about that state across a possibly
     * complicated chain of logic. Conversely, however, if you don't call this method, you
     * won't see any changes to the state of the gamepads.
     *
     * @return whether any state of the gamepads has (probably) changed
     */
    public final boolean updateGamepads()
        {
        // Called NOT from loop()
        synchronized (this.loopLock)
            {
            this.gamepadInputQueried = true;
            boolean result = this.gamePadCaptureStateChanged.getAndSet(false);
            if (result)
                {
                gamepadAssign(this.gamepad1, this.gamepad1Captured);
                gamepadAssign(this.gamepad2, this.gamepad2Captured);
                }
            return result;
            }
        }

    /** Capture the gamepad state so that it will be available for a later updateGamepads() */
    private void captureGamepadState()
        {
        // We conservatively indicate that things have changed
        boolean changed1 = true, changed2 = true;
        //
        if (this.gamepad1Captured == null)
            this.gamepad1Captured = new Gamepad();
        else if (super.gamepad1 != null)
            changed1 = !gamepadsSame(this.gamepad1Captured, super.gamepad1);
        //
        if (this.gamepad2Captured == null)
            this.gamepad2Captured = new Gamepad();
        else if (super.gamepad2 != null)
            changed2 = !gamepadsSame(this.gamepad2Captured, super.gamepad2);
        //
        if (super.gamepad1 != null) gamepadAssign(this.gamepad1Captured, super.gamepad1);
        if (super.gamepad2 != null) gamepadAssign(this.gamepad2Captured, super.gamepad2);
        //
        boolean changed = changed1 || changed2;
        //
        if (changed)
            {
            // Log.v(LOGGING_TAG, String.format("gamepad state: #%d", this.gamepadStateCount.getAndIncrement()));
            }
        //
        this.gamePadCaptureStateChanged.compareAndSet(false, changed);
        }

    boolean isNewGamepadStateAvailable()
        {
        return this.gamePadCaptureStateChanged.get();
        }

    /** Are the states of two gamepads equivalent? */
    private static boolean gamepadsSame(com.qualcomm.robotcore.hardware.Gamepad p1, com.qualcomm.robotcore.hardware.Gamepad p2)
        {
        if (p1.left_stick_x != p2.left_stick_x) return false;
        if (p1.left_stick_y != p2.left_stick_y) return false;
        if (p1.right_stick_x != p2.right_stick_x) return false;
        if (p1.right_stick_y != p2.right_stick_y) return false;
        if (p1.dpad_up != p2.dpad_up) return false;
        if (p1.dpad_down != p2.dpad_down) return false;
        if (p1.dpad_left != p2.dpad_left) return false;
        if (p1.dpad_right != p2.dpad_right) return false;
        if (p1.a != p2.a) return false;
        if (p1.b != p2.b) return false;
        if (p1.x != p2.x) return false;
        if (p1.y != p2.y) return false;
        if (p1.guide != p2.guide) return false;
        if (p1.start != p2.start) return false;
        if (p1.back != p2.back) return false;
        if (p1.left_bumper != p2.left_bumper) return false;
        if (p1.right_bumper != p2.right_bumper) return false;
        if (p1.left_trigger != p2.left_trigger) return false;
        if (p1.right_trigger != p2.right_trigger) return false;
        if (p1.user != p2.user) return false;
        if (p1.id != p2.id) return false;
        if (p1.timestamp != p2.timestamp) return false;
        //
        return true;
        }

    /** Copy the state of one gamepad into another */
    private static void gamepadAssign(com.qualcomm.robotcore.hardware.Gamepad dst, com.qualcomm.robotcore.hardware.Gamepad src)
        {
        dst.left_stick_x = src.left_stick_x;
        dst.left_stick_y = src.left_stick_y;
        dst.right_stick_x = src.right_stick_x;
        dst.right_stick_y = src.right_stick_y;
        dst.dpad_up = src.dpad_up;
        dst.dpad_down = src.dpad_down;
        dst.dpad_left = src.dpad_left;
        dst.dpad_right = src.dpad_right;
        dst.a = src.a;
        dst.b = src.b;
        dst.x = src.x;
        dst.y = src.y;
        dst.guide = src.guide;
        dst.start = src.start;
        dst.back = src.back;
        dst.left_bumper = src.left_bumper;
        dst.right_bumper = src.right_bumper;
        dst.left_trigger = src.left_trigger;
        dst.right_trigger = src.right_trigger;
        dst.user = src.user;
        dst.id = src.id;
        dst.timestamp = src.timestamp;
        }

    //----------------------------------------------------------------------------------------------
    // Private state and construction
    //----------------------------------------------------------------------------------------------

    private volatile boolean                started;
    private volatile boolean                stopRequested;
    private SynchronousOpModeHardwareFactory hardwareFactory = null;
    private         AtomicBoolean           gamePadCaptureStateChanged = new AtomicBoolean(false);
    private         boolean                 gamepadInputQueried = false;
    private final   Object                  loopLock = new Object();

    private         Thread                  loopThread;
    private final   ExecutorService         mainThreadExecutor    = ThreadPool.newSingleThreadExecutor();
    private final   ExecutorService         workerThreadsExecutor = ThreadPool.newCachedThreadPool();
    private         RuntimeException        exceptionThrownOnMainThread;
    private final   AtomicReference<RuntimeException> firstExceptionThrownOnASynchronousWorkerThread = new AtomicReference<RuntimeException>();

    private         Gamepad                 gamepad1Captured = null;
    private         Gamepad                 gamepad2Captured = null;

    // State only intended to support debugging and logging
    private         AtomicInteger           gamepadStateCount = new AtomicInteger(0);

    public SynchronousOpMode()
        {
        }

    //----------------------------------------------------------------------------------------------
    // Management of synchronous threads
    //----------------------------------------------------------------------------------------------

    /**
     * An instance of SynchronousThreadRoot is called on the loop() thread in order to start up 
     * the main() thread. Other instances are used to support synchronous worker threads.
     */
    private class SynchronousThreadRoot implements java.lang.Runnable
        {
        //--------------------------------------------------------------
        // State
        final IInterruptableRunnable threadBody;
        final boolean                isMain;

        //--------------------------------------------------------------
        // Construction
        SynchronousThreadRoot(IInterruptableRunnable threadBody, boolean isMain)
            {
            this.threadBody = threadBody;
            this.isMain     = isMain;
            }

        //--------------------------------------------------------------
        // Running
        public final void run()
            {
            // Remember the thing that can thunk from this thread back to the loop() thread.
            SynchronousOpMode.this.setThreadThunker();
            try
                {
                this.threadBody.run();
                if (this.isMain)
                    requestOpModeStop();
                }
            catch (InterruptedException|CancellationException ignored)
                {
                // If the main() method itself doesn't catch the interrupt, at least
                // we will do so here. The whole point of such interrupts is to
                // get the thread to shut down, which we are about to do here by
                // falling off the end of run().
                }
            catch (RuntimeException e)
                {
                // Remember exceptions so we can throw them later back over in loop()
                if (this.isMain)
                    {
                    SynchronousOpMode.this.exceptionThrownOnMainThread = e;
                    }
                else
                    {
                    // Only remember the first one for a worker
                    SynchronousOpMode.this.firstExceptionThrownOnASynchronousWorkerThread.compareAndSet(null, e); 
                    }
                }
            // 'Thread falls off the end here and terminates.
            }
        }

    private void createSynchronousWorkerThread(final IInterruptableRunnable threadBody, final boolean isMain)
        {
        if (this.isStopRequested())
            throw new IllegalStateException("createSynchronousWorkerThread: stop requested");
        
        if (!isMain) SwerveThreadContext.assertSynchronousThread();
        //
        ExecutorService service = isMain ? this.mainThreadExecutor : this.workerThreadsExecutor;
        //
        service.execute(new Runnable()
            {
            @Override public void run()
                {
                ThreadPool.logThreadLifeCycle(isMain ? "synch main" : "synch worker", new SynchronousThreadRoot(threadBody, isMain));
                }
            });
        }
    
    private void setThreadThunker()
        {
        SwerveThreadContext context = SwerveThreadContext.createIfNecessary();
        context.opMode = this;
        context.isSynchronousThread = true;
        }

    //----------------------------------------------------------------------------------------------
    // init(), init_loop(), start(), loop(), and stop()
    //----------------------------------------------------------------------------------------------

    /**
     * Advanced: The robot controller runtime calls init(), once, to request that we initialize ourselves
     */
    @Override public final void init()
        {
        try {
            // Call the subclass hook in case they might want to do something interesting
            this.preInitHook();

            // Remember who the loop thread is so that we know whom to communicate with from a
            // synchronous thread. Note: we ASSUME here that init() and loop() run on the same thread
            loopThread = Thread.currentThread();

            // Remember the old hardware map somewhere that user code can easily get at it if it wants.
            this.unthunkedHardwareMap = super.hardwareMap;
            // Make a new processed hardware map, and remember it in a variable that shadows the super one.
            // Note that we always leave the super one unchanged; this is important to OpModeShutdownNotifier.
            this.hardwareFactory = new SynchronousOpModeHardwareFactory(this, this.useExperimentalHardwareMap);
            this.hardwareMap     = this.hardwareFactory.createProcessedHardwareMap();

            // Similarly replace the telemetry variable
            this.telemetry = new TelemetryDashboardAndLog(this);

            // We're being asked to start, not stop
            this.started = false;
            this.stopRequested = false;
            this.loopCount.set(0);

            this.exceptionThrownOnMainThread = null;
            this.firstExceptionThrownOnASynchronousWorkerThread.set(null);

            // Create the main thread and start it up and going!
            this.createSynchronousWorkerThread(new IInterruptableRunnable()
                {
                @Override
                public void run() throws InterruptedException
                    {
                    Log.d(LOGGING_TAG, String.format("starting OpMode {%s}", SynchronousOpMode.this.getClass().getSimpleName()));
                    SynchronousOpMode.this.main();
                    }
                }, true);

            // Call the subclass hook in case they might want to do something interesting
            this.postInitHook();
            }
        catch (Exception e)
            {
            Log.e(LOGGING_TAG, String.format("exception thrown in init(): %s", Util.getStackTrace(e)));
            throw e;    // Rethrow so this exception gets displayed on phone displays
            }
        }

    /**
     * Advanced: The robot controller runtime calls init_loop() repeatedly after the Init button is
     * pressed but before start() is called. This has little utility in SynchronousOpMode,
     * but for consistency we provide subclass hooks that mirror how we handle the other
     * overrideable OpMode methods.
     */
    @Override public final void init_loop()
        {
        this.preInitLoopHook();

        synchronized (this.loopLock)
            {
            // Capture the gamepad state for later processing
            this.captureGamepadState();

            this.loopLock.notifyAll();
            }

        this.postInitLoopHook();
        }

    /**
     * Advanced: start() is called when the autonomous or the teleop mode begins: the robot
     * should start moving!
     *
     * @see #waitForStart()
     */
    @Override public final void start()
        {
        // Call the subclass hook in case they might want to do something interesting
        this.preStartHook();
        
        synchronized (this.loopLock)
            {
            this.started = true;
            this.loopLock.notifyAll();
            }

        // Call the subclass hook in case they might want to do something interesting
        this.postStartHook();
        }

    /**
     * Advanced: The robot controller runtime calls loop() on a frequent basis, nominally every few ms or so.
     * 
     * Our implementation here just executes the work that has been requested from the
     * synchronous threads.
     */
    @Override public final void loop()
        {
        // Protect the whole silly thing. If we throw in there, 'caller is just going to tell
        // on us, so we want to get to know what's happening before he does.
        try {
            // Call the subclass hook in case they might want to do something interesting
            this.preLoopHook();

            // Validate our assumption of init() and loop() running on the same thread.
            assertTrue(this.isLoopThread());

            synchronized (this.loopLock)
                {
                // Keep track of how many loop() calls we've seen
                this.loopCount.getAndIncrement();

                // If we had an exception thrown by a synchronous thread, then throw it here. 'Sort
                // of like thunking the exceptions. Exceptions from the main thread take
                // priority over those from worker threads. Note that the reads here are indeed
                // racing with the writes that are throwing, but that's ok.
                RuntimeException e = this.exceptionThrownOnMainThread;
                if (e == null)
                    {
                    e = this.firstExceptionThrownOnASynchronousWorkerThread.get();
                    }
                if (e != null)
                    {
                    throw e;
                    }

                // Capture the gamepad state for later processing
                this.captureGamepadState();

                // Call the subclass hook in case they might want to do something interesting
                this.midLoopHook();

                // Tell people that this loop cycle is complete
                this.loopLock.notifyAll();
                }

            // Call the subclass hook in case they might want to do something interesting
            this.postLoopHook();
            }
        catch (Exception e)
            {
            Log.e(LOGGING_TAG, String.format("exception thrown in loop(): %s", Util.getStackTrace(e)));
            throw e;    // Rethrow so this exception gets displayed on phone displays
            }
        }

    /**
     * Advanced: The robot controller runtime calls stop() to shut down the OpMode.
     *
     * We take steps as best as is possible to ensure that the main() thread is terminated
     * before this call returns.
     */
    @Override public final void stop()
        {
        try {
            // Call the subclass hook in case they might want to do something interesting
            this.preStopHook();
            Log.d(LOGGING_TAG, String.format("stopping OpMode {%s}...", this.getClass().getSimpleName()));

            // Next time synchronous threads ask, yes, we do want to stop
            this.stopRequested = true;

            // Give all of our worker threads a heads up to get out of town
            this.workerThreadsExecutor.shutdownNow();
            this.mainThreadExecutor.shutdownNow();

            // Serially wait for these folk to pack up and leave
            ThreadPool.awaitTerminationOrExitApplication(this.workerThreadsExecutor, 10, TimeUnit.SECONDS, "synchronous threads", "unreasonable delay in user code?");
            ThreadPool.awaitTerminationOrExitApplication(this.mainThreadExecutor, 10, TimeUnit.SECONDS, "synchronous main thread", "unreasonable delay in user code?");

            if (this.hardwareFactory != null)
                {
                this.hardwareFactory.stop();
                this.hardwareFactory = null;
                }

            Log.d(LOGGING_TAG, String.format("...stopped"));
            this.postStopHook();
            }
        catch (Exception e)
            {
            Log.e(LOGGING_TAG, String.format("exception thrown in stop(): %s", Util.getStackTrace(e)));
            throw e;    // Rethrow so this exception gets displayed on phone displays
            }
        }

    //----------------------------------------------------------------------------------------------
    // Advanced: loop thread subclass hooks
    //----------------------------------------------------------------------------------------------

    /**
     * Advanced: the various 'hook' calls calls preInitHook(), postInitHook(), preLoopHook(), 
     * etc are hooks that advanced users might want to override in their subclasses to something
     * interesting. 
     * 
     * No particular semantic is implied or required, though the timing of the calls is defined:
     * the 'pre' and 'post' variations are called at the beginning and the end of their respective
     * methods, while midLoopHook() is called in loop() after variable state (e.g. gamepads) has
     * been established.
     */
    protected void preInitHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void postInitHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void preInitLoopHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void postInitLoopHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void preStartHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void postStartHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void preLoopHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void midLoopHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void postLoopHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void preStopHook() { /* hook for subclasses */ }
    /**
     * Advanced: a hook for subclasses
     * @see #preInitHook()
     */
    protected void postStopHook() { /* hook for subclasses */ }

    //----------------------------------------------------------------------------------------------
    // Thunking helpers
    //----------------------------------------------------------------------------------------------

    private static SynchronousOpMode getThreadSynchronousOpMode()
        {
        return (SynchronousOpMode)(SwerveThreadContext.getOpMode());
        }

    /**
     * Advanced: Answer as to whether the current thread is in fact the loop thread
     * 
     * @see SwerveThreadContext#isSynchronousThread()
     */
    private boolean isLoopThread()
        {
        return this.loopThread.getId() == Thread.currentThread().getId();
        }
    }