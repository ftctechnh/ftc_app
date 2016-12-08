/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.detection.objects;

import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;

/**
 * Implements a single ellipse (acts like RotatedRect) with advanced measurement utilities
 */
public class Ellipse extends Detectable implements Comparable<Ellipse> {
    private RotatedRect rect;

    /**
     * Instantiate a null ellipse
     */
    public Ellipse() {
        this.rect = new RotatedRect();
    }

    /**
     * Create an ellipse based on an OpenCV rotated rectangle
     *
     * @param rect OpenCV rotated rectangle which bounds the ellipse
     */
    public Ellipse(RotatedRect rect) {
        this.rect = rect;
    }

    /**
     * Get the OpenCV rectangle which circumscribes the ellipse
     *
     * @return OpenCV rectange
     */
    public RotatedRect getRect() {
        return rect;
    }

    /**
     * Get the size of the ellipse (ignoring inclination)
     *
     * @return Size of the ellipse
     */
    public Size size() {
        return rect.size;
    }

    public double height() {
        return size().height;
    }

    /**
     * Offset the object, translating it by a specific offset point
     *
     * @param offset Point to offset by, e.g. (1, 0) would move object 1 px right
     */
    @Override
    public void offset(Point offset) {
        this.rect = new RotatedRect(new Point(rect.center.x + offset.x, rect.center.y + offset.y),
                rect.size, rect.angle);
    }

    public double width() {
        return size().width;
    }

    /**
     * Get the angle of inclination of the ellipse
     *
     * @return Angle of inclination
     */
    public double angle() {
        return rect.angle;
    }

    /**
     * Get the center of the ellipse
     *
     * @return Center of the ellipse as a point
     */
    public Point center() {
        return rect.center;
    }

    public double left() {
        return center().x - (width() / 2);
    }

    public double right() {
        return center().x + (width() / 2);
    }

    public double top() {
        return center().y - (height() / 2);
    }

    public double bottom() {
        return center().y + (height() / 2);
    }

    /**
     * Gets the area of the ellipse
     *
     * @return Area = semi-major axis * semi-minor axis * PI
     */
    public double area() {
        return semiMajorAxis() * semiMinorAxis() * Math.PI;
    }

    /**
     * Get the major axis of the ellipse
     *
     * @return 2a
     */
    private double majorAxis() {
        return Math.max(height(), width());
    }

    /**
     * Get the minor axis of the ellipse
     *
     * @return 2b
     */
    private double minorAxis() {
        return Math.min(height(), width());
    }

    /**
     * Get the semi-major axis of the ellipse
     *
     * @return a
     */
    private double semiMajorAxis() {
        return majorAxis() / 2;
    }

    /**
     * Get the semi-minor axis of the ellipse
     *
     * @return b
     */
    private double semiMinorAxis() {
        return minorAxis() / 2;
    }

    /**
     * Gets the first flattening ratio for the ellipse
     *
     * @return First flattening ratio = (a-b)/a, where a=semi-major axis and b=semi-minor axis)
     */
    public double flattening() {
        return (semiMajorAxis() - semiMinorAxis()) / semiMajorAxis();
    }

    /**
     * Scale this ellipse by a scaling factor about its center
     *
     * @param factor Scaling factor, 1 for no scale, less than one to contract, greater than one to expand
     */
    public Ellipse scale(double factor) {
        RotatedRect r = rect.clone();
        r.size = new Size(factor * rect.size.width, factor * rect.size.height);
        return new Ellipse(r);
    }

    /**
     * Gets the eccentricity of the ellipse, between 0 (inclusive) and 1 (exclusive)
     *
     * @return e = sqrt(1-(b^2/a^2)), where a=semi-major axis and b=semi-minor axis
     */
    public double eccentricity() {
        return Math.sqrt(1 - (semiMinorAxis() * semiMinorAxis()) / (semiMajorAxis() * semiMajorAxis()));
    }

    /**
     * Returns true if the ellipse is ENTIRELY inside the contour
     *
     * @param contour The contour to test against
     * @return True if the ellipse is entirely inside the contour, false otherwise
     */
    public boolean isInside(Contour contour) {
        //TODO this algorithm checks for entirety; make an isEntirelyInside() and isPartiallyInside()
        //FIXME this is an inaccurate method using only the bounding box of the contour
        //TODO try ray-casting (even-odd) algorithm - use center as point (will show partial matches)
        //TODO also try all 4 points to match (will ensure it is entirely inside)
        return left() >= contour.left() && right() <= contour.right() &&
                top() >= contour.top() && bottom() <= contour.bottom();
    }

    /**
     * Transpose this rectangle so that x becomes y and vice versa
     *
     * @return Transposed rectangle instance
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public Ellipse transpose() {
        return new Ellipse(new RotatedRect(
                new Point(rect.center.y, rect.center.x),
                new Size(rect.size.height, rect.size.width), rect.angle));
    }

    public String getLocationString() {
        return String.format("(%.0f, %.0f)", rect.center.x, rect.center.y);
    }

    /***
     * Compare ellipses by area
     *
     * @param another Another ellipse
     * @return 1 if this is larger, -1 if another is larger, 0 otherwise
     */
    @Override
    public int compareTo(Ellipse another) {
        return this.area() > another.area() ? 1 : this.area() < another.area() ? -1 : 0;
    }
}
