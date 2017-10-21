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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="PushbotPotato: Teleop POV", group="PushbotPotato")
public class PushbotTeleopPOV_LinearWith2Servos extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot robot = new HardwarePushbot();   // Use a Pushbot's hardware
    // could also use HardwarePushbotMatrix class.

    double clawOffset = 0;                       // Servo mid position
    final double CLAW_SPEED = 0.04;                   // sets rate to move servo

    boolean precisionMode = false;

    @Override
    public void runOpMode() {
        double left;
        double right;
        double drive;
        double turn;
        double precDrive;
        double precTurn;
        double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("PushbotPotato", "Robot is waiting...");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        precisionMode = false;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.a) {
                precisionMode = true;
            }
            if (gamepad1.x) {
                precisionMode = false;
            }

                // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
                // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
                // This way it's also easy to just drive straight, or just turn.
                drive = -gamepad1.left_stick_y;
                turn = gamepad1.right_stick_x;
                precDrive = drive / 4;
                precTurn = turn / 6;

                // Combine drive and turn for blended motion.
                left = drive + turn;
                right = drive - turn;

                if (precisionMode == true) {
                    left = precDrive + precTurn;
                    right = precDrive - precTurn;
                }

                // Normalize the values so neither exceed +/- 1.0
                max = Math.max(Math.abs(left), Math.abs(right));
                if (max > 1.0) {
                    left /= max;
                    right /= max;
                }

                // Output the safe values to the motor drives.
                robot.leftDrive.setPower(left);
                robot.rightDrive.setPower(right);

                // Use gamepad left & right triggers to open and close the claw
                if (gamepad1.left_trigger > 0)
                    clawOffset += CLAW_SPEED;
                else if (gamepad1.right_trigger > 0)
                    clawOffset -= CLAW_SPEED;

                // Set the max of servo rotation for the claw
                if (clawOffset < -0.4)
                    clawOffset = -0.4;

                // Move both servos to new position.  Assume servos are mirror image of each other.
                clawOffset = Range.clip(clawOffset, -0.4, 1);
                robot.clawr.setPosition(robot.MID_SERVO - clawOffset);
                robot.clawl.setPosition(-robot.MID_SERVO + clawOffset);

                // Use gamepad buttons to move arm up (Y) and down (A)
                if (gamepad1.left_bumper)
                    robot.armDrive.setPower(robot.ARM_UP_POWER);
                else if (gamepad1.right_bumper)
                    robot.armDrive.setPower(robot.ARM_DOWN_POWER);
                else
                    robot.armDrive.setPower(0.0);

                // Send telemetry message to signify robot running;
                telemetry.addData("claw", "Offset = %.2f", clawOffset);
                telemetry.addData("rawClaw", robot.clawl.getPosition());
                telemetry.addData("rawClaw", robot.clawr.getPosition());
                telemetry.addData("left", "%.2f", left);
                telemetry.addData("right", "%.2f", right);
                telemetry.update();

                // Pace this loop so jaw action is reasonable speed.
                sleep(50);
            }
        }
    }