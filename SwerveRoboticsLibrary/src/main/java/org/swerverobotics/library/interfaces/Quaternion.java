package org.swerverobotics.library.interfaces;

import java.util.Queue;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Quaternion">https://en.wikipedia.org/wiki/Quaternion</a>
 */
public class Quaternion
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public double w;
    public double x;
    public double y;
    public double z;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Quaternion()
        {
        this.w = 1;
        this.x = this.y = this.z = 0;
        }
    public Quaternion(double w, double x, double y, double z)
        {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public double magnitude()
        {
        return Math.sqrt(w*w + x*x + y*y + z*z);
        }

    public void normalize()
        {
        double mag = this.magnitude();
        w /= mag;
        x /= mag;
        y /= mag;
        z /= mag;
        }
    
    public Quaternion congugate()
        {
        Quaternion result = new Quaternion();
        result.w =  w;
        result.x = -x;
        result.y = -y;
        result.z = -y;
        return result;
        }
    
    }
