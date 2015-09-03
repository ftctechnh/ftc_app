package org.swerverobotics.library.internal;

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
            ExecutorService monitor = this.monitor;
            this.monitor = null;
            
            monitor.shutdownNow();
            monitor.awaitTermination(1000, TimeUnit.DAYS);
            }
        }

    private class Monitor implements Runnable
        {
        @Override public void run()
            {
            try {
                monitoredThread.join();
                terminalThread.interrupt();
                }
            catch (InterruptedException|RuntimeInterruptedException e)
                {
                }
            }
        }
    
    }
