package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Instances of MagneticFlux represent a three-dimensional magnetic strength vector. Units
 * are in tesla (NOT microtesla).
 */
public class MagneticFlux
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /** the flux in the X direction */
    public final double x;
    /** the flux in the Y direction */
    public final double y;
    /** the flux in the Z direction */
    public final double z;

    /** the time on the System.nanoTime() clock at which the data was acquired. If no
     * timestamp is associated with this particular set of data, this value is zero */
    public long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public MagneticFlux()
        {
        this(0,0,0, 0);
        }
    public MagneticFlux(double x, double y, double z, long nanoTime)
        {
        this.x = x;
        this.y = y;
        this.z = z;
        this.nanoTime = nanoTime;
        }
    public MagneticFlux(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.x = buffer.getShort() / scale;
        this.y = buffer.getShort() / scale;
        this.z = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }
    }
