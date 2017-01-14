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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Comp_DriveAndButtonPusherOpmode", group="Competition")  // @Autonomous(...) is the other common choice
//@Disabled
public class Comp_DriveAndButtonPusherOpmode extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    Bogg bogg;

    private static int PUSHER_IN_LIMIT = -500;      // pusher rod is about 5000 encoder steps
    private static int PUSHER_OUT_LIMIT = -4750;    // limits based upon starting at "0"
                                                    // need a overrun buffer of ~250 steps
    private int leftPusherEncoder;
    private int rightPusherEncoder;

    @Override
    public void runOpMode() {
        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        bogg = new Bogg(hardwareMap, gamepad1);

        //resetting the pusher "zero" setpoint/scale
        bogg.pusherLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bogg.pusherRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        //put the motors back into run mode
        bogg.pusherLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bogg.pusherRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();

        bogg.driveEngine.setEngineMode(DriveEngine.engineMode.defaultMode);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//            telemetry.addData("Run Time: " + runtime.toString());
            telemetry.addData("x", gamepad1.left_stick_y);
            telemetry.addData("y", gamepad1.right_stick_y);
            bogg.driveEngine.drive();
            //telemetry.update();

            //Running the left pusher
            leftPusherEncoder = bogg.pusherLeftMotor.getCurrentPosition();
            if (gamepad1.left_bumper) {
                // reverse (encoder values increase)
                if (leftPusherEncoder < PUSHER_IN_LIMIT) {
                    bogg.pusherLeftMotor.setPower(gamepad1.left_trigger);
                } else {
                    bogg.pusherLeftMotor.setPower(0);
                }
            }
            else {
                //forward (encoder values decrease)
                if (leftPusherEncoder > PUSHER_OUT_LIMIT) {
                    bogg.pusherLeftMotor.setPower(-gamepad1.left_trigger);
                } else {
                    bogg.pusherLeftMotor.setPower(0);
                }
            }

            //Running the right pusher
            rightPusherEncoder = bogg.pusherRightMotor.getCurrentPosition();
            if (gamepad1.right_bumper) {
                // reverse (encoder values increase)
                if (rightPusherEncoder < PUSHER_IN_LIMIT) {
                    bogg.pusherRightMotor.setPower(gamepad1.right_trigger);
                } else {
                    bogg.pusherRightMotor.setPower(0);
                }            }
            else {
                //forward (encoder values decrease)
                if (rightPusherEncoder > PUSHER_OUT_LIMIT) {
                    bogg.pusherRightMotor.setPower(-gamepad1.right_trigger);
                } else {
                    bogg.pusherRightMotor.setPower(0);
                }
            }

            telemetry.addData("Pushers",  "Position %7d :%7d",
                    bogg.pusherLeftMotor.getCurrentPosition(),
                    bogg.pusherRightMotor.getCurrentPosition());
            telemetry.update();

            idle();     // allow something else to run (aka, release the CPU)
        }
    }
}