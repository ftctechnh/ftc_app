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
package org.firstinspires.ftc.omegas.drivercontrolled;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@SuppressWarnings("unused")
@TeleOp(name = "Omegas: Linear OpMode", group = "Linear Opmode")
public class OmegasLinear extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = null;

    // IPS Units
    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Ω = new HardwareOmegas() {
            @Override
            public void init() {
                initDriveMotors(hardwareMap);
                initBeaconators(hardwareMap);
                initLiftServo(hardwareMap);
                initAppContext(hardwareMap);
                initTelemetry(telemetry);

                sayMessage();
            }
        };

        /**
         * The following should, if uncommented, extend and retract
         * beaconators when the trigger keys are pressed.
         */
        new Thread() {
            public void run() {
                while (opModeIsActive()) {
                    if (gamepad2.left_bumper) {
                        Ω.positionServo(Ω.getLeftBeaconator(), 1.0);
                    } else if (gamepad2.left_trigger > 0) {
                        Ω.positionServo(Ω.getLeftBeaconator(), 0.0);
                    } else if (gamepad2.right_bumper) {
                        Ω.positionServo(Ω.getRightBeaconator(), 0.0);
                    } else if (gamepad2.right_trigger > 0) {
                        Ω.positionServo(Ω.getRightBeaconator(), 1.0);
                    }
                }
            }
        }.start();

        if (gamepad2.a) {
            Ω.getLiftServo().setPosition(Ω.getLiftServo().getPosition() + 0.25);
        } else if (gamepad2.b) {
            Ω.getLiftServo().setPosition(Ω.getLiftServo().getPosition() - 0.25);
        }

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            Ω.getLeftBackMotor().setPower(-gamepad1.left_stick_y);
            Ω.getLeftFrontMotor().setPower(-gamepad1.left_stick_y);
            Ω.getRightBackMotor().setPower(-gamepad1.right_stick_y);
            Ω.getRightFrontMotor().setPower(-gamepad1.right_stick_y);

            telemetry.addData("Status", "Run Time: " + runtime.toString() + " " + -gamepad1.right_stick_y + " " + -gamepad1.right_stick_y);
        }
    }
}
