package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * This class manages the drive wheel motors for teleop based on provided values taken from the game pad
 */
public class GamePadShiftDrive {

    public static final String TAG = "GamePadShiftDrive";
    public final static String tag_left_wheel = "left_wheel";
    public final static String tag_right_wheel = "right_wheel";

    //private ElapsedTime runtime = new ElapsedTime();

    protected double driveFactor = 0.7;
    protected double throttleLeft = 0.8;
    protected double throttleRight = 0.8;
    protected double speedRanges = 2; // how many sped ranges are requested, valid values are 2 to 10;
    protected double speedStep = 0.5; // factor added or subtracted on shift
    protected double powerLeft = 0.0;
    protected double powerRight = 0.0;

    protected DcMotor leftMotor    = null;
    protected DcMotor  rightMotor   = null;

    /**
     *  Initialize EncoderDrive for Op Mode.
     *
     * opMode - parent op mode provides access to telemetry and other services. we report to during initialization
     * hwMap - provides access to motors for initialization and setting speed
     * ranges - indicates the number of speeds (gears) requested and is used to calculate speed step when shift button is pressed.
     *
     * */
    public void initializeForOpMode(LinearOpMode opMode, HardwareMap hwMap, int ranges ) throws InterruptedException {

        /**
         * Send telemetry message to signify robot waiting;
         */
        opMode.telemetry.addData("Status", "Resetting Encoders");
        opMode.telemetry.update();

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get(tag_left_wheel);
        rightMotor  = hwMap.dcMotor.get(tag_right_wheel);

        // set initial direction
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        // set zero power mode
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); // can be set to FLOAT or BREAK
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); // can be set to FLOAT or BREAK

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        powerLeft = 0.0;
        powerRight = 0.0;

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // clip the abs value of ranges to our valid range and store as double
        // calculate speed step by dividing the number of requested ranges.
        speedRanges = Range.clip(Math.abs(ranges), 2, 10);
        speedStep = 1.0/speedRanges;

        /** Send telemetry message to indicate successful Encoder reset */
        opMode.telemetry.addData("Path0",  "Starting at %7d :%7d",
                leftMotor.getCurrentPosition(),
                rightMotor.getCurrentPosition());
    }

    /** inquiry methods */
    public double getSpeedStep() { return speedStep; }
    public int getSpeedRanges() { return (int)speedRanges; }
    public double getDriveFactor() { return driveFactor; }
    public double getThrottlePositionLeft() { return throttleLeft; }
    public double getThrottlePositionRight() { return throttleRight; }
    public double getPowerLeft() { return powerLeft; }
    public double getPowerRight() { return powerRight; }

    /** increases the current drive factor up by speedStep until limit of 1.0 */
    public void upShift() {
        driveFactor = Range.clip( driveFactor + speedStep, 0.0, 1.0);
    }

    /** reduces the current drive factor up by speedStep until limit of 1.0 */
    public void downShift() {
        driveFactor = Range.clip( driveFactor - speedStep, 0.0, 1.0);
    }

    /** Set throttle position left - valid range is -1.0 to 1.0 */
    public void setThrottlePositionLeft( double tpLeft ) {
        throttleLeft =  Range.clip( tpLeft, -1.0, 1.0 );
    }

    /** Set throttle position right - valid range is -1.0 to 1.0 */
    public void setThrottlePositionRight( double tpRight ) {
        throttleRight = Range.clip( tpRight, -1.0, 1.0 );
    }

    /** Set updated motor poere levels based on current values */
    public void updatePowerLevels() {
        powerLeft =  Range.clip( (throttleLeft * driveFactor), -1.0, 1.0 );
        powerRight = Range.clip( (throttleRight * driveFactor), -1.0, 1.0 );

        leftMotor.setPower( powerLeft );
        rightMotor.setPower( powerRight );
    }
}
