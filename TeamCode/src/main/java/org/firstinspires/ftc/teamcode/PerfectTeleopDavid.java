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
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@TeleOp(name = "! Perfect Teleop David!", group = "Concept")
//@Disabled
public class PerfectTeleopDavid extends LinearOpMode {

    /* this says use ArmHardwareClass */
MasterHardwareClass robot = new MasterHardwareClass();

    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        // Wait for the start button
        telemetry.addLine("!☻ Start Claw Control ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Display the current value
            telemetry.addData("Servo Controls", "X is OPEN, Y is CLOSE");
            telemetry.addData("Motor Controls", "Use the D-Pad ↑ & ↓ buttons!");
            telemetry.update();

        /* Servo Control */
            if (gamepad1.x) {
                robot.clawServo.setPower(robot.clawOpen);
            }
            if (gamepad1.y) {
                robot.clawServo.setPower(robot.clawClose);
            }

            if (gamepad1.y != true && gamepad1.x != true) {
                robot.clawServo.setPower(robot.clawStill);
            }

        /* Vertical Arm Motor */
            if (gamepad1.dpad_up) {
                robot.verticalArmMotor.setPower(1);
            } else {
                robot.verticalArmMotor.setPower(0);
            }
            if (gamepad1.dpad_down) {
                robot.verticalArmMotor.setPower(-1);
            } else {
                robot.verticalArmMotor.setPower(0);
            }
            /*These values are used for the drive*/

            double frontLeft;
            double frontRight;
            double backLeft;
            double backRight;


        /* Send telemetry message to signify that the robot's ready to play! */
            telemetry.addLine("♥ ♪ Ready to Run ♪ ♥");
            telemetry.update();


                if (gamepad1.right_stick_x < 0 || gamepad1.right_stick_x > 0) {
                    double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
                    double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
                    double rightX = gamepad1.right_stick_x;
                    final double r1 = gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
                    final double r2 = gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x;
                    final double r3 = gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x;
                    final double r4 = gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;

                    frontLeft = r1;
                    frontRight = r2;
                    backLeft = r3;
                    backRight = r4;


                    robot.frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


                    if (robot.FrontLeftPower != frontLeft) {
                        robot.frontLeftMotor.setPower(r1);
                        robot.FrontLeftPower = frontLeft;
                    }
                    if (robot.FrontRightPower != frontRight) {
                        robot.frontRightMotor.setPower(r2);
                        robot.FrontRightPower = frontRight;
                    }
                    if (robot.BackLeftPower != backLeft) {
                        robot.backLeftMotor.setPower(r3);
                        robot.BackLeftPower = backLeft;
                    }
                    if (robot.BackRightPower != backRight)
                        robot.backRightMotor.setPower(r4);
                    robot.BackRightPower = backRight;
                }

                //Hey there!

                double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
                r = r / 2; //Don't let rotation dominate movement

                double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
                double rightX = gamepad1.right_stick_x;

                double GLY = gamepad1.left_stick_y;
                double GRX = gamepad1.right_stick_x;
                double GLX = gamepad1.left_stick_x;


                final double v1 = GLY + GRX + GLX;
                final double v2 = GLY + GRX - GLX;
                final double v3 = GLY - GRX - GLX;
                final double v4 = GLY - GRX + GLX;
                frontLeft = v1;
                frontRight = v2;
                backLeft = v3;
                backRight = v4;

                if (robot.FrontLeftPower != frontLeft) {
                    robot.frontLeftMotor.setPower(v1);
                    robot.FrontLeftPower = frontLeft;
                }
                if (robot.FrontRightPower != frontRight) {
                    robot.frontRightMotor.setPower(v2);
                    robot.FrontRightPower = frontRight;
                }
                if (robot.BackLeftPower != backLeft) {
                    robot.backLeftMotor.setPower(v3);
                    robot.BackLeftPower = backLeft;
                }
                if (robot.BackRightPower != backRight)
                    robot.backRightMotor.setPower(v4);
                robot.BackRightPower = backRight;

            }
        }


    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/

}
