package org.swerverobotics.library;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import android.util.SparseArray;

import junit.framework.Assert;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;

/**
 * SynchronousOpMode is a base class that can be inherited from in order to
 * write op modes that can be coded in a traditional programming style.
 *
 * Extend this class and implement the main() method to add your own code.
 */
public abstract class SynchronousOpMode extends OpMode implements IThunkDispatcher
    {
    //----------------------------------------------------------------------------------------------
    // Public State
    //----------------------------------------------------------------------------------------------

    /**
     * provides access to the first gamepad controller.
     * 
     * The game pad variables are redeclared here so as to hide those in our OpMode superclass
     * as the latter may be updated by robot controller runtime at arbitrary times and in a manner 
     * which is not synchronized with processing on a synchronous thread. We take pains to ensure 
     * that the variables declared here do not suffer from that problem.
     */
    public IGamepad gamepad1 = null;
    /** 
     * provides access to the second gamepad controller.
     * @see #gamepad1 */
    public IGamepad gamepad2 = null;

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
     * 
     * Usually, much less time than this maximum is expended.
     */
    public long getMsLoopDwellMax()                    { return msLoopDwellMax; }
    /** @see #getMsLoopDwellMax() */
    public void setMsLoopDwellMax(long msLoopDwellMax) { this.msLoopDwellMax = msLoopDwellMax; }
    private long msLoopDwellMax = 15;

    /**
     * Advanced: loopDwellCheckCount is the number of thunks we will execute in loop()
     * before checking whether we've exceeded msLoopDwellMax.
     */
    public int loopDwellCheckCount = 5;

    /**
     * Advanced: the number of times the loop thread has been called
     */
    public int getLoopCount() { return this.loopCount.get(); }
    private final AtomicInteger loopCount = new AtomicInteger(0);

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
    // Key Public and Protected Methods
    //----------------------------------------------------------------------------------------------

    /**
     * Central idea: implement main() (in a subclass) to contain your robot logic!
     */
    protected abstract void main() throws InterruptedException;

    /**
     * In your main() method, first perform any necessary data and hardware initialization,
     * then call waitForStart() to await the commencement of the game.
     */
    public final void waitForStart() throws InterruptedException
        {
        synchronized (this.loopLock)
            {
            while (!this.started())  // avoid spurious wakeups
                {
                this.loopLock.wait();
                }
            }
        }

    /**
     * Answer as to whether this opMode is active and the robot should continue onwards. If the
     * opMode is not active, synchronous threads should terminate at their earliest convenience.
     * 
     * @see #started()
     * @see #stopRequested()
     */
    public final boolean opModeIsActive()
        {
        return !this.stopRequested() && this.started();
        }

    /**
     * Has the opMode been started?
     * 
     * @see #opModeIsActive()
     * @see #stopRequested()
     */
    public final boolean started()
        {
        return this.started;
        }

    /**
     * Has the the stopping of the opMode been requested?
     * 
     * @see #opModeIsActive()
     * @see #started()
     */
    public final boolean stopRequested()
        {
        return this.stopRequested || Thread.currentThread().isInterrupted();
        }
    
    /**
     * Answer as to whether there's (probably) any state different in any of the game pads
     * since the last time that this method was called. Calling this method atomically clears
     * the the state.
     * 
     * @see #newGamePadInputAvailable() 
     */
    public final boolean newGamePadInputAvailable()
        {
        // We *wish* there was a way that we could hook or get a callback from the
        // incoming gamepad change messages, but, alas, at present we can find no
        // way of doing that.
        //
        this.gamepadInputQueried = true;
        return this.gamePadStateChanged.getAndSet(false);
        }

    /**
     * Similar to newGamePadInputAvailable(), but doesn't auto-reset the state when called
     *
     * @see #newGamePadInputAvailable()
     */
    public final boolean isNewGamePadInputAvailable()
        {
        this.gamepadInputQueried = true;
        return this.isNewGamePadInputAvailableInternal();
        }
    
    private final boolean isNewGamePadInputAvailableInternal()
        {
        return this.gamePadStateChanged.get();
        }
    
    /**
     * Put the current thread to sleep for a bit as it has nothing better to do.
     *
     * idle(), which must be called on a synchronous thread, never on the loop() thread, causes the
     * synchronous thread to go to sleep until it is likely that the robot controller runtime
     * has gotten back in touch (by calling loop() again) and thus the state reported
     * by the various hardware devices and sensors might be different than what it was previously.
     *
     * One can use this method when you have nothing better to do until the underlying
     * robot controller runtime gets back in touch with us. Thread.yield() has similar effects, but
     * idle() / synchronousThreadIdle() is more efficient.
     * 
     * @see #synchronousThreadIdle() 
     */
    public final void idle() throws InterruptedException
        {
        synchronized (this.loopLock)
            {
            // If new input has arrived since anyone last looked, then let our caller process that
            // if he is looking at the game pad input. If he's not, then we save some cycles and
            // processing power by waiting instead of spinning.
            if (this.gamepadInputQueried && this.isNewGamePadInputAvailableInternal())
                {
                Thread.yield();     // avoid tight loop if caller not looking at gamepad input
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
     * Idles the current thread until stimulated by the robot controller runtime.
     * 
     * The current thread must be a synchronous thread.
     *
     * @see #idle()
     */
    public static void synchronousThreadIdle() throws InterruptedException
        {
        getThreadSynchronousOpMode().idle();
        }

    /**
     * Advanced: createSynchronousWorkerThread() is used to create secondary worker threads
     * from your main thread, should you wish to do so. 
     * 
     * Warning: multithreaded programming *is* rocket science!
     * 
     * Like the main() thread, synchronous worker threads should frequently call
     * opModeIsActive() and return from their loop body if the opMode has stopped.
     */
    public Thread createSynchronousWorkerThread(IInterruptableRunnable threadBody)
        {
        return this.createSynchronousWorkerThread(threadBody, false);
        }

    /**
     * Advanced: wait until all thunks that have been dispatched from the current (synchronous)
     * thread have completed their execution over on the loop() thread and their effects
     * to have reached the hardwaree.
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
     */
    public void waitForThreadsWritesToReachHardware() throws InterruptedException
        {
        this.waitForLoopCycleEmptyOfActionKey
            (
            SynchronousThreadContext.getThreadContext().actionKeyWritesFromThisThread
            );
        }

    //----------------------------------------------------------------------------------------------
    // Private state and construction
    //----------------------------------------------------------------------------------------------

    private volatile boolean                started;
    private volatile boolean                stopRequested;
    private final   ActionQueueAndHistory   actionQueueAndHistory = new ActionQueueAndHistory();
    private         AtomicBoolean           gamePadStateChanged = new AtomicBoolean(false);
    private         boolean                 gamepadInputQueried = false;
    private final   Object                  loopLock = new Object();
    private final   SparseArray<IAction>    singletonLoopActions = new SparseArray<IAction>();
    private static  AtomicInteger           prevSingletonKey = new AtomicInteger(0);

    private         Thread                  loopThread;
    private         Thread                  mainThread;
    private final   Queue<Thread>           synchronousWorkerThreads = new ConcurrentLinkedQueue<Thread>();
    private         RuntimeException        exceptionThrownOnMainThread;
    private final   AtomicReference<RuntimeException> firstExceptionThrownOnASynchronousWorkerThread = new AtomicReference<RuntimeException>();
    private final   int                     msWaitForMainThreadTermination              = 250;
    private final   int                     msWaitForSynchronousWorkerThreadTermination = 50;

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

        Queue<IAction>   queue;
        ActionKeyHistory history;

        //-----------------------------------------------------------------------
        // Construction

        ActionQueueAndHistory()
            {
            this.queue   = this.newQueue();
            this.history = this.newHistory();
            }
        private Queue<IAction> newQueue()
            {
            return new LinkedList<IAction>();
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

        synchronized void add(IAction action)
            {
            this.queue.add(action);
            this.onChanged();
            }

        synchronized IAction poll()
            {
            IAction result = this.queue.poll();
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
            for (IAction action : this.queue)
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
    private class SynchronousThreadRoot implements Runnable
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
                // Remember exceptions so we can throw them later
                if (this.isMain)
                    {
                    SynchronousOpMode.this.exceptionThrownOnMainThread = e;
                    }
                else
                    {
                    SynchronousOpMode.this.firstExceptionThrownOnASynchronousWorkerThread.compareAndSet(null, e); 
                    }
                }
            }
        }

    private void stopSynchronousThread(Thread thread, int msWait)
    // Note: the thread might not EVER have been started, so may not have any 
    // SynchronousThreadContext.
        {
        // Notify the thread that we wish it to stop what it's doing, clean up, and return.
        thread.interrupt();

        // Wait a while until the thread is no longer alive. If he doesn't clear out
        // in a reasonable amount of time, then just give up on him.
        try
            {
            thread.join(msWait);
            }
        catch (InterruptedException ignored)
            { 
            Util.handleCapturedInterrupt();
            }
        }

    private void stopSynchronousWorkerThreads(int msWait)
    // Do the shutdown in parallel so we're not serially taking the timeout hits.
    // We hope that will be a little faster.
        {
        List<Thread> interruptedThreads = new LinkedList<Thread>();
        //
        for (;;)
            {
            Thread thread = this.synchronousWorkerThreads.poll();
            if (null == thread)
                break;
            thread.interrupt();
            interruptedThreads.add(thread);
            }
        
        for (Thread thread : interruptedThreads)
            {
            try
                {
                thread.join(msWait);
                }
            catch (InterruptedException ignored) 
                {
                Util.handleCapturedInterrupt();
                }
            }
        }

    private Thread createSynchronousWorkerThread(IInterruptableRunnable threadBody, boolean isMain)
        {
        if (this.stopRequested())
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
        SynchronousThreadContext.setThreadThunker(this);
        }

    //----------------------------------------------------------------------------------------------
    // init(), start(), loop(), and stop()
    //----------------------------------------------------------------------------------------------

    /**
     * The robot controller runtime calls init(), once, to request that we initialize ourselves
     */
    @Override public final void init()
        {
        // Call the subclass hook in case they might want to do something interesting
        this.preInitHook();

        // Replace the op mode's hardware map variable with one whose contained
        // object implementations will thunk over to the loop thread as they need to.
        this.unthunkedHardwareMap = super.hardwareMap;
        this.hardwareMap = (new ThunkingHardwareFactory(this.useExperimentalThunking)).createThunkedHardwareMap(this.unthunkedHardwareMap);

        // Similarly replace the telemetry variable
        this.telemetry = new TelemetryDashboardAndLog(super.telemetry);

        // Remember who the loop thread is so that we know whom to communicate with from a 
        // synchronous thread. Note: we ASSUME here that init() and loop() run on the same thread
        loopThread = Thread.currentThread();

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
                SynchronousOpMode.this.main();
                }
            }, true);
        this.mainThread.start();

        // Call the subclass hook in case they might want to do something interesting
        this.postInitHook();
        }

    /**
     * start() is called when the autonomous or the teleop mode begins: the robot
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
     * The robot controller runtime calls loop() on a frequent basis, nominally every few ms or so.
     * 
     * Our implementation here just executes the work that has been requested from the
     * synchronous threads.
     */
    @Override public final void loop()
        {
        // Call the subclass hook in case they might want to do something interesting
        this.preLoopHook();

        // Validate our assumption of init() and loop() running on the same thread.
        if (BuildConfig.DEBUG) Assert.assertEquals(true, this.isLoopThread());

        synchronized (this.loopLock)
            {
            // Keep track of how many loop() calls we've seen
            this.loopCount.getAndIncrement();
            
            // The history of what was executed int the previous loop() call is now irrelevant
            this.actionQueueAndHistory.clearHistory();
            
            // If we had an exception thrown by a synchronous thread, then throw it here. 'Sort
            // of like thunking the exceptions. Exceptions from the main thread take
            // priority over those from worker threads.
            RuntimeException e = this.exceptionThrownOnMainThread;
            if (e == null) 
                {
                e = this.firstExceptionThrownOnASynchronousWorkerThread.get();
                }
            if (e != null)
                {
                throw e;
                }

            // Capture the gamepad states safely so that in a synchronous thread we don't see torn writes
            boolean diff1 = true;
            boolean diff2 = true;
            //
            if (this.gamepad1 == null)
                this.gamepad1 = new ThreadSafeGamepad(super.gamepad1);
            else
                diff1 = ((IGamepadInternal)this.gamepad1).updateGamepad(super.gamepad1);
            //
            if (this.gamepad2 == null)
                this.gamepad2 = new ThreadSafeGamepad(super.gamepad2);
            else
                diff2 = ((IGamepadInternal)this.gamepad2).updateGamepad(super.gamepad2);
            //
            this.gamePadStateChanged.compareAndSet(false, diff1 || diff2);

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
                IAction action;
                synchronized (this.actionQueueAndHistory)
                    {
                    action = this.actionQueueAndHistory.poll();
                    if (null == action)
                        break;
                    }

                // Execute the work that needs to be done on the loop thread
                action.doAction();

                // Periodically check whether we've run long enough for this loop() call.
                if (i % this.loopDwellCheckCount == 0)
                    {
                    if (System.nanoTime() >= nanotimeMax)
                        break;
                    }
                }

            // Dig out and execute any of our singleton actions.
            List<IAction> actions = this.snarfSingletons();
            for (IAction action : actions)
                {
                action.doAction();
                }

            // Tell people that this loop cycle is complete
            this.loopLock.notifyAll();
            }

        // Call the subclass hook in case they might want to do something interesting
        this.postLoopHook();
        }
    
    /**
     * The robot controller runtime calls stop() to shut down the OpMode. 
     *
     * We take steps as best as is possible to ensure that the main() thread is terminated
     * before this call returns.
     */
    @Override public final void stop()
        {
        // Call the subclass hook in case they might want to do something interesting
        this.preStopHook();

        // Next time synchronous threads ask, yes, we do want to stop
        this.stopRequested = true;

        // Clean up any worker threads 
        this.stopSynchronousWorkerThreads(this.msWaitForSynchronousWorkerThreadTermination);
        
        // Notify the main() thread that we wish it to stop what it's doing, clean up, and return.
        this.stopSynchronousThread(this.mainThread, this.msWaitForMainThreadTermination);
        
        // Call the subclass hook in case they might want to do something interesting
        this.postStopHook();
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
    @Override public void executeOnLoopThread(IAction action)
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
    @Override public void executeSingletonOnLoopThread(int singletonKey, IAction action)
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
     * (Internal) Return a new key by which actions can be scheduled using executeSingletonOnLoopThread()
     */
    public static int staticGetNewSingletonKey()
        {
        return prevSingletonKey.incrementAndGet();
        }

    /**
     * Advanced: If we are running on a synchronous thread, then return the object
     * which is managing the internal from the current thread to the loop() thread.
     * If we are not on a synchronous thread, then the behaviour is undefined.
     */
    public static IThunkDispatcher getThreadThunker()
        {
        SynchronousThreadContext.assertSynchronousThread();
        return SynchronousThreadContext.getThreadContext().getThunker();
        }

    //----------------------------------------------------------------------------------------------
    // Thunking helpers
    //----------------------------------------------------------------------------------------------

    private static SynchronousOpMode getThreadSynchronousOpMode()
        {
        return (SynchronousOpMode)(getThreadThunker());
        }

    /**
     * (Internal) Wait until we encounter a loop() cycle that doesn't (yet) contain any actions which
     * are also thunks and whose key is the one indicated.
     */
    public void waitForLoopCycleEmptyOfActionKey(int actionKey) throws InterruptedException
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
     * (Internal)
     * 
     *  @see #waitForLoopCycleEmptyOfActionKey(int) 
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

    private ArrayList<IAction> snarfSingletons()
    // Atomically retrieve a copy of the singleton loop actions. The lock on that object
    // is a leaf lock, meaning that no further locks may be acquired if that lock is held.
    // By this protocol we avoid deadlock, and that is a wonderful thing.
        {
        ArrayList<IAction> result = new ArrayList<IAction>();
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