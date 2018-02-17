package org.firstinspires.ftc.teamcode.general;

/**
 * Created by Derek on 2/17/2018.
 */

public class Timer {

    private long start;

    public void start() {
        start = System.nanoTime();
    }

    public long getTime() {
        return (long) ((System.nanoTime() - start)  / 1000000000.0);
    }
}
