package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.common.math.Line;
import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;

import java.util.ArrayList;

public class PurePursuitController {

    // Start slowing down when we're 24 inches away
    public static double START_DECELERATION_THRESH = 24;

    // To find the radius, we first need to find the center. To do this, we'll find the
    // intersection of two lines - the perpendicular bisector of the start and endpoint,
    // and the line perpendicular to robot's current heading passing through robot's current
    // position
    public static Point arcCenterFromStartTangentPointAndEndpoint(Pose start, Point end) {
        Line minDistance = new Line(start, end);
        Line minDistPerpBisector = new Line(Line.midpoint(start, end),
                minDistance.perpendicularSlope());

        Line perpHeading = new Line(start, Math.tan(start.heading + Math.PI /2));
        return minDistPerpBisector.intersect(perpHeading);
    }

    // Standard counter-clockwise angle conventions apply
    public static double pointPoseMinAngle(Pose start, Point target) {
        double targetAngle = Math.atan2(target.y - start.y, target.x - start.x);
        return MathUtil.angleWrap(targetAngle - start.heading);
    }

    // Note - this algorithm *only* moves the robot forward. That's fine for now, but is
    // a pretty big limitation in the long run
    public static SixWheelPowers goToPosition(SixWheelHardware robot, Point target) {
        Point center = arcCenterFromStartTangentPointAndEndpoint(robot.localizer.pose(), target);
        double radius = Line.distance(robot.localizer.pos(), center);
        double angle = pointPoseMinAngle(robot.localizer.pose(), target);

        // Compute how much of torque budget to expend from distance to target
        double distance = radius * angle;
        double greater_pow = Math.max(distance / START_DECELERATION_THRESH, 1);

        // Now compute turn powers
        double m_c = 2 * radius / SixWheelHardware.TRACK_WIDTH;
        double lesser_pow = greater_pow * (m_c - 1) / (m_c + 1);

        if (angle >= 0) { // If we're turning counter-clockwise
            return new SixWheelPowers(lesser_pow, greater_pow);
        } else { // For clockwise, bigger power is on the left
            return new SixWheelPowers(greater_pow, lesser_pow);
        }
    }

}
