/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.image;

import org.lasarobotics.vision.util.MathUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Transform manipulation and correction
 */
public class Transform {
    /**
     * Rotate an image by an angle (counterclockwise)
     *
     * @param image Transform matrix
     * @param angle Angle to rotate by (counterclockwise) from -360 to 360
     */
    public static void rotate(Mat image, double angle) {
        //Calculate size of new matrix
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));

        int newWidth = (int) (image.width() * cos + image.height() * sin);
        int newHeight = (int) (image.width() * sin + image.height() * cos);

        // rotating image
        Point center = new Point(newWidth / 2, newHeight / 2);
        Mat rotMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0); //1.0 means 100 % scale

        Size size = new Size(newWidth, newHeight);
        Imgproc.warpAffine(image, image, rotMatrix, image.size());
    }

    public static void flip(Mat img, FlipType flipType) {
        Core.flip(img, img, flipType.val);
    }

    /**
     * Scale an image by a scale factor
     *
     * @param img    The image to scale
     * @param factor The scale factor. If greater than 1, image will dilate. Otherwise,
     *               image will constrict.
     */
    private static void scale(Mat img, double factor) {
        resize(img, new Size(img.size().width * factor, img.size().height * factor));
    }

    /**
     * Scales an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     * This function will always create the smallest image if the ratio of approxSize
     * to the image's actual size do not match.
     *
     * @param img        The image
     * @param approxSize The target size
     */
    public static void scale(Mat img, Size approxSize) {
        scale(img, approxSize, false, false);
    }

    /**
     * Scales an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     *
     * @param img          The image
     * @param approxSize   The target size
     * @param maximize     If maximize is true, then if the approxSize aspect ratio
     *                     does not match the target, then the largest possible image
     *                     would be used. If false (default), the the smallest image
     *                     would be used.
     * @param integerScale If true (default), then only integer scale factors would be used.
     *                     Otherwise, any scale factor can be used.
     */
    private static void scale(Mat img, Size approxSize, boolean maximize, boolean integerScale) {
        double scale = makeScale(img, approxSize, maximize, integerScale);
        scale(img, scale);
    }

    /**
     * Enlarges an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     * This function will always create the largest image if the ratio of approxSize
     * to the image's actual size do not match.
     *
     * @param img        The image
     * @param approxSize The target size
     */
    public static void enlarge(Mat img, Size approxSize) {
        enlarge(img, approxSize, false);
    }

    /**
     * Shrinks an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     * This function will always create the smallest image if the ratio of approxSize
     * to the image's actual size do not match.
     *
     * @param img        The image
     * @param approxSize The target size
     */
    public static void shrink(Mat img, Size approxSize) {
        shrink(img, approxSize, false);
    }

    /**
     * Enlarges an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     * This function will always create the largest image if the ratio of approxSize
     * to the image's actual size do not match.
     *
     * @param img          The image
     * @param approxSize   The target size
     * @param integerScale If true (default), then only integer scale factors would be used.
     *                     Otherwise, any scale factor can be used.
     */
    private static void enlarge(Mat img, Size approxSize, boolean integerScale) {
        double scale = makeScale(img, approxSize, true, integerScale);
        if (MathUtil.equal(scale, 1) || scale < 1) {
            return;
        }
        scale(img, scale);
    }

    /**
     * Shrink an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     * This function will always create the smallest image if the ratio of approxSize
     * to the image's actual size do not match.
     *
     * @param img          The image
     * @param approxSize   The target size
     * @param integerScale If true (default), then only integer scale factors would be used.
     *                     Otherwise, any scale factor can be used.
     */
    private static void shrink(Mat img, Size approxSize, boolean integerScale) {
        double scale = makeScale(img, approxSize, false, integerScale);
        if (MathUtil.equal(scale, 1) || scale > 1) {
            return;
        }
        scale(img, scale);
    }

    /**
     * Scales an image to an approximate size. The scale will always be equal
     * on the x and y axis, regardless of the approxSize.
     *
     * @param img          The image
     * @param approxSize   The target size
     * @param maximize     If maximize is true, then if the approxSize aspect ratio
     *                     does not match the target, then the largest possible image
     *                     would be used. If false (default), the the smallest image
     *                     would be used.
     * @param integerScale If true (default), then only integer scale factors would be used.
     *                     Otherwise, any scale factor can be used.
     */
    private static double makeScale(Mat img, Size approxSize, boolean maximize, boolean integerScale) {
        Size imageSize = img.size();
        double ratioWidth = approxSize.width / imageSize.width;
        double ratioHeight = approxSize.height / imageSize.height;
        double ratio = maximize ? Math.max(ratioWidth, ratioHeight) : Math.min(ratioWidth, ratioHeight);
        if (MathUtil.equal(ratio, 1))
            return 1;
        if (integerScale) {
            //The scale factor is always greater than 1
            double scale = (ratio < 1) ? 1 / ratio : ratio;
            //If you are actually increasing the size of the object, use ceiling()
            //Otherwise, use floor()
            scale = maximize ^ (ratio < 1) ? Math.ceil(scale) : Math.floor(scale);
            //Get the actual ratio again
            return (ratio < 1) ? 1 / scale : scale;
        } else {
            return ratio;
        }
    }

    private static void resize(Mat img, Size size) {
        int interpolation;
        if (MathUtil.equal(size.area(), img.size().area()))
            return;
        else if (size.width > img.size().width && size.height > img.size().height)
            interpolation = Imgproc.CV_INTER_CUBIC; //enlarge image
        else if (size.width < img.size().width && size.height < img.size().height)
            interpolation = Imgproc.CV_INTER_AREA; //shrink image
        else
            interpolation = Imgproc.CV_INTER_LINEAR; //not entirely sure, so use safe option
        Imgproc.resize(img, img, size, 0, 0, interpolation);
    }

    /**
     * Type of reflection
     * Creates a reflection matrix based on the type of value
     */
    public enum FlipType {
        //Reflect across Y axis
        FLIP_ACROSS_Y(0),
        //Reflect across X axis
        FLIP_ACROSS_X(1),
        //Reflect across both axis simultaneously
        FLIP_BOTH(-1);

        final int val;

        FlipType(int a) {
            this.val = a;
        }
    }
}
