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

    /** the time on the System.nanoTime() clock at which the data was acquired */
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

    //----------------------------------------------------------------------------------------------
    // Arithmetic
    //----------------------------------------------------------------------------------------------

    public Velocity plus(Velocity him)
        {
        return new Velocity(
            this.velocX + him.velocX,
            this.velocY + him.velocY,
            this.velocZ + him.velocZ,
            Math.max(this.nanoTime, him.nanoTime));
        }

    //----------------------------------------------------------------------------------------------
    // Integration
    //----------------------------------------------------------------------------------------------

    /**
     * Integrate between two velocities to determine a change in position
     * @param prev   the previously measured velocity
     * @return       the change in position between the previous position and the receiver
     */
    public Position integrate(Velocity prev)
        {
        // We assume that the mean of the two velocities has been acting during the entire interval
        double sInterval = (this.nanoTime - prev.nanoTime) * 1e-9;
        return new Position(
                (this.velocX + prev.velocX) * 0.5 * sInterval,
                (this.velocY + prev.velocY) * 0.5 * sInterval,
                (this.velocZ + prev.velocZ) * 0.5 * sInterval,
                this.nanoTime
        );
        }
    }
