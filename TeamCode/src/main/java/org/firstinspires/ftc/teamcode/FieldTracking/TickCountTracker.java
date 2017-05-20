package org.firstinspires.ftc.teamcode.fieldtracking;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by ROUS on 3/2/2017.
 */
public class TickCountTracker {
    public static final double COUNTS_PER_MOTOR_REV = 1680;    // AndyMark NeveRest 60 Never Motor Encoder
    public static final double DRIVE_GEAR_REDUCTION = .625;     // Motor is geared 60: 1 but that is accounted for in ticks above - This is <1.0 if geared UP or > 1.0 if reduced beyond ticks above
    public static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    public static final double PI = 3.141592653f;
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * PI);
    public static final double ROBOT_WHEEL_WIDTH = 14.5;        //Width in inches from left drive wheel center to right drive wheel center

    static final double   ADJACENT_SQUARED     = (ROBOT_WHEEL_WIDTH);  //Wheel width squared is used for adjacent calc of deflection

    protected int leftTicks;
    protected int rightTicks;
    protected Vector2d coordinate;
    protected double dirRad;

    public TickCountTracker() {
        this.leftTicks = 0;
        this.rightTicks = 0;
        this.dirRad = 0.0;
        this.coordinate = new Vector2d();
    }

    /**
     * Format position and direction as a string
     */
    public String formatAsString()
    {
        return String.format("%s-> %.08fd", this.coordinate.formatAsString(), Math.toDegrees(this.dirRad));
    }

    public void initializeRad(double xin, double yin, double ccwDirRad, int left, int right) {
        this.leftTicks = left;
        this.rightTicks = right;
        this.dirRad = ccwDirRad;
        this.coordinate.set(xin,yin);
    }

    public void initializeDeg(double xin, double yin, double ccwDirDeg, int left, int right) {
        initializeRad(xin, yin, Math.toRadians(ccwDirDeg), left, right);
    }
    public void updateTicks(int left, int right) {

        //calculate distance traveled by each wheel
        double dLeft = ((double) (left - this.leftTicks)) / COUNTS_PER_INCH;
        double dRight = ((double) (right - this.rightTicks)) / COUNTS_PER_INCH;

        //calculate the abs diff in distance moved between the wheel (opposite)
        double opposite = Math.abs(dLeft - dRight);
        //calculate the average distance moved(centroid or robot).
        double moveAvg = ((dLeft + dRight)/2.0);
        //the wheel base is the adjacent of a right triangle
        //calculate the hypotenuse of the triangle a^2 + b^2 = c^2
        double hypotenuse = Math.sqrt(ADJACENT_SQUARED + opposite*opposite);

        //basic calc of deflection angel is theta = inv cosine (adjacent/hypotenuse);
        double deflection = Math.acos(ROBOT_WHEEL_WIDTH / hypotenuse);

        /**
         * sign the angle based on weather abs(left) > abs(right)
         * IE: did we 1) drift right (left > right)
         *            2) straight (left = right) (theta = 0)
         *            3) drift left ( right > left)
         */
        if (dLeft > dRight) {
            deflection *= -1.0;
        }

        /**
         * finally calculate dx dy from the updated average traveled distance and added deflection
         */
        this.dirRad += deflection;
        double dx = (Math.cos(this.dirRad) * moveAvg);
        double dy = (Math.sin(this.dirRad) * moveAvg);

        this.coordinate.add(dx, dy);

        //record into to the robot log
        RobotLog.ii("TickCountTracker", "Ticks[%d,&d] delta[%.04f,%.04f] DxDy[%.04f,%.04f] A,D[%.04fd,%.04f\" Dir[%.04f] Pos[%s]",
                left, right, dLeft, dRight, dx, dy, Math.toDegrees(deflection), moveAvg,
                Math.toDegrees(this.dirRad), coordinate.formatAsString());

        this.leftTicks = left;
        this.rightTicks = right;
    }
}
