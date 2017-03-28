package org.firstinspires.ftc.teamcode.enhancements;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

/**
 * This thread code has been adapted from the BatteryHandler class as part of the FTC SDK.
 *
 * Took Mak a while to finish this code so be nice to it pweese
 */

public abstract class EZThread
{
    //Required so that we can stop all tasks (otherwise are conserved through the program).
    private static ArrayList<EZThread> activeThreads = new ArrayList<> ();
    public static void killAllThreads()
    {
        //Kill all currently running threads.
        if (activeThreads.size () >= 0)
            for (EZThread thread : activeThreads)
                thread.stop ();
    }

    private static Context appContext;
    public static void initializeThreadCreator (Context providedContext)
    {
        appContext = providedContext;
    }

    private long delay;
    protected Handler mainHandler;
    Runnable mainRunnable = new Runnable()
    {
        public void run()
        {
            actionPerUpdate ();
            mainHandler.postDelayed(mainRunnable, delay);
        }
    };

    public EZThread (long delay)
    {
        activeThreads.add (this);

        this.delay = delay;
        mainHandler = new Handler (appContext.getMainLooper ());

        resume ();
    }

    public void resume ()
    {
        mainHandler.postDelayed(mainRunnable, 10);
    }

    public void stop ()
    {
        mainHandler.removeCallbacks(mainRunnable);
    }

    public abstract void actionPerUpdate();
}
