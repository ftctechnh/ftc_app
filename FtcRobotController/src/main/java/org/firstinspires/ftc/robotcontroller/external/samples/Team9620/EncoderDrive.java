package org.firstinspires.ftc.robotcontroller.external.samples.Team9620;

import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;


public class EncoderDrive {

    public static final String TAG = "EncoderDrive";
    public final static String tag_left_wheel = "left_wheel";
    public final static String tag_right_wheel = "right_wheel";

    private ElapsedTime runtime = new ElapsedTime();

    public DcMotor  leftMotor    = null;
    public DcMotor  rightMotor   = null;

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // AndyMark NeveRest 40 Never Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // Motor is geared 40 : 1 but that is accounted for in ticks above - This is < 1.0 if geared UP or > 1.0 if reduced beyond ticks above
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV*DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * Math.PI);

    /** Initialize EncoderDrive for Op Mode.*/
    public void initializeForOpMode( LinearOpMode opMode, HardwareMap hwMap ) throws InterruptedException {

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

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Set all motors to run with encoders.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set zero power behavior to brake mode
        RobotLog.ii(TAG, "ZP Behavior before - Left:=%s Right:=%s"
                , ZPMToString(leftMotor.getZeroPowerBehavior())
                , ZPMToString((rightMotor.getZeroPowerBehavior())));
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RobotLog.ii(TAG, "ZP Behavior after - Left:=%s Right:=%s"
                , ZPMToString(leftMotor.getZeroPowerBehavior())
                , ZPMToString((rightMotor.getZeroPowerBehavior())));

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();

        /** Send telemetry message to indicate successful Encoder reset */
        opMode.telemetry.addData("Path0",  "Starting at %7d :%7d",
                leftMotor.getCurrentPosition(),
                rightMotor.getCurrentPosition());
    }

    /**
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDriveBase( LinearOpMode opMode,
                             double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        // Ensure that the opmode is still active
        if (opMode.opModeIsActive() && 0.0 != leftInches || 0.0 != rightInches ) {

            // Determine initial and target positions
            int initialLeftPos = leftMotor.getCurrentPosition();
            int initialRightPos = rightMotor.getCurrentPosition();
            int targetLeftPos = initialLeftPos + (int)(leftInches * COUNTS_PER_INCH);
            int targetRightPos = initialRightPos + (int)(rightInches * COUNTS_PER_INCH);

            // calculate the needed speed differential between the two sides
            // such that each side should arrive at the desired location at the same time
            // by biasing the speed to one side or the other.
            int initialLeftDelta = Math.abs( initialLeftPos-targetLeftPos );
            int initialRightDelta = Math.abs( initialRightPos-targetRightPos );
            int maxDelta = Math.max( initialLeftDelta, initialRightDelta );
            // one or both speeds will be <= speed without exceeding speed.
            double leftSpeed = speed * (initialLeftDelta/maxDelta);
            double rightSpeed = speed * (initialRightDelta/maxDelta);

            // Display it for the drive
            opMode.telemetry.addData("IS", "IS=%.04f", speed);
            opMode.telemetry.addData("LS", "TS=%.04f CS=%.04f", leftSpeed, leftSpeed);
            opMode.telemetry.addData("RS", "TS=%.04f CS=%.04f", rightSpeed, rightSpeed);
            opMode.telemetry.addData("TR", "LT=%.04f RT=%.04f", initialLeftDelta, initialRightDelta);
            opMode.telemetry.update();

            // pass target positions on to the motor controllers
            leftMotor.setTargetPosition(targetLeftPos);
            rightMotor.setTargetPosition(targetRightPos);

            // Turn On RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftMotor.setPower(Math.abs(leftSpeed));
            rightMotor.setPower(Math.abs(rightSpeed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while ( opMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftMotor.isBusy() && rightMotor.isBusy()) ) {

                // calculate the current delta from target position
                int curLeftDelta = Math.abs( leftMotor.getCurrentPosition()-targetLeftPos );
                int curRightDelta = Math.abs( rightMotor.getCurrentPosition()-targetRightPos );
                if ( 0 == curLeftDelta && 0 == curRightDelta ) {break;} // we have reached our target.

                // calculate progress ratio in the range of 1.0 to 0.0 indicating how much of the distance is left to traverse
                // check denominator to and assign value of 1.0 to avoid division by zero.
                double leftProgressRatio = (0 != initialLeftDelta ?  (curLeftDelta/initialLeftDelta) : 0.0 );
                double rightProgressRatio = (0 != initialRightDelta ?  (curRightDelta/initialRightDelta) : 0.0 );

                // if one side or the other is at it's target location it's power will go to zero
                // the side that is lagging behind the other will represent the max ratio.
                double maxProgressRatio = Math.max( leftProgressRatio, rightProgressRatio );

                // re-calculate speed differential between the two sides based on progress towards the goal
                // if both sides are progressing equally they will both maintain current power levels
                // if one side is getting ahead it's power will be reduced by up to 100%, but hopefully only a small percentage
                // as progress is balanced out, the power level will come back up
                double newLeftSpeed = leftSpeed * (leftProgressRatio/maxProgressRatio);
                double newRightSpeed = rightSpeed * (rightProgressRatio/maxProgressRatio);

                // update motor speed based on latest position.
                leftMotor.setPower(Math.abs(leftSpeed));
                rightMotor.setPower(Math.abs(rightSpeed));

                // Display it for the drive
                opMode.telemetry.addData("IS", "IS=%.04f", speed);
                opMode.telemetry.addData("LS", "TS=%.04f CS=%.04f", leftSpeed, newLeftSpeed);
                opMode.telemetry.addData("RS", "TS=%.04f CS=%.04f", rightSpeed, newRightSpeed);
                opMode.telemetry.addData("TR", "LT=%.04f RT=%.04f", curLeftDelta, curRightDelta);
                opMode.telemetry.update();

                // we may want to replace this idel() with sleep(50 or 100);
                opMode.idle(); // give the system a moment as we wait for things to progress
            }

            // Set zero power behavior to brake mode
            RobotLog.ii(TAG, "ZP Behavior before - Left:=%s Right:=%s"
                    , ZPMToString(leftMotor.getZeroPowerBehavior())
                    , ZPMToString((rightMotor.getZeroPowerBehavior())));
            leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RobotLog.ii(TAG, "ZP Behavior after - Left:=%s Right:=%s"
                    , ZPMToString(leftMotor.getZeroPowerBehavior())
                    , ZPMToString((rightMotor.getZeroPowerBehavior())));

            // Stop all motion;
            leftMotor.setPower(0);
            rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            opMode.sleep(100);   // optional pause after each move
        }
    }

    /**
     *  Method to perfmorm a relative piviot turn, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDriveStraight( LinearOpMode opMode, double speed, double distance, boolean bForward, double timeoutS) {
        /** Calculates drive distance based on wheelbase*/
        /**
         * based on D = 2*PI*R * ( Angle/360 )
         */
        double dist = ( bForward ? distance : -distance );
        encoderDriveBase( opMode, speed, dist, dist, timeoutS );
    }

     /**
     *  Method to perfmorm a relative piviot turn, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderPivotTurn( LinearOpMode opMode, double speed, double ccwAngle, double timeoutS ) {
        /** Calculates drive distance based on wheelbase*/
        /**
          * based on D = 2*PI*R * ( Angle/360 )
          */
        double radius = RobotParameters.wheelBase/2.0;
        double dist = ((2.0 * Math.PI * radius) * ( ccwAngle/ 360.0 ));
        opMode.telemetry.addData("Turn",  "PivotTurn F:%.04f LT:%.04f RT:%.04f", ccwAngle, -dist,  dist);
        encoderDriveBase( opMode, speed, -dist, dist, timeoutS );
    }

    /**
     *  Method to perfmorm a relative arc turn Left assuming arcRadiius represents the
     *  distance from the arc center to the center of the wheelbase, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderArcTurnLeft( LinearOpMode opMode, double speed, double arcRadius, double ccwAngle, boolean bForward, double timeoutS ) {
        /** Calculates drive distance based on wheelbase*/
        /**
         * based on D = 2*PI*R * ( Angle/360 )
         */
        double radius = RobotParameters.wheelBase/2.0;
        double turnFactor = ( ccwAngle/ 360.0 )*(bForward ? 1.0 : -1.0);
        double distLeft = ((2.0 * Math.PI * ( arcRadius - radius )) * turnFactor);
        double distRight = ((2.0 * Math.PI * ( arcRadius + radius )) * turnFactor);
        opMode.telemetry.addData("Turn",  "ATLeft F:%.04f LT:%.04f RT:%.04f", turnFactor, distLeft,  distRight);
        encoderDriveBase( opMode, speed, distLeft, distRight, timeoutS );
    }

    /**
     *  Method to perfmorm a relative arc turn right assuming arcRadiius represents the
     *  distance from the arc center to the center of the wheelbase, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderArcTurnRight( LinearOpMode opMode, double speed, double arcRadius, double ccwAngle, boolean bForward, double timeoutS) {
        /** Calculates drive distance based on wheelbase*/
        /**
         * based on D = 2*PI*R * ( Angle/360 )
         */
        double radius = RobotParameters.wheelBase/2.0;
        double turnFactor = ( ccwAngle/ 360.0 )*(bForward ? 1.0 : -1.0);
        double distLeft = ((2.0 * Math.PI * ( arcRadius + radius )) * turnFactor );
        double distRight = ((2.0 * Math.PI * ( arcRadius - radius )) * turnFactor );
        opMode.telemetry.addData("Turn",  "ATRight F:%.04f LT:%.04f RT:%.04f", turnFactor, distLeft,  distRight);
        encoderDriveBase( opMode, speed, distLeft, distRight, timeoutS );
    }

    static final String ZPMToString( DcMotor.ZeroPowerBehavior zpm ){
        String value = "UNKNOWN";
        switch( zpm ){
            case BRAKE:{
                value = "BREAK";
                break;
            }
            case FLOAT:{
                value = "FLAOT";
                break;
            }
            case UNKNOWN: // UNKNOWN fall through to default
            default:
            {
                break;
            }
        }
        return value;
    }
}
