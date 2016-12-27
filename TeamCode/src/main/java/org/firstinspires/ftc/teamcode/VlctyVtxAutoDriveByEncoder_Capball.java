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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This program takes place in the 30 second autonomous period.
 * The robot will hit the Capball, dislodging it from the starting position and then will
 * park on the middle square. If all goes right, this should score 10 points for our alliance.
 * The robot should start the Driver_Control period parked on the center square.
 *
 * The code REQUIRES that you DO have encoders on the wheels.
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 */

@Autonomous(name="Velocity Vortex: Auto Capball Square", group="Pushbot")
//@Disabled
public class VlctyVtxAutoDriveByEncoder_Capball extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot         robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.7;
    static final double     TURN_SPEED              = 0.65;

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

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
sleep(1000);
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
sleep(250);
        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.leftMotor.getCurrentPosition(),
                          robot.rightMotor.getCurrentPosition());
        telemetry.update();

        robot.armMotor.setPower(robot.ARM_UP_POWER);
        sleep(250);
        robot.armMotor.setPower(0.0);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        telemetry.addData("Path", "before encoderdrive 48");
        telemetry.update();
        sleep(500);
//Robot's middle starting 6 feet in from either side (3 tiles) on blue team wall with particles.
// Move forward 4 feet to approach the Capball for removal.
        encoderDrive(DRIVE_SPEED, 48, 48, 10.0);  // S1: Forward 48 Inches with 10 Sec timeout
        telemetry.addData("Path", "After encoderdrive 48");
        telemetry.update();
        sleep(500);
//Turn 4 inches to the right to remove capball to the beacon area.
        //encoderDrive(DRIVE_SPEED, 7, 7, 2.0);  //Forward 7 Inches with 2 Sec timeout
        encoderDrive(TURN_SPEED,   4, -4, 4.0);  // S2: Turn Right 4 Inches with 4 Sec timeout
        // Pause after hitting the cap ball.
        sleep(250);
        //Turn back 4 inches to square back up to the square
        encoderDrive(TURN_SPEED, -4, 4, 4.0);
        //Pause for 2 seconds to make sure capball is out of the way of the square to park.
        sleep(2000);
        //Move forward 9 inches to park onto the center square.
        encoderDrive(DRIVE_SPEED, 9, 9, 4.0);  // S3: Move forward 9 Inches with 4 Sec timeout

        //robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
        //robot.rightClaw.setPosition(0.0);
        sleep(3000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

//    private void encoderDrive(double driveSpeed, int i, int i1, double v) {
  //  }

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
        telemetry.addData("Path", "before opmode is active");
        telemetry.update();
        sleep(1500);

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            telemetry.addData("Path", "inside opmode: %7d :%7d",robot.leftMotor.getCurrentPosition(),robot.rightMotor.getCurrentPosition());
            telemetry.update();
sleep(1500);

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            telemetry.addData("after new target set",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.update();
            sleep(1500);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            robot.leftMotor.getCurrentPosition(),
                                            robot.rightMotor.getCurrentPosition());
                telemetry.update();
            }

            //Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            telemetry.addData("PathJ", "inside opmode: %7d :%7d",robot.leftMotor.getCurrentPosition(),robot.rightMotor.getCurrentPosition());
            telemetry.update();
            sleep(1500);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
