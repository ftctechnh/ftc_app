/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.HardwareMapper;


/**
 * Wrapper class for the Modern Robotics I2C Range Sensor. Provides for easy retrieval of
 * sensor data.
 */
@SuppressWarnings("unused")
public final class MRRange
{
    private RobotBase _robot = null;                        // Robot we're working with
    private ModernRoboticsI2cRangeSensor _range = null;     // Our range sensor


    /**
     * Constructor- Creates a range sensor
     *
     * @param myRobot The robot we're working with
     */
    MRRange(RobotBase myRobot)
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
        HardwareMapper mapHelper = new HardwareMapper(_robot);  // Assists with mapping the sensor

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