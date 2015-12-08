package org.swerverobotics.library.internal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A class that implements a gating semantic similar to a Windows manual reset event
 */
public class ManualResetEvent
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private volatile CountDownLatch event;
    private final    Object         lock = new Object();

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ManualResetEvent(boolean signalled)
        {
        if (signalled)
            {
            event = new CountDownLatch(0);
            }
        else
            {
            event = new CountDownLatch(1);
            }
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public void set()
        {
        event.countDown();
        }

    public void reset()
        {
        synchronized (lock)
            {
            if (event.getCount() == 0)
                {
                event = new CountDownLatch(1);
                }
            }
        }

    public void waitOne() throws InterruptedException
        {
        event.await();
        }

    public boolean waitOne(int timeout, TimeUnit unit) throws InterruptedException
        {
        return event.await(timeout, unit);
        }

    public boolean isSignalled()
        {
        return event.getCount() == 0;
        }
    }
