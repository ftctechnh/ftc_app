package org.firstinspires.ftc.teamcode.components.scale;

abstract public class Ramp implements IScale
{
    private Point point1;
    private Point point2;

    public Ramp(Point point1, Point point2)
    {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Point getPoint2() {
        return point2;
    }

    public double scaleX(double x)
    {
        if (!domainContains(x))
            return getValueOutsideOfDomain(x);
        else
            return scale(x);
    };

    private boolean domainContains(double x)
    {
        return x >= Math.min(point1.getX(), point2.getX()) &&
                x <= Math.max(point1.getX(), point2.getX());
    }

    private double getValueOutsideOfDomain(double x)
    {
        if (point1.getX() < point2.getX())
            return x < point1.getX() ? point1.getY() : point2.getY();
        else
            return x > point1.getX() ? point1.getY() : point2.getY();
    }

    abstract protected double scale(double x);

    public double scaleY(double y)
    {
        if (!rangeContains(y))
            return  getValueOutsideOfRange(y);
        else
            return inverse(y);
    }

    private boolean rangeContains(double y)
    {
        return y >= Math.min(point1.getY(), point2.getY()) &&
                y <= Math.max(point1.getY(), point2.getY());
    }

    private double getValueOutsideOfRange(double y)
    {
        if (point1.getY() < point2.getY())
            return y < point1.getY() ? point1.getX() : point2.getX();
        else
            return y > point1.getY() ? point1.getX() : point2.getX();
    }

    abstract protected double inverse(double y);
}
