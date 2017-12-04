package org.firstinspires.ftc.team11248.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Driving with omni wheels
 */
public class HolonomicDriver_11248 {

    //    CONSTANTS     //
    public static final double OMNI_WHEEL_ANGLE_CORRECTION = Math.PI/4;
    public static final double FRONT_OFFSET = 0;
    public static final double LEFT_OFFSET = Math.PI/2;
    public static final double BACK_OFFSET = Math.PI;
    public static final double RIGHT_OFFSET = 3 * Math.PI / 2;


    //    STATEFUl      //
    private Telemetry telemetry;
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    int frontLeftRotations, frontRightRotations, backLeftRotations, backRightRotations;


    // TODO: 12/11/2016 oz add some comments about this stuff. Also u might want to make is slow public for simplicities sake
    public static double MAX_TURN = .4;
    public static double MAX_SPEED = .6;
    public static final double SLOW_SPEED = .4;
    private boolean isSlow = false;
    private boolean isDrift = false;
    private boolean fastMode = false;

    /*
     * The angle used to offset the front of the robot
     */
    private double offsetAngle, directionAngle;

    /*
     * Whether or not to log telemetry data
     */
    private boolean silent = true;

    /**
     * creates new HolonomicDriver_11248.
     * @param frontLeft {DcMotor} - front left motor
     * @param frontRight {DcMotor} - front right motor
     * @param backLeft {DcMotor} - back left motor
     * @param backRight {DcMotor} - back right motor
     * @param telemetry {Telemetry} - telemetry
     */
    public HolonomicDriver_11248(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft,
                                 DcMotor backRight, Telemetry telemetry) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.telemetry = telemetry;

        resetDriveEncoders();
        recordPosition();
        setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public void stop(){
        drive(0,0,0);
    }


    /**
     * Team 11248's driving method for omni wheel drive
     * @param x - values of x to drive (double -1 to +1)
     * @param y - values of y to drive (double -1 to +1)
     * @param rotate - values for rotation to drive (double -1 to +1)
     * @param smooth - boolean declaring if driving values are smoothed (low values easier to control)
     */

    public void drive(double x, double y, double rotate, boolean smooth){
        double FL, FR, BL, BR, angle, r;
        double MAX_TURN, MAX_SPEED;
        //## CALCULATE VALUES ##

        /*
         * Protects range of joysticks
         */
        x = Range.clip(x, -1, 1);
        y = Range.clip(y, -1, 1);
        rotate = Range.clip(rotate, -1, 1);


        /* This makes the rotation and speed ratios relative to the rotation value
         * So when we don't have a rotation we can drive at full speed instead of a fraction of speed
         * If rotation is being used, a ratio is induced to prevent a value greater than 1
         */

        if (smooth) {
            MAX_TURN = Math.abs(rotate) * HolonomicDriver_11248.MAX_TURN;
            MAX_SPEED = 1 - MAX_TURN;

        } else {
            MAX_SPEED = HolonomicDriver_11248.MAX_SPEED;
            MAX_TURN = HolonomicDriver_11248.MAX_TURN;
        }

        //Using a function on variable rotate will smooth out the slow values but still give full range
//        if((smooth || isSlow) && rotate !=0) rotate = rotate * rotate * rotate/Math.abs(rotate);

        rotate *= MAX_TURN;

        angle = Math.atan2(y, x);


        /* Gets the radius of our left joystick to vary our total speed
        * Checks if r is greater than 1 (cannot assume joystick gives perfect circular values)
        */
        r = Math.sqrt( (x*x) + (y*y) );
        if(r>1) r=1;

        angle += (Math.PI/4) + offsetAngle + directionAngle;//take our angle and shift it 90 deg (PI/4)

        if(smooth || isSlow) r = r*r; //Using a function on variable r will smooth out the slow values but still give full range


        double SPEED = 1;
        if(isSlow)
            SPEED = SLOW_SPEED;


        /* Takes new angle and radius and converts them into the motor values
         * Multiples by our speed reduction ratio and our slow speed ratio
         */

        double fastSpeed = fastMode ? Math.sqrt(2) : 1;

        FL = BR = Math.sin(angle) * MAX_SPEED * r * fastSpeed;
        FR = BL = Math.cos(angle) * MAX_SPEED * r * fastSpeed;

        FL -= rotate; // implements rotation
        FR -= rotate;
        BL += rotate;
        BR += rotate;


        /* Prevent fatal error cause by slightly imperfect joystick values
         * Will drive in approximate direction if true
         */
        frontLeft.setPower( Range.clip(-FL * SPEED, -1, 1)); // -rot fl br y
        frontRight.setPower( Range.clip(-FR * SPEED, -1, 1)); // -
        backLeft.setPower( Range.clip(BL * SPEED, -1, 1)); // +
        backRight.setPower( Range.clip(BR * SPEED, -1, 1)); //+

        recordPosition();


        /*
        * Will drive in aproxamite direction if true

        if(Math.abs(FL) >1 || Math.abs(FR) >1 ||
                Math.abs(BR) >1 || Math.abs(BL) >1) {

            FL /=  Math.abs(FL);
            FR /=  Math.abs(FR);
            BR /=  Math.abs(BR);
            BL /=  Math.abs(BL);
        }
         */


//        if (!silent) {
//            telemetry.addData("OMNI_DRIVER: ", "radius: " + r);
//            telemetry.addData("OMNI_DRIVER: ", "x: " + x);
//            telemetry.addData("OMNI_DRIVER: ", "y: " + y);
//            telemetry.addData("OMNI_DRIVER: ", "rotate: " + rotate);
//            telemetry.addData("OMNI_DRIVER: ", "FL: " + frontLeft.getPower());
//            telemetry.addData("OMNI_DRIVER: ", "FR: " + frontRight.getPower());
//            telemetry.addData("OMNI_DRIVER: ", "BR: " + backRight.getPower());
//            telemetry.addData("OMNI_DRIVER: ", "BL: " + backLeft.getPower());
//            telemetry.update();
//        }



    }


