package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * <p>
 *      Class to manage the range sensor.
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
@SuppressWarnings("all")
final class SensorRange
{
    private Robot _robot = null;                            // Robot we're working with
    private ModernRoboticsI2cRangeSensor _range = null;     // Our range sensor


    /**
     * Constructor- Creates a range sensor
     *
     * @param myRobot The robot we're working with
     */
    SensorRange(Robot myRobot)
    {
        _robot = myRobot;
    }


    /**
     * Maps a range sensor
     *
     * @param NAME The name of the sensor as appears in the configuration file
     */
    void mapRange(final String NAME)
    {
        HardwareAux mapHelper = new HardwareAux(_robot);    // Assists with mapping the sensor

        _range = mapHelper.mapMRRange(NAME);
    }


    /**
     * Reads and returns a distance from the range sensor
     *
     * @param UNIT The unit at which to read the range sensor with
     *
     * @return The distance read from the range sensor
     */
    double distance(final DistanceUnit UNIT)
    {
        /*
            The range sensor reads 0 when everything is out of range. Because a small number such
            as 0 messes with logic, I opted to reassign that value to a large number that I like.
         */
        final double DEFAULT_DISTANCE = 1_024;

        double distance;                // The distance read by the sensor

        distance = _range.getDistance(UNIT);

        if(distance == 0)
            distance = DEFAULT_DISTANCE;

        return distance;
    }
}