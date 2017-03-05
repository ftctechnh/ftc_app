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
package org.firstinspires.ftc.teamcode.prototype;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.oldbot.ROUSHardwareMap;


@TeleOp(name="Drive", group="Robot")
@Disabled
public class Drive extends LinearOpMode {

    /* Declare OpMode members. */
    ROUSHardwareMap robot = new ROUSHardwareMap();              //'Use a ROUS sharedware
    //public final static double press_r = .6;
    //public final static double press_l = .6;

    @Override
    public void runOpMode() throws InterruptedException {
        double Left;
        double Right;
        double IntakeO; // intake motor spinning out --->
        double IntakeI; // intake motor spinning in  <---
        // double Aim;


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)

            IntakeO = gamepad2.left_trigger;         //Intake direction ---> (no negative before gampad2
            //because we want the motor to spin in the opposite direction
            // of IntakeI)
            IntakeI = -gamepad2.right_trigger;       //Intake direction <---
            //Aim = -gamepad2.right_stick_y / 2;

            if (gamepad1.right_bumper) {
                Left = gamepad1.left_stick_y;
                Right = -gamepad1.right_stick_y;
                robot.leftMotor.setPower(Right);
                robot.rightMotor.setPower(Left);
            } else {
                Left = -gamepad1.left_stick_y;
                Right = gamepad1.right_stick_y;
                robot.leftMotor.setPower(Left);
                robot.rightMotor.setPower(Right);
            }
            if (gamepad2.right_trigger >= .1) {           //The if/else statement is a safety mechanism so that the
                //program doesn't crash in the event that both triggers
                //are pressed simultaneously
                robot.Intake.setPower(IntakeI);

            } else {
                robot.Intake.setPower(IntakeO);

            }
            if (gamepad2.left_bumper) {
                robot.PressR.setPosition(.78);
            } else {
                robot.PressR.setPosition(.95);
            }

            if (gamepad2.right_bumper) {
                    robot.PressL.setPosition(.76);
            } else {
                    robot.PressL.setPosition(.95);
            }

                //robot.AimingMotor.setPower(Aim);

                //if (gamepad2.right_bumper) {
                //robot.shootMotorL.setPower(1);
                //robot.shootMotorR.setPower(1);

                //}

                //if (gamepad2.dpad_left) {
                //      robot.Press_left.setPosition(press_l);

                // }

                //if (gamepad2.dpad_right) {
                // robot.Press_right.setPosition(press_r);

                // }
            }
        }
    }

