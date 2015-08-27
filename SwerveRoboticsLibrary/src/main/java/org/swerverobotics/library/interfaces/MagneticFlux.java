package org.swerverobotics.library.interfaces;

/**
 * 
 */
public class MagneticFlux
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // Units: tesla
    public double x;
    public double y;
    public double z;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public MagneticFlux()
        {
        this(0,0,0);
        }
    public MagneticFlux(double x, double y, double z)
        {
        this.x = x;
        this.y = y;
        this.z = z;
        }
    public MagneticFlux(double[] xyz)
        {
        this(xyz[0], xyz[1], xyz[2]);
        }
    }
