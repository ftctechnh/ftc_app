package org.swerverobotics.library;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import android.util.*;
import static junit.framework.Assert.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;

/**
 * SynchronousOpMode is a base class that can be inherited from in order to
 * write op modes that can be coded in a traditional programming style.
 *
 * Extend this class and implement the {@link #main()} method to add your own code.
 */
public abstract class SynchronousOpMode extends OpMode implements IThunkDispatcher
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
    public Gamepad gamepad1 = new Gamepad();
    /** 
     * Provides access to the second gamepad controller. Only changes as a result of calling
     * {@linkplain #updateGamepads()}.
     *
     * @see #gamepad1
     */
    public Gamepad gamepad2 = new Gamepad();

    /**
     * provides access to an object by which telemetry information can be transmitted
     * from the robot controller to the driver station.
     * 
     * As with game pads, we hid the 'telemetry' variable of the super class and replace it
     * with one that can work on synchronous threads.
     */
    public TelemetryDashboardAndLog telemetry;

    /** Advanced: the number of nanoseconds in a millisecond. */
    public static final long NANO_TO_MILLI = 1000000;

    /**
     * Advanced: msLoopDwellMax is the (soft) maximum number of milliseconds that
     * our loop() implementation will spend in any one call before returning. 
     * Usually, much less time than this maximum is expended.
     *
     * @return the current maximum loop dwell time.
     */
    public long getMsLoopDwellMax()                    { return msLoopDwellMax; }
    /**
     * Advanced: Sets the maximum loop dwell time.
     *
     * @param msLoopDwellMax the new maximum loop dwell time, in milliseconds
     * @see #getMsLoopDwellMax()
     * */
    public void setMsLoopDwellMax(long msLoopDwellMax) { this.msLoopDwellMax = msLoopDwellMax; }

    private long msLoopDwellMax = 15;

    /**
     * Advanced: loopDwellCheckCount is the number of thunks we will execute in loop()
     * before checking whether we've exceeded msLoopDwellMax.
     */
    public int loopDwellCheckCount = 5;

    /**
     * Advanced: the number of times loop() has been called on the loop thread.
     * @return the number of times loop() has been called.
     */
    public int getLoopCount() { return this.loopCount.get(); }
    private final AtomicInteger loopCount = new AtomicInteger(0);

    /**
     * We define a *local* hardwareMap variable here to hide the one in our base
     * class as the one we want user code to see is the one with the thunking in it.
     */
    public HardwareMap hardwareMap = null;

    /**
     * Advanced: unthunkedHardwareMap contains the original hardware map provided
     * in OpMode before it was replaced with a version that does thunking.
     */
    public HardwareMap unthunkedHardwareMap = null;

    /**
     * Advanced: use experimental approaches to thunking hardware devices
     */
    protected boolean useExperimentalThunking = false;

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
     * Put the current thread to sleep for a bit as it has nothing better to do.
     *
     * idle(), which must be called on a synchronous thread, never on the loop() thread, causes the
     * synchronous thread to go to sleep until it is likely that there's something useful to do.
     * Specifically, it currently waits at most until the next end of a loop() cycle, and might return
     * much earlier if there is new gamepad state available.
     *
     * One should use this method when you have nothing better to do in your code, usually
     * at the very end of your while(opModeIsActive()) loop in TeleOp. Calling Thread.yield()
     * has similar effects, but idle() uses processor resources more effectively. Calling idle()
     * is entirely optional: it just helps make the system a little more responsive and a
     * little more efficient.
     *
     * {@link #idle()} is similar to waitOneFullHardwareCycle(), but makes no guarantees as to
     * completing any particular number of hardware cycles, if any.
     *
     * @throws InterruptedException thrown if the thread is interrupted
     * @see #main()
     * @see #synchronousThreadIdle()
     * @see #waitOneFullHardwareCycle()
     */
    public final void idle() throws InterruptedException
        {
        synchronized (this.loopLock)
            {
            // If new input has arrived since anyone last looked, then let our caller process that
            // if he is looking at the game pad input. If he's not, or if there's nothing there,
            // then we save some cycles and processing power by waiting instead of spinning.
            if (this.gamepadInputQueried && isNewGamepadStateAvailable())
                {
                Thread.yield();     // avoid tight loop
                return;
                }
            
            // Otherwise, we know there's nothing to do until at least the next loop() call.
            // The trouble is, it's hard to know when that is. We might be running here 
            // *immediately* before loop() is about to run. Looking at loop counts could allow
            // us to guarantee that we wait at least one whole cycle, yes, but that's overkill,
            // that's not what we're looking for. So instead, we just wait until loop() pings us
            // it the bottom of it's cycle, which may be a bit less than a whole loop(), but is
            // the reasonable compromise.
            this.loopLock.wait();
            }
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
     * @return a new synchronous thread on which the indicated code will run. The thread has
     *         not yet been started.
     * @see #main()
     * @see #opModeIsActive()
     */
    public Thread createSynchronousWorkerThread(IInterruptableRunnable threadBody)
        {
        return this.createSynchronousWorkerThread(threadBody, false);
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
     */
    public void waitForThreadsWritesToReachHardware() throws InterruptedException
        {
        this.waitForLoopCycleEmptyOfActionKey
            (
            SynchronousThreadContext.getThreadContext().actionKeyWritesFromThisThread
            );
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
        // Called from loop()
        boolean changed1 = true, changed2 = true;
        //
        if (this.gamepad1Captured == null)
            this.gamepad1Captured = new Gamepad();
        else
            changed1 = !gamepadsSame(this.gamepad1Captured, super.gamepad1);
        //
        if (this.gamepad2Captured == null)
            this.gamepad2Captured = new Gamepad();
        else
            changed2 = !gamepadsSame(this.gamepad2Captured, super.gamepad2);
        //
        gamepadAssign(this.gamepad1Captured, super.gamepad1);
        gamepadAssign(this.gamepad2Captured, super.gamepad2);
        //
        boolean changed = changed1 || changed2;
        //
        if (changed)
            {
            Log.v(LOGGING_TAG, String.format("gamepad state: %d", this.gamepadStateCount.getAndIncrement()));
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
    private         ThunkingHardwareFactory hardwareFactory = null;
    private final   ActionQueueAndHistory   actionQueueAndHistory = new ActionQueueAndHistory();
    private         AtomicBoolean           gamePadCaptureStateChanged = new AtomicBoolean(false);
    private         boolean                 gamepadInputQueried = false;
    private final   Object                  loopLock = new Object();
    private final   SparseArray<Runnable>   singletonLoopActions = new SparseArray<Runnable>();
    private static  AtomicInteger           prevSingletonKey = new AtomicInteger(0);

    private         Thread                  loopThread;
    private         Thread                  mainThread;
    private final   Collection<Thread>      synchronousWorkerThreads = new ConcurrentLinkedQueue<Thread>();
    private         RuntimeException        exceptionThrownOnMainThread;
    private final   AtomicReference<RuntimeException> firstExceptionThrownOnASynchronousWorkerThread = new AtomicReference<RuntimeException>();
    private final static int                msWaitForMainThreadTermination              = 250;
    private final static int                msWaitForSynchronousWorkerThreadTermination = 50;

    private         Gamepad                 gamepad1Captured = null;
    private         Gamepad                 gamepad2Captured = null;

    // State only intended to support debugging and logging
    private         AtomicInteger           gamepadStateCount = new AtomicInteger(0);

    public SynchronousOpMode()
        {
        }

    //----------------------------------------------------------------------------------------------
    // Types
    //----------------------------------------------------------------------------------------------

    private class ActionQueueAndHistory
        {
        //-----------------------------------------------------------------------
        // Types

        private class ActionKeyHistory
            {
            private boolean[] array;

            ActionKeyHistory()
                {
                this.array = new boolean[30];   // 30 is pretty arbitrary
                }

            void put(int index, boolean value)
                {
                if (index >= this.array.length)
                    {
                    this.array = Arrays.copyOf(this.array, Math.max(this.array.length,index) + 5);
                    }
                this.array[index] = value;
                }

            boolean valueAt(int index)
                {
                if (index >= this.array.length)
                    return false;
                return this.array[index];
                }
            }

        //-----------------------------------------------------------------------
        // State

        Queue<Runnable>  queue;
        ActionKeyHistory history;
        Queue<Runnable>  historicalActions;

        //-----------------------------------------------------------------------
        // Construction

        ActionQueueAndHistory()
            {
            this.queue   = this.newQueue();
            this.history = this.newHistory();
            if (BuildConfig.DEBUG && false)
                this.historicalActions = new LinkedList<Runnable>();
            }
        private Queue<Runnable> newQueue()
            {
            return new LinkedList<Runnable>();
            }
        private ActionKeyHistory newHistory()
            {
            return new ActionKeyHistory();
            }

        //-----------------------------------------------------------------------
        // Operations

        synchronized void clear()
            {
            this.queue   = this.newQueue();
            this.history = this.newHistory();
            this.onChanged();
            }

        synchronized void clearHistory()
            {
            this.history = this.newHistory();
            this.onChanged();
            }

        synchronized void add(Runnable action)
            {
            assertTrue(!BuildConfig.DEBUG || action!=null);
            this.queue.add(action);
            this.onChanged();
            }

        synchronized Runnable poll()
            {
            Runnable result = this.queue.poll();
            if (result != null)
                {
                if (result instanceof IActionKeyed)
                    {
                    IActionKeyed keyed = (IActionKeyed)result;
                    for (int actionKey : keyed.getActionKeys())
                        {
                        this.history.put(actionKey, true);
                        }
                    }
                if (this.historicalActions != null)
                    {
                    this.historicalActions.add(result);
                    }

                this.onChanged();
                }
            return result;
            }

        synchronized boolean containsActionKey(int queryKey)
            {
            // Is the key present in our history?
            if (this.history.valueAt(queryKey))
                {
                return true;
                }

            // Is the key present in pending stuff?
            for (Runnable action : this.queue)
                {
                if (action instanceof IActionKeyed)
                    {
                    IActionKeyed keyed = (IActionKeyed)action;
                    for (int actionKey : keyed.getActionKeys())
                        {
                        if (actionKey == queryKey)
                            {
                            return true;
                            }
                        }
                    }
                }

            // Not present
            return false;
            }

        private void onChanged()
            {
            this.notifyAll();
            }
        public void waitForChange() throws InterruptedException
            {
            // Note: caller must hold the monitor
            this.wait();
            }
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
                }
            catch (InterruptedException|RuntimeInterruptedException ignored)
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

    private void interruptSynchronousThreads()
        {
        for (Thread thread : this.synchronousWorkerThreads)
            {
            thread.interrupt();
            }
        if (this.mainThread != null)
            {
            this.mainThread.interrupt();
            }
        }

    private void waitForSynchronousWorkerThreads(int msWait) throws InterruptedException
        {
        for (Thread thread : this.synchronousWorkerThreads)
            {
            thread.join(msWait);
            }
        }

    private void waitForMainThread(int msWait) throws InterruptedException
        {
        if (this.mainThread != null)
            {
            this.mainThread.join(msWait);
            }
        }

    private Thread createSynchronousWorkerThread(IInterruptableRunnable threadBody, boolean isMain)
        {
        if (this.isStopRequested())
            throw new IllegalStateException("createSynchronousWorkerThread: stop requested");
        
        if (!isMain) SynchronousThreadContext.assertSynchronousThread();
        //
        Thread thread = new Thread(new SynchronousThreadRoot(threadBody, isMain));
        if (isMain)
            {
            thread.setName("Sync main");
            }
        else
            {
            thread.setName("Sync worker");
            this.synchronousWorkerThreads.add(thread);
            }
        return thread;
        }
    
    private void setThreadThunker()
        {
        SynchronousThreadContext.create(this, this);
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

            // Make this thread easy to find in the debugger
            Thread.currentThread().setName("FTC loop() thread");

            // Remember who the loop thread is so that we know whom to communicate with from a
            // synchronous thread. Note: we ASSUME here that init() and loop() run on the same thread
            loopThread = Thread.currentThread();

            // Remember the old hardware map somewhere that user code can easily get at it if it wants.
            this.unthunkedHardwareMap = super.hardwareMap;
            // Make a new thunking one, and remember it in a variable that shadows the super one.
            // Note that we always leave the super one unchanged; this is important to OpModeShutdownNotifier.
            this.hardwareFactory      = new ThunkingHardwareFactory(this, this.useExperimentalThunking);
            this.hardwareMap          = this.hardwareFactory.createThunkedHardwareMap();

            // Similarly replace the telemetry variable
            this.telemetry = new TelemetryDashboardAndLog(super.telemetry);

            // Paranoia: clear any state that may just perhaps be lingering
            this.clearSingletons();
            this.actionQueueAndHistory.clear();
            this.synchronousWorkerThreads.clear();

            // We're being asked to start, not stop
            this.started = false;
            this.stopRequested = false;
            this.loopCount.set(0);

            this.exceptionThrownOnMainThread = null;
            this.firstExceptionThrownOnASynchronousWorkerThread.set(null);

            // Create the main thread and start it up and going!
            this.mainThread = this.createSynchronousWorkerThread(new IInterruptableRunnable()
                {
                @Override public void run() throws InterruptedException
                    {
                    Log.d(LOGGING_TAG, String.format("starting OpMode {%s}", SynchronousOpMode.this.getClass().getSimpleName()));
                    SynchronousOpMode.this.main();
                    }
                }, true);
            this.mainThread.start();

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

        // Make waitOneFullHardwareCycle work before start is called
        synchronized (this.loopLock)
            {
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
            assertTrue(!BuildConfig.DEBUG || this.isLoopThread());

            synchronized (this.loopLock)
                {
                // Keep track of how many loop() calls we've seen
                this.loopCount.getAndIncrement();

                // The history of what was executed int the previous loop() call is now irrelevant
                this.actionQueueAndHistory.clearHistory();

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

                // Start measuring time so we don't spend too long here in loop(). That might
                // happen if we got flooded with a bevy of non-waiting actions and we didn't have
                // this check here.
                long nanotimeStart = System.nanoTime();
                long nanotimeMax   = nanotimeStart + this.getMsLoopDwellMax() * NANO_TO_MILLI;

                // Do any actions we've been asked to execute here on the loop thread
                for (int i = 1; ; i++)
                    {
                    // Get the next action in the queue. Get out of here if there aren't any more
                    Runnable action;
                    synchronized (this.actionQueueAndHistory)
                        {
                        action = this.actionQueueAndHistory.poll();
                        if (null == action)
                            break;
                        }

                    // Execute the work that needs to be done on the loop thread
                    executeAction(action);

                    // Periodically check whether we've run long enough for this loop() call.
                    if (i % this.loopDwellCheckCount == 0)
                        {
                        if (System.nanoTime() >= nanotimeMax)
                            break;
                        }
                    }

                // Dig out and execute any of our singleton actions.
                List<Runnable> actions = this.snarfSingletons();
                for (Runnable action : actions)
                    {
                    executeAction(action);
                    }

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

    void executeAction(Runnable action)
        {
        try {
            action.run();
            }
        catch (Exception e)
            {
            // Ignore. Actions generally are responsible for cleaning up their own
            // mess; they shouldn't be disturbing us. Thus, we eat any exceptions to keep
            // things moving.
            Log.e(LOGGING_TAG, "action exception leaked through to loop thread: " + e);
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

            try {
                // Give all of our worker threads a heads up to get out of town
                this.interruptSynchronousThreads();

                // Serially wait for these folk to pack up and leave
                this.waitForSynchronousWorkerThreads(this.msWaitForSynchronousWorkerThreadTermination);
                this.waitForMainThread(this.msWaitForMainThreadTermination);
                }
            catch (InterruptedException e)
                {
                // We were waiting for the threads to shutdown but then *we* got an interrupt
                // while doing so. That's an entirely unexpected situation; it shouldn't happen
                // architecturally in the robot controller app.
                Log.e(LOGGING_TAG, String.format("unexpected interrupt: %s", Util.getStackTrace(e)));
                throw SwerveRuntimeException.wrap(e);
                }

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
    // IThunkDispatcher
    //----------------------------------------------------------------------------------------------

    /**
     * Advanced: Execute the indicated action on the loop thread given that we are on a synchronous thread
     */
    @Override public void executeOnLoopThread(Runnable action)
        {
        SynchronousThreadContext.assertSynchronousThread();
        this.actionQueueAndHistory.add(action);
        }

    /**
     * Advanced: Execute the indicated action on the loop thread if we are on a synchronous thread
     * or the loop() thread.
     *
     * If a previous call has been made with the same key, then replace that previous action;
     * otherwise, add a new action with the key.
     */
    @Override public void executeSingletonOnLoopThread(int singletonKey, Runnable action)
        {
        SynchronousThreadContext.assertSynchronousThread();
        synchronized (this.singletonLoopActions)
            {
            this.singletonLoopActions.put(singletonKey, action);
            }
        }

    /**
     * Advanced: Return a new key for use in executeSingletonOnLoopThread()
     */
    @Override public int getNewSingletonKey()
        {
        return staticGetNewSingletonKey();
        }
    
    /**
     * Advanced/Internal: Return a new key by which actions can be scheduled using executeSingletonOnLoopThread()
     * @return the new singleton key.
     */
    public static int staticGetNewSingletonKey()
        {
        return prevSingletonKey.incrementAndGet();
        }

    /**
     * Advanced: If we are running on a synchronous thread, then return the object
     * which manages thunking from the current thread to the loop() thread.
     *
     * @return the thunk dispatcher object, or null if we are not on a synchronous thread
     */
    public static IThunkDispatcher getThreadThunker()
        {
        return SynchronousThreadContext.getContextualThunker();
        }

    //----------------------------------------------------------------------------------------------
    // Thunking helpers
    //----------------------------------------------------------------------------------------------

    private static SynchronousOpMode getThreadSynchronousOpMode()
        {
        return (SynchronousOpMode)(SynchronousThreadContext.getContextualOpMode());
        }

    /**
     * Advanced/Internal: Wait until we encounter a loop() cycle that doesn't (yet) contain any actions which
     * are also thunks and whose key is the one indicated.
     * @param actionKey the key used to indicate which actions are of interest
     */
    private void waitForLoopCycleEmptyOfActionKey(int actionKey) throws InterruptedException
        {
        synchronized (this.actionQueueAndHistory)
            {
            while (this.actionQueueAndHistory.containsActionKey(actionKey))
                {
                this.actionQueueAndHistory.waitForChange();
                }
            }
        }
    /**
     * Advanced/Internal: Wait until we encounter a loop() cycle that doesn't (yet) contain any actions which
     * are also thunks and whose key is the one indicated.
     * @param actionKey the key used to indicate which actions are of interest
     * @throws InterruptedException thrown if the thread is interrupted
     */
    public static void synchronousThreadWaitForLoopCycleEmptyOfActionKey(int actionKey) throws InterruptedException
        {
        SynchronousOpMode.getThreadSynchronousOpMode().waitForLoopCycleEmptyOfActionKey(actionKey);
        }

    /**
     * Advanced: Answer as to whether the current thread is in fact the loop thread
     * 
     * @see SynchronousThreadContext#isSynchronousThread() 
     */
    private boolean isLoopThread()
        {
        return this.loopThread.getId() == Thread.currentThread().getId();
        }

    private ArrayList<Runnable> snarfSingletons()
    // Atomically retrieve a copy of the singleton loop actions. The lock on that object
    // is a leaf lock, meaning that no further locks may be acquired if that lock is held.
    // By this protocol we avoid deadlock, and that is a wonderful thing.
        {
        ArrayList<Runnable> result = new ArrayList<Runnable>();
        synchronized (this.singletonLoopActions)
            {
            for (int i = 0; i < this.singletonLoopActions.size(); i++)
                {
                int key = this.singletonLoopActions.keyAt(i);
                result.add(this.singletonLoopActions.get(key));
                }
            this.singletonLoopActions.clear();
            }
        return result;
        }

    private void clearSingletons()
        {
        synchronized (this.singletonLoopActions)
            {
            this.singletonLoopActions.clear();
            }
        }
    }