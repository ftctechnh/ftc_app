package org.swerverobotics.library.thunking;

import com.qualcomm.ftcrobotcontroller.*;
import junit.framework.Assert;
import org.swerverobotics.library.IAction;
import org.swerverobotics.library.exceptions.SwerveRuntimeException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThunkBase contains most of the functionality for thunking
 */
public abstract class ThunkBase implements IAction, IActionKeyed
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private SynchronousThreadContext context;
    public  int                      actionKey = nullActionKey;

    public static final int          nullActionKey = 0;
    static AtomicInteger             prevActionKey = new AtomicInteger(nullActionKey);
    
    public static int getNewActionKey()
        {
        return prevActionKey.incrementAndGet();
        }
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkBase()
        {
        this.context = SynchronousThreadContext.getThreadContext();
        }
    public ThunkBase(int actionKey)
        {
        this.context = SynchronousThreadContext.getThreadContext();
        this.actionKey = actionKey;
        }

    //----------------------------------------------------------------------------------------------
    // IActionKeyed
    //----------------------------------------------------------------------------------------------
    
    @Override public int getActionKey()
        {
        return this.actionKey;
        }
    
    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    public void doLoopThreadCore()
        {
        // Do what we came here to do
        this.actionOnLoopThread();

        // Tell all of our waiters that we are done
        synchronized (this)
            {
            this.notifyAll();
            }
        }

    public void doLoopThreadThunkCompletion()
        {
        // Also tell the synchronous thread on which we were created that we are done
        this.context.noteThunkCompletion(this);
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
        this.context.noteThunkDispatching(this);
        try
            {
            this.context.getThunker().executeOnLoopThread(this);
            }
        catch (Exception e)
            {
            // This shouldn't happen, as we shouldn't see any checked exceptions
            // since none have been declared. In any event, we note the failure
            // then do what we can.
            this.context.noteThunkDispatchFailure(this);
            throw SwerveRuntimeException.Wrap(e);
            }
        }
    }