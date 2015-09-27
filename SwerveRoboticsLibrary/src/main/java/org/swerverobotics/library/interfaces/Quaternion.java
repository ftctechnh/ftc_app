package org.swerverobotics.library.interfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A Quaternion can indicate an orientation in three-space without the trouble of
 * possible gimbal-lock.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Quaternion">https://en.wikipedia.org/wiki/Quaternion</a>
 * @see <a href="https://en.wikipedia.org/wiki/Gimbal_lock">https://en.wikipedia.org/wiki/Gimbal_lock</a>
 * @see <a href="https://www.youtube.com/watch?v=zc8b2Jo7mno">https://www.youtube.com/watch?v=zc8b2Jo7mno</a>
 * @see <a href="https://www.youtube.com/watch?v=mHVwd8gYLnI">https://www.youtube.com/watch?v=mHVwd8gYLnI</a>
 */
public class Quaternion
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public final double w;
    public final double x;
    public final double y;
    public final double z;

    /** the time on the System.nanoTime() clock at which the data was acquired */
    public final long nanoTime;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Quaternion()
        {
        this.w = 1;
        this.x = this.y = this.z = 0;
        this.nanoTime = 0;
        }
    public Quaternion(double w, double x, double y, double z)
        {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        this.nanoTime = 0;
        }
    public Quaternion(II2cDeviceClient.TimestampedData ts, double scale)
        {
        ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
        this.w = buffer.getShort() / scale;
        this.x = buffer.getShort() / scale;
        this.y = buffer.getShort() / scale;
        this.z = buffer.getShort() / scale;
        this.nanoTime = ts.nanoTime;
        }
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public double magnitude()
        {
        return Math.sqrt(w*w + x*x + y*y + z*z);
        }

    public Quaternion normalized()
        {
        double mag = this.magnitude();
        return new Quaternion(
            w / mag,
            x / mag,
            y / mag,
            z / mag);
        }

    public Quaternion congugate()
        {
        return new Quaternion(w, -x, -y, -z);
        }

    }
