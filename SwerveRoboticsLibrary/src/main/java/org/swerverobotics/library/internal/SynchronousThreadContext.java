package org.swerverobotics.library.internal;

import junit.framework.Assert;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.*;

/**
 * SynchronousThreadContext maintains the internal context for a synchronous thread.
 */
public class SynchronousThreadContext
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /**
     * getThread() returns the Thread for which we are the internal context.
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

    public SynchronousThreadContext(IThunker thunker)
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
    public void noteThunkDispatching(IAction thunk)
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
    public void noteThunkDispatchFailure(IAction thunk)
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
    public void noteThunkCompletion(IAction thunk)
        {
        synchronized (this.lock)
            {
            if (BuildConfig.DEBUG) Assert.assertEquals(true, this.dispatchedThunkCount > 0);

            if (--this.dispatchedThunkCount == 0)
                {
                this.lock.notifyAll();
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
            // Don't leave until any previous work issued by our associated thread
            // has been completed. The while() loop is necessary in order to deal
            // with 'spurious wakeups'.
            while (this.dispatchedThunkCount > 0)
                {
                this.lock.wait();
                }
            }
        }

    //----------------------------------------------------------------------------------------------
    // Lookup
    //----------------------------------------------------------------------------------------------

    public static void setThreadThunker(IThunker thunker)
        {
        tlsThunker.set(new SynchronousThreadContext(thunker));
        }

    public static SynchronousThreadContext getThreadContext()
        {
        return tlsThunker.get();
        }
    
    public static boolean isSynchronousThread()
        {
        return getThreadContext() != null;
        }

    /**
     * tlsThunker is the thread local variable by which a SynchronousThreadContext is associated with a thread
     */
    private static final ThreadLocal<SynchronousThreadContext> tlsThunker = new ThreadLocal<SynchronousThreadContext>()
        {
        @Override protected SynchronousThreadContext initialValue() { return null; }
        };

    }