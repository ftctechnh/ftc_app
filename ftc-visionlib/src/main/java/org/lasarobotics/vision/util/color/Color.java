/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.util.color;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Implements a color in any color space
 */
public abstract class Color {

    Scalar scalar;

    /**
     * Instantiate a color from a scalar value
     *
     * @param s Scalar value
     */
    Color(Scalar s) {
        setScalar(s);
    }

    /**
     * Create a color based on a colorspaace and a scalar value
     *
     * @param s          Scalar value
     * @param colorSpace Colorspace
     * @return Color instance
     */
    public static Color create(Scalar s, ColorSpace colorSpace) {
        Class<? extends Color> colorClass = colorSpace.getColorClass();

        try {
            return colorClass.getConstructor(Scalar.class).newInstance(s);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot create new color instance.");
        }
    }

    /**
     * Create a blank RGBA matrix (8-bit color info * 4 channels)
     *
     * @param width  Matrix width
     * @param height Matrix height
     * @return RGBA image matrix
     */
    public static Mat createMatRGBA(int width, int height) {
        return new Mat(height, width, CvType.CV_8UC4);
    }

    /**
     * Create a blank Grayscale matrix (8-bit color info)
     *
     * @param width  Matrix width
     * @param height Matrix height
     * @return Grayscale matrix
     */
    public static Mat createMatGRAY(int width, int height) {
        return new Mat(height, width, CvType.CV_8UC1);
    }

    /**
     * Rapidly convert an RGBA matrix to a Grayscale matrix, bypassing
     * most of the color conversion overhead.
     *
     * @param rgba RGBA matrix
     * @return Grayscale matrix
     */
    public static Mat rapidConvertRGBAToGRAY(Mat rgba) {
        Mat gray = new Mat(rgba.rows(), rgba.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGBA2GRAY);
        return gray;
    }

    /**
     * Convert a matrix in one color space to another
     *
     * @param in       Input matrix
     * @param spaceIn  Input colorspace
     * @param spaceOut Output colorspace
     * @return Matrix in output colorspace
     */
    public static Mat convertColorMat(Mat in, ColorSpace spaceIn, ColorSpace spaceOut) {
        if (spaceIn == spaceOut)
            return in;
        if (!spaceIn.canConvertTo(spaceOut))
            throw new IllegalArgumentException("Cannot convert color to the desired color space.");

        Mat output = in.clone();

        try {
            for (int i = 0; i < spaceIn.getConversionsTo(spaceOut).length; i += 3) {
                int conversion = spaceIn.getConversionsTo(spaceOut)[i];
                int inputDim = spaceIn.getConversionsTo(spaceOut)[i + 1];
                int outputDim = spaceIn.getConversionsTo(spaceOut)[i + 2];

                Imgproc.cvtColor(output, output, conversion, outputDim);
            }
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot convert color to the desired color space.");
        }

        return output;
    }

    /**
     * Get the color's scalar value
     *
     * @return Scalar value
     */
    public Scalar getScalar() {
        return scalar;
    }

    private void setScalar(Scalar s) {
        this.scalar = parseScalar(s);
    }

    /**
     * Get the color's scalar value in Android-native RGBA
     *
     * @return Scalar value converted to RGBA
     */
    public Scalar getScalarRGBA() {
        return convertColorScalar(ColorSpace.RGBA);
    }

    /**
     * Get the colorspace ID
     *
     * @return Colorspace ID
     */
    protected abstract ColorSpace getColorSpace();

    /**
     * Parse a scalar value into the colorspace
     *
     * @param s Scalar value
     * @return Colorspace scalar value
     */
    protected abstract Scalar parseScalar(Scalar s);

    /**
     * Convert this color to another colorspace
     *
     * @param to Colorspace to convert to
     * @return Color in other colorspace
     */
    public Color convertColor(ColorSpace to) {
        Scalar output = convertColorScalar(to);

        Class<? extends Color> colorClass = to.getColorClass();

        try {
            return colorClass.getConstructor(Scalar.class).newInstance(output);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot convert color to the desired color space.");
        }
    }

    /**
     * Convert this color to a different colorspace and return a scalar
     *
     * @param to Colorspace to convert to
     * @return Scalar in other colorspace
     */
    public Scalar convertColorScalar(ColorSpace to) {
        if (getColorSpace() == to)
            return getScalar();
        if (!getColorSpace().canConvertTo(to))
            throw new IllegalArgumentException("Cannot convert color to the desired color space.");

        Scalar output = this.getScalar();

        try {
            for (int i = 0; i < getColorSpace().getConversionsTo(to).length; i += 3) {
                int conversion = getColorSpace().getConversionsTo(to)[i];
                int inputDim = getColorSpace().getConversionsTo(to)[i + 1];
                int outputDim = getColorSpace().getConversionsTo(to)[i + 2];

                Mat pointMatTo = new Mat();
                Mat pointMatFrom = new Mat(1, 1, CvType.CV_8UC(inputDim), output);
                Imgproc.cvtColor(pointMatFrom, pointMatTo, conversion, outputDim);
                output = new Scalar(pointMatTo.get(0, 0));
                pointMatTo.release();
                pointMatFrom.release();
            }
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot convert color to the desired color space.");
        }

        return output;
    }
}
