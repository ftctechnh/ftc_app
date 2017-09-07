package org.firstinspires.ftc.teamcode.Core.Utility;


/**
 * <p>
 *      Utility class that deals with color.
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
