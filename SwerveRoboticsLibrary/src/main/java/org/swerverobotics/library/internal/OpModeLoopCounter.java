package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.IOpModeLoopCounter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple little class that counts loop() calls in non-synchronous opmodes
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
        this.executor       = Executors.newSingleThreadExecutor();

        if (this.linearOpMode != null)
            {
            this.executor.submit(new Runnable() {
                @Override public void run()
                    {
                    while (!stopRequested)
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
                    }
                });
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
        this.executor.shutdown();
        }
    }
