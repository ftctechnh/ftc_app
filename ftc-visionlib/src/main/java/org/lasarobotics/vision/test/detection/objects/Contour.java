package org.lasarobotics.vision.test.detection.objects;

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

    MatOfPoint mat;

    public Contour(MatOfPoint data) {
        this.mat = data;
    }

    public Contour(MatOfPoint2f data) {
        this.mat = new MatOfPoint(data.toArray());
    }

    public MatOfPoint getData() {
        return mat;
    }

    public MatOfPoint2f getFloatData() {
        return new MatOfPoint2f(mat.toArray());
    }

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

    public Point center() {
        //TODO this is an UNWEIGHTED CENTER - which means that for unusual shapes, it can be inaccurate
        Point sum = new Point(0, 0);
        Point[] points = this.getPoints();

        for (Point p : points)
            sum = new Point(sum.x + p.x, sum.y + p.y);
        return new Point(sum.x / points.length, sum.y / points.length);
    }

    public double height() {
        return (int) size().height;
    }

    public double width() {
        return (int) size().width;
    }

    public double top() {
        return topLeft().y;
    }

    public double bottom() {
        return topLeft().y + size().height;
    }

    public double left() {
        return topLeft().x;
    }

    public double right() {
        return topLeft().x + size().width;
    }

    public Rect getBoundingRect() {
        return new Rect((int) top(), (int) left(), (int) width(), (int) height());
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
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        Point[] points = getPoints();
        for (Point p : points) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
        }

        return new Point(minX, minY);
    }

    public Size size() {
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

        return new Size(maxX - minX, maxY - minY);
    }

    public double arcLength(boolean closed) {
        return Imgproc.arcLength(getFloatData(), closed);
    }

    public Point[] getPoints() {
        return mat.toArray();
    }
}
