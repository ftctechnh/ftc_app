package org.firstinspires.ftc.teamcode.misc;
import org.firstinspires.ftc.teamcode.misc.RobotConstants;

public class FtcUtils {
    public static double scale(double d, double l, double r) {
        if (d < l) return l;
        if (d > r) return r;
        return d;
    }

    public static double servoScale(double d) {
        return scale(d, 0, 1);
    }

    public static double motorScale(double d) {
        return scale(d, -1, 1);
    }

    public static double sign(double d) {
        if (d >= 0.0) return 1.0;
        else return -1.0;
    }
    public static double threshold(double d) {
        return threshold(d, d);
    }

    public static double threshold(double d, double o) {
        if (Math.abs(d) > RobotConstants.threshold)
            return o;
        else
            return 0.0;
    }

    public static double roundTwoDecimalPlaces(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    public static double abs(double a) {
        return (a <= 0.0) ? -a : a;
    }

    public static double normalizeDegrees(double degrees) {
        while (degrees >= 180.0) degrees -= 360.0;
        while (degrees < -180.0) degrees += 360.0;
        return degrees;
    }
}
