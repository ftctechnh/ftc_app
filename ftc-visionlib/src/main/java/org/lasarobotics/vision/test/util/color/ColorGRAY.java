package org.lasarobotics.vision.test.util.color;

import org.opencv.core.Scalar;

/**
 * Implements a grayscale color
 */
public class ColorGRAY extends Color {

    public ColorGRAY(Scalar s) {
        super(s);
    }

    public ColorGRAY(int v) {
        super(new Scalar(v));
    }

    public ColorSpace getColorSpace() {
        return ColorSpace.GRAY;
    }

    @Override
    protected Scalar parseScalar(Scalar s) {
        if (s.val.length < 1)
            throw new IllegalArgumentException("Scalar must have 1 dimension.");
        return new Scalar(s.val[0]);
    }

    public int value() {
        return (int) scalar.val[0];
    }
}
