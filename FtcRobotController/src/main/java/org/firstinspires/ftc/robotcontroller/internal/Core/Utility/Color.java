package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

/**
 * Created by pmkf2 on 9/2/2018.
 */


public class Color
{
    public enum ColorID
    {
        RED("RED"),
        BLUE("BLUE"),
        GREEN("GREEN"),
        WHITE("WHITE"),
        UNKNOWN("UNKNOWN");

        //private Scalar scalar; //scalar val
        private String name; //name of the color



        //Constructor for Color object

        ColorID(final String NAME)
        {
            name = NAME;
        }

        //returns string val of current enum
        public String asString()
        {
            return name;
        }

    }

}
