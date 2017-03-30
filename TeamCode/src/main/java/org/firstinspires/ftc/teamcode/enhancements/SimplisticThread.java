package org.firstinspires.ftc.teamcode.enhancements;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

/**
 * This thread code has been adapted from the BatteryHandler class as part of the FTC SDK.
 *
 * Took Mak a while to finish this code so be nice to it pweese
 */

public abstract class SimplisticThread
{
    //Required so that we can stop all tasks (otherwise are conserved through the program).
    private static ArrayList<SimplisticThread> activeThreads = null;
    public static void killAllThreads()
    {
        //Kill all currently running threads.
        if (activeThreads.size () >= 0)
            for (SimplisticThread thread : activeThreads)
                thread.stop ();

        activeThreads = null;
    }

    private static Handler threadHandler;
    public static void initializeThreadCreator (Context providedContext)
    {
        if (threadHandler == null)
            threadHandler = new Handler (providedContext.getMainLooper ());
    }

    /**
     * Although ugly, running is a failsafe for when the thread is being posted and stopped at the same time, and also is
     * useful for debugging and determining whether certain threads are active.
     */
    private boolean running = false;
    public SimplisticThread (long delay)
    {
        if (activeThreads == null)
            activeThreads = new ArrayList<> ();

        activeThreads.add (this);

        this.delay = delay;

        start ();
    }

    private long delay;
    private Runnable mainRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (running)
            {
                actionPerUpdate ();
                threadHandler.postDelayed (mainRunnable, delay);
            }
        }
    };

    public boolean isRunning()
    {
        return running;
    }

    public void start ()
    {
        running = true;
        threadHandler.post(mainRunnable);
    }

    public void stop ()
    {
        running = false;
        threadHandler.removeCallbacks (mainRunnable);
    }

    //Manually overridden.
    public abstract void actionPerUpdate();
}
