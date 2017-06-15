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
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;

@TeleOp(name="Nullbot: Teleop Relative", group="Nullbot")
public class NullbotTeleopRelative extends LinearOpMode {

    /* Declare OpMode members. */
    NullbotHardware robot = new NullbotHardware();

    final double secondsPerRotation = 2;
    final double turnVolatility = 4; // Higher number makes turning more jerklike, but faster
    final boolean adjustDirectionWhileMoving = true;

    final double headingAdjustmentRate = (2*Math.PI) / (2*robot.hz);
    double initialHeading;
    double desiredHeading;
    double difference;
    double turnSpeed;

    boolean wasLeftBumperPressed;
    boolean wasRightBumperPressed;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, true);

        waitForStart();

        initialHeading = Math.toRadians(robot.gyro.getHeading());
        desiredHeading = initialHeading;

        wasLeftBumperPressed = false;
        wasRightBumperPressed = false;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double[] motorPowers = getDesiredDirection();

            // Auto turning code
            double heading = Math.toRadians(robot.gyro.getHeading());
            difference = getAngleDifference(desiredHeading, heading);
            boolean autoAdjust = (Math.abs(difference) > Math.PI/90);
            turnSpeed = difference / (Math.PI / turnVolatility);
            turnSpeed = -constrain(turnSpeed); // Clamp it
            // If we're turning the wrong way, multiply this by -1

            // Whether or not the user wants the robot to turn
            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            if (turnRelevant) { // Fine tune turning based on right stick
                desiredHeading -= gamepad1.right_stick_x*headingAdjustmentRate;
            }

            // Turn 45 degrees based on right and left buttons
            if (!wasLeftBumperPressed && gamepad1.left_bumper) {desiredHeading += Math.PI/4;}
            if (!wasRightBumperPressed && gamepad1.right_bumper) {desiredHeading -= Math.PI/4;}

            telemetry.addLine()
                    .addData("==== Current direction", heading)
                    .addData("==== Desired direction", desiredHeading)
                    .addData("==== Difference", difference)
                    .addData("==== Turnspeed", turnSpeed)
                    .addData("==== Auto adjust?", autoAdjust)
                    .addData("==== Lock left", !wasLeftBumperPressed && gamepad1.left_bumper)
                    .addData("==== Lock right", !wasRightBumperPressed && gamepad1.right_bumper);

            telemetry.update();
            // Normalize desired heading between 0 and tau radians
            if (desiredHeading < 0) {desiredHeading += Math.PI * 2;}
            if (desiredHeading > Math.PI * 2) {desiredHeading -= Math.PI * 2;}

            // Store bumper positions for next run through loop
            wasLeftBumperPressed = gamepad1.left_bumper;
            wasRightBumperPressed = gamepad1.right_bumper;

            if (motorPowers == null) { // We're only going to turn

                if (autoAdjust) {
                    robot.frontLeft.setPower(turnSpeed);
                    robot.backLeft.setPower(turnSpeed);
                    robot.frontRight.setPower(turnSpeed*-1);
                    robot.backRight.setPower(turnSpeed*-1);
                } else {
                    for (DcMotor m : robot.motorArr) {
                        m.setPower(0);
                    }
                }
            } else { // Otherwise, if we're moving
                for (int i = 0; i < robot.motorArr.length; i++) {
                    if (adjustDirectionWhileMoving && autoAdjust) {
                        if (i % 2 == 1) { // If it's a left motor
                            robot.motorArr[i].setPower(clamp(motorPowers[i] + turnSpeed));
                        } else { // If this is a right motor
                            robot.motorArr[i].setPower(clamp(motorPowers[i] - turnSpeed));
                        }
                    } else {
                        robot.motorArr[i].setPower(motorPowers[i]);
                    }
                }
            }
            // Run above code at 25hz
            robot.writeLogTick(gamepad1);
            robot.waitForTick(1000 / robot.hz);
        }

    }
    public double getAngleDifference(double d1, double d2) {
        double diff = d2 - d1;
        if (d1 > Math.PI) {d1 -= 2 * Math.PI;}
        if (d2 > Math.PI) {d2 -= 2 * Math.PI;}

        double diff2 = d2 - d1;

        if (Math.abs(diff) < Math.abs(diff2)) {
            return diff;
        } else {
            return diff2;
        }
    }

    public double c(int frac) { // Take in a numerator of a fraction, and return Pi*num/12
        return Math.PI*((double) frac)/6.0;
    }
    public double constrain(double d) {
        return Math.max(-1, Math.min(1, d));
    }
    public double getDist(Gamepad g) {
        return Math.sqrt(g.left_stick_x*g.left_stick_x + g.left_stick_y*g.left_stick_y);
    }
    public double[] invert(double[] dirs) {
        for (int i = 0; i < dirs.length; i++) {
            dirs[i] *= -1;
        }
        return dirs;
    }
    public double[] getDesiredDirection() {
        double theta = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x);
        if (getDist(gamepad1) < 0.5) {
            if (gamepad1.dpad_left && !gamepad1.dpad_up && !gamepad1.dpad_down) {
                // Right
                return new double[]{1, -1, -1, 1};
            } else if (gamepad1.dpad_right && !gamepad1.dpad_down) {
                // Right-down
                return new double[]{0, -1, -1, 0};
            } else if (gamepad1.dpad_up && !gamepad1.dpad_left && !gamepad1.dpad_right) {
                // Down
                return new double[]{-1, -1, -1, -1};
            } else if (gamepad1.dpad_left && gamepad1.dpad_down) {
                // Left-down
                return new double[]{-1, 0, 0, -1};
            } else if (gamepad1.dpad_right && !gamepad1.dpad_up && !gamepad1.dpad_down) {
                // Left
                return new double[]{-1, 1, 1, -1};
            } else if (gamepad1.dpad_left && gamepad1.dpad_up) {
                // Left-up
                return new double[]{0, 1, 1, 0};
            } else if (gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right) {
                // Up
                return new double[]{1, 1, 1, 1};
            } else if (gamepad1.dpad_up && gamepad1.dpad_right) {
                // Up-right
                return new double[]{1, 0, 0, 1};
            } else {
                return null;
            }
        }

        if (c(-6) <= theta && theta < c(-5)) {
            // Right
            return new double[]{1, -1, -1, 1};
        } else if (c(-5) <= theta && theta < c(-4)) {
            // Right-down
            return new double[]{0, -1, -1, 0};
        } else if (c(-4) <= theta && theta < c(-2)) {
            // Down
            return new double[]{-1, -1, -1, -1};
        } else if (c(-2) <= theta && theta < c(-1)) {
            // Left-down
            return new double[]{-1, 0, 0, -1};
        } else if (c(-1) <= theta && theta < c(1)) {
            // Left
            return new double[]{-1, 1, 1, -1};
        } else if (c(1) <= theta && theta < c(2)) {
            // Left-up
            return new double[]{0, 1, 1, 0};
        } else if (c(2) <= theta && theta < c(4)) {
            // Up
            return new double[]{1, 1, 1, 1};
        } else if (c(4) <= theta && theta < c(5)) {
            // Up-right
            return new double[]{1, 0, 0, 1};
        } else {
            // Right also
            return new double[]{1, -1, -1, 1};
        }
    }
}
