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
package org.firstinspires.ftc.teamcode.OpModeLib;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;
import org.firstinspires.ftc.teamcode.Libs.DataLogger;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "OpModeLib - TeleOp Rear Drive Template", group = "OpModeLib")
@Disabled
public class TeleOpRearWheelDriveTemplate extends LinearOpMode {
    /**
     * Instantiate all objects needed in this class
     */
    private static final double MAX_POS = 1.0;     // Maximum rotational position
    private static final double MIN_POS = 0.0;     // Minimum rotational position
    private final static HardwareTestPlatform robot = new HardwareTestPlatform();
    private Servo servo;
    private DataLogger Dl;

    @Override
    public void runOpMode() {
        double left = 0;
        double right = 0;
        double max;
        begin();

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("TeleOpRearWheelDrive Active", "");    //
        telemetry.update();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            left = gamepad1.left_stick_y + gamepad1.left_stick_x;
            right = gamepad1.left_stick_y - gamepad1.left_stick_x;

            telemetry.addData("left", String.valueOf(left));
            telemetry.addData("right", String.valueOf(right));
            telemetry.update();

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0) {
                left /= max;
                right /= max;
            }

            robot.motorLR.setPower(left);
            robot.motorRR.setPower(right);

            if (gamepad1.b) {
                servo.setPosition(.4);
            }

            if (gamepad1.x) {
                servo.setPosition(.6);
            }

            // Use gamepad left & right Bumpers to open and close the claw
            while (gamepad1.right_bumper) {
                robot.motorFeeder.setPower(1);
            }

            // Use gamepad left & right Bumpers to open and close the claw
            while (gamepad1.right_trigger > 0) {
                robot.motorFeeder.setPower(-1);
            }

            // Use gamepad left & right Bumpers to open and close the claw
            if (gamepad1.left_bumper) {
                robot.motorFeeder.setPower(0);
            }

            idle();
        }
    }

    private void begin() {

        /** Setup the dataLogger
         * The dataLogger takes a set of fields defined here and sets up the file on the Android device
         * to save them to.  We then log data later throughout the class.
         */
        Dl = new DataLogger("Vuforia_Log");
        Dl.addField("State");
        Dl.addField("robotX");
        Dl.addField("robotY");
        Dl.addField("robotBearing");
        Dl.addField("zInt");
        Dl.addField("ODS");
        Dl.addField("colorSensorRight");
        Dl.addField("LRMotorPos");
        Dl.newLine();

        /**
         * Inititialize the robot's hardware.  The hardware configuration can be found in the
         * HardwareTestPlatform.java class.
         */
        robot.init(hardwareMap);
        servo = hardwareMap.servo.get("pusher");
        robot.servoODS.setPosition(.5);

        double position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
        servo.setPosition(position);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
}
