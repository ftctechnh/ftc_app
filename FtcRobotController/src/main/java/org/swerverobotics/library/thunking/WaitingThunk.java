package org.swerverobotics.library.thunking;

/**
 * Thunks derived from WaitingThunk queue up their work, then synchronously wait
 * for that work to complete before dispatch() returns.
 */
public abstract class WaitingThunk extends NonwaitingThunk
    {
    @Override public void dispatch() throws InterruptedException
        {
        super.dispatch();

        synchronized (this)
            {
            this.wait();
            }
        }
    }
