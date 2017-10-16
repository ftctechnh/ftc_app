/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.teamcode.Core.Utility;


/**
 * Utility class that manages color- could be applicable to both color sensors and things like
 * alliance color.
 */
@SuppressWarnings("unused")
public class UtilColor
{
    /**
     * Enumeration that stores colors
     */
    public enum Color
    {
        RED("RED") ,
        BLUE("BLUE") ,
        GREEN("GREEN") ,
        WHITE("WHITE") ,
        UNKNOWN("UNKNOWN");

        private String name;                // Name of the color


        /**
         * Constructor for a Color type object, assigns a name to the color
         *
         * @param NAME String name to set to the color
         */
        Color(final String NAME)
        {
            name = NAME;
        }


        /**
         * @return Returns the corresponding string value of the enumeration
         */
        String asString()
        {
            return this.name;
        }
    }
}
