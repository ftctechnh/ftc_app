package org.firstinspires.ftc.teamcode.common;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

public class MathUtil {
    private static final double EPSILON = 1e-6;

    public static double angleWrap(double angle) {
        double negTauToTau = angle % (Math.PI * 2);
        if (Math.abs(negTauToTau) > Math.PI) {
            negTauToTau += -Math.copySign(negTauToTau, Math.PI * 2);
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
        return Math.abs(d1 - d2) < EPSILON;
    }
}

