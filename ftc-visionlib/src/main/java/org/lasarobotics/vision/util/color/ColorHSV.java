/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.util.color;

import org.opencv.core.Scalar;

/**
 * Implements a color in the HSV color space
 */
public class ColorHSV extends Color {

    public ColorHSV(Scalar s) {
        super(s);
    }

    /**
     * An HSV (hue, saturation, value) color
     * This is NOT an HSL color - they live in different spaces.
     *
     * @param h Hue, from 0 to 255
     * @param s Saturation, from 0 to 255
     * @param v Value, from 0 to 255
     */
    public ColorHSV(int h, int s, int v) {
        super(new Scalar(h, s, v));
    }

    /**
     * Get the HSV colorspace
     *
     * @return ColorSpace.HSV
     */
    public ColorSpace getColorSpace() {
        return ColorSpace.HSV;
    }

    /**
     * Parse a scalar value into the colorspace
     *
     * @param s Scalar value
     * @return Colorspace scalar value
     */
    @Override
    protected Scalar parseScalar(Scalar s) {
        if (s.val.length < 3)
            throw new IllegalArgumentException("Scalar must have 3 dimensions.");
        return new Scalar(s.val[0], s.val[1], s.val[2]);
    }

    /**
     * Get the color hue
     *
     * @return Hue (0-255)
     */
    public int hue() {
        return (int) scalar.val[0];
    }

    /**
     * Get the color saturation
     *
     * @return Saturation (0-255)
     */
    public int saturation() {
        return (int) scalar.val[1];
    }

    /**
     * Get the color value
     *
     * @return Value (0-255)
     */
    public int value() {
        return (int) scalar.val[2];
    }
}
