/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.util.color;

import org.opencv.core.Scalar;

/**
 * Implements a single color in the RGB/RGBA space
 */
public class ColorRGBA extends Color {

    /**
     * Instantiate a 3-channel (no transparency) RGB(A) color
     *
     * @param r Red channel (0-255)
     * @param g Green channel (0-255)
     * @param b Blue channel (0-255)
     */
    public ColorRGBA(int r, int g, int b) {
        super(new Scalar(r, g, b, 255));
    }

    /**
     * Instantiate a 4-channel RGBA color
     *
     * @param r Red channel (0-255)
     * @param g Green channel (0-255)
     * @param b Blue channel (0-255)
     * @param a Alpha channel (0-255), where 0 is transparent and 255 is opaque
     */
    public ColorRGBA(int r, int g, int b, int a) {
        super(new Scalar(r, g, b, a));
    }

    /**
     * Instantiate a 3- or 4-channel RGB(A) color from a Scalar value
     *
     * @param scalar Scalar value with 3-4 channels
     */
    public ColorRGBA(Scalar scalar) {
        super(scalar);
    }

    /**
     * Instantiate a 3- or 4-channel RGB(A) color from a hex code
     *
     * @param hexCode Hex code, such as #ffffff (white) or #0000ffaa (partially transparent blue)
     *                Code must be 6 or 8 characters long
     */
    public ColorRGBA(String hexCode) {
        super(parseHexCode(hexCode));
    }

    private static Scalar parseHexCode(String hexCode) {
        //remove hex key #
        if (!hexCode.startsWith("#"))
            hexCode = "#" + hexCode;
        //ensure that the length is correct
        if (hexCode.length() != 7 && hexCode.length() != 9)
            throw new IllegalArgumentException("Hex code must be of length 6 or 8 characters.");
        //get the integer representation
        int color = android.graphics.Color.parseColor(hexCode);
        //get the r,g,b,a values
        return new Scalar(android.graphics.Color.red(color),
                android.graphics.Color.green(color),
                android.graphics.Color.blue(color),
                android.graphics.Color.alpha(color));
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
            throw new IllegalArgumentException("Scalar must have 3 or 4 dimensions.");

        return new Scalar(s.val[0], s.val[1], s.val[2], (s.val.length >= 4) ? (int) s.val[3] : 255);
    }

    /**
     * Get the red value
     *
     * @return Red channel (0-255)
     */
    public int red() {
        return (int) scalar.val[0];
    }

    /**
     * Get the green value
     *
     * @return Green channel (0-255)
     */
    public int green() {
        return (int) scalar.val[1];
    }

    /**
     * Get the blue value
     *
     * @return Blue channel (0-255)
     */
    public int blue() {
        return (int) scalar.val[2];
    }

    /**
     * Get the alpha value
     *
     * @return Alpha channel (0-255)
     */
    public int alpha() {
        return (int) scalar.val[3];
    }

    /**
     * Get the RGBA colorspace
     *
     * @return ColorSpace.RGBA
     */
    @Override
    public ColorSpace getColorSpace() {
        return ColorSpace.RGBA;
    }

    /**
     * Get the color as an android.graphics.Color-friendly integer
     *
     * @return android.graphics.Color-based integer
     */
    public int getInteger() {
        return android.graphics.Color.argb((int) scalar.val[3], (int) scalar.val[0],
                (int) scalar.val[1], (int) scalar.val[2]);
    }
}
