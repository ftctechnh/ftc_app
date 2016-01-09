package com.qualcomm.ftcrobotcontroller.hardware;

public class Power {

    public static final double NORMAL_SPEED = 0.75;
    public static final double FULL_SPEED = 1.0;
    public static final double SLOW_TURN = 0.15;
    public static final double FULL_STOP = 0.0;

    public static double speedCurve(double x) {
        return (0.598958 * Math.pow(x, 3)) - (4.43184 * Math.pow(10, -16) * Math.pow(x, 2)) + (0.201042 * x);
    }

    public static double powerClamp(double x) {
        if (x > 1) {
            return 1;
        }
        if (x < 0) {
            return 0;
        }
        return x;
    }

}
