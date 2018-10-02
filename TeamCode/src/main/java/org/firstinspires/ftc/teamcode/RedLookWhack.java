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
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Autonomous opmode for Red Alliance in the 2017-2018 FTC "Relic Recovery" game.
 * Look at left Jewel, determine its color, then make the correct robot turn to
 * knock off the Blue Jewel.
 *
 * It runs on a Trainerbot.
 */

@Autonomous(name="Red Look and Whack", group="Trainerbot")
//@Disabled
public class RedLookWhack extends LinearOpMode {

    Trainerbot robot   = new Trainerbot();

    static final double TURN_SPEED  = 0.20;     // slow to minimize wheel slippage
    static final double KNOCK_ANGLE = 0.25;     // radians
    static final double DEPLOYED    = 0.04;     // Extended from front of robot
    static final double STOWED      = 1.00;     // Folded back toward rear
    private boolean redIsLeft;

    @Override
    public void runOpMode() {
        // Initialize the drive train.
        // The init() method of the hardware class does all the work here.
        robot.init(hardwareMap);
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define class members, initialize the subsystems they refer to.
        Servo servo = hardwareMap.get(Servo.class, "paddle");
        servo.setPosition(STOWED);
        ColorSensor colorSensor;
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
        // Turn the LED on.
        colorSensor.enableLed(true);

        // Wait for the game to start (driver presses PLAY).
        waitForStart();

        // Get paddle out in front of robot, so its sensor can see the left Jewel.
        servo.setPosition(DEPLOYED);
        sleep(2000); // Give time to get there.

        // Determine left Jewel color.
        // Read the RGB data, and report colors to driver station.
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.update();

        //  To Do: handle error cases: green dominant, red == blue, no colors seen.
        redIsLeft = colorSensor.red() > colorSensor.blue() ? true : false;
        if (redIsLeft) { // knock the left Jewel off
            turnAngle(TURN_SPEED, -KNOCK_ANGLE);
        }
        else { // knock the right Jewel off
            turnAngle(TURN_SPEED, KNOCK_ANGLE);
        }
    }

    // Turn on vertical axis between the drive wheels.
    public void turnAngle (double speed, double angle) {
        double leftArc  = (-robot.DRIVE_WHEEL_SEPARATION/2.0) * angle;
        double rightArc = (robot.DRIVE_WHEEL_SEPARATION/2.0) * angle;

        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {
            robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Determine new target positions, and pass those to motor controller.
            newLeftTarget = (int) (leftArc * robot.COUNTS_PER_INCH);
            newRightTarget = (int) (rightArc * robot.COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // There are other motor run modes.
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Note: Reverse movement is obtained by setting a negative target, not speed.
            robot.leftDrive.setPower(speed);
            robot.rightDrive.setPower(speed);

            // Stop as soon as one motor has reached its target position.
            while (opModeIsActive() &&
              (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {
            }

            // Set motors in reasonable condition for next move.
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Give time for Driver person to see the colors, and evaluate the decision
            // making.
            sleep (5000);
        }
    }
}
