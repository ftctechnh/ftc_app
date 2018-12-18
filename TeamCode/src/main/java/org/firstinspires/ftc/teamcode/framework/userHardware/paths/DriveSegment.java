package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

public class DriveSegment extends Segment {

    private final double distance, speed, angle;

    private boolean hasAngle;

    public DriveSegment(String name, double distance, double speed) {
        this(name, distance, speed, 0);
        hasAngle = false;
    }

    public DriveSegment(String name, double distance, double speed, double angle) {
        super(name, SegmentType.DRIVE);
        hasAngle = true;
        this.distance = distance;
        this.speed = speed;
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public Double getAngle(){
        if(!hasAngle) return null;
        return angle;
    }
}

