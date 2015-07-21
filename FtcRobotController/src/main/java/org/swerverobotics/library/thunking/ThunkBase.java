package org.swerverobotics.library.thunking;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;

/**
 * ThunkBase contains most of the functionality for thunking
 */
public abstract class ThunkBase implements IThunk
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    ThreadThunkContext context;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkBase()
        {
        this.context = ThreadThunkContext.getThreadContext();
        }

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /**
     * Executed on the loop() thread, doThunk() is called to carry out the work of the thunk
     */
    public void doThunk()
        {
        // Do what we came here to do
        this.actionOnLoopThread();

        // Tell all of our waiters that we are done
        synchronized (this)
            {
            this.notifyAll();
            }

        // Also tell the thread on which we were created that we are done
        this.context.noteThunkCompletion(this);
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