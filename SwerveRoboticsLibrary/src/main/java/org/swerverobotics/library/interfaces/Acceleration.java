package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Acceleration represents a directed acceleration in three-space.
 * Units are as specified in sensor initialization. The time at which the data was
 * acquired is provided so as to facilitate integration of accelerations.
 */
public class Acceleration
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /** the acceleration in the X direction */
    public double accelX;
    /** the acceleration in the Y direction */
    public double accelY;
    /** the acceleration in the Z direction */
    public double accelZ;

    /** the time on the System.nanoTime() clock at which the data was acquired. If no
     * timestamp is associated with this particular set of data, this value is zero */
    public long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Acceleration()
        {
        this(0,0,0,0);
        }
    public Acceleration(double accelX, double accelY, double accelZ, long nanoTime)
        {
        this.accelX = accelX;
        this.accelY = accelY;
        this.accelZ = accelZ;
        this.nanoTime = nanoTime;
        }
    public Acceleration(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.accelX = buffer.getShort() / scale;
        this.accelY = buffer.getShort() / scale;
        this.accelZ = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }

    }
