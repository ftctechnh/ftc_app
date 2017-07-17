/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.util.color;

import org.opencv.core.Scalar;

/**
 * Implements a grayscale color
 */
public class ColorGRAY extends Color {

    /**
     * Instantiate a Grayscale (8-bit) color from a Scalar
     *
     * @param s Scalar value containing one number (0-255)
     */
    public ColorGRAY(Scalar s) {
        super(s);
    }

    /**
     * Instantiate a Grayscale (8-bit) color from an integer
     *
     * @param v Value (0-255)
     */
    public ColorGRAY(int v) {
        super(new Scalar(v));
    }

    /**
     * Get the GRAY colorspace
     *
     * @return Colorspace.GRAY
     */
    public ColorSpace getColorSpace() {
        return ColorSpace.GRAY;
    }

    /**
     * Parse a scalar value into the colorspace
     *
     * @param s Scalar value
     * @return Colorspace scalar value
     */
    @Override
    protected Scalar parseScalar(Scalar s) {
        if (s.val.length < 1)
            throw new IllegalArgumentException("Scalar must have 1 dimension.");
        return new Scalar(s.val[0]);
    }

    /**
     * Get brightness value
     *
     * @return Value (0-255)
     */
    public int value() {
        return (int) scalar.val[0];
    }
}
