package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * AngularVelocity represents a rotation rate in three-space. Units are as specified
 * in sensor initialization, either radians/second or degrees/second.
 */
public class AngularVelocity
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /** the rotational rate about the X axis */
    public final double rateX;
    /** the rotational rate about the Y axis */
    public final double rateY;
    /** the rotational rate about the Z axis */
    public final double rateZ;

    /** the time on the System.nanoTime() clock at which the data was acquired. If no
     * timestamp is associated with this particular set of data, this value is zero */
    public final long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public AngularVelocity()
        {
        this(0,0,0, 0);
        }
    public AngularVelocity(double rateX, double rateY, double rateZ, long nanoTime)
        {
        this.rateX = rateX;
        this.rateY = rateY;
        this.rateZ = rateZ;
        this.nanoTime = nanoTime;
        }
    public AngularVelocity(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.rateX = buffer.getShort() / scale;
        this.rateY = buffer.getShort() / scale;
        this.rateZ = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }
    }
