package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Velocity represents a directed velocity in three-space.
 * Units are as the same as for Acceleration, but integrated for time.
 */
public class Velocity
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /** the velocity in the X direction */
    public final double velocX;
    /** the velocity in the Y direction */
    public final double velocY;
    /** the velocity in the Z direction */
    public final double velocZ;

    /** the time on the System.nanoTime() clock at which the data was acquired. If no
     * timestamp is associated with this particular set of data, this value is zero */
    public final long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Velocity()
        {
        this(0,0,0,0);
        }
    public Velocity(double velocX, double velocY, double velocZ, long nanoTime)
        {
        this.velocX = velocX;
        this.velocY = velocY;
        this.velocZ = velocZ;
        this.nanoTime = nanoTime;
        }
    public Velocity(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.velocX = buffer.getShort() / scale;
        this.velocY = buffer.getShort() / scale;
        this.velocZ = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }

    }
