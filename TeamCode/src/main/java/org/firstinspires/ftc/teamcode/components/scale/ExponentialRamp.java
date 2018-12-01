package org.firstinspires.ftc.teamcode.components.scale;

/**
 * This ramp is built from two points and creates an exponential function that takes an x value
 * as an input and gives a y value as output. The ramp also can take in a y value and give the
 * inverse.
 */
public class ExponentialRamp extends Ramp
{
    private double J;
    private double K;

    /**
     * This ramp's function -- f(x) -- is built using the two points passed into the constructor.
     * From these points it builds a exponential function
     * @param point1 The first point the exponential ramp run through.
     * @param point2 The second point the exponential ramp runs through.
     * @see Point
     */
    public ExponentialRamp(Point point1, Point point2)
    {
        super(point1, point2);
        K = Math.log(point1.getY() / point2.getY()) / (point1.getX() - point2.getX());
        J = point1.getY() / Math.exp(K * point1.getX());
    }

    /**
     * Uses the x value passed in to calculate the value of f(x)
     * @param x the x value to be scaled by the ramp
     * @return Returns the value of f(x)
     */
    @Override
    protected double scale(double x)
    {
        return J * Math.exp(K * x);
    }

    /**
     * Uses the y value passed in to calculate the inverse value of f(x)
     * @param y the y value to be inversely scaled by the ramp
     * @return Returns the inverse of f(y)
     */
    @Override
    protected double inverse(double y)
    {
        return (Math.log(y / J)) / K;
    }
}
