/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;


import org.opencv.core.Scalar;

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
        // Scalars are done in BGR (blue, green, red)
        RED("RED"     , new Scalar(0   , 0   , 255)) ,
        BLUE("BLUE"   , new Scalar(255 , 0   , 0))   ,
        GREEN("GREEN" , new Scalar(0   , 128 , 0))   ,
        WHITE("WHITE" , new Scalar(255 , 255 , 255)) ,
        UNKNOWN("UNKNOWN");


        private Scalar _scalar;             // Scalar value of the color
        private String _name;               // Name of the color


        /**
         * Constructor for a Color type object, assigns a _name to the color
         *
         * @param NAME String name to set to the color
         */
        Color(final String NAME)
        {
            _name = NAME;
        }


        /**
         * Constructor for a Color type object, assigns a _name to the color
         *
         * @param NAME String name to set to the color
         * @param SCALAR Scalar color values
         */
        Color(final String NAME , final Scalar SCALAR)
        {
            _name = NAME;
            _scalar = SCALAR;
        }


        /**
         * @return Returns the corresponding string value of the enumeration
         */
        public String asString()
        {
            return this._name;
        }


        /**
         * @return Returns OpenCV Scalar value
         */
        public Scalar scalar()
        {
            return _scalar;
        }
    }
}