    /*
     * moves the robot based off of analogue inputs
     * @param {double} x                The x value
     * @param {double} y                The y value
     * @param {double} rotation         The rotation value
     * @param {double} [modifier]      The modifier for the power.
     * @param {boolean} [smooth]        Whether or not to smooth the modifier
     */

    public void drive(double x, double y,double rotate){
        this.drive(x, y, rotate, false);
    }


    /*
       ENCODER METHODS
     */

    public void resetDriveEncoders(){
       setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       recordPosition();
       setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setDriveMode(DcMotor.RunMode runMode){
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
    }


    public void recordPosition(){

        frontLeftRotations = frontLeft.getCurrentPosition();
        frontRightRotations = frontRight.getCurrentPosition();
        backLeftRotations = backLeft.getCurrentPosition();
        backRightRotations = backRight.getCurrentPosition();
    }

    public void printDriveRotations(){
        telemetry.addData("HOLONOMIC", "FL: " + frontLeftRotations);
        telemetry.addData("HOLONOMIC", "FR: " + frontRightRotations);
        telemetry.addData("HOLONOMIC", "BL: " + backLeftRotations);
        telemetry.addData("HOLONOMIC", "BR: " + backRightRotations);
    }

    /*
     * returns the value for isSlow
     */
    public boolean getIsSlow() {
        return isSlow;
    }

    /*
     * Sets the value for isSlow
     */
    public void setIsSlow(boolean isSlow) {
        this.isSlow = isSlow;
    }

    /*
     * Toggle isSlow
     */
    public void toggleSlow() {
        isSlow = !isSlow;
    }


    public boolean isDriftModeOn(){
        return isDrift;
    }

    public void setDriftMode(boolean on){
        isDrift = on;
        if (isDrift) {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        } else {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

    }


    public void toggleDriftMode(){
        isDrift = !isDrift;
        setDriftMode(isDrift);
    }

    public void setFastMode(boolean isOn){
        fastMode = isOn;
    }

    /*
     * Sets the offset angle
     * @param angle     the angle to offset by
     */
    public void setOffsetAngle(double angle) {
        offsetAngle = angle;
    }

    public double getOffsetAngle() {
        return offsetAngle;
    }

    public void setDirectionAngle(double angle){
        directionAngle = angle;
    }

    /*
     * Set the telemetry to silent or not
     * @param telemetry     whether or not to silence telemetry
     */
    public void turnTelemetryOn(boolean on) {
        silent = !on;
    }
}
