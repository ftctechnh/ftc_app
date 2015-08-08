package org.swerverobotics.library.internal;

import junit.framework.Assert;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.*;

/**
 * ThunkBase contains most of the code for thunking a call from a synchronous thread to the loop() thread
 */
public abstract class Thunk implements IAction, IActionKeyed
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private SynchronousThreadContext context;
    public  ArrayList<Integer>       actionKeys;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Thunk()
        {
        this.context    = SynchronousThreadContext.getThreadContext();
        this.actionKeys = new ArrayList<Integer>(3);
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
    
    @Override public ArrayList<Integer> getActionKeys()
        {
        return this.actionKeys;
        }
    
    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    public void doLoopThreadCore()
        {
        // Do what we came here to do
        this.actionOnLoopThread();

        // Tell all those waiting on the completion of this thunk that we are done
        synchronized (this)
            {
            this.notifyAll();
            }
        }

    public void doLoopThreadThunkCompletion()
        {
        }

    /**
     * Executed on the loop() thread, doAction() is called to carry out the work of the thunk
     */
    public void doAction()
        {
        this.doLoopThreadCore();
        this.doLoopThreadThunkCompletion();
        }

    /**
     * Derived classes should implement actionOnLoopThread() to actually carry out work.
     */
    protected abstract void actionOnLoopThread();

    /**
     * Dispatch this thunk over to the loop thread.
     *
     * If exceptions are thrown, then tell the rest of the infrastructure that we
     * won't in fact be later calling them back with a noteThunkCompletion
     */
    public void dispatch() throws InterruptedException
        {
        if (BuildConfig.DEBUG) Assert.assertEquals(true, SynchronousThreadContext.isSynchronousThread());
        
        // It's a synchronous thread. Head over to the loop() thread to do the work.
        try
            {
            this.context.getThunker().executeOnLoopThread(this);
            }
        catch (Exception e)
            {
            // This shouldn't happen, as we shouldn't see any checked exceptions
            // since none have been declared. In any event, we note the failure
            // then do what we can.
            throw SwerveRuntimeException.wrap(e);
            }
        }
    }