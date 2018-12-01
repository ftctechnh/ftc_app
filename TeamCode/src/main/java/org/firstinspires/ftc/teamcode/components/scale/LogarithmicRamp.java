package org.firstinspires.ftc.teamcode.components.scale;

/**
 * This ramp is built from two points and creates a logarithmic function that takes an x value
 * as an input and gives a y value as output. The ramp also can take in a y value and give the
 * inverse.
 */
public class LogarithmicRamp extends Ramp
{
    private double Q;
    private double R;

    /**
     * This ramp's function -- f(x) -- is built using the two points passed into the constructor.
     * From these points it builds a logarithmic function
     * @param point1 The first point the exponential ramp run through.
     * @param point2 The second point the exponential ramp runs through.
     * @see Point
     */
    public LogarithmicRamp(Point point1, Point point2)
    {
        super(point1, point2);
        if (point1.getX() == 0 || point2.getX() == 0) {
            throw new IllegalArgumentException("Logarithmic scales can start at x=0");
        }

        Q = (point2.getY() - point1.getY()) / Math.log(point2.getX() / point1.getX());
        R = Math.exp(point1.getY() / Q) / point1.getX();
    }

    /**
     * Uses the x value passed in to calculate the value of f(x)
     * @param x the x value to be scaled by the ramp
     * @return Returns the value of f(x)
     */
    @Override
    protected double scale(double x)
    {
        if (x == 0) {
            return getPoint1().getY();
        }
        return Q * Math.log(R * x);
    }

    /**
     * Uses the y value passed in to calculate the inverse value of f(x)
     * @param y the y value to be inversely scaled by the ramp
     * @return Returns the inverse of f(y)
     */
    @Override
    protected double inverse(double y)
    {
        return Math.exp(y / Q) / R;
    }
}
