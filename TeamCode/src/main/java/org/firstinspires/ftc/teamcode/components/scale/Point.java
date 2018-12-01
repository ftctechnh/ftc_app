package org.firstinspires.ftc.teamcode.components.scale;

public class Point
{
    private double x;
    private double y;

    /**
     * Makes a point object
     * @param x the x value
     * @param y the y value
     */
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x value
     * @return the x value
     */
    public double getX()
    {
        return x;
    }

    /**
     * Gets the y value
     * @return the y value
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the x value
     * @param x the new x value to be set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y value
     * @param y the new y value to be set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the origin point
     * @return the origin point
     */
    public static Point getOriginPoint() {
        return new Point(0,0);
    }
}
