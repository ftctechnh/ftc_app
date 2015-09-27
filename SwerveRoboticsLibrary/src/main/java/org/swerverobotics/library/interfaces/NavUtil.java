package org.swerverobotics.library.interfaces;

/**
 * NavUtil is a collection of utilities that provide useful manipulations of
 * objects related to navigation. It is typically most convenient to import these functions
 * statically using:
 *
 * <pre></pre>import static org.swerverobotics.library.interfaces.NavUtil.*;</pre>
 */
public class NavUtil
    {
    //----------------------------------------------------------------------------------------------
    // Arithmetic
    //----------------------------------------------------------------------------------------------

    public static Position plus(Position a, Position b)
        {
        return new Position(
            a.x + b.x,
            a.y + b.y,
            a.z + b.y,
            Math.max(a.nanoTime, b.nanoTime));
        }
    
    public static Velocity plus(Velocity a, Velocity b)
        {
        return new Velocity(
            a.velocX + b.velocX,
            a.velocY + b.velocY,
            a.velocZ + b.velocZ,
            Math.max(a.nanoTime, b.nanoTime));
        }

    //----------------------------------------------------------------------------------------------
    // Integration
    //----------------------------------------------------------------------------------------------

    /**
     * Integrate between two velocities to determine a change in position using an assumption
     * that the mean of the velocities has been acting the entire interval.

     * @param cur    the current velocity
     * @param prev   the previous velocity
     * @return       an approximation to the change in position over the interval
     *
     * @see <a href="https://en.wikipedia.org/wiki/Simpson%27s_rule">Simpson's Rule</a>
     */
    public static Position meanIntegrate(Velocity cur, Velocity prev)
        {
        // We assume that the mean of the two velocities has been acting during the entire interval
        double sInterval = (cur.nanoTime - prev.nanoTime) * 1e-9;
        return new Position(
                (cur.velocX + prev.velocX) * 0.5 * sInterval,
                (cur.velocY + prev.velocY) * 0.5 * sInterval,
                (cur.velocZ + prev.velocZ) * 0.5 * sInterval,
                cur.nanoTime
                );
        }
    
    /**
     * Integrate between two accelerations to determine a change in velocity using an assumption
     * that the mean of the accelerations has been acting the entire interval.
     *
     * @param cur    the current acceleration
     * @param prev   the previous acceleration
     * @return       an approximation to the change in velocity over the interval
     *
     * @see <a href="https://en.wikipedia.org/wiki/Simpson%27s_rule">Simpson's Rule</a>
     */
    public static Velocity meanIntegrate(Acceleration cur, Acceleration prev)
        {
        double sInterval = (cur.nanoTime - prev.nanoTime) * 1e-9;
        return new Velocity(
                (cur.accelX + prev.accelX) * 0.5 * sInterval,
                (cur.accelY + prev.accelY) * 0.5 * sInterval,
                (cur.accelZ + prev.accelZ) * 0.5 * sInterval,
                cur.nanoTime
                );
        }
    
    }
