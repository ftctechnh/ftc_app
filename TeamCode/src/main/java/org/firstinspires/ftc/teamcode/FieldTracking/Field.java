package org.firstinspires.ftc.teamcode.fieldtracking;


/**
 * Created by ROUS on 3/4/2017.
 */
public class Field {
    /**
     * This class exists to describe the coordinates of objects and fixed positions on the field.
     *
     *                            -X
     *     red             gears          tools
     *         +--------------O----+---------O---------+
     *         ||  /               |                  /|
     *         ||/                 |                /  |
     *         |                   |             /     |
     *  +----+ |                   |          /        O legos
     *  |red | |                   |       /           |
     *  |    | |                   |    /              |
     *  |    | |                +--+--+                |
     *  | -Y | +----------------+  +  +----------------+   Y
     *  |    | |                +--+--+                |
     *  |    | |              /    |                   O wheels
     *  |    | |           /       |                   |
     *  +----+ |        /          |                   |
     *         |     /             |                   |
     *         |  /                |                 /||
     *         |/                  |               /  ||
     *         +-------------------+-------------------+
     *                  +---------------------+           blue
     *                  |          X   blue   |
     *                  +---------------------+
     *
     *
     *  NOTE: all coordinates and dimensions in inches, angles in Degrees
     *
     */

    public static double   FIELD_WIDTH_D = 144.0;
    public static double   FIELD_HALF_WIDTH_D = FIELD_WIDTH_D/2.0;
    public static double   NEAR_BEACON_OFFSET = 12.0;
    public static double   FAR_BEACON_OFFSET = 36.0;

    public static Vector2d BLUE_CENTER_VORTEX_XY = DirectionDistance.CreateVector2d(45.0, 15.0);
    public static Vector2d BLUE_WHEELS_BEACON_XY = new Vector2d(+NEAR_BEACON_OFFSET, +FIELD_HALF_WIDTH_D);
    public static Vector2d BLUE_LEGOS_BEACON_XY = new Vector2d(-FAR_BEACON_OFFSET, +FIELD_HALF_WIDTH_D);
    public static Vector2d RED_GEARS_BEACON_XY = new Vector2d(-FIELD_HALF_WIDTH_D, -FAR_BEACON_OFFSET );
    public static Vector2d RED_TOOLS_BEACON_XY = new Vector2d(-FIELD_HALF_WIDTH_D, +FAR_BEACON_OFFSET );
    public static Vector2d POSITION1 = new Vector2d(FIELD_HALF_WIDTH_D,-36); //left position
    public static Vector2d POSITION2 = new Vector2d(FIELD_HALF_WIDTH_D-9,FIELD_HALF_WIDTH_D); //center position
    public static Vector2d POSITION3 = new Vector2d(FIELD_HALF_WIDTH_D,22.25); //right position
    // calculating this one requires understanding the position of the robot center relative to width and length.
    public static Vector2d RED_START_LEFT_XY = new Vector2d( -8.0, -FIELD_HALF_WIDTH_D + 9.0 );

}
