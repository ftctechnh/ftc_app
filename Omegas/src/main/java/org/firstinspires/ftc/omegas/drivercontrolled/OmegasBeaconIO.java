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
 * This file contains an minimal modification of [...].omegas.drivercontrolled.OmegasLinear, to
 * allow beacon controls, (extension and retraction,) to be individually handled by the trigger
 * and bumper buttons. The names of OpModes appear on the menu of the FTC Driver Station. When
 * an selection is made from the menu, the corresponding OpMode class is instantiated on the
 * Robot Controller and executed.
 */

@TeleOp(name = "Omegas: Linear OpMode: Individual Beacon IO Controls", group = "Linear Opmode")
public class OmegasBeaconIO extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = new HardwareOmegas() {
        @Override
        public void init() {
            initDriveMotors(hardwareMap);
            initBeaconators(hardwareMap);
        }
    };

    // IPS Units
    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /**
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        Ω.initDriveMotors(hardwareMap);
        Ω.initBeaconators(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        /**
         * The following should, if uncommented, extend and retract
         * beaconators when the trigger keys are pressed.
         */
        new Thread(new Runnable() {
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
        }).start();

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
