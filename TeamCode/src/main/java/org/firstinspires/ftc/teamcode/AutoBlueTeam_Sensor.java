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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *ADB
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform LOL XDDD based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Auto Blue Right (Sensor)", group="Pushbot")
//@Disabled
public class AutoBlueTeam_Sensor extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot_TT         robot   = new HardwarePushbot_TT();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();
    ModernRoboticsI2cGyro gyro    = null;                    // Additional Gyro device

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: ANDY Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.1;
    static final double     PUSH_SPEED             = 0.1;

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        gyro = robot.gyro;

        int colorBlueSensed;
        colorBlueSensed = 0;

        int sensorLoopCycles = 0;

        boolean lastResetState = false;
        boolean curResetState  = false;


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Calibrate Gyro sensor before starting
        calibrateGyro();

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.frontLeftMotor.getCurrentPosition(),
                robot.frontRightMotor.getCurrentPosition(),
                robot.backLeftMotor.getCurrentPosition(),
                robot.backRightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // This is for the nearest blue beacon towards our robot
        //Step 1
//        gyroDrive(DRIVE_SPEED, 24.0, 0.0);    // Drive FWD
        encoderDrive(DRIVE_SPEED, 21, 21, 10.0); // Drive fwd
        telemetry.addData("Step 1 GyroDrive", " Completed");
        telemetry.update();
//        sleep(250);

        //Step 2
        gyroTurn( TURN_SPEED, -55.0);         // Turn  CCW to -55 Degrees
        telemetry.addData("Step 2 GyroTurn", " Completed");
        telemetry.update();
//        sleep(250);

//        gyroHold( TURN_SPEED, -45.0, 0.5);    // Hold -45 Deg heading for a 1/2 second
//        telemetry.addData("Step 2 Gyro HOLD", " Completed");
//        telemetry.update();
//        sleep(2000);


//        Step 3
//        gyroDrive(DRIVE_SPEED, 24.0, 0.0);    // Drive FWD
        encoderDrive(DRIVE_SPEED, 42, 42, 10.0); // Drive fwd
        telemetry.addData("Step 3a GyroDrive", " Completed");
        telemetry.update();
//        sleep(250);

        encoderDrive(PUSH_SPEED, 9 , 9, 10.0); // Drive fwd
        telemetry.addData("Step 3b GyroDrive", " Completed");
        telemetry.update();


//        sleep(250);

        //Step 4
        gyroTurn( TURN_SPEED, -90.0);         // Turn  CCW to -45 Degrees
        telemetry.addData("Step 4 GyroTurn", " Completed");
        telemetry.update();
//        sleep(250);

//        gyroHold( TURN_SPEED, -45.0, 0.5);    // Hold -45 Deg heading for a 1/2 second
//        telemetry.addData("Step 4 Gyro HOLD", " Completed");
//        telemetry.update();
//        sleep(2000);

        //encoderDrive(DRIVE_SPEED, 4, 4, 3.0);
        telemetry.addData("Before Color sense", "loop");
        telemetry.update();
