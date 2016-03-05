package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ThreadPool;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.IOpModeLoopCounter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple little class that counts loop() calls for non-loop() opmodes.
 * Don't forget to close() the counter when you're done with it
 */
public class OpModeLoopCounter implements IOpModeLoopCounter
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    final AtomicInteger       linearCount;
    final LinearOpMode        linearOpMode;
    final SynchronousOpMode   synchronousOpMode;
    final ExecutorService     executor;
    volatile boolean          stopRequested;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public OpModeLoopCounter(OpMode opMode)
        {
        this.synchronousOpMode = opMode instanceof SynchronousOpMode ? ((SynchronousOpMode)opMode) : null;
        this.linearOpMode      = opMode instanceof LinearOpMode      ? ((LinearOpMode)opMode)      : null;

        if (this.synchronousOpMode==null && this.linearOpMode==null)
            throw new IllegalArgumentException("OpModeLoopCounter can only be use with Linear and Synchronous OpModes");

        this.linearCount    = new AtomicInteger(0);
        this.stopRequested  = false;
        this.executor       = ThreadPool.newSingleThreadExecutor();

        if (this.linearOpMode != null)
            {
            this.executor.execute(new Runnable() { @Override public void run()
                {
                while (!stopRequested && !Thread.currentThread().isInterrupted())
                    {
                    try {
                        synchronized (linearOpMode)
                            {
                            linearOpMode.wait(); // See LinearOpMode#loop()
                            }
                        }
                    catch (InterruptedException e)
                        {
                        break;
                        }
                    linearCount.incrementAndGet();
                    }
                }});
            }
        }

    //----------------------------------------------------------------------------------------------
    // IOpModeLoopCounter
    //----------------------------------------------------------------------------------------------

    @Override public int getLoopCount()
        {
        if (this.synchronousOpMode != null)
            return this.synchronousOpMode.getLoopCount();
        else
            return this.linearCount.get();
        }

    @Override public synchronized void close()
        {
        this.stopRequested = true;
        this.executor.shutdownNow();
        ThreadPool.awaitTerminationOrExitApplication(this.executor, 10, TimeUnit.SECONDS, "loop counter", "internal error");
        }
    }
