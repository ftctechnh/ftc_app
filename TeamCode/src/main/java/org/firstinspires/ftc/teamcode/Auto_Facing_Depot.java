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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.HardwarePushbotdemo;
import org.firstinspires.ftc.teamcode.DriveBaseHardwareMap;
import org.firstinspires.ftc.teamcode.ConceptTensorFlowObjectDetection;
import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Pushbot: Auto Facing Depot", group="Pushbot")
//@Disabled
public class Auto_Facing_Depot extends LinearOpMode {

    /* Declare OpMode members. */
    DriveBaseHardwareMap robot       = new DriveBaseHardwareMap();
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.5 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED = 0.5;
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.top_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.bot_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.top_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.bot_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.top_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.bot_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.top_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.bot_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.top_left.getCurrentPosition(),
                robot.top_right.getCurrentPosition(),
                robot.bot_left.getCurrentPosition(),
                robot.bot_right.getCurrentPosition(),

                telemetry.update());

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
          encoderDrive(DRIVE_SPEED, 8.75, -8.75, 5.0);
          encoderDrive(DRIVE_SPEED, 40, 40, 5.0);
          encoderDrive(DRIVE_SPEED, -16,16,5.0 );
        encoderDrive(DRIVE_SPEED, 40,40,5.0 );
        robot.intake_left.setPosition(0.0);
        sleep(1000);
        robot.intake_left.setPosition(0.70);
        sleep(500);
        encoderDrive(DRIVE_SPEED, -87, -87, 5.0);


    }


    //robot.oof.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newBotLeftTarget;
        int newBotRightTarget;
        int newTopRightTarget;
        int newTopLeftTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newBotLeftTarget = robot.bot_left.getCurrentPosition() - (int)(leftInches * COUNTS_PER_INCH);
            newBotRightTarget = robot.bot_right.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newTopLeftTarget = robot.top_left.getCurrentPosition() - (int)(leftInches * COUNTS_PER_INCH);
            newTopRightTarget = robot.top_right.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.top_left.setTargetPosition(newTopLeftTarget);
            robot.bot_left.setTargetPosition(newBotLeftTarget);
            robot.top_right.setTargetPosition(newTopRightTarget);
            robot.bot_right.setTargetPosition(newBotRightTarget);

            telemetry.addData( "Start encoderDrive leftInches:", leftInches );
            telemetry.addData("BottomLeft",robot.bot_left.getCurrentPosition());
            telemetry.addData("BottomLeftBusy",robot.bot_left.isBusy());
            telemetry.addData("TopLeft",robot.top_left.getCurrentPosition());
            telemetry.addData("TopLeftBusy",robot.top_left.isBusy());
            telemetry.addData("BottomRight",robot.bot_right.getCurrentPosition());
            telemetry.addData("BottomRightBusy",robot.bot_right.isBusy());
            telemetry.addData("TopRight",robot.top_right.getCurrentPosition());
            telemetry.addData("TopRightBusy",robot.top_right.isBusy());

            //robot.leftDrive.getCurrentPosition(),
            telemetry.update();

            // Turn On RUN_TO_POSITION
            robot.bot_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bot_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.top_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.top_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.bot_left.setPower(Math.abs(speed));
            robot.bot_right.setPower(Math.abs(speed));
            robot.top_left.setPower(Math.abs(speed));
            robot.top_right.setPower(Math.abs(speed));



            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.bot_left.isBusy()) && (robot.top_left.isBusy() ) && (robot.top_right.isBusy() ) && (robot.bot_right.isBusy())) {

                // Display it for the driver.
                //telemetry.addData("Path1", "Running ", newRightTarget);
                //telemetry.addData("Path2", "Running ");

            }
            telemetry.addData( "After while leftInches:", leftInches );
            telemetry.addData("BottomLeft",robot.bot_left.getCurrentPosition());
            telemetry.addData("BottomLeftBusy",robot.bot_left.isBusy());
            telemetry.addData("TopLeft",robot.top_left.getCurrentPosition());
            telemetry.addData("TopLeftBusy",robot.top_left.isBusy());
            telemetry.addData("BottomRight",robot.bot_right.getCurrentPosition());
            telemetry.addData("BottomRightBusy",robot.bot_right.isBusy());
            telemetry.addData("TopRight",robot.top_right.getCurrentPosition());
            telemetry.addData("TopRightBusy",robot.top_right.isBusy());

            //robot.leftDrive.getCurrentPosition(),
            telemetry.update();
            // Stop all motion;
            //robot.leftDrive.setPower(0);
            robot.bot_left.setPower(0);
            robot.bot_right.setPower(0);
            robot.top_left.setPower(0);
            robot.top_right.setPower(0);

            // Turn off RUN_TO_POSITION
            //robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bot_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.top_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bot_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.top_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



            sleep(500);   // optional pause after each move
        }
    }

}