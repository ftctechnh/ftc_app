package org.swerverobotics.library.interfaces;

/**
 * NavUtil is a collection of utilities that provide useful manipulations of
 * objects related to navigation. It is typically most convenient to import these functions
 * statically using:
 *
 * <pre>import static org.swerverobotics.library.interfaces.NavUtil.*;</pre>
 */
public class NavUtil
    {
    //----------------------------------------------------------------------------------------------
    // Arithmetic: some handy helpers
    //----------------------------------------------------------------------------------------------

    public static Position     plus(Position a, Position b)         { return new Position    (a.x      + b.x,      a.y      + b.y,      a.z      + b.y,      Math.max(a.nanoTime, b.nanoTime)); }
    public static Velocity     plus(Velocity a, Velocity b)         { return new Velocity    (a.velocX + b.velocX, a.velocY + b.velocY, a.velocZ + b.velocZ, Math.max(a.nanoTime, b.nanoTime)); }
    public static Acceleration plus(Acceleration a, Acceleration b) { return new Acceleration(a.accelX + b.accelX, a.accelY + b.accelY, a.accelZ + b.accelZ, Math.max(a.nanoTime, b.nanoTime)); }

    public static Position     minus(Position a, Position b)         { return new Position    (a.x      - b.x,      a.y      - b.y,      a.z      - b.y,      Math.max(a.nanoTime, b.nanoTime)); }
    public static Velocity     minus(Velocity a, Velocity b)         { return new Velocity    (a.velocX - b.velocX, a.velocY - b.velocY, a.velocZ - b.velocZ, Math.max(a.nanoTime, b.nanoTime)); }
    public static Acceleration minus(Acceleration a, Acceleration b) { return new Acceleration(a.accelX - b.accelX, a.accelY - b.accelY, a.accelZ - b.accelZ, Math.max(a.nanoTime, b.nanoTime)); }

    public static Position     scale(Position p, double scale)       { return new Position    (p.x      * scale, p.y      * scale, p.z      * scale, p.nanoTime); }
    public static Velocity     scale(Velocity v, double scale)       { return new Velocity    (v.velocX * scale, v.velocY * scale, v.velocZ * scale, v.nanoTime); }
    public static Acceleration scale(Acceleration a, double scale)   { return new Acceleration(a.accelX * scale, a.accelY * scale, a.accelZ * scale, a.nanoTime); }

    public static Position     integrate(Velocity v, double dt)      { return new Position(v.velocX * dt, v.velocY * dt, v.velocZ * dt, v.nanoTime); }
    public static Velocity     integrate(Acceleration a, double dt)  { return new Velocity(a.accelX * dt, a.accelY * dt, a.accelZ * dt, a.nanoTime); }

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
        double duration = (cur.nanoTime - prev.nanoTime) * 1e-9;
        Velocity meanVelocity = scale(plus(cur, prev), 0.5);
        return integrate(meanVelocity, duration);
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
        double duration = (cur.nanoTime - prev.nanoTime) * 1e-9;
        Acceleration meanAcceleration = scale(plus(cur,prev), 0.5);
        return integrate(meanAcceleration, duration);
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private NavUtil() { }
    }
