package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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
    //returns elapssed time in MILLISECONDS
    double getElapsedTime() {
        return (getCurrentTime() - startTime)/1000000L;
    }
}