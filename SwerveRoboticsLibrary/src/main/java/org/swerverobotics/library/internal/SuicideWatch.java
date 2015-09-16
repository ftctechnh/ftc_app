package org.swerverobotics.library.internal;

import android.util.Log;

import org.swerverobotics.library.exceptions.RuntimeInterruptedException;

/**
 * SuicideWatch is a little utility class that monitors a thread for termination. When that
 * occurs, a second thread is interrupted.
 */
public class SuicideWatch
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    Thread                  threadWhichIsMonitored;
    HandshakeThreadStarter  threadToBeInterrupted;
    HandshakeThreadStarter  monitor;
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    public SuicideWatch(Thread threadWhichIsMonitored, HandshakeThreadStarter threadToBeInterrupted)
        {
        this.threadWhichIsMonitored = threadWhichIsMonitored;
        this.threadToBeInterrupted  = threadToBeInterrupted;
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
            starter.handshake();

            log("awaiting death...");
            try {
                threadWhichIsMonitored.join();
                log("...suicide");
                threadToBeInterrupted.stop();
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
