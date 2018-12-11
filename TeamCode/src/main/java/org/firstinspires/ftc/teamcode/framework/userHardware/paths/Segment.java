package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

public abstract class Segment {
    private boolean isRunning = false;
    private boolean isDone = false;

    protected final SegmentType type;

    public Segment(SegmentType type) {
        this.type = type;
    }

    public SegmentType getType() {
        return type;
    }

    public enum SegmentType {
        DRIVE,
        TURN
    }
}

