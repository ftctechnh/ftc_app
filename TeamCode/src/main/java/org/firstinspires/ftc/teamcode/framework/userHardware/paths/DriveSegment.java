package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

public class DriveSegment extends Segment {

    private final double distance, speed, angle;
    private final int error;

    private boolean hasAngle;

    public DriveSegment(String name, double distance, double speed, int error) {
        this(name, distance, speed, error, 0);
        hasAngle = false;
    }

    public DriveSegment(String name, double distance, double speed, int error, double angle) {
        super(name, SegmentType.DRIVE);
        hasAngle = true;
        this.distance = distance;
        this.speed = speed;
        this.error = error;
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public int getError() {
        return error;
    }

    public Double getAngle() {
        if (!hasAngle) return null;
        return angle;
    }
}

