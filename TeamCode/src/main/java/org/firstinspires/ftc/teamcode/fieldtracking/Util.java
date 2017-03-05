package org.firstinspires.ftc.teamcode.fieldtracking;

/**
 * Created by ROUS on 3/3/2017.
 * catch all for helper classes
 */
public class Util {

    public static final double  TWO_PI = (Math.PI * 2.0); /**360 Degrees*/
    public static final double  HALF_PI = (Math.PI / 2.0);/**90 Degrees*/

    /**Compare values equal to +- tol*/
    public static boolean FuzzyEqual( double v1, double v2, double tol ) {
        double delta = Math.abs(v2 - v1);
        return ( delta <= tol );
    }

    /**Compare value equals 0.0 +- tol*/
    public static boolean FuzzyZero( double v1, double tol ) {
        double delta = Math.abs(v1);
        return ( delta <= tol );
    }

    /**calculate equivalent angle 0 - 2PI */
    public static final double OptomizeAngleZero_TwoPi( double angle ){
        double result = angle;
        if ( angle >= 0.0 ) {
            while (result > TWO_PI) {
                result -= TWO_PI;
            }
        } else {
            while (result < 0) {
                result += TWO_PI;
            }
        }
        return result;
    }

    /**calculate equivalent angle -PI to +PI */
    public static final double OptomizeAngleNegPi_PosPi( double angle ){
        double result = angle;
        if ( angle >= 0.0 )
        {
            while (result > Math.PI) {
                result -= TWO_PI;
            }
        } else {
            while (result < -Math.PI) {
                result += TWO_PI;
            }
        }
        return result;
    }

}
