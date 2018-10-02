/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the Trainerbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode.
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would start with sample PushbotAutoDriveByTime.
 *
 *  This code ALSO requires that the drive Motors have been configured such
 *  that a positive power command moves them forwards, and causes the
 *  encoders to count UP.
 *
 *  The code contains several fundamental robot movements, like driving
 *  straight, driving in an arc, or turning on itself. To calibrate, uncomment one,
 *  predict where the robot should go, run the movement, and see if the robot
 *  actually waent there.
 *
 *  The fundamental movements are wrappers for encoderDrive(speed, leftInches,
 *  rightInches, timeoutS). That method does the actual movement.
 *
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers
 *  to generate the run profile.
 *
 * This class waa coopied from FtcRobotController external sample
 * PushbotAutoDriveByEncoder_Linear and modified for use on a Trainerbot.
 *
 */

@Autonomous(name="Calibrate", group="Trainerbot")
//@Disabled
public class Calibrate extends LinearOpMode {
  // If timeouts are to be avoided, check with
  private static double LONG_TIME = 1e10;

    /* Declare OpMode members. */
    Trainerbot robot   = new Trainerbot();
    private ElapsedTime runtime = new ElapsedTime();

    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.2;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.leftDrive.getCurrentPosition(),
                          robot.rightDrive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // Forward 2 tiles.
        // encoderDrive(DRIVE_SPEED,  48,  48, 5.0);
        // Turn right
        //encoderDrive(TURN_SPEED,   12, -12, 4.0);
        // Run curving to right, one radian
        turnAngleRadiusDrive(DRIVE_SPEED, 1.00, 40);

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
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)
              (leftInches * robot.COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightInches * robot.COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            robot.leftDrive.getCurrentPosition(),
                                            robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    //    Adaptation of encoderDrive for different left and right speeds.
    public void turnAngleRadiusDrive (double speed, double angle, double radius) {
        double leftArc  = (radius - robot.DRIVE_WHEEL_SEPARATION/2.0) * angle;
        double rightArc = (radius + robot.DRIVE_WHEEL_SEPARATION/2.0) * angle;
        double leftSpeed = speed * (radius - robot.DRIVE_WHEEL_SEPARATION/2.0)/
          (radius + robot.DRIVE_WHEEL_SEPARATION);
        double rightSpeed = speed;

        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Determine new target position, and pass to motor controller
            newLeftTarget = (int) (leftArc * robot.COUNTS_PER_INCH);
            newRightTarget = (int) (rightArc * robot.COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Adjust speeds, reset the timeout time.
            // Left motor speed (R â€“ d/2)/(R + d/2) * max speed.
            // Right motor goes arc = (R + d/2) * Î¸ at max speed.
            // might need to clip speeds here
            robot.leftDrive.setPower(Math.abs(leftSpeed));
            robot.rightDrive.setPower(Math.abs(rightSpeed));
            runtime.reset();
            // This should take care of Î¸ and R > 0. There are 3 other signed cases.
            // Degenerate cases: Î¸ = 0, R = 0, R = Â±d/2, R = âˆž.
            // While we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
              (runtime.seconds() < LONG_TIME) &&
              (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Goal", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Now at", "%7d :%7d",
                  robot.leftDrive.getCurrentPosition(),
                  robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
