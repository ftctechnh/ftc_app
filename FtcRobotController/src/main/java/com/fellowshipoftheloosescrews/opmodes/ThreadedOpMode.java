package com.fellowshipoftheloosescrews.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Thomas on 6/24/2015.
 *
 * A custom "linear" OpMode that adds another thread for
 * better autonomous programming.
 */
public abstract class ThreadedOpMode extends OpMode implements Runnable {

    protected boolean isRunning = false;

    /**
     * Called when the OpMode calls start()
     */
    public abstract void onStart();

    /**
     * Implement this method to make your custom thread. This will be called in the new
     * Thread following the start of loop()
     */
    public abstract void run();

    /**
     * Called when the OpMode calls loop(). Use this to update motors or telemetry data
     * from run()
     */
    public abstract void onLoop();

    /**
     * Called after the worker thread is terminated. Make sure to clean up your code and shut
     * down communications with microcontrollers if we need to.
     */
    public abstract void onStop();

    private Thread workerThread;

    @Override
    public void start() {
        workerThread = new Thread(this);
        workerThread.setName("Worker Thread");
        onStart();
    }

    private boolean isFirstLoop = true;

    @Override
    public void loop() {
        if(isFirstLoop)
        {
            isRunning = true;
            workerThread.start();
            isFirstLoop = false;
        }
        onLoop();
    }

    @Override
    public void stop() {
        isRunning = false;
        onStop();
    }
}
