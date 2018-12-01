package org.firstinspires.ftc.teamcode.components.scale;

public class LinearScale implements IScale
{
    private double scaleFactor;
    private double scaleOffset;

    /**
     * Creates the linear scale factor and offset
     * @param scaleFactor The scale factor
     * @param scaleOffset The scale offset
     */
    public LinearScale(double scaleFactor, double scaleOffset)
    {
        this.scaleFactor = scaleFactor;
        this.scaleOffset = scaleOffset;
    }

    /**
     * Scales the x
     * @param x The x to be scaled
     * @return the scaled X
     */
    @Override
    public double scaleX(double x)
    {
        return scaleFactor * x + scaleOffset;
    }

    /**
     * Scales the y
     * @param y The y value for be scaled
     * @return the scaled y
     */
    @Override
    public double scaleY(double y)
    {
        return (y - scaleOffset) / scaleFactor;
    }
}
