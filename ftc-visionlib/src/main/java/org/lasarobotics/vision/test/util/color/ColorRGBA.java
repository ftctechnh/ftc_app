package org.lasarobotics.vision.test.util.color;

import org.opencv.core.Scalar;

/**
 * Implements a single color in the RGB/RGBA space
 */
public class ColorRGBA extends Color {

    public ColorRGBA(int r, int g, int b) {
        super(new Scalar(r, g, b, 255));
    }

    public ColorRGBA(int r, int g, int b, int a) {
        super(new Scalar(r, g, b, a));
    }

    public ColorRGBA(Scalar scalar) {
        super(scalar);
    }

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

    @Override
    protected Scalar parseScalar(Scalar s) {
        if (s.val.length < 3)
            throw new IllegalArgumentException("Scalar must have 3 or 4 dimensions.");

        return new Scalar(s.val[0], s.val[1], s.val[2], (s.val.length >= 4) ? (int) s.val[3] : 255);
    }

    public int red() {
        return (int) scalar.val[0];
    }

    public int green() {
        return (int) scalar.val[1];
    }

    public int blue() {
        return (int) scalar.val[2];
    }

    public int alpha() {
        return (int) scalar.val[3];
    }

    @Override
    public ColorSpace getColorSpace() {
        return ColorSpace.RGBA;
    }

    public int getInteger() {
        return android.graphics.Color.argb((int) scalar.val[3], (int) scalar.val[0],
                (int) scalar.val[1], (int) scalar.val[2]);
    }
}
