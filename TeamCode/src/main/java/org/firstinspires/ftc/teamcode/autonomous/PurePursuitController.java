package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.util.Range;

import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.common.Line;
import org.firstinspires.ftc.teamcode.common.MathUtil;
import org.firstinspires.ftc.teamcode.common.Point;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;

public class PurePursuitController {

    final static double PREFERRED_ANGLE = 0;
    final static double DECELERATION_RATE = Math.toRadians(30);


    // Our goal - determine a ciruclar arc that goes from our current robot position and heading
    // to our target robot position and heading. Once we know the radius of that circle,
    public static SixWheelPowers goToPosition(SixWheelHardware robot, Point target, double speedFactor) {
        // To find the radius, we first need to find the center. To do this, we'll find the
        // intersection of two lines - the perpendicular bisector of the start and endpoint,
        // and the line perpendicular to robot's current heading passing through robot's current
        // position

        Line minDistance = new Line(robot.localizer.pos(), target);
        Line minDistPerpBisector = new Line(Line.midpoint(robot.localizer.pos(), target),
                minDistance.perpendicularSlope());

        Line perpHeading = new Line(robot.localizer.pos(), Math.tan(robot.localizer.h() + Math.PI /2));

        Point center = minDistPerpBisector.intersect(perpHeading);
        double radius = Line.distance(robot.localizer.pos(), center);


    }
}
