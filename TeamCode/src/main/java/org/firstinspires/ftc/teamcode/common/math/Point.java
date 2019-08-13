package org.firstinspires.ftc.teamcode.common.math;

public class Point implements Comparable {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point rotated(double angle) {
        double newX = x * Math.cos(angle) - y * Math.sin(angle);
        double newY = x * Math.sin(angle) + y * Math.cos(angle);
        return new Point(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return MathUtil.approxEquals(point.x, x) &&
                MathUtil.approxEquals(point.y, y);
    }

    @Override
    public int hashCode() {
        return Double.valueOf(x).hashCode() ^ Double.valueOf(y).hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        Point p = (Point) o;
        return Integer.compare(this.hashCode(), o.hashCode());
    }
}
