package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Position represents a coordinate position in three-space.
 * Units are as the same as for Velocity, but integrated for time.
 */
public class Position
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /** the location in the X direction */
    public final double x;
    /** the location in the Y direction */
    public final double y;
    /** the location in the Z direction */
    public final double z;

    /** the time on the System.nanoTime() clock at which the data was acquired. If no
     * timestamp is associated with this particular set of data, this value is zero */
    public final long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Position()
        {
        this(0,0,0,0);
        }
    public Position(double x, double y, double z, long nanoTime)
        {
        this.x = x;
        this.y = y;
        this.z = z;
        this.nanoTime = nanoTime;
        }
    public Position(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.x = buffer.getShort() / scale;
        this.y = buffer.getShort() / scale;
        this.z = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }
    }
