package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Point;

import java.util.Iterator;
import java.util.Set;

public class PurePursuitPath {
}

abstract class PurePursuitPathComponent {
    public double lookAhead;

    public abstract Point intersect(Point robotPos);
}

class Segment extends PurePursuitPathComponent {
    public Point start;
    public Point end;

    public Segment(Point start, Point end, double lookAhead) {
        this.start = start;
        this.end = end;
        this.lookAhead = lookAhead;
    }

    @Override
    public Point intersect(Point robotPos) {
        Set<Point> intersections = MathUtil.lineSegmentCircleIntersection(start, end, robotPos, lookAhead);

        if (intersections.size() == 0) {
            return null;
        } else if (intersections.size() == 2) {
            Point[] arr = (Point[]) intersections.toArray();

            if (arr[0].distance(robotPos) >= arr[1].distance(robotPos)) {
                intersections.remove(arr[0]);
            } else {
                intersections.remove(arr[1]);
            }
        }
        return intersections.iterator().next();
    }
}

class PointStop extends PurePursuitPathComponent {
    public Point p;

    public PointStop(Point p, double lookAhead) {
        this.p = p;
        this.lookAhead = lookAhead;
    }

    @Override
    public Point intersect(Point robotPos) {
        if (p.distance(robotPos) < lookAhead) {
            return p;
        } else {
            return null;
        }
    }
}