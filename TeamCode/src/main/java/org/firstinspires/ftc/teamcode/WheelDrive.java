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

/**
 ☺ yey this works ☺
 */
@TeleOp(name = "♪ ♥ Drive Mode 2 ♥  ♪", group = "Concept")
//@Disabled
public class WheelDrive extends LinearOpMode {

    /* this says use ArmHardwareClass */
MasterHardwareClass robot = new MasterHardwareClass();

    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;

    double x;
    double y;

//    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        // Wait for the start button
        telemetry.addLine("!☻ Ready to Run ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Display the current value
            telemetry.addLine("Hi~♪");
            telemetry.addData("Claw Opening Controls", "X is Close, B is Open");
            telemetry.addData("Claw Moving Controls", "Use the D-Pad ↑ & ↓ buttons!");

            telemetry.addData("Front Left Power", robot.frontLeftMotor.getPower());
            telemetry.addData("Front Right Power", robot.frontRightMotor.getPower());
            telemetry.addData("Back Left Power", robot.backLeftMotor.getPower());
            telemetry.addData("Back Right Power", robot.backRightMotor.getPower());
            telemetry.update();

        /* Set the arm up */
//            if (robot.gemServo.getPosition() != robot.xPosUp) {
//                robot.gemServo.setPosition(robot.xPosUp);
//           \ }
            robot.gemServo.setPosition(robot.xPosUp);

        /* Servo Control */
            if (gamepad2.x) {
                if (robot.ClawPower != robot.clawClose) {
                    robot.clawServo.setPower(robot.clawClose);
                    robot.ClawPower = robot.clawClose;
                }
            }

            if (gamepad2.b) {
                if (robot.ClawPower != robot.clawOpen) {
                    robot.clawServo.setPower(robot.clawOpen);
                    robot.ClawPower = robot.clawOpen;
                }
            }

            if (!gamepad2.b && !gamepad2.x) {
                if (robot.ClawPower != robot.clawStill) {
                    robot.clawServo.setPower(robot.clawStill);
                    robot.ClawPower = robot.clawStill;
                }
            }

        /* Vertical Arm Motor */
            if (gamepad2.dpad_up) {
                if (robot.VerticalArmPower != 1) {
                    robot.verticalArmMotor.setPower(1);
                    robot.VerticalArmPower = 1;
                }
            } else {
                if (robot.VerticalArmPower != 0) {
                    robot.verticalArmMotor.setPower(0);
                    robot.VerticalArmPower = 0;
                }

                if (gamepad2.dpad_down) {
                    if (robot.VerticalArmPower != -1) {
                        robot.verticalArmMotor.setPower(-1);
                        robot.VerticalArmPower = -1;
                    }
                } else {
                    if (robot.VerticalArmPower != 0) {
                        robot.verticalArmMotor.setPower(0);
                        robot.VerticalArmPower = 0;
                    }
                }
            }

            /*If the left bumper is being pressed it allows the robot to run at only 1/5 of its full speed*/
            if ((gamepad1.left_bumper)) {
                y = gamepad1.left_stick_y / robot.bumperSlowest;
                x = gamepad1.left_stick_x/ robot.bumperSlowest;
                moveYAxis(-y);
                moveXAxis(x);
            }
            /*If the left bumper is not being pressed, this allows the robot to run at full speed*/
            else {
                if(gamepad1.right_bumper) {
                    y = gamepad1.left_stick_y / robot.bumperFastest;
                    x = gamepad1.left_stick_x/ robot.bumperFastest;
                    moveYAxis(-y);
                    moveXAxis(x);
                }
                else{
                    y = gamepad1.left_stick_y / robot.nobumper;
                    x = gamepad1.left_stick_x/ robot.nobumper;
                    moveYAxis(-y);
                    moveXAxis(x);
                }
            }

            if(gamepad1.left_bumper && gamepad1.right_stick_x > 0 || gamepad1.left_bumper && gamepad1.right_stick_x < 0){
                x = gamepad1.right_stick_x/ robot.bumperSlowest;
                rotateXAxis(x);
            }
            else{
                if(gamepad1.right_bumper && gamepad1.right_stick_x > 0 || gamepad1.right_bumper && gamepad1.right_stick_x < 0){
                    x = gamepad1.right_stick_x/ robot.bumperFastest;
                    rotateXAxis(x);
                }
                else{
                    x = gamepad1.right_stick_x/ robot.nobumper;
                    rotateXAxis(x);
                }
            }
        }
    }

    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/

    /* This method does all of the math that calculates the power to set on the wheels
    for moving on the X-Axis (Strafing left & right) */
    public void moveXAxis(double x) {

        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;
        double max;

        /*calculate the power for each wheel*/
        frontLeft = -x;
        frontRight = -x;
        backLeft = x;
        backRight = +x;

        /*Set power on each wheel*/
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(frontLeft);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(frontRight);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(backLeft);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight) {
            robot.backRightMotor.setPower(backRight);
            robot.BackRightPower = backRight;
        }
    }


    /* This method does all of the math that calculates the power to set on the wheels
    for moving on the Y-Axis (Moving forward & backward */
    public void moveYAxis(double y) {

        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;
        double max;

        /*calculate the power for each wheel*/
        frontLeft = -y;
        frontRight = +y;
        backLeft = -y;
        backRight = +y;

        /*Set power on each wheel*/
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(frontLeft);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(frontRight);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(backLeft);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight) {
            robot.backRightMotor.setPower(backRight);
            robot.BackRightPower = backRight;
        }
    }

    /* This function rotates the robot */
    public void rotateXAxis(double x){

        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

                /*calculate the power for each wheel*/
        frontLeft = -x;
        frontRight = -x;
        backLeft = -x;
        backRight = -x;

                /*Set power on each wheel*/
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(frontLeft);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(frontRight);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(backLeft);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight) {
            robot.backRightMotor.setPower(backRight);
            robot.BackRightPower = backRight;
        }
    }
}
