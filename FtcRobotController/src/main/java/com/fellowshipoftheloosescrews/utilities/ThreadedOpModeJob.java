package com.fellowshipoftheloosescrews.utilities;

import com.fellowshipoftheloosescrews.utilities.ThreadedOpModeWrapper;

/**
 * Created by Thomas on 6/28/2015.
 */
public abstract class ThreadedOpModeJob implements Runnable {

    /**
     * Used just like start() from OpMode
     */
    public abstract void start();

    /**
     * Used just like loop() from OpMode
     */
    public abstract void loop();

    /**
     * Used just like stop() from OpMode
     */
    public abstract void stop();

    /**
     * The parent ThreadedOpModeWrapper object, used to get
     * normal OpMode functionality.
     */
    public ThreadedOpModeWrapper opMode;

    public boolean isRunning;

    /**
     * Sets the parent ThreadedOpModeWrapper
     * @param parentOpMode the parent OpMode. This only needs to be set
     *                     during the constructor of ThreadedOpModeWrapper
     */
    public void setParentOpMode(ThreadedOpModeWrapper parentOpMode)
    {
        opMode = parentOpMode;
    }

    public abstract void run();

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
