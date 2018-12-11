package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

public class DriveSegment extends Segment {

    private final double distance;
    private final double speed;

    public DriveSegment(double distance, double speed) {
        super(SegmentType.DRIVE);
        this.distance = distance;
        this.speed = speed;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }
}

