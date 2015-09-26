package org.swerverobotics.library.internal;

import android.util.Log;

import org.swerverobotics.library.exceptions.RuntimeInterruptedException;
import org.swerverobotics.library.interfaces.IAction;

/**
 * SuicideWatch is a little utility class that monitors a thread for termination. When that
 * occurs, an action is invoked.
 */
public class DeathWatch
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    Thread                  threadWhichIsMonitored;
    IAction                 action;
    HandshakeThreadStarter  monitor;
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    public DeathWatch(Thread threadWhichIsMonitored, IAction action)
        {
        this.threadWhichIsMonitored = threadWhichIsMonitored;
        this.action                 = action;
        this.monitor                = new HandshakeThreadStarter("suicide watch", new Monitor());
        }

    //----------------------------------------------------------------------------------------------
    // Startup and shutdown
    //----------------------------------------------------------------------------------------------
    
    public synchronized void start()
        {
        monitor.start();
        }

    public synchronized void stop(int msWait)
        {
        monitor.stop(msWait);
        }
    public synchronized void stop()
        {
        stop(0);
        }

    private class Monitor implements IHandshakeable
        {
        @Override public void run(HandshakeThreadStarter starter)
            {
            starter.doHandshake();

            log("awaiting death...");
            try {
                threadWhichIsMonitored.join();
                log("...suicide");
                action.doAction();
                }
            catch (InterruptedException|RuntimeInterruptedException e)
                {
                log("...aborting");
                }
            }
        }

    static final String loggingTag = "SuicideWatch";

    private void log(String message)
        {
        Log.d(loggingTag, String.format("thread(%d): %s", Thread.currentThread().getId(), message));
        }
    
    }
