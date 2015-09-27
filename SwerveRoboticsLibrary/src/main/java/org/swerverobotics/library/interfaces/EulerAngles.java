package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Instances of EulerAngles represent a direction in three-dimensional space by way of rotations.
 * Units are as specified in sensor initiation. Angles are in rotation order (heading, then roll,
 * then pitch) and are right-handed about their respective axes.
 */
public class EulerAngles
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /** the rotation about the Z axis */
    public final double heading;
    /** the rotation about the Y axis */
    public final double roll;
    /** the rotation about the X axix */
    public final double pitch;

    /** the time on the System.nanoTime() clock at which the data was acquired. If no
     * timestamp is associated with this particular set of data, this value is zero */
    public final long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public EulerAngles()
        {
        this(0,0,0, 0);
        }
    public EulerAngles(double heading, double roll, double pitch, long nanoTime)
        {
        this.heading  = heading;
        this.roll     = roll;
        this.pitch    = pitch;
        this.nanoTime = nanoTime;
        }
    public EulerAngles(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.heading = buffer.getShort() / scale;
        this.roll    = buffer.getShort() / scale;
        this.pitch   = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }
    }