//        sleep(1000);

        // Light sensor loop
        if (robot.color.red() > robot.color.blue()) {
            colorBlueSensed = 1;
            telemetry.addData("Detecting", "Red in IF");
//            sleep(2000);
            encoderDrive(PUSH_SPEED,    -10, -10, 3.0);
//            encoderDrive(TURN_SPEED,    -3,6,3.0);
            gyroTurn(TURN_SPEED, -60.0);
            encoderDrive(PUSH_SPEED,    18,18,3.0);
            gyroTurn(TURN_SPEED, -90.0);
            encoderDrive(PUSH_SPEED,    9,9,3.0);
        } else if (robot.color.blue() > robot.color.red()){
            colorBlueSensed = 2;
            telemetry.addData("Detecting", "Blue in IF");
//            sleep(2000);
            encoderDrive(PUSH_SPEED, 11,11, 3.0);
        } else {
            colorBlueSensed = 0;
            telemetry.addData("Detecting", "Neither in IF");
//            sleep(2000);
        }

        //Did not find either color. So now start the loop
        sensorLoopCycles = 0;
        while (robot.color.blue() == 0 && robot.color.red() == 0 && colorBlueSensed == 0 && sensorLoopCycles < 6){
            encoderDrive(PUSH_SPEED,1,1,3.0);

            if (robot.color.red() > robot.color.blue()) {
                colorBlueSensed = 1;
                telemetry.addData("Detecting", "Red in Loop");
//            sleep(1000);
                encoderDrive(PUSH_SPEED,    -10, -10, 3.0);
//            encoderDrive(TURN_SPEED,    -3,6,3.0);
                gyroTurn(TURN_SPEED, -60.0);
                encoderDrive(PUSH_SPEED,    18,18,3.0);
                gyroTurn(TURN_SPEED, -90.0);
                encoderDrive(PUSH_SPEED,    9,9,3.0);
            } else if (robot.color.blue() > robot.color.red()){
                colorBlueSensed = 2;
                telemetry.addData("Detecting", "Blue in Loop");
//            sleep(2000);
                encoderDrive(PUSH_SPEED, 10,10, 3.0);
            } else {
                colorBlueSensed = 0;
                telemetry.addData("Detecting", "Neither in loop");
//            sleep(2000);
            }
            sensorLoopCycles = sensorLoopCycles + 1 ;
        }
        telemetry.addData("First Color sense loop", "completed");
        telemetry.update();
//        sleep(5000);

        //Look for the second beacon
