package org.lasarobotics.vision.test.detection.objects;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;

/**
 * Implements a single rectangle object with advanced measurement capabilities
 */
public class Rectangle extends Detectable {
    RotatedRect rect = new RotatedRect();

    public Rectangle(RotatedRect rect) {
        this.rect = rect;
    }

    public Rectangle(Rect rect) {
        setRect(rect);
    }

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

    public RotatedRect getRotatedRect() {
        return rect;
    }

    public Rect getBoundingRect() {
        return rect.boundingRect();
    }

    public Size size() {
        return rect.size;
    }

    public double height() {
        return size().height;
    }

    public double width() {
        return size().width;
    }

    public double angle() {
        return rect.angle;
    }

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

    public double area() {
        return width() * height();
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
}
