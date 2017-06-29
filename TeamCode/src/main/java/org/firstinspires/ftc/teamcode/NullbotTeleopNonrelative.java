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
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.FileWriter;

@TeleOp(name="Nullbot: Teleop Nonrelative", group="Nullbot")
public class NullbotTeleopNonrelative extends LinearOpMode {

    /* Declare OpMode members. */
    NullbotHardware robot = new NullbotHardware();

    public double controlThreshold = 0.2;
    public double maxMotorOutput = 0.0;

    final double eighthTurn = Math.PI / 4.0;

    ElapsedTime calibrationTimer = new ElapsedTime();
    FileWriter w;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, true);

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double heading = Math.toRadians(robot.gyro.getHeading());
            double robotPerpDriveDir = getDesiredDirection() - heading;
            double m = gamepad1.left_trigger*2;

            double turnSpeed = Math.min(1.0, Math.max(-1.0, gamepad1.right_stick_x * -1));

            robot.frontLeft.setPower(clampRange(m * Math.sin(robotPerpDriveDir + eighthTurn) + turnSpeed));
            robot.backLeft.setPower(clampRange(m * Math.cos(robotPerpDriveDir + eighthTurn) + turnSpeed));
            robot.frontRight.setPower(clampRange(m * Math.cos(robotPerpDriveDir + eighthTurn) - turnSpeed));
            robot.backRight.setPower(clampRange(m * Math.sin(robotPerpDriveDir + eighthTurn) - turnSpeed));

            // Telemetry

            telemetry.addLine()
                    .addData("Gyro", heading);
            telemetry.addLine()
                    .addData("Unrelative direction", getDesiredDirection());
            telemetry.addLine()
                    .addData("Relative direction", robotPerpDriveDir);
            telemetry.update();

            // Run above code at 25hz
            robot.writeLogTick(gamepad1);
            robot.waitForTick(1000 / robot.hz);
        }
    }

    /*public double lockToVals(double d) {
        // Binary search could be used here to increase speed
        double closestVal = 0.0;
        double closestDiff = 20.0;
        for (double v : lockTauFracs) {
            if (Math.abs(v * Math.PI * 2 - d) <= Math.abs(v * Math.PI * 2 - closestDiff)) {
                closestVal = v * Math.PI * 2;
                closestDiff = Math.abs(v * Math.PI * 2 - d);
            }
        }

        return closestVal;
    }*/

    public double getDesiredDirection() {
        // The below if statement is pretty gross, but it's the best way to do things
        if (gamepad1.dpad_left) {
            if (gamepad1.dpad_up) {
                return Math.PI/4;
            } else if (gamepad1.dpad_down) {
                return 3*Math.PI/4;
            } else {
                return Math.PI/2;
            }
        } else if (gamepad1.dpad_right) {
            if (gamepad1.dpad_up) {
                return 7*Math.PI/4;
            } else if (gamepad1.dpad_down) {
                return 5*Math.PI/4;
            } else {
                return 3*Math.PI/2;
            }
        } else if (gamepad1.dpad_up) {
            return 0.0;
        } else if (gamepad1.dpad_down) {
            return Math.PI;
        }
        return 0.0;
    }

    public double constrainAngle(double o) {
        o = Math.PI * 2 * Math.floor((o + Math.PI) / (Math.PI*2));
        if (o < 0) {
            o += Math.PI*2;
        }
        return o;
    }

    public double sqr(double v) {
        return v*v;
    }

    public double rEV (double v) { // Remove extraneous values
        if (v < controlThreshold) {
            v = 0;
        } else if (v >= 1) {
            v = 1;
        }
        return v;
    }
    public double clampRange(double v) {
        if (Math.abs(v) > 1) {
            maxMotorOutput = Math.max(maxMotorOutput, Math.abs(v));
            return v / maxMotorOutput;
        }
        return v;
    }
}
