package com.fellowshipoftheloosescrews.opmodes;

import com.fellowshipoftheloosescrews.utilities.ThreadedOpModeJob;

/**
 * Created by FTC7123B on 6/28/2015.
 */
public class ThreadingTest extends ThreadedOpModeJob {
    @Override
    public void start() {

    }

    @Override
    public void loop() {
        opMode.telemetry.addData("Threading test", "It works!");
    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        while(isRunning())
        {
            System.out.println("Hello world!");
            sleep(1000);
        }
        System.out.println("Stopping");
    }
}
