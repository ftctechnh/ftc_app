package org.firstinspires.ftc.teamcode.misc;

public class FtcUtils {
    public static final double threshold = .15;

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
        if (Math.abs(d) > threshold)
            return o;
        else
            return 0.0;
    }

    public static double arduinoScale(double val, double min1, double max1, double min2, double max2) {
        return val / (max1 - min1) * (max2 - min2);
    }
}
