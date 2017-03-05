package org.firstinspires.ftc.teamcode.fieldtracking;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

/**
 * Created by ROUS on 3/3/2017.
 */
public class VectorFUtil {
    public static VectorF CrossProduct(final VectorF vA, final VectorF vB ) {
        /**
         * C = AxB = (AyBz − AzBy, AzBx − AxBz, AxBy − AyBx);
         */
        VectorF vC = new VectorF( vA.get(1)*vB.get(2) - vA.get(2)*vB.get(1)     /** AyBz − AzBy */
                                , vA.get(2)*vB.get(0) - vA.get(0)*vB.get(2)     /** AzBx − AxBz */
                                , vA.get(0)*vB.get(1) - vA.get(1)*vB.get(0) );  /** AxBy − AyBx)*/
        return vC;
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
        if ( Util.FuzzyEqual( numerator, 1.0, 1e-6) ) { /**check for parallel coincident vectors*/
            theta = 0.0; /**vectors aligned in same direction*/
        } else {
            if (Util.FuzzyZero(numerator, 1e-6)) { /**check for parallel opposing vectors*/
                theta = Math.PI; /**vectors aligned in opposing directions*/
            } else {
                theta = Math.acos(cosTheta);

                /**Use the sign of the determinant of the unit vectors vectors (A,B)
                 * to determine the side of the ray A that B falls on to properly
                 * assign angles greater than 180 because we are looking for CCW angels only where
                 * A is the vector to test and B is the reference vector (ray)
                 * _position = sign( Ax * By - Bx *Ay );
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
        return theta;
    }
}
