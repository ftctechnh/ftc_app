package org.lasarobotics.vision.test.util.color;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Implements a color in any color space
 */
public abstract class Color {

    protected Scalar scalar;

    Color(Scalar s) {
        setScalar(s);
    }

    public static Color create(Scalar s, ColorSpace colorSpace) {
        Class<? extends Color> colorClass = colorSpace.getColorClass();

        try {
            return colorClass.getConstructor(Scalar.class).newInstance(s);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot create new color instance.");
        }
    }

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

    public Scalar getScalar() {
        return scalar;
    }

    public void setScalar(Scalar s) {
        this.scalar = parseScalar(s);
    }

    public Scalar getScalarRGBA() {
        return convertColorScalar(ColorSpace.RGBA);
    }

    public abstract ColorSpace getColorSpace();

    protected abstract Scalar parseScalar(Scalar s);

    public Color convertColor(ColorSpace to) {
        Scalar output = convertColorScalar(to);

        Class<? extends Color> colorClass = to.getColorClass();

        try {
            return colorClass.getConstructor(Scalar.class).newInstance(output);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot convert color to the desired color space.");
        }
    }

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
