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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

//Custom opmode for debugging the left/right pusher motors

@TeleOp(name="MotorPusherEncoderOpmode", group="Testing")  // @Autonomous(...) is the other common choice
//@Disabled
public class MotorPusherEncoderOpmode extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor pusherLeftMotor = null;
    DcMotor pusherRightMotor = null;

    static int posA = 1000;
    static int posB = 3000;
    boolean gotoPosA = true;

    @Override
    public void runOpMode() {
        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        pusherLeftMotor = hardwareMap.dcMotor.get("pusher_left");
        pusherRightMotor = hardwareMap.dcMotor.get("pusher_right");

        pusherLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        pusherRightMotor.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        pusherLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pusherRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        pusherLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pusherRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pusherLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pusherRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                pusherLeftMotor.getCurrentPosition(),
                pusherRightMotor.getCurrentPosition());

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.a) {
                gotoPosA = true;
            }
            if (gamepad1.b) {
                gotoPosA = false;
            }
            if (gotoPosA ) {
                // Turn On RUN_TO_POSITION
                pusherLeftMotor.setTargetPosition(posA);
                pusherRightMotor.setTargetPosition(posA);
            }
            else {
                // Turn On RUN_TO_POSITION
                pusherLeftMotor.setTargetPosition(posB);
                pusherRightMotor.setTargetPosition(posB);
            }
            pusherLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pusherRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            pusherLeftMotor.setPower(Math.abs(100));   // setPower range is 0 to 100%
            pusherRightMotor.setPower(Math.abs(100));

            telemetry.addData("Path:",  "Starting at %7d :%7d",
                    pusherLeftMotor.getCurrentPosition(),
                    pusherRightMotor.getCurrentPosition());
            telemetry.update();

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < 10) &&
                    (pusherLeftMotor.isBusy() && pusherRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", 3000,  3000);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        pusherLeftMotor.getCurrentPosition(),
                        pusherRightMotor.getCurrentPosition());
                telemetry.update();
            }

            telemetry.update();

            // Stop all motion;
            pusherLeftMotor.setPower(0);
            pusherRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            pusherLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            pusherRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            idle();     // allow something else to run (aka, release the CPU)
        }
    }
}