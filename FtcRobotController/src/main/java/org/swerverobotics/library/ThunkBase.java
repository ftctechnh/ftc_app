package org.swerverobotics.library;

/**
 * ThunkBase contains most of the functionality for thunking
 */
public abstract class ThunkBase implements IThunk
    {
    ThreadThunkContext context;

    public ThunkBase()
        {
        this.context = ThreadThunkContext.getThreadContext();
        }

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
     */
    public void dispatch()
        {
        this.context.noteThunkDispatching(this);
        try
            {
            this.context.getThunker().executeOnLoopThread(this);
            }
        catch (Exception e)
            {
            this.context.noteThunkDispatchFailure(this);
            throw e;
            }
        }
    }