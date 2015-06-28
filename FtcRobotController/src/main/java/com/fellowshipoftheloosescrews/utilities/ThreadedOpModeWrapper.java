package com.fellowshipoftheloosescrews.utilities;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Thomas on 6/24/2015.
 *
 * A custom "linear" OpMode that adds another thread for
 * better autonomous programming.
 */
public class ThreadedOpModeWrapper extends OpMode {

    private ThreadedOpModeJob opModeJob;

    public ThreadedOpModeWrapper(ThreadedOpModeJob job)
    {
        opModeJob = job;
        job.setParentOpMode(this);
    }

    public Thread jobThread;
    private boolean isFirstLoop;

    @Override
    public void start() {
        jobThread = new Thread(opModeJob);
        opModeJob.start();
        isFirstLoop = true;
    }

    @Override
    public void loop() {
        startJobThread();
        opModeJob.loop();
    }

    private void startJobThread()
    {
        if(isFirstLoop)
        {
            jobThread.start();
            opModeJob.setIsRunning(true);
            isFirstLoop = false;
        }
    }

    @Override
    public void stop() {
        opModeJob.setIsRunning(false);
        opModeJob.stop();
    }
}
