package org.swerverobotics.library.interfaces;

/**
 * Instances of EulerAngles represent a direction in three-dimensional space by way of rotations
 */
public class EulerAngles
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // Angles are in rotation order (heading, roll, pitch) and are right-handed 
    // about their respective axes
    
    // Units: radians
    public double heading;  // rotation about Z
    public double roll;     // rotation about Y
    public double pitch;    // rotation about X

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public EulerAngles()
        {
        this(0,0,0);
        }
    public EulerAngles(double heading, double roll, double pitch)
        {
        this.heading = heading;
        this.roll = roll;
        this.pitch = pitch;
        }
    public EulerAngles(double[] angles)
        {
        this(angles[0], angles[1], angles[2]);
        }
    }
