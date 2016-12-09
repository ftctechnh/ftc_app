/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Auto Testing Blue", group="DM")
// @Disabled
public class AutoBetaBlue extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareDM         robot   = new HardwareDM ();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final int        COUNTS_PER_MOTOR_REV    = 7 ;    // eg: AM Neverrest
    static final double     DRIVE_GEAR_REDUCTION    = 40.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (4 * COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.8;     // Nominal speed for better accuracy.
    static final double     TURN_SPEED              = 0.5;     // Nominal half speed for better accuracy.

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable




    // State used for reading Gyro
    Orientation angles;
    Acceleration gravity;

    // Keep track of the reference start heading at start of match
    double startHeading;


    /* Shooter constants */

    static final int     NR_MAX_RPM              = 6600;
    static final int     SHOOT_MAX_RPM           = NR_MAX_RPM * COUNTS_PER_MOTOR_REV;

    static double           shootSpeed              = .65;
    static boolean          shootPressed            = false;

    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.lfDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rfDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.lfDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rfDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.lfDrive.getCurrentPosition(),
                          robot.rfDrive.getCurrentPosition());
        telemetry.update();

        // Setup max shooter motor speed limit
        robot.lShoot.setMaxSpeed(SHOOT_MAX_RPM);
        robot.rShoot.setMaxSpeed(SHOOT_MAX_RPM);

        // Use encoder for shooter speed
        robot.lShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,

        // Spin up the shooter
        //robot.lShoot.setPower(shootSpeed);
        //robot.rShoot.setPower(shootSpeed);
        telemetry.addData("Path", " Start");
        telemetry.update();

        // Move forward 26 inches
        encoderDrive(DRIVE_SPEED,  26,  26, 5.0, false);  // S1: Forward 24 Inches with 5 Sec timeout

        sleep(500);

        gyroTurn(TURN_SPEED, 90);



        // Fire the balls
        //robot.fire.setPower(1.0);
        //sleep(5000);        // Wait 5 seconds for shot to finish

        // Stop the shooter
        //robot.fire.setPower(0.0);
        //robot.lShoot.setPower(0.0);
        //robot.rShoot.setPower(0.0);

        // Intake full reverse to push cap ball
        //robot.intake.setPower(-1.0);

        // Drive forward to push cap ball
        //encoderDrive(DRIVE_SPEED, 18, 18, 3.0, true);

        // Back off and Turn right
        //encoderDrive(DRIVE_SPEED, -10, -10, 1.0, true);
        //encoderDrive(DRIVE_SPEED, amIBlue()*25, amIBlue()*-25, 3.0, true);

        // And drive onto center vortex
        //encoderDrive(DRIVE_SPEED, -28, -28, 5.0, true);

        // And shut down
        //robot.intake.setPower(0.0);


        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS,
                             boolean useGyro) throws InterruptedException {
        int newLFTarget;
        int newRFTarget;
        int newLRTarget;
        int newRRTarget;

        final double MINSPEED = 0.30;
        final double SPEEDINCR = 0.015;
        double curSpeed;                // Keep track of speed as we ramp up

        double holdHeading;

        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;
        double max;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller

            newLFTarget = robot.lfDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newLRTarget = robot.lrDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRFTarget = robot.rfDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newRRTarget = robot.rrDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            while(robot.lfDrive.getTargetPosition() != newLFTarget){
                robot.lfDrive.setTargetPosition(newLFTarget);
                sleep(1);
            }
            while(robot.lrDrive.getTargetPosition() != newLRTarget){
                robot.lrDrive.setTargetPosition(newLRTarget);
                sleep(1);
            }
            while(robot.rfDrive.getTargetPosition() != newRFTarget){
                robot.rfDrive.setTargetPosition(newRFTarget);
                sleep(1);
            }
            while(robot.rrDrive.getTargetPosition() != newRRTarget){
                robot.rrDrive.setTargetPosition(newRRTarget);
                sleep(1);
            }

            // Record the starting heading
            angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
            holdHeading = angles.firstAngle;

            // Turn On RUN_TO_POSITION
            while (robot.lfDrive.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                robot.lfDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               sleep(1);
            }

            while (robot.lrDrive.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                robot.lrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1);
            }
            while (robot.rfDrive.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                robot.rfDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1);
            }
            while (robot.rrDrive.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
                robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1);
            }


            // reset the timeout time and start motion.
            runtime.reset();

            speed = Math.abs(speed);    // Make sure its positive
            curSpeed = Math.min(MINSPEED,speed);

            robot.lfDrive.setPower(Math.abs(curSpeed));
            robot.lrDrive.setPower(Math.abs(curSpeed));
            robot.rfDrive.setPower(Math.abs(curSpeed));
            robot.rrDrive.setPower(Math.abs(curSpeed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   ((robot.lfDrive.isBusy())
                           || (robot.rfDrive.isBusy()))) {

                // Ramp up motor powers as needed
                if (curSpeed < speed) {
                    curSpeed += SPEEDINCR;
                }

                leftSpeed = curSpeed;
                rightSpeed = curSpeed;

                if (useGyro){
                    // adjust relative speed based on heading error if desired
                    error = getError(holdHeading);
                    steer = getSteer(error, P_DRIVE_COEFF);

                    // if driving in reverse, the motor correction also needs to be reversed
                    if (leftInches < 0)
                        steer *= -1.0;

                    leftSpeed -= steer;
                    rightSpeed += steer;

                    // Normalize speeds if any one exceeds +/- 1.0;
                    max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                    if (max > 1.0)
                    {
                        leftSpeed /= max;
                        rightSpeed /= max;
                    }


                }

                // And rewrite the motor speeds
                robot.lfDrive.setPower(Math.abs(leftSpeed));
                robot.lrDrive.setPower(Math.abs(leftSpeed));
                robot.rfDrive.setPower(Math.abs(rightSpeed));
                robot.rrDrive.setPower(Math.abs(rightSpeed));

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d %7d : %7d %7d", newLFTarget,  newLRTarget,
                    newRFTarget, newRRTarget);
                telemetry.addData("Path2",  "Running at %7d %7d : %7d  %7d",
                                            robot.lfDrive.getCurrentPosition(),
                                            robot.lrDrive.getCurrentPosition(),
                                            robot.rfDrive.getCurrentPosition(),
                                            robot.rrDrive.getCurrentPosition());
                telemetry.update();

                // Allow time for other processes to run.
                idle();
            }


            // Stop all motion;
            robot.lfDrive.setPower(0);
            robot.lrDrive.setPower(0);
            robot.rfDrive.setPower(0);
            robot.rrDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            while (robot.lfDrive.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
                robot.lfDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            while (robot.lrDrive.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
                robot.lrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            while (robot.rfDrive.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
                robot.rfDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            while (robot.rrDrive.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
                robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }


        }
    }


    /**
     *  Method to spin on central axis to point in a new direction.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the heading (angle)
     *  2) Driver stops the opmode running.
     *
     * @param speed Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    /**
     *  Method to obtain & hold a heading for a finite amount of time
     *  Move will stop once the requested time has elapsed
     *
     * @param speed      Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     * @param holdTime   Length of time (in seconds) to hold the specified heading.
     */
    public void gyroHold( double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        robot.lfDrive.setPower(0);
        robot.lrDrive.setPower(0);
        robot.rfDrive.setPower(0);
        robot.rrDrive.setPower(0);
    }

    /**
     * Perform one cycle of closed loop heading control.
     *
     * @param speed     Desired speed of turn.
     * @param angle     Absolute Angle (in Degrees) relative to last gyro reset.
     *                  0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                  If a relative angle is required, add/subtract from current heading.
     * @param PCoeff    Proportional Gain coefficient
     * @return
     */
    boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = -speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        robot.lfDrive.setPower(leftSpeed);
        robot.lrDrive.setPower(rightSpeed);
        robot.rfDrive.setPower(leftSpeed);
        robot.rrDrive.setPower(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // Read IMU
        angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - angles.firstAngle;
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @param PCoeff  Proportional Gain Coefficient
     * @return
     */
    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }


    public double amIBlue() {
        return 1.0;
    }
}
