package com.fellowshipoftheloosescrews.utilities;

/**
 * Created by Thomas on 6/28/2015.
 * <br/><br/>
 * This is part of the ThreadedOpMode framework, this is the actual job.
 * Note that this doesn't extend OpMode, so to make it work you have to give it to a
 * ThreadedOpModeWrapper. To register it with the OpModeRegister, use a line like this:
 *
 * <br/><br/>manager.register(new ThreadedOpModeWrapper(new YOUR_OPMODE_CLASS()));<br/><br/>
 *
 * There are several examples in the com.fellowshipoftheloosescrews.opmodes package
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

    private boolean isRunning;

    /**
     * Fill this method with your custom thread job. This is run in a concurrent thread,
     * independent of the rest of your OpMode. When the driver presses the stop button
     * on the controller, isRunning will be set to false. Use this to make sure you stop
     * when you should.
     */
    public abstract void run();

    /**
     * Sets the parent ThreadedOpModeWrapper
     * @param parentOpMode the parent OpMode. This only needs to be set
     *                     during the constructor of ThreadedOpModeWrapper
     */
    public void setParentOpMode(ThreadedOpModeWrapper parentOpMode)
    {
        opMode = parentOpMode;
    }

    /**
     * This is used to check if the OpMode thread is being told to stop.
     * @param isRunning true after start(), but false after stop()
     */
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }


    /**
     * If the current job is running. This is set to false when the wrapper
     * finishes. Make sure you check this if you're using an infinite loop!
     * <br/><br/>
     * For example, if you want to keep adding one to a variable in a loop,
     * you could do something like this:
     *
     * while(isRunning())<br/>
     * {<br/>
     *     x++;<br/>
     * }<br/>
     *<br/>
     * Or just make sure that you check if it's false in your code at various points<br/><br/>
     * while(true)          <br/>
     * {                    <br/>
     *     x++;             <br/>
     *     if(!isRunning()) <br/>
     *         return;      <br/>
     * }                    <br/>
     *
     * @return If the thread can continue running.
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     * Sleeps for a number of milliseconds, this is just a wrapper for Thread.sleep(milliseconds).
     * Use this to make the ThreadedOpModes more readable.
     * @param milliseconds the number of milliseconds to sleep
     */
    protected void sleep(int milliseconds)
    {
        sleep(milliseconds, 0);
    }

    /**
     * same as sleep(milliseconds), but includes the option for nanoseconds.
     * @param milliseconds the number of milliseconds to sleep
     * @param nanoSeconds the number of nanoseconds to sleep
     */
    protected void sleep(int milliseconds, int nanoSeconds)
    {
        try {
            Thread.sleep(milliseconds, nanoSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
