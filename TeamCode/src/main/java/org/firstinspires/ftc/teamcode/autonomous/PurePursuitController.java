package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.common.math.Line;
import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;

public class PurePursuitController {

    final static double PREFERRED_ANGLE = 0;
    final static double DECELERATION_RATE = Math.toRadians(30);

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

    public static double pointPoseMinAngle(Pose start, Point target) {
        double targetAngle = Math.atan2(target.y - start.y, target.x - start.x);
        return MathUtil.angleWrap(targetAngle - start.heading);

    }

    // Our goal - determine a ciruclar arc that goes from our current robot position and heading
    // to our target robot position and heading. Once we know the radius of that circle, we can
    // use the track width to calculate the power difference ratio
    public static SixWheelPowers goToPosition(SixWheelHardware robot, Point target, double speedFactor) {
        // To find the radius, we first need to find the center. To do this, we'll find the
        // intersection of two lines - the perpendicular bisector of the start and endpoint,
        // and the line perpendicular to robot's current heading passing through robot's current
        // position

        Point center = arcCenterFromStartTangentPointAndEndpoint(robot.localizer.pose(), target);
        double radius = Line.distance(robot.localizer.pos(), center);

        // Determine which direction we need to turn

        return null;
    }
}
