package org.firstinspires.ftc.teamcode.components.scale;

public class LinearScale implements IScale
{
    private double scaleFactor;
    private double scaleOffset;

    public LinearScale(double scaleFactor, double scaleOffset)
    {
        this.scaleFactor = scaleFactor;
        this.scaleOffset = scaleOffset;
    }

    @Override
    public double scaleX(double x)
    {
        return scaleFactor * x + scaleOffset;
    }

    @Override
    public double scaleY(double y)
    {
        return (y - scaleOffset) / scaleFactor;
    }
}
