package org.lasarobotics.vision.test.detection.objects;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;

/**
 * Implements a single ellipse (acts like RotatedRect) with advanced measurement utilities
 */
public class Ellipse extends Detectable implements Comparable<Ellipse> {
    private final RotatedRect rect;

    public Ellipse(RotatedRect rect) {
        this.rect = rect;
    }

    public RotatedRect getRect() {
        return rect;
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

    /**
     * Gets the area of the ellipse
     *
     * @return Area = semi-major axis * semi-minor axis * PI
     */
    public double area() {
        return semiMajorAxis() * semiMinorAxis() * Math.PI;
    }

    public double majorAxis() {
        return Math.max(height(), width());
    }

    public double minorAxis() {
        return Math.min(height(), width());
    }

    public double semiMajorAxis() {
        return majorAxis() / 2;
    }

    public double semiMinorAxis() {
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

    /***
     * Compare ellipses by area
     *
     * @param another Another ellipse
     * @return 1 if this is larger, -1 if another is larger, 0 otherwise
     */
    @Override
    public int compareTo(@NotNull Ellipse another) {
        return this.area() > another.area() ? 1 : this.area() < another.area() ? -1 : 0;
    }
}
