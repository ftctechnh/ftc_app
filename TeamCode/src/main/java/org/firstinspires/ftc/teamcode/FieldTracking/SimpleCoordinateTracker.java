package org.firstinspires.ftc.teamcode.fieldtracking;

/**
 * Created by ROUS on 2/27/2017.
 */

/**
 * *Keeps track of current coordinate based on navigation input
 */
public class SimpleCoordinateTracker {
    /**
     * *public data members for
     */
    public double direction; /**robot orientation as ccw direction relative to x-axis*/
    public Vector2d coordinate;/**robot center cordinate on the field*/

    /**
     * *constructors create initialize the data members
     */

    /**default constructor intializes the object with x=0.0, y=0.0*/
    public SimpleCoordinateTracker() {
        this.direction = 0.0;
        this.coordinate = new Vector2d(0.0,0.0);
    }

    /**Format robot coordinate position and direction as a string*/
    public String formatAsString(){
        return String.format("Pos: %s Dir:%.04f)", this.direction, this.coordinate.formatAsString());
    }

    public SimpleCoordinateTracker setPosition( double x, double y ){this.coordinate = new Vector2d(x, y);
    return this;
    }

    public SimpleCoordinateTracker setPosition(final Vector2d vec){
        return setPosition(vec.x,vec.y);
    }

    public SimpleCoordinateTracker setDirection(double ccwAngle){
        this.direction = ccwAngle;
        return this;
    }

    public SimpleCoordinateTracker setPositionAndDirection(double x, double y, double ccwAngle){
        return setDirection(ccwAngle).setPosition(x,y);
    }

    public SimpleCoordinateTracker shiftPositionByXY(double x, double y){
        return shiftPositionByXY(new Vector2d(x, y));
    }

    public SimpleCoordinateTracker shiftPostionByDirectionDistance(final DirectionDistance dd){
        return shiftPositionByXY(dd.asVector2d());
    }

    public SimpleCoordinateTracker shiftPositionByXY(final Vector2d vec){
        this.coordinate.add(vec);
        return this;
    }

    public SimpleCoordinateTracker turnRelative(double ccwAngle){
        this.direction += ccwAngle;
        return this;
    }

    public SimpleCoordinateTracker moveOnCurentHeading(double distance){
        DirectionDistance dd = new DirectionDistance(this.direction, distance);
        this.coordinate.add(dd.asVector2d());
        return this;
    }
}
