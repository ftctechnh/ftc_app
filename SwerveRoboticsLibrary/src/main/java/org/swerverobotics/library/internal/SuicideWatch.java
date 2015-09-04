package org.swerverobotics.library.internal;

import android.util.Log;

import org.swerverobotics.library.exceptions.RuntimeInterruptedException;
import org.swerverobotics.library.exceptions.SwerveRuntimeException;

import java.util.concurrent.*;

/**
 * SuicideWatch is a little utility class that monitors a thread for termination. When that
 * occurs, a second thread is interrupted.
 */
public class SuicideWatch
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    Thread          monitoredThread;
    Thread          terminalThread;
    ExecutorService monitor;
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    public SuicideWatch(Thread monitoredThread, Thread terminalThread)
        {
        this.monitoredThread = monitoredThread;
        this.terminalThread  = terminalThread;
        this.monitor         = null; 
        log("constructed");
        }

    //----------------------------------------------------------------------------------------------
    // Startup and shutdown
    //----------------------------------------------------------------------------------------------
    
    public synchronized void arm() throws InterruptedException
        {
        this.disarm();
        
        this.monitor = Executors.newSingleThreadExecutor();
        this.monitor.execute(new Monitor());
        }
    
    public synchronized void disarm() throws InterruptedException
        {
        if (this.monitor != null)
            {
            log("disarming...");
            ExecutorService monitor = this.monitor;
            this.monitor = null;
            
            monitor.shutdownNow();
            monitor.awaitTermination(1000, TimeUnit.DAYS);
            log("...disarmed");
            }
        }

    private class Monitor implements Runnable
        {
        @Override public void run()
            {
            log("awaiting death...");
            try {
                monitoredThread.join();
                log("...suicide");
                terminalThread.interrupt();
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
