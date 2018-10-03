package org.firstinspires.ftc.teamcode.components.scale;

public class ExponentialRamp extends Ramp
{
    private double J;
    private double K;

    public ExponentialRamp(Point point1, Point point2)
    {
        super(point1, point2);
        K = Math.log(point1.getY() / point2.getY()) / (point1.getX() - point2.getX());
        J = point1.getY() / Math.exp(K * point1.getX());
    }

    @Override
    protected double scale(double x)
    {
        return J * Math.exp(K * x);
    }

    @Override
    protected double inverse(double y)
    {
        return (Math.log(y / J)) / K;
    }
}
