package org.firstinspires.ftc.teamcode.components.scale;

/**
 * This ramp is built from two points and creates a ramp function that takes an x value
 * as an input and gives a y value as output. The ramp also can take in a y value and give the
 * inverse.
 */
abstract public class Ramp implements IScale
{
    private Point point1;
    private Point point2;

    /**
     * This ramp's function -- f(x) -- is built using the two points passed into the constructor.
     * From these points it builds a logarithmic function
     * @param point1 The first point the exponential ramp run through.
     * @param point2 The second point the exponential ramp runs through.
     * @see Point
     */
    public Ramp(Point point1, Point point2)
    {
        this.point1 = point1;
        this.point2 = point2;
    }

    /**
     * Gets the first point passed to the constructor
     * @return Returns the first point
     */
    public Point getPoint1() {
        return point1;
    }

    /**
     * Gets the second point passed to the constructor
     * @return Returns the second point
     */
    public Point getPoint2() {
        return point2;
    }

    /**
     * Scales the x
     * @param x The x to be scaled
     * @return Returns the scaled X
     */
    public double scaleX(double x)
    {
        if (!domainContains(x))
            return getValueOutsideOfDomain(x);
        else
            return scale(x);
    };

    /**
     * Checks that the domain contains an x value
     * @param x x point
     * @return Returns true if the x value is within the domain contains
     */
    private boolean domainContains(double x)
    {
        return x >= Math.min(point1.getX(), point2.getX()) &&
                x <= Math.max(point1.getX(), point2.getX());
    }

    /**
     * Gets value of the ramp when the value exceeds the minimum or maximum x value of the points
     * passed to the method
     * @param x x value
     * @return Returns the minimum or maximum y value depending
     */
    private double getValueOutsideOfDomain(double x)
    {
        if (point1.getX() < point2.getX())
            return x < point1.getX() ? point1.getY() : point2.getY();
        else
            return x > point1.getX() ? point1.getY() : point2.getY();
    }

    /**
     * Ramps the x value
     * @param x value to ramp
     * @return Returns the ramped value
     */
    abstract protected double scale(double x);

    /**
     * Scales the y
     * @param y The x to be scaled
     * @return Returns the scaled y
     */
    public double scaleY(double y)
    {
        if (!rangeContains(y))
            return getValueOutsideOfRange(y);
        else
            return inverse(y);
    }

    /**
     * Checks that the domain contains an y value
     * @param y x point
     * @return Returns true if the y value is within the range contains
     */
    private boolean rangeContains(double y)
    {
        return y >= Math.min(point1.getY(), point2.getY()) &&
                y <= Math.max(point1.getY(), point2.getY());
    }

    /**
     * Gets value of the ramp when the value exceeds the minimum or maximum x value of the points
     * passed to the method
     * @param y y value
     * @return Returns the minimum or maximum x value
     */
    private double getValueOutsideOfRange(double y)
    {
        if (point1.getY() < point2.getY())
            return y < point1.getY() ? point1.getX() : point2.getX();
        else
            return y > point1.getY() ? point1.getX() : point2.getX();
    }

    /**
     * Ramps the y value inversely
     * @param y y value to ramp
     * @return Returns the inverse value of the y
     */
    abstract protected double inverse(double y);
}
