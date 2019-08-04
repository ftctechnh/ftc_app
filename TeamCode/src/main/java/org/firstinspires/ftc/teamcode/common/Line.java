package org.firstinspires.ftc.teamcode.common;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Line {
    double slope;
    double intercept;

    public Line(Point p1, Point p2) {
        this(
                p1,
                (p2.y - p1.y) / (p2.x - p1.x)
        );
    }

    public Line(Point point, double slope) {
        this(slope, point.y - point.x * slope);
    }

    public Line(double slope, double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    public double perpendicularSlope() {
        return -1.0 / slope;
    }

    public double evaluate(double x) {
        return x * slope + intercept;
    }

    public Point intersect(Line l2) {
        if (this.slope == l2.slope) {
            throw new IllegalArgumentException("Parallel lines do not intersect!");
        }
        double xIntersect = (l2.intercept - this.intercept) / (this.slope - l2.slope);
        return new Point(xIntersect, this.evaluate(xIntersect));
    }

    public static Point midpoint(Point p1, Point p2) {
        return new Point(p1.x + p2.x / 2, p1.y + p2.y / 2);
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
