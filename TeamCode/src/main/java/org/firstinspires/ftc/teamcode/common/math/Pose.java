package org.firstinspires.ftc.teamcode.common.math;

public class Pose extends Point {
    public double heading;

    public Pose(double x, double y, double heading) {
        super(x, y);
        this.heading = heading;
    }

    public Pose(Point p, double heading) {
        this(p.x, p.y, heading);
    }

    public Pose add(Pose p2) {
        return new Pose(x + p2.x, y + p2.y, heading + p2.heading);
    }
    public Pose minus(Pose p2) {
        return new Pose(x - p2.x, y - p2.y, heading - p2.heading);
    }
    public Pose scale(double d) {return new Pose(x * d, y * d, heading * d);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pose pose = (Pose) o;
        return MathUtil.approxEquals(pose.heading, heading);
    }
}
