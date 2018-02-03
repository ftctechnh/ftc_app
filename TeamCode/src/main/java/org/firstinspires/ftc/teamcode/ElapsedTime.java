package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/30/2017.
 */


public class ElapsedTime {
    private long startTime;
    private long currentTime;
    ElapsedTime () {}
    void start() {
        this.startTime = System.nanoTime();
    }
    private long getCurrentTime() {
        currentTime = System.nanoTime();
        return currentTime;
    }
    //returns elapsed time in MILLISECONDS
    double getElapsedTime() {
        return (getCurrentTime() - startTime)/1000000L;
    }
    void resetTime() {
        long time = System.nanoTime();
        this.startTime = time;
        this.currentTime = time;
    }
}