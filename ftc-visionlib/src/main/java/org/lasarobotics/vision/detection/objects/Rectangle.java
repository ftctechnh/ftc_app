/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.detection.objects;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;

/**
 * Implements a single rectangle object with advanced measurement capabilities
 */
public class Rectangle extends Detectable {
    private RotatedRect rect = new RotatedRect();

    /**
     * Create a null rectangle
     */
    public Rectangle() {
        this.rect = new RotatedRect();
    }

    /**
     * Create a rectangle bounded by four positions
     *
     * @param top    Top-most Y value
     * @param left   Left-most X value
     * @param bottom Bottom-most Y value
     * @param right  Right-most X value
     */
    public Rectangle(double top, double left, double bottom, double right) {
        double width = Math.abs(right - left);
        double height = Math.abs(bottom - top);
        Point center = new Point(left + (width / 2.0), top + (height / 2.0));
        setRect(new Rect((int) (center.x - (width / 2)), (int) (center.y - (height / 2)), (int) width, (int) height));
    }

    /**
     * Create a rectangle based on a center and its size
     *
     * @param center Center of the rectangle
     * @param width  Width of the rectangle
     * @param height Height of the rectangle
     */
    public Rectangle(Point center, double width, double height) {
        setRect(new Rect((int) (center.x - (width / 2)), (int) (center.y - (height / 2)), (int) width, (int) height));
    }

    /**
     * Create a rectangle based on an OpenCV rotated rectangle
     *
     * @param rect OpenCV rotated rectangle
     */
    public Rectangle(RotatedRect rect) {
        this.rect = rect;
    }

    /**
     * Create a rectangle based on an OpenCV rectangle
     *
     * @param rect OpenCV rectangle
     */
    public Rectangle(Rect rect) {
        setRect(rect);
    }

    /**
     * Create a rectangle of specified size with top = 0 and left = 0
     *
     * @param rectSize Rectangle size
     */
    public Rectangle(Size rectSize) {
        setRect(new Rect(0, 0, (int) rectSize.width, (int) rectSize.height));
    }

    /**
     * Create a rectangle of specified size positioned at a specified location
     *
     * @param rectSize Rectangle size
     * @param topLeft  Top left corner of the rectangle
     */
    public Rectangle(Size rectSize, Point topLeft) {
        setRect(new Rect(0, 0, (int) rectSize.width, (int) rectSize.height));
    }

    /**
     * Create a rectangle based on a set of points
     *
     * @param points Set of points (at least 4) defining the rectangle
     */
    public Rectangle(Point[] points) {
        //Find top-left and bottom-right
        Point min = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        Point max = new Point(Double.MIN_VALUE, Double.MIN_VALUE);
        for (Point p : points) {
            if (p.x < min.x) {
                min.x = p.x;
            }
            if (p.y < min.y) {
                min.y = p.y;
            }
            if (p.x > max.x) {
                max.x = p.x;
            }
            if (p.y > max.y) {
                max.y = p.y;
            }
        }
        setRect(new Rect(min, max));
    }

    private void setRect(Rect rect) {
        this.rect = new RotatedRect(new Point(rect.tl().x + rect.size().width / 2,
                rect.tl().y + rect.size().height / 2), rect.size(), 0.0);
    }

    /**
     * Get the OpenCV rotated rectangle
     *
     * @return OpenCV rotated rectangle
     */
    public RotatedRect getRotatedRect() {
        return rect;
    }

    /**
     * Get the OpenCV rectangle
     *
     * @return OpenCV rectangle
     */
    public Rect getBoundingRect() {
        return rect.boundingRect();
    }

    private Size size() {
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
     * Get the angle of inclination of the rectangle
     *
     * @return Angle of inclination
     */
    public double angle() {
        return rect.angle;
    }

    /**
     * Get the center of the rectangle
     *
     * @return Center of the rectangle
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
     * Get the area of the rectangle
     *
     * @return Area of the rectangle = w * h
     */
    public double area() {
        return width() * height();
    }

    /**
     * Clip this rectangle to be entirely within a selected rectangle
     *
     * @param rectangle Rectangle to clip to
     * @return The clipped rectangle
     */
    public Rectangle clip(Rectangle rectangle) {
        return new Rectangle(Math.max(rectangle.top(), this.top()),
                Math.max(rectangle.left(), this.left()),
                Math.min(rectangle.bottom(), this.bottom()),
                Math.min(rectangle.right(), this.right()));
    }

    /**
     * Returns true if the ellipse is ENTIRELY inside the contour
     *
     * @param contour The contour to test against
     * @return True if the ellipse is entirely inside the contour, false otherwise
     */
    public boolean isInside(Contour contour) {
        //TODO this algorithm checks for entirety; make an isEntirelyInside() and isPartiallyInside()
        return left() >= contour.left() && right() <= contour.right() &&
                top() >= contour.top() && bottom() <= contour.bottom();
    }

    /**
     * Transpose this rectangle so that x becomes y and vice versa
     *
     * @return Transposed rectangle instance
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public Rectangle transpose() {
        return new Rectangle(new Point(rect.center.y, rect.center.x), rect.size.height, rect.size.width);
    }

    @Override
    public String toString() {
        return "rows: " + top() + " " + bottom() + " cols: " + left() + " " + right();
    }
}
