package org.firstinspires.ftc.teamcode.Libs;

/**
 * Created by caseyzandbergen on 10/13/16.
 */

/**
 * Class provides methods used in navigation of the field
 */
public class Navigation {

    /**
     * Method provides a bearing to a new x,y coordinate pair based on an existing x,y location
     *
     * @param x1 Current X
     * @param x2 Target X
     * @param y1 Curent Y
     * @param y2 Target Y
     * @return Bearing in degrees
     */
    public double bearing(double x1, double x2, double y1, double y2) {
        double bearing = 0;
        double rad2Deg = 57.2957795130823209;

        double theta = Math.atan2(x2 - x1, y2 - y1);

        if (theta < 0) {
            theta += (Math.PI * 2);
        }

        return rad2Deg * theta;
    }

}
