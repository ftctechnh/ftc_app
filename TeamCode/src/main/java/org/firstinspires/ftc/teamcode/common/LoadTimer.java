package org.firstinspires.ftc.teamcode.common;

public class LoadTimer {
    private long t0;
    private long t1;

    public LoadTimer() {
        t0 = System.nanoTime();
    }

    public LoadTimer stop() {
        t1 = System.nanoTime();
        return this;
    }

    public long nanos() {return t1 - t0;}
    public int micros() {return (int) ((t1 - t0) / 1000);}
    public int millis() {return (int) ((t1 - t0) / 1000000);}
}
