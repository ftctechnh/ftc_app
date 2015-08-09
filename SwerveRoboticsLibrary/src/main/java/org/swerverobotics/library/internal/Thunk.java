package org.swerverobotics.library.internal;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.swerverobotics.library.interfaces.*;

/**
 * ThunkBase contains most of the code for thunking a call from a synchronous thread to the loop() thread
 */
public abstract class Thunk implements IAction, IActionKeyed
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private SynchronousThreadContext context;
    public  List<Integer>            actionKeys;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Thunk()
        {
        this.context    = SynchronousThreadContext.getThreadContext();
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
     * Executed on the loop() thread, doAction() is called to carry out the work of the thunk
     */
    public void doAction()
        {
        // Do what we came here to do
        this.actionOnLoopThread();

        // Tell all those waiting on the completion of this thunk that we are done
        synchronized (this)
            {
            this.notifyAll();
            }
        }

    /**
     * Derived classes should implement actionOnLoopThread() to actually carry out work.
     */
    protected abstract void actionOnLoopThread();

    /**
     * Dispatch this thunk over to the loop thread.
     */
    public void dispatch() throws InterruptedException
        {
        SynchronousThreadContext.assertSynchronousThread();
        this.context.getThunker().executeOnLoopThread(this);
        }
    }