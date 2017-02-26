/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.detection.objects;

import org.lasarobotics.vision.util.MathUtil;
import org.lasarobotics.vision.util.color.Color;
import org.lasarobotics.vision.util.color.ColorSpace;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

/**
 * A detectable object
 */
public abstract class Detectable {
    /**
     * Offset a list of objects, translating them by a specific offset point
     *
     * @param detectables List of detectables
     * @param offset      Point to offset by, e.g. (1, 0) would move objects 1 px right
     * @return Modified list of detectables
     */
    public static List<? extends Detectable> offset(List<? extends Detectable> detectables, Point offset) {
        for (int i = 0; i < detectables.size(); i++)
            detectables.get(i).offset(offset);
        return detectables;
    }

    /**
     * Get the x-coordinate of the left side of the object
     *
     * @return Left side of the object
     */
    protected abstract double left();

    /**
     * Get the x-coordinate of the right side of the object
     *
     * @return Right side of the object
     */
    protected abstract double right();

    /**
     * Get the y-coordinate of the top side of the object
     *
     * @return Top side of the object
     */
    protected abstract double top();

    /**
     * Get the y-coordinate of the bottom side of the object
     *
     * @return Bottom side of the object
     */
    protected abstract double bottom();

    /**
     * Get the width of the object
     *
     * @return Width of the object
     */
    public abstract double width();

    /**
     * Get the height of the object
     *
     * @return Height of the object
     */
    public abstract double height();

    /**
     * Get the top-left point of the object
     *
     * @return Top-left point of the object
     */
    public Point topLeft() {
        return new Point(left(), top());
    }

    /**
     * Get the bottom right point of the object
     *
     * @return Bottom right point of the object
     */
    public Point bottomRight() {
        return new Point(right(), bottom());
    }

    /**
     * Gets the average color of the object
     *
     * @param img      The image matrix, of any color size
     * @param imgSpace The image's color space
     * @return The average color of the region
     */
    public Color averageColor(Mat img, ColorSpace imgSpace) {
        //Coerce values to stay within screen dimensions
        double leftX = MathUtil.coerce(0, img.cols() - 1, left());
        double rightX = MathUtil.coerce(0, img.cols() - 1, right());

        double topY = MathUtil.coerce(0, img.rows() - 1, top());
        double bottomY = MathUtil.coerce(0, img.rows() - 1, bottom());

        //Input points into array for calculation
        //TODO rectangular submatrix-based calculation isn't perfectly accurate when you have ellipses or weird shapes
        Mat subMat = img.submat((int) topY, (int) bottomY, (int) leftX, (int) rightX);

        //Calculate average and return new color instance
        return Color.create(Core.mean(subMat), imgSpace);
    }

    /**
     * Offset the object, translating it by a specific offset point
     *
     * @param offset Point to offset by, e.g. (1, 0) would move object 1 px right
     */
    public abstract void offset(Point offset);
}
