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

    @Override
    public void start() {
        opModeJob.start();
    }

    @Override
    public void loop() {
        opModeJob.loop();
    }

    @Override
    public void stop() {
        opModeJob.setIsRunning(false);
        opModeJob.stop();
    }
}
