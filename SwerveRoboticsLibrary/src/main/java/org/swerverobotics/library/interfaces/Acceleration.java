package org.swerverobotics.library.interfaces;

/**
 * 
 */
public class Acceleration
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public double x;
    public double y;
    public double z;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Acceleration()
        {
        this(0,0,0);
        }
    public Acceleration(double x, double y, double z)
        {
        this.x = x;
        this.y = y;
        this.z = z;
        }
    public Acceleration(double[] xyz)
        {
        this(xyz[0], xyz[1], xyz[2]);
        }
    }
