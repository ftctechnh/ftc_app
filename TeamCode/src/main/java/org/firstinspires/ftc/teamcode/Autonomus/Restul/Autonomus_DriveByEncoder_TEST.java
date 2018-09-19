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

package org.firstinspires.ftc.teamcode.Autonomus.Restul;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareRobot;

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

@Autonomous(name="Encoder", group="TEST")
@Disabled
public class Autonomus_DriveByEncoder_TEST extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime     runtime = new ElapsedTime();
    HardwareRobot robot = new HardwareRobot();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // ANDYMARK_TICKS_PER_REV
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP ????????
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference ????????
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     COUNTE_PER_CM = COUNTS_PER_INCH / 2.54;
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    double     deplasare               = 10; // cm

    @Override
    public void runOpMode() {

        // INITIALIZARE
        robot.init(hardwareMap);

        // STATUS = DONE
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();
        robot.FrontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FrontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BackLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BackRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0" , "FL: " + robot.FrontLeftMotor.getCurrentPosition());
        telemetry.addData("Path0" , "FR: " + robot.FrontRightMotor.getCurrentPosition());
        telemetry.addData("Path0" , "BL: " + robot.BackLeftMotor.getCurrentPosition());
        telemetry.addData("Path0" , "BR: " + robot.BackRightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        while (opModeIsActive())
        {
            if (gamepad1.dpad_up)
            {
                deplasare++;
                sleep(300);
            }
            if (gamepad1.dpad_down)
            {
                deplasare--;
                sleep(300);
            }
            if (gamepad1.a)
            {
                encoderDrive(DRIVE_SPEED,  0, deplasare, 0,  5.0);  // S1: Forward 10 cm with 5 Sec timeout
                //encoderDrive(TURN_SPEED,   0, 0, 10, 5.0);  // S2: Turn Right 10 cm with 5 Sec timeout
                encoderDrive(DRIVE_SPEED, 0, -deplasare, 0, 5.0);  // S3: Reverse 10 cm with 5 Sec timeout
                telemetry.addData("Path", "Complete");
                telemetry.update();
            }

        }

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
                             double x, double y, double z,
                             double timeoutS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        double FRONT_LEFT_POWER   =   - y + z;
        double FRONT_RIGHT_POWER  =   + y + z;
        double BACK_LEFT_POWER    =   - y + z;
        double BACK_RIGHT_POWER   =   + y + z;

        /*
        double FRONT_LEFT_POWER   =   - x - y + z;
        double FRONT_RIGHT_POWER  =   - x + y + z;
        double BACK_LEFT_POWER    =     x - y + z;
        double BACK_RIGHT_POWER   =     x + y + z;
        */
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = robot.FrontLeftMotor.getCurrentPosition() + (int)(FRONT_LEFT_POWER * COUNTE_PER_CM);
            newFrontRightTarget = robot.FrontRightMotor.getCurrentPosition() + (int)(FRONT_RIGHT_POWER * COUNTE_PER_CM);
            newBackLeftTarget = robot.FrontLeftMotor.getCurrentPosition() + (int)(BACK_LEFT_POWER * COUNTE_PER_CM);
            newBackRightTarget = robot.FrontRightMotor.getCurrentPosition() + (int)(BACK_RIGHT_POWER * COUNTE_PER_CM);

            robot.FrontLeftMotor.setTargetPosition(newFrontLeftTarget);
            robot.FrontRightMotor.setTargetPosition(newFrontRightTarget);
            robot.BackLeftMotor.setTargetPosition(newBackLeftTarget);
            robot.BackRightMotor.setTargetPosition(newBackRightTarget);

            // Turn On RUN_TO_POSITION
            robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.FrontLeftMotor.setPower(Math.abs(speed));
            robot.FrontRightMotor.setPower(Math.abs(speed));
            robot.BackLeftMotor.setPower(Math.abs(speed));
            robot.BackRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (robot.FrontLeftMotor.isBusy() && robot.FrontRightMotor.isBusy()
                           && robot.BackLeftMotor.isBusy() && robot.BackRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1" , "TO FL: " + newFrontLeftTarget);
                telemetry.addData("Path1" , "TO FR: " + newFrontRightTarget);
                telemetry.addData("Path1" , "TO BL: " + newBackLeftTarget);
                telemetry.addData("Path1" , "TO BR: " + newBackRightTarget);
                telemetry.update();
                telemetry.addData("Path0" , "FL: " + robot.FrontLeftMotor.getCurrentPosition());
                telemetry.addData("Path0" , "FR: " + robot.FrontRightMotor.getCurrentPosition());
                telemetry.addData("Path0" , "BL: " + robot.BackLeftMotor.getCurrentPosition());
                telemetry.addData("Path0" , "BR: " + robot.BackRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.FrontLeftMotor.setPower(0);
            robot.FrontRightMotor.setPower(0);
            robot.BackLeftMotor.setPower(0);
            robot.BackRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250);   // optional pause after each move
        }
    }
}
