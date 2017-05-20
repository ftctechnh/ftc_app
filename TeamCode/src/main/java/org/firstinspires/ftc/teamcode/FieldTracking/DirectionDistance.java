package org.firstinspires.ftc.teamcode.fieldtracking;

/**
 * Created by ROUS on 2/27/2017.
 */
public class DirectionDistance {
    /**
     * static variables exposed by this class
    **/
    public static final DirectionDistance X_AXIS = new DirectionDistance(1.0,0.0);
    public static final DirectionDistance Y_AXIS = new DirectionDistance(1.0,90.0);

    /**
     * public data members for
     */
    public double distIn = 0.0; /**initial xy_postion in inches from center*/
    public double dirRad = 1.0;/**initial orientation in ccw rad from x-axis*/

    /**
     * constructors create and initialize the data members
     */

    /**default constructor initializes the object with x=0.0, y=0.0*/
    public DirectionDistance(){
        this.dirRad =0.0;
        this.distIn = 0.0;
    }
    /**copy constructor initializes the object with values from the other object*/
    public DirectionDistance(final DirectionDistance other) {
        this.dirRad = other.dirRad;
        this.distIn = other.distIn;
    }

    /**value constructor initializes the ojebt with provided x,y values*/
    public DirectionDistance(double ccwAngleDeg, double distIn){
        this.dirRad = Math.toRadians (ccwAngleDeg);
        this.distIn = distIn;
    }

    /**valid direction ust have a non zero length*/
    public boolean isEmpty() {return this.distIn > 0.0; }

    /**
     * Format this direction and distance as a string
     */
    public String formatAsString(){
        return String.format("(Dir:%.04fd,L:%.04f\")",Math.toDegrees(this.dirRad),this.distIn);
    }
    public DirectionDistance setDirRadDist (double ccwDirRad, double distInch){
        this.dirRad = ccwDirRad;
        this.distIn = distInch;
        return this;
    }
    /**sets both direction and distance values at once*/
    public DirectionDistance setDirDegDist(double ccwDirDeg, double distInch){
        return setDirRadDist(Math.toRadians(ccwDirDeg), distInch);
    }
    public DirectionDistance addAngleRad(double ccwAngleRad){
        this.dirRad += ccwAngleRad;
        return this;
    }

    public DirectionDistance addAngleDeg (double ccwAngleDeg){
        return addAngleRad(Math.toRadians(ccwAngleDeg));
    }

    public DirectionDistance subtractAngle(double ccwAngleDeg){
        this.dirRad -= Math.toRadians(ccwAngleDeg);
        return this;
    }

    public DirectionDistance addDist(double distInch){
        this.distIn += distInch;
        return this;
    }

    public DirectionDistance subtractDist(double distInch){
        this.distIn -= distInch;
        return this;
    }

    public Vector2d asVector2d(){
        return new Vector2d(Math.cos(this.dirRad)* this.distIn, Math.sin(this.dirRad)* this.distIn);
    }

    public static Vector2d CreateVector2d(double dirDeg, double distIn){
        double dirRad = Math.toRadians(dirDeg);
        return new Vector2d(Math.cos(dirRad)* distIn, Math.sin(dirRad)* distIn);
    }
}
