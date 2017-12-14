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


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.HardwareMapper;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilColor.Color;


/**
 * Wrapper class for the Modern Robotics I2C Color Sensor. Provides for easy retrieval of sensor
 * data.
 */
@SuppressWarnings("unused")
public final class MRColor
{
    private RobotBase _robot;                               // Robot we're working with
    private ModernRoboticsI2cColorSensor _color;            // Color sensor


    /**
     * Constructor- creates a color sensor
     *
     * @param myRobot The robot we're working with
     */
    MRColor(RobotBase myRobot)
    {
        _robot = myRobot;
    }


    /**
     * Maps a color sensor
     *
     * @param NAME Name of the sensor as appears in the configuration file
     */
    void mapColor(final String NAME , final int ADDRESS)
    {
        HardwareMapper mapHelper = new HardwareMapper(_robot);

        _color = mapHelper.mapMRColor(NAME , ADDRESS);
    }


    int alpha()
    {
        return _color.alpha();
    }


    /**
     * Returns the color seen by the sensor
     *
     * @return The color detected by the sensor
     */
    Color getColor()
    {
        final int BUFFER = 2;       // Minimum difference between RBG values before it matters

        if(_color.red() > _color.blue() + BUFFER)
            return Color.RED;

        else if(_color.blue() > _color.red() + BUFFER)
            return Color.BLUE;

        else if(_color.alpha() > 10)
            return Color.WHITE;

        else
            return Color.UNKNOWN;
    }


    /**
     * Toggles whether the LED is on or off
     *
     * @param LED_ON True if the LED is to be turned on, false if the LED is to be turned off
     */
    void toggleLED(final boolean LED_ON)
    {
        _color.enableLed(LED_ON);
    }
}