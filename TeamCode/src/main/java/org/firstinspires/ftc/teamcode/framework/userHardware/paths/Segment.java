package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

public abstract class Segment {
    private boolean isRunning = false, isDone = false;

    private int number;

    private final String name;

    private final SegmentType type;

    public Segment(String name, SegmentType type) {
        this.name = name;
        this.type = type;
    }

    protected void setNumber(int number) {
        this.number = number;
    }

    protected int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public SegmentType getType() {
        return type;
    }

    public enum SegmentType {
        DRIVE,
        TURN
    }

    protected void start() {
        isRunning = true;
        isDone = false;
    }

    protected void stop() {
        isRunning = false;
        isDone = true;
    }

    protected void pause() {
        isRunning = false;
    }

    protected void resume() {
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isDone() {
        return isDone;
    }
}

