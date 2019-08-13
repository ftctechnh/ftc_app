package org.firstinspires.ftc.teamcode.common.math;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MathUtil {
    public static final double EPSILON = 1e-6;

    public static double angleWrap(double angle) {
        double negTauToTau = angle % (Math.PI * 2);

        if (Math.abs(negTauToTau) > Math.PI) {
            negTauToTau -= Math.copySign(Math.PI * 2, negTauToTau);
        }
        return negTauToTau;
    }
    public static double clamp(double d) {
        return Math.min(Math.max(d, -1), 1);
    }

    public static double deadZone(double d, double thresh) {
        return (Math.abs(d) < thresh) ? 0 : d;
    }

    public static Pose relativeOdometryUpdate(Pose fieldPose, Pose robotPoseDelta) {
        double dtheta = robotPoseDelta.heading;
        double sineTerm, cosTerm;

        if (approxEquals(dtheta, 0)) {
            sineTerm = 1.0 - dtheta * dtheta / 6.0;
            cosTerm = dtheta / 2.0;
        } else {
            sineTerm = Math.sin(dtheta) / dtheta;
            cosTerm = (1 - Math.cos(dtheta)) / dtheta;
        }

        Point fieldPositionDelta = new Point(
                sineTerm * robotPoseDelta.x - cosTerm * robotPoseDelta.y,
                cosTerm * robotPoseDelta.x + sineTerm * robotPoseDelta.y
        );

        Pose fieldPoseDelta = new Pose(fieldPositionDelta.rotated(fieldPose.heading), robotPoseDelta.heading);

        return fieldPose.add(fieldPoseDelta);
    }

    public static boolean approxEquals(double d1, double d2) {
        if (Double.isInfinite(d1)) {
            // Infinity - infinity is NaN, so we need a special case
            return d1 == d2;
        } else {
            return Math.abs(d1 - d2) < EPSILON;
        }
    }

    public static Set<Point> lineSegmentCircleIntersection(Point ul1, Point ul2, Point o, double radius) {
        Point l1 = new Point(ul1.x - o.x, ul1.y - o.y);
        Point l2 = new Point(ul2.x - o.x, ul2.y - o.y);

        double d_x = l2.x - l1.x;
        double d_y = l2.y - l1.y;
        double d_r = Math.hypot(d_x, d_y);
        double determinant = l1.x * l2.y - l2.x * l1.y;
        double discriminant = Math.pow(radius, 2) * Math.pow(d_r, 2) - Math.pow(determinant, 2);

        List<Point> offsets = new LinkedList<>();
        if (MathUtil.approxEquals(discriminant, 0)) {
            offsets.add(new Point(0, 0));
        } else if (discriminant > 0) {
            double x_determinant = Math.signum(d_y) * d_x * Math.sqrt(discriminant);
            double y_determinant = Math.abs(d_y) * Math.sqrt(discriminant);
            offsets.add(new Point(x_determinant, y_determinant));
            offsets.add(new Point(-x_determinant, -y_determinant));
        }

        Set<Point> intersections = new TreeSet<>();
        for (Point offset : offsets) {
            intersections.add(new Point (
                (determinant * d_y + offset.x) / Math.pow(d_r, 2) + o.x,
                (-determinant * d_x + offset.y) / Math.pow(d_r, 2) + o.y
            ));
        }

        // Verify intersection within bounds of line segment
        intersections.removeIf(point ->
                (!between(ul1.x, ul2.x, point.x)) ||
                (!between(ul1.y, ul2.y, point.y))
        );

        return intersections;
    }

    public static boolean between(double r1, double r2, double val) {
        return val > Math.min(r1, r2) && val < Math.max(r1, r2);
    }
}

