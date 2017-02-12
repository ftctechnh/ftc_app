package org.firstinspires.ftc.teamcode.math;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.util.Locale;

/**
 * Created by blaisg on 11/19/2016.
 */
public class VectorUtil {
    public static VectorF CrossProduct(final VectorF vA, final VectorF vB ) {
        /**
         * C = AxB = (AyBz − AzBy, AzBx − AxBz, AxBy − AyBx);
         */
        VectorF vC = new VectorF( vA.get(1)*vB.get(2) - vA.get(2)*vB.get(1)     /** AyBz − AzBy */
                                , vA.get(2)*vB.get(0) - vA.get(0)*vB.get(2)     /** AzBx − AxBz */
                                , vA.get(0)*vB.get(1) - vA.get(1)*vB.get(0) );  /** AxBy − AyBx)*/
        return vC;
    }

    public static VectorF FlateneZ( VectorF vA ) {
        vA.put(2,0.0f);
        return vA;
    }

    public static VectorF FlatenedZ( final VectorF vA ) {
        return new VectorF(vA.get(0), vA.get(1), 0.0f);
    }

    public static VectorF Normalize( VectorF vA ) {
        float mag = vA.magnitude();
        if ( mag > 0f ) {
            vA.multiply(1.0f / mag); /** this will throw an exception if magnitued is == 0*/
        }
        return vA;
    }

    public static VectorF Normalized( final VectorF vA ) {
        return vA.multiplied(1.0f/vA.magnitude());
    }

    private static boolean FuzzyEqual( double v1, double v2, double tol ) {
        double delta = Math.abs(v2 - v1);
        return ( delta <= tol );
    }

    private static boolean FuzzyZero( double v1, double tol ) {
        return FuzzyEqual(v1,0.0, tol);
    }

    public static double CalculateXYAngleCCW( final VectorF vA, final VectorF vB ) {
        /**
         *                  (A . B)                                            (A . B)
         * cosine(theta) = ---------   which yields "angle theta" = arccosine(----------)
         *                 (|A|*|B|)                                          (|A|*|B|)
         */
        VectorF nA = Normalize(FlatenedZ(vA));
        VectorF nB = Normalize(FlatenedZ(vB));
        double numerator = (double)nA.dotProduct(nB);
        double denominator = (double) (nA.magnitude() * nB.magnitude());
        double cosTheta = (numerator / denominator);
        double theta = 0.0;
        if ( FuzzyEqual( numerator, 1.0, 1e-6) ) { /**check for parallel coincident vectors*/
            theta = 0.0; /**vectors aligned in same direction*/
        } else {
            if (FuzzyZero(numerator, 1e-6)) { /**check for parallel opposing vectors*/
                theta = Math.PI; /**vectors aligned in opposing directions*/
            } else {
                theta = Math.acos(cosTheta);

                /**Use the sign of the determinant of the unit vectors vectors (A,B)
                 * to determine the side of the ray A that B falls on to properly
                 * assign angles greater than 180 because we are looking for CCW angels only where
                 * A is the vector to test and B is the reference vector (ray)
                 * position = sign( Ax * By - Bx *Ay );
                 * pos = 0 vectors are parallel
                 * pos > 0 (+1) A is CCW left of B
                 * pos < 0 (-1) A is CCW right of B
                 */
                double pos = Math.sin( nA.get(0) * nB.get(1) - nA.get(1) * nB.get(0) );
                if ( pos > 0.0 ) /**check for angle greater than 180*/
                {
                    theta = ( Math.PI*2.0 - theta );
                }
            }
        }
        RobotLog.vv("AngleCalc"," num/denom= %f / %f -> cs=%f theta=%fr %fd", numerator, denominator, cosTheta, theta, Math.toDegrees(theta));
        return theta;
    }

    public static double CalculateCWSignedXYAngleBetweenVectors( final VectorF vAFrom, final VectorF vBTo ) {
        /**
         *                  (A . B)                                            (A . B)
         * cosine(theta) = ---------   which yields "angle theta" = arccosine(----------)
         *                 (|A|*|B|)                                          (|A|*|B|)
         */
        VectorF nA = Normalize(FlatenedZ(vAFrom));
        VectorF nB = Normalize(FlatenedZ(vBTo));
        double numerator = (double)nA.dotProduct(nB);
        double denominator = (double) (nA.magnitude() * nB.magnitude());
        double cosTheta = (numerator / denominator);
        double theta = 0.0;
        if ( FuzzyEqual( numerator, 1.0, 1e-6) ) { /**check for parallel coincident vectors*/
            theta = 0.0; /**vectors aligned in same direction*/
        } else {
            if (FuzzyZero(numerator, 1e-6)) { /**check for parallel opposing vectors*/
                theta = Math.PI; /**vectors aligned in opposing directions*/
            } else {
                theta = Math.acos(cosTheta);

                /**Use the sign of the determinant of the unit vectors vectors (A,B)
                 * to determine the side of the ray A that B falls on to properly
                 * assign CCW Deflection as negative and CW Deflection as positive.
                 * A is the vector to test and B is the reference vector (ray)
                 * position = sign( Bx * Ay - Ax *By );
                 * pos = 0 vectors are parallel
                 * pos > 0 (+1) B is CW right of Ag
                 * pos < 0 (-1) B is CCW left of a
                 */
                double pos = Math.sin( nA.get(0) * nB.get(1) - nA.get(1) * nB.get(0) );
                if ( pos > 0.0 ) /**check for angle to the left*/
                {
                    theta = -theta;
                }
            }
        }
        RobotLog.vv("AngleCalc"," num/denom= %f / %f -> cs=%f theta=%fr %fd", numerator, denominator, cosTheta, theta, Math.toDegrees(theta));
        return theta;
    }

    public static VectorF RotateVectorXYVectorByCCWAngle( final VectorF vec, double ccwAngle ) {
        VectorF nVecXY = FlatenedZ(vec);
        double ccwAngelRad = Math.toDegrees(ccwAngle);

        /**
         *  to rotate a 2d xy vector by angle f
         *
         *  x' = x cos f - y sin f
         *  y' = y cos f + x sin f
         */
         double xp = (double)(nVecXY.get(0)) * Math.cos(ccwAngelRad) - (double)(nVecXY.get(1)) * Math.sin(ccwAngelRad);
         double yp = (double)(nVecXY.get(1)) * Math.cos(ccwAngelRad) + (double)(nVecXY.get(0)) * Math.sin(ccwAngelRad);

        return new VectorF((float)xp, (float)yp, vec.get(2));
    }

    public static String FormatVectorXY(final VectorF vec) {
        return String.format(Locale.getDefault(),"[%.04f, %.04f]", vec.get(0), vec.get(1));
    }

    public static String FormatVectorXYZ(final VectorF vec) {
        return String.format(Locale.getDefault(),"[%.04f, %.04f, %.04f]", vec.get(0), vec.get(1), vec.get(2));
    }

    public static String FormatVectorXYZW(final VectorF vec) {
        return String.format(Locale.getDefault(),"[%.04f, %.04f, %.04f, &.04f]", vec.get(0), vec.get(1), vec.get(2), vec.get(3));
    }

    public static String FormatMatrix(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
