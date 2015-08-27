package org.swerverobotics.library.interfaces;

/**
 * 
 */
public class AngularVelocity
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // Units: radians per second
    public double rpsX;
    public double rpsY;
    public double rpsZ;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public AngularVelocity()
        {
        this(0,0,0);
        }
    public AngularVelocity(double rpsX, double rpsY, double rpsZ)
        {
        this.rpsX = rpsX;
        this.rpsY = rpsY;
        this.rpsZ = rpsZ;
        }
    public AngularVelocity(double[] xyz)
        {
        this(xyz[0], xyz[1], xyz[2]);
        }
    }
