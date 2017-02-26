/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.detection.objects;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Implements a single contour (MatOfPoint) with advanced measurement utilities
 */
public class Contour extends Detectable {

    private final MatOfPoint mat;
    private Point topLeft = null;
    private Size size = null;

    /**
     * Instantiate a contour from an OpenCV matrix of points (float)
     *
     * @param data OpenCV matrix of points
     */
    public Contour(MatOfPoint data) {
        this.mat = data;
    }

    /**
     * Instantiate a contour from an OpenCV matrix of points (double)
     *
     * @param data OpenCV matrix of points
     */
    public Contour(MatOfPoint2f data) {
        this.mat = new MatOfPoint(data.toArray());
    }

    private void calculate() {
        if (topLeft != null)
            return;

        //Calculate size and topLeft at the same time
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        Point[] points = getPoints();
        for (Point p : points) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (p.x > maxX) {
                maxX = p.x;
            }
            if (p.y > maxY) {
                maxY = p.y;
            }
        }

        size = new Size(maxX - minX, maxY - minY);
        topLeft = new Point(minX, minY);
    }

    /**
     * Get data as a float
     *
     * @return OpenCV matrix of points
     */
    public MatOfPoint getData() {
        return mat;
    }

    /**
     * Get data as a double
     *
     * @return OpenCV matrix of points
     */
    public MatOfPoint2f getDoubleData() {
        return new MatOfPoint2f(mat.toArray());
    }

    /**
     * Get the number of points
     *
     * @return Number of points, i.e. length
     */
    public int count() {
        return getData().rows();
    }

    /**
     * Get the area of the contour
     * <p/>
     * A highly precise method that integrates the contour with respect to its arc length.
     *
     * @return Area of the contour
     */
    public double area() {
        return Imgproc.contourArea(mat);
    }

    /**
     * Tests if the contour is closed (convex)
     *
     * @return True if closed (convex), false otherwise
     */
    public boolean isClosed() {
        return Imgproc.isContourConvex(mat);
    }

    /**
     * Get the centroid of the object (a weighted center)
     *
     * @return Centroid of the object as a point
     */
    public Point centroid() {
        //C_{\mathrm x} = \frac{1}{6A}\sum_{i=0}^{n-1}(x_i+x_{i+1})(x_i\ y_{i+1} - x_{i+1}\ y_i)
        //C_{\mathrm y} = \frac{1}{6A}\sum_{i=0}^{n-1}(y_i+y_{i+1})(x_i\ y_{i+1} - x_{i+1}\ y_i)

        if (count() < 2)
            return center();

        double xSum = 0.0;
        double ySum = 0.0;
        double area = 0.0;
        Point[] points = this.getPoints();

        for (int i = 0; i < points.length - 1; i++) {
            //cross product, (signed) double area of triangle of vertices (origin,p0,p1)
            double signedArea = (points[i].x * points[i + 1].y) - (points[i + 1].x * points[i].y);
            xSum += (points[i].x + points[i + 1].x) * signedArea;
            ySum += (points[i].y + points[i + 1].y) * signedArea;
            area += signedArea;
        }

        if (area == 0)
            return center();

        double coefficient = 3 * area;
        return new Point(xSum / coefficient, ySum / coefficient);
    }

    /**
     * Get the center of the object
     *
     * @return Center of the object as a point
     */
    public Point center() {
        calculate();
        return new Point(topLeft.x + (size.width / 2), topLeft.y + (size.height / 2));
    }

    public double height() {
        calculate();
        return (int) size.height;
    }

    public double width() {
        calculate();
        return (int) size.width;
    }

    public double top() {
        calculate();
        return topLeft.y;
    }

    public double bottom() {
        calculate();
        return topLeft.y + size.height;
    }

    public double left() {
        calculate();
        return topLeft.x;
    }

    public double right() {
        calculate();
        return topLeft.x + size.width;
    }

    /**
     * Get a bounding rectangle surrounding the contour
     *
     * @return Returns an OpenCV rectangle
     */
    public Rect getBoundingRect() {
        return new Rect((int) top(), (int) left(), (int) width(), (int) height());
    }

    /**
     * Get a bounding rectangle surrounding the contour
     *
     * @return Returns an FTCVision rectangle
     */
    public Rectangle getBoundingRectangle() {
        calculate();
        return new Rectangle(center(), size.width, size.height);
    }

    public Point bottomRight() {
        return new Point(right(), bottom());
    }

    /**
     * Gets the top-left corner of the contour
     *
     * @return The top left corner of the contour
     */
    public Point topLeft() {
        calculate();
        return topLeft;
    }

    /**
     * Get the size of the contour i.e. a width and height
     *
     * @return Size as (width, height)
     */
    public Size size() {
        calculate();
        return size;
    }

    /**
     * Offset the object, translating it by a specific offset point
     *
     * @param offset Point to offset by, e.g. (1, 0) would move object 1 px right
     */
    @Override
    public void offset(Point offset) {
        Point[] points = this.getPoints();

        for (int i = 0; i < points.length; i++)
            points[i] = new Point(points[i].x + offset.x, points[i].y + offset.y);

        mat.fromArray(points);
    }

    /**
     * Returns true if the contour is MOSTLY inside a given area
     * That is, the centroid is within the bounded area
     *
     * @param rect Rectangle to check agains
     * @return True if the contour is mostly inside the rectangle, false otherwise
     */
    public boolean isMostlyInside(Rectangle rect) {
        return centroid().inside(rect.getBoundingRect());
    }

    /**
     * Get the arc length of the contour
     *
     * @param closed True if the contour should be calculated as closed
     * @return Arc length
     */
    public double arcLength(boolean closed) {
        return Imgproc.arcLength(getDoubleData(), closed);
    }

    /**
     * Get the point array of the contour
     *
     * @return Point array of contour
     */
    private Point[] getPoints() {
        return mat.toArray();
    }
}
