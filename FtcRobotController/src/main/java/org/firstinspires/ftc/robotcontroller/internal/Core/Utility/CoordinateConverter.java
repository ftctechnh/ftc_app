package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

/**
 * Created by Joel on 9/2/2018.
 */

public class CoordinateConverter {

    public CoordinateConverter(){
        radius = 0;
        theta = 0;
        x = 0;
        y = 0;
    }

    private double radius;
    private double theta;

    private double x;
    private double y;

    public final static int POLAR = 0;
    public final static int CARTESIAN = 1;

    public void setCoordinates(double radiusOrX, double thetaOrY, int type){
        if (type == this.CARTESIAN){

            x = radiusOrX;
            y = thetaOrY;

            radius = Math.hypot(x,y);
            theta = Math.toDegrees(Math.atan(y/x));
        }

        else if (type == this.POLAR){

            radius = radiusOrX;
            theta = thetaOrY;

            x = radius * Math.cos(Math.toRadians(theta));
            y = radius * Math.sin(Math.toRadians(theta));
        }

    }

    public double getRadius(){
        return radius;
    }

    public double getTheta(){
        return theta;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
