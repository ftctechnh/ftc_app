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
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;


@TeleOp(name="Nullbot: Teleop Nonrelative", group="Nullbot")
public class NullbotTeleopNonrelative extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();

    final double turnVolatility = 4; // Higher number makes turning more jerklike, but faster

    final double moveMotorThreshold = 0.10;
    final double triggerThreshold = 0.10;
    final double minSlowModePower = 0.3;
    double initialHeading;
    double desiredHeading;
    double difference;
    double turnSpeed;
    double desiredMax;
    double heading;


    boolean wasLeftBumperPressed;
    boolean wasRightBumperPressed;
    boolean scale;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, true);

        waitForStart();

        initialHeading = robot.getGyroHeading();
        desiredHeading = initialHeading;

        wasLeftBumperPressed = false;
        wasRightBumperPressed = false;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            scale = true;
            // Calculate speed reduction
            desiredMax = 1;

            if (gamepad1.left_trigger > triggerThreshold) {// Left trigger activates slow mode
                desiredMax = minSlowModePower + ((1 - minSlowModePower) * (1 - gamepad1.left_trigger));

            }

            adjustDesiredHeading();

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;
            // Auto turning code
            heading = robot.getGyroHeading();
            difference = getAngleDifference(desiredHeading, heading);

            if (!turnRelevant) {
                //boolean autoAdjust = (Math.abs(difference) > Math.PI/90);
                turnSpeed = difference / (Math.PI / turnVolatility);
                turnSpeed = clamp(turnSpeed); // Clamp it
                // If we're turning the wrong way, multiply this by -1
            } else {
                turnSpeed = gamepad1.right_stick_x;
                desiredHeading = heading;
            }

            double[] unscaledMotorPowers = getDesiredDirection();
            // Add turning and scaling to powers

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                unscaledMotorPowers[i] *= desiredMax;
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] += turnSpeed;
                } else {
                    unscaledMotorPowers[i] -= turnSpeed;
                }
            }

            if (scale) {
                // Now, let's scale those powers
                double greatest = 0;
                for (double d : unscaledMotorPowers) {
                    greatest = Math.max(greatest, Math.abs(d));
                }
                for (int i = 0; i < unscaledMotorPowers.length; i++) {
                    unscaledMotorPowers[i] = unscaledMotorPowers[i] / greatest;
                }
            }
            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                // Clamp here is necessary, as it protects us from minor math rounding errors
                // For example, scaling can result in a value being turned from sqrt(2)/2 to 1.0001,
                // which would crash our software if we didn't clamp it
                robot.motorArr[i].setPower(chop(clamp(unscaledMotorPowers[i])));
            }

            telemetry.addLine()
                    .addData("TurnSpeed", turnSpeed);
            telemetry.addLine()
                    .addData("Raw gyro direction", robot.getGyroHeadingRaw());
            telemetry.addLine()
                    .addData("Raw compass direction", robot.getCompassHeading());
            telemetry.addLine()
                    .addData("Gyro error", robot.gyroError);
            telemetry.addLine()
                    .addData("EAG direction", robot.getGyroHeading());
            telemetry.addLine()
                    .addData("Initial compass heading", robot.initialCompassHeading);
            telemetry.update(); // Send telemetry data to driver station

            // Run above code at 25hz
            robot.writeLogTick(gamepad1);
            robot.waitForTick(1000 / robot.hz);
        }

    }
    private double chop(double d) { // Cutoff all signals being sent to the motor below a threshold
        if (Math.abs(d) < moveMotorThreshold) {
            return 0;
        } else {
            return d;
        }
    }
    private void adjustDesiredHeading() {
        // Turn 45 degrees based on right and left buttons
        if (!wasLeftBumperPressed && gamepad1.left_bumper) {desiredHeading += Math.PI/4;}
        if (!wasRightBumperPressed && gamepad1.right_bumper) {desiredHeading -= Math.PI/4;}

        // Normalize desired heading between 0 and tau radians
        if (desiredHeading < 0) {desiredHeading += Math.PI * 2;}
        if (desiredHeading > Math.PI * 2) {desiredHeading -= Math.PI * 2;}

        // Store bumper positions for next run through loop
        wasLeftBumperPressed = gamepad1.left_bumper;
        wasRightBumperPressed = gamepad1.right_bumper;
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

    public double getDist(Gamepad g) {
        return Math.sqrt(g.left_stick_x*g.left_stick_x + g.left_stick_y*g.left_stick_y);
    }

    public double[] getDesiredDirection() {
        double controllerAngle;
        if (gamepad1.dpad_right) {
            if (gamepad1.dpad_up) {
                controllerAngle = Math.PI/4;
            } else if (gamepad1.dpad_down) {
                controllerAngle = 3*Math.PI/4;
            } else {
                controllerAngle = Math.PI/2;
            }
        } else if (gamepad1.dpad_left) {
            if (gamepad1.dpad_up) {
                controllerAngle = 7*Math.PI/4;
            } else if (gamepad1.dpad_down) {
                controllerAngle = 5*Math.PI/4;
            } else {
                controllerAngle = 3*Math.PI/2;
            }
        } else if (gamepad1.dpad_up) {
            controllerAngle = 0.0;
        } else if (gamepad1.dpad_down) {
            controllerAngle = Math.PI;
        } else {
            // If we're not moving, don't scale the values
            scale = false;
            return new double[]{0, 0, 0, 0};
        }

        // When calculating the robot's angle, we don't have to take into account the initial heading,
        // because it is always 0 (we'll always calibrate the gyro)
        double robotAngle = robot.normAngle(controllerAngle + heading);
        double[] unscaledPowers = new double[4];
        unscaledPowers[0] = Math.sin(robotAngle + Math.PI/4);
        unscaledPowers[1] = Math.cos(robotAngle + Math.PI/4);
        unscaledPowers[2] = unscaledPowers[1];
        unscaledPowers[3] = unscaledPowers[0];
        return unscaledPowers;
    }
}
