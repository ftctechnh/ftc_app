package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;


/**
 * <p>
 *      Class to manage the color sensor.
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
final class SensorColor
{
    private Robot _robot;                                   // Robot w'ere working with
    private ModernRoboticsI2cColorSensor _color;            // Color sensor


    /**
     * Colors we're looking for with the sensor
     */
    enum Color
    {
        RED("RED") ,
        BLUE("BLUE") ,
        WHITE("WHITE") ,
        UNKNOWN("UNKNOWN");

        private String name;

        Color(String s)
        {
            name = s;
        }


        /**
         * @return Returns the corresponding string value of the enumeration
         */
        String asString()
        {
            return this.name;
        }
    }


    /**
     * Constructor- creates a color sensor
     *
     * @param myRobot The robot we're working with
     */
    SensorColor(Robot myRobot)
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
        HardwareAux mapHelper = new HardwareAux(_robot);

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


    void enableLED(final boolean LED_ON)
    {
        _color.enableLed(LED_ON);
    }
}