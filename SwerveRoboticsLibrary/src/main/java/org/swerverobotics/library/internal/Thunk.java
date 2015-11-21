package org.swerverobotics.library.internal;

import android.util.Log;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;

/**
 * ThunkBase contains most of the code for thunking a call from a synchronous thread to the loop() thread
 *
 * @see <a href="https://en.wikipedia.org/wiki/Thunk">https://en.wikipedia.org/wiki/Thunk</a>
 */
public abstract class Thunk implements Runnable, IActionKeyed
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private   final SynchronousThreadContext context;
    protected final Object                   theLock;
    protected       RuntimeException         exception;
    public    final List<Integer>            actionKeys;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Thunk()
        {
        this.context    = SynchronousThreadContext.getThreadContext();
        this.theLock    = new Object();
        this.exception  = null;
        this.actionKeys = new LinkedList<Integer>();
        }

    //----------------------------------------------------------------------------------------------
    // Action key management
    //----------------------------------------------------------------------------------------------

    public static final int          nullActionKey = 0;
    static AtomicInteger             prevActionKey = new AtomicInteger(nullActionKey);
    
    public static int getNewActionKey()
        {
        return prevActionKey.incrementAndGet();
        }
    
    public void addActionKey(int actionKey)
        {
        this.actionKeys.add(actionKey);
        }

    //----------------------------------------------------------------------------------------------
    // IActionKeyed
    //----------------------------------------------------------------------------------------------
    
    @Override public List<Integer> getActionKeys()
        {
        return this.actionKeys;
        }
    
    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    /**
     * Executed on the loop() thread, run() is called to carry out the work of the thunk
     */
    public void run()
        {
        try {
            // Do what we came here to do
            this.actionOnLoopThread();
            }
        catch (RuntimeException e)
            {
            // Record the exception to be rethrown back on the waiting thread after we wake him
            this.exception = e;
            Log.e(SynchronousOpMode.LOGGING_TAG, "exception thrown during action: " + e);
            }

        // Tell all those waiting on the completion of this thunk that we are done
        synchronized (theLock)
            {
            theLock.notifyAll();
            }
        }

    protected void waitForCompletion() throws InterruptedException
        {
        // Wait until the action is carried out on the loop thread
        synchronized (theLock)
            {
            theLock.wait();
            }

        // If an exception was thrown on the loop thread, then re-throw it here
        if (this.exception != null)
            {
            throw this.exception;
            }
        }

    /**
     * Derived classes should implement actionOnLoopThread() to actually carry out work.
     */
    protected abstract void actionOnLoopThread();

    /**
     * Dispatch this thunk over to the loop thread.
     */
    protected void dispatch() throws InterruptedException
        {
        SynchronousThreadContext.assertSynchronousThread();
        this.context.getThunker().executeOnLoopThread(this);
        }
    }