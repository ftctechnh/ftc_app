package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

public class TurnSegment extends Segment {

    private final double angle;
    private final double speed;
    private final double error;
    private final int period;


    public TurnSegment(double angle, double speed, double error, int period) {
        super(SegmentType.TURN);
        this.angle = angle;
        this.speed = speed;
        this.error = error;
        this.period = period;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpeed() {
        return speed;
    }

    public double getError() {
        return error;
    }

    public int getPeriod() {
        return period;
    }
}
