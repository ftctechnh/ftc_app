package org.firstinspires.ftc.teamcode.components.scale;

public class LogarithmicRamp extends Ramp
{
    private double Q;
    private double R;

    public LogarithmicRamp(Point point1, Point point2)
    {
        super(point1, point2);
        if (point1.getX() == 0 || point2.getX() == 0) {
            throw new IllegalArgumentException("Logarithmic scales can start at x=0");
        }

        Q = (point2.getY() - point1.getY()) / Math.log(point2.getX() / point1.getX());
        R = Math.exp(point1.getY() / Q) / point1.getX();
    }

    @Override
    protected double scale(double x)
    {
        if (x == 0) {
            return getPoint1().getY();
        }
        return Q * Math.log(R * x);
    }

    @Override
    protected double inverse(double y)
    {
        return Math.exp(y / Q) / R;
    }
}
