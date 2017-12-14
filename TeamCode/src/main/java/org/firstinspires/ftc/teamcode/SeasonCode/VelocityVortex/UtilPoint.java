package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


/**
 * <p>
 *      Utility class that deals with coordinates in R2 (2d)
 * </p>
 *
 *
 * <p>
 *      The class allows for smooth and quick conversion between cartesian and polar systems.
 *      It is important to understand that, although the constructor asks for a coordinate type,
 *      it only needs the information to know which values are already given and which values
 *      need to be calculated. Once you call the constructor, the coordinate type does not matter.
 * </p>
 *
 * <p>
 *     Assume all angular measurements are in degrees.
 *     Polar coordinates operate on a system where the angle is from 0 to 360 and the radius
 *     is always positive. 0 is "east", 90 is "north", etc.
 *     When setting values using accessors, the other values are changed automatically.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("unused")
final class UtilPoint
{
    // Polar coordinates (radius , angle)
    private double _r;
    private double _theta;

    // Cartesian coordinates (x , y)
    private double _x;
    private double _y;


    /**
     * Deals with the types of coordinates in R2 that this class will manage.
     */
    enum Type
    {
        POLAR ,
        CARTESIAN
    }


    /**
     * Takes coordinate values and a coordinate type and calculates the values for the other
     * coordinate type. It does so by calling a method.
     *
     * @param LEFT The left side of the coordinate
     * @param RIGHT The right side of the coordinate
     * @param TYPE The type of coordinate, Polar or Cartesian
     */
    UtilPoint(final double LEFT , final double RIGHT , final Type TYPE)
    {
        setCoordinates(LEFT , RIGHT , TYPE);
    }


    /**
     * Takes coordinate values and a coordinate type and calculates the values for the other
     * coordinate type.
     *
     * @param LEFT The left side of the coordinate
     * @param RIGHT The right side of the coordinate
     * @param TYPE The type of coordinate, Polar or Cartesian
     */
    private void setCoordinates(final double LEFT , final double RIGHT , final Type TYPE)
    {
        if(TYPE == Type.POLAR)
        {
            _r = LEFT;
            _theta = RIGHT;

            _x = _r * Math.cos(Math.toRadians(_theta));
            _y = _r * Math.sin(Math.toRadians(_theta));
        }
        else
        {
            _x = LEFT;
            //noinspection SuspiciousNameCombination
            _y = RIGHT;

            _r = Math.hypot(_x , _y);
            _theta = Math.toDegrees(Math.atan2(_y , _x));
        }

        // Constricts angle between 0 and 360
        _theta = UtilBasic.trimAngle((int)_theta);
    }


    /**
     * Get the polar coordinate radius.
     *
     * @return Returns the polar coordinate radius.
     */
    double r()
    {
        return _r;
    }


    /**
     * Get the polar coordinate angle.
     *
     * @return Returns the polar coordinate angle.
     */
    double theta()
    {
        return _theta;
    }


    /**
     * Sets the polar coordinate angle to a new value and recalculates the cartesian coordinates.
     *
     * @param NEW The value to change the polar coordinate angle to.
     */
    void setTheta(final double NEW)
    {
        _theta = NEW;
        setCoordinates(_r , _theta , Type.POLAR);
    }


    /**
     * Get the cartesian coordinate x value.
     *
     * @return Returns the cartesian coordinate x value.
     */
    double x()
    {
        return _x;
    }


    /**
     * Get the cartesian coordinate y value.
     *
     * @return Returns the cartesian coordinate y value.
     */
    double y()
    {
        return _y;
    }
}