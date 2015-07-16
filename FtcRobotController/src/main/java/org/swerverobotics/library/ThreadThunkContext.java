package org.swerverobotics.library;


import com.qualcomm.ftcrobotcontroller.BuildConfig;

/**
 * ThreadThunkContext maintains the thunking context for a given synchronous thread.
 */
public class ThreadThunkContext
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /**
     * getThread() returns the Thread for which we are the thunking context.
     */
    public Thread getThread() { return this.thread; }

    /**
     * getThunker() returns the channel by which we can thunk from a synchronous
     * thread to the loop() thread.
     */
    public IThunker getThunker() { return this.thunker; }

    private final Thread   thread;
    private final IThunker thunker;
    private final Object   lock;
    private       int      dispatchedThunkCount;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadThunkContext(IThunker thunker)
        {
        this.thread = Thread.currentThread();
        this.thunker = thunker;
        this.lock = new Object();
        this.dispatchedThunkCount = 0;
        }

    //----------------------------------------------------------------------------------------------
    // Notifications
    //----------------------------------------------------------------------------------------------

    /**
     * Note that a thunk was dispatched from the thread for which we are the context.
     *
     * Note that this can be called from an arbitrary thread, including in particular
     * the loop() thread. It is commonly called only from synchronous threads.
     */
    public void noteThunkDispatching(IThunk thunk)
        {
        synchronized (this.lock)
            {
            this.dispatchedThunkCount++;
            }
        }

    /**
     * A thunk that previously called us with noteThunkDispatching is calling
     * us back to inform us that there was a failure in the dispatching logic,
     * and he thus *won't* be calling us later with noteThunkCompletion().
     */
    public void noteThunkDispatchFailure(IThunk thunk)
        {
        noteThunkCompletion(thunk);
        }

    /**
     * Note that a thunk that was previously dispatched from the thread for which we
     * are the context has completed its work.
     *
     * Note that this can be called from an arbitrary thread, including in particular
     * the loop() thread. In fact, it is most commonly called from the loop() thread.
     */
    public void noteThunkCompletion(IThunk thunk)
        {
        synchronized (this.lock)
            {
            if (BuildConfig.DEBUG && !(this.dispatchedThunkCount > 0))
                throw new AssertionError();

            if (--this.dispatchedThunkCount == 0)
                {
                this.notifyAll();
                }
            }
        }

    /**
     * Wait until all any thunks that are currently in flight and have been dispatched
     * from the the thread with which we are associated have completed
     *
     * Note that this can be called from an arbitrary thread, including in particular
     * the loop() thread. It is commonly called only from synchronous threads.
     */
    public void waitForThreadThunkCompletions() throws InterruptedException
        {
        synchronized (this.lock)
            {
            this.wait();
            }
        }

    //----------------------------------------------------------------------------------------------
    // Lookup
    //----------------------------------------------------------------------------------------------

    public static void setThreadThunker(IThunker thunker)
        {
        tlsThunker.set(new ThreadThunkContext(thunker));
        }

    public static ThreadThunkContext getThreadContext()
        {
        return tlsThunker.get();
        }

    /**
     * tlsThunker is the thread local variable by which we are associated with a thread
     */
    private static final ThreadLocal<ThreadThunkContext> tlsThunker = new ThreadLocal<ThreadThunkContext>()
        {
        @Override protected ThreadThunkContext initialValue() { return null; }
        };

    }