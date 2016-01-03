package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This class provides useful static methods for
 * various drive-related maths.
 */
public class DriveMath {

    public static double[] vector(double angle, double magnitude) {
        //Generate a 2D vector from polar coordinates.
        double result[] = new double[2]; //Set up a 2-item array of doubles to hold result.
        result[0] = Math.sin(Math.toRadians(angle)) * magnitude;
        result[1] = Math.cos(Math.toRadians(angle)) * magnitude;
        return result;
    }

    public static double power(double number, double exponent) {
        //Perform a power while preserving the sign.
        double absNumber = Math.abs(number);
        double result = Math.pow(absNumber,exponent);
        result = (number<0) ? -result : result;
        return result;
    }

    public static double limit(double number, double min, double max) {
        //Limit a number to a range.
        if (number<min) return min;
        if (number>max) return max;
        return number;
    }

    public static double threeWay(boolean neg,boolean pos) {
        //Performs a 'threeWay' - fwd/bckwd/stop - op on two booleans.
        if (neg) return -1;
        if (pos) return 1;
        return 0;
    }
}
