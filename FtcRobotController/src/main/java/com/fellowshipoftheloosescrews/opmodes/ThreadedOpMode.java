package com.fellowshipoftheloosescrews.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Thomas on 6/24/2015.
 *
 * A custom "linear" OpMode that adds another thread for
 * better autonomous programs
 */
public class ThreadedOpMode extends OpMode {

    private class WorkerJob implements Runnable {
        public WorkerJob(ThreadedOpMode parentOpMode) {

        }

        @Override
        public void run() {

        }
    }

    @Override
    public void start() {
        Thread workerThread = new Thread(new WorkerJob(this));
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
}
