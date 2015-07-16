package org.swerverobotics.library;

/**
 * Thunks derived from WaitingThunk queue up their work, then synchronously wait
 * for that work to complete before dispatch() returns.
 */
public abstract class WaitingThunk extends ThunkBase
    {
    @Override public void dispatch()
        {
        super.dispatch();

        try
            {
            synchronized (this)
                {
                this.wait();
                }
            }
        catch (InterruptedException e)
            {
            Thread.currentThread().interrupt();
            }
        }
    }