//        encoderDrive(DRIVE_SPEED, -5,-5, 3.0);
//        gyroTurn(TURN_SPEED,0.0);
//        encoderDrive(DRIVE_SPEED, 30,30, 3.0);
//
//
//        // Looking for white line
//        robot.color2.enableLed(true);
//        sensorLoopCycles = 0;
//        while ((robot.color2.alpha() < 4 || robot.color2.red() < 4 || robot.color2.green()<4 || robot.color2.blue()< 4) && sensorLoopCycles < 6){
//            encoderDrive(PUSH_SPEED,1,1,3.0);
//            sensorLoopCycles = sensorLoopCycles + 1 ;
//        }
//
//        //Now we are on white line
//        //Turning off active mode on color 2 as white line has been found
//        robot.color2.enableLed(false);
//        gyroTurn(TURN_SPEED,-90.0);
//        encoderDrive(DRIVE_SPEED, 4, 4, 3.0); //  Forward 24 inches with 3 Sec timeout
//
//        // Light sensor loop
//        sensorLoopCycles = 0;
//        while (robot.color.blue() == 0 && robot.color.red() == 0 && sensorLoopCycles < 6){
//            encoderDrive(PUSH_SPEED,1,1,3.0);
//
//            if (robot.color.red() > robot.color.blue()) {
//                colorBlueSensed = 1;
//                telemetry.addData("Detecting", "Red");
////            sleep(500);
//                encoderDrive(PUSH_SPEED,    -12, -12, 3.0);
////            encoderDrive(TURN_SPEED,    -3,6,3.0);
//                gyroTurn(TURN_SPEED, -60.0);
//                encoderDrive(PUSH_SPEED,    20,20,3.0);
//                gyroTurn(TURN_SPEED, -90.0);
//                encoderDrive(PUSH_SPEED,    9,9,3.0);
//            } else if (robot.color.blue() > robot.color.red()){
//                colorBlueSensed = 2;
//                telemetry.addData("Detecting", "Blue");
////            sleep(500);
//                encoderDrive(PUSH_SPEED, 4,4, 3.0);
//            } else {
//                colorBlueSensed = 0;
//                telemetry.addData("Detecting", "Neither");
////            sleep(500);
//            }
//            sensorLoopCycles = sensorLoopCycles + 1 ;
//        }
//        telemetry.update();

        encoderDrive(DRIVE_SPEED,  -10, -10, 3.0); // Back up and park
        gyroTurn(TURN_SPEED, -100.0);
        encoderDrive(DRIVE_SPEED,  -24, -24, 3.0); // Back up and park
        gyroTurn(TURN_SPEED, -75.0);
        encoderDrive(DRIVE_SPEED,  -18, -18, 3.0); // Back up and park
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
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.frontLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.frontRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLeftTarget = robot.backLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.backRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.frontLeftMotor.setTargetPosition(newLeftTarget);
            robot.frontRightMotor.setTargetPosition(newRightTarget);
            robot.backLeftMotor.setTargetPosition(newLeftTarget);
            robot.backRightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontRightMotor.setPower(Math.abs(speed));
            robot.frontLeftMotor.setPower(Math.abs(speed));
            robot.backRightMotor.setPower(Math.abs(speed));
            robot.backLeftMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (robot.frontLeftMotor.isBusy() && robot.backLeftMotor.isBusy() && robot.frontRightMotor.isBusy() && robot.backRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            robot.frontLeftMotor.getCurrentPosition(),
                                            robot.frontRightMotor.getCurrentPosition(),
                                            robot.backLeftMotor.getCurrentPosition(),
                                            robot.backRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeftMotor.setPower(0);
            robot.frontRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void calibrateGyro() {

        // Ensure that the opmode is still active
        // start calibrating the gyro.
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        robot.gyro.calibrate();

        // make sure the gyro is calibrated.
        while (!isStopRequested() && robot.gyro.isCalibrating()) {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %d. Press Start.", gyro.getIntegratedZValue());
            telemetry.update();
            idle();
        }
        gyro.resetZAxisIntegrator();

        // wait for the start button to be pressed.
//      waitForStart();

    }

    /**
     *  Method to drive on a fixed compass bearing (angle), based on encoder counts.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the desired position
     *  2) Driver stops the opmode running.
     *
     * @param speed      Target speed for forward motion.  Should allow for _/- variance for adjusting heading
     * @param distance   Distance (in inches) to move from current position.  Negative distance means move backwards.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroDrive ( double speed,
                            double distance,
                            double angle) {

        int     newLeftTarget;
        int     newRightTarget;
        int     moveCounts;
        double  max;
        double  error;
        double  steer;
        double  leftSpeed;
        double  rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int)(distance * COUNTS_PER_INCH);
            newLeftTarget = robot.frontLeftMotor.getCurrentPosition() + moveCounts;
            newRightTarget = robot.frontRightMotor.getCurrentPosition() + moveCounts;
            newLeftTarget = robot.backLeftMotor.getCurrentPosition() + moveCounts;
            newRightTarget = robot.backRightMotor.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            robot.frontLeftMotor.setTargetPosition(newLeftTarget);
            robot.frontRightMotor.setTargetPosition(newRightTarget);
            robot.backLeftMotor.setTargetPosition(newLeftTarget);
            robot.backRightMotor.setTargetPosition(newRightTarget);

            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            robot.frontLeftMotor.setPower(speed);
            robot.frontRightMotor.setPower(speed);
            robot.backLeftMotor.setPower(speed);
            robot.backRightMotor.setPower(speed);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (robot.frontLeftMotor.isBusy() && robot.frontRightMotor.isBusy() && robot.backLeftMotor.isBusy()&& robot.backRightMotor.isBusy() )) {

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;

                leftSpeed = speed - steer;
                rightSpeed = speed + steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0)
                {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                robot.frontLeftMotor.setPower(leftSpeed);
                robot.frontRightMotor.setPower(rightSpeed);
                robot.backLeftMotor.setPower(leftSpeed);
                robot.backRightMotor.setPower(rightSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
                telemetry.addData("Target",  "%7d:%7d",      newLeftTarget,  newRightTarget);
                telemetry.addData("Actual",  "%7d:%7d:%7d:%7d",      robot.frontLeftMotor.getCurrentPosition(),
                        robot.frontRightMotor.getCurrentPosition(),robot.backLeftMotor.getCurrentPosition(),robot.backRightMotor.getCurrentPosition());
                telemetry.addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeftMotor.setPower(0);
            robot.frontRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

        telemetry.addData("Current Heading: ", gyro.getHeading()) ;
        telemetry.update();
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
        robot.frontLeftMotor.setPower(0);
        robot.frontRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
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
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        robot.frontLeftMotor.setPower(leftSpeed);
        robot.frontRightMotor.setPower(rightSpeed);
        robot.backLeftMotor.setPower(leftSpeed);
        robot.backRightMotor.setPower(rightSpeed);


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

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
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

}



