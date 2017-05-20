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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.prototype.DriveForKidsHardware;


@TeleOp(name="Shifter test", group="Robot")
//@Disabled
public class Shiftertest extends LinearOpMode {

    /* Declare OpMode members. */
    DriveForKidsHardware robot = new DriveForKidsHardware();              //'Use a ROUS sharedware

    @Override
    public void runOpMode() throws InterruptedException {
        double Left;
        double Right;


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            int shifter10 = 10;


            if (shifter10 == 1){
                Left = gamepad1.left_stick_y * .1;
                Right = gamepad1.right_stick_y * .1;
                robot.leftMotor.setPower(Left);
                robot.rightMotor.setPower(Right);
                if (gamepad1.dpad_up) {
                    shifter10 = 2;
                }

                if (shifter10 == 2){
                    Left = gamepad1.left_stick_y * .2;
                    Right = gamepad1.right_stick_y * .2;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 3;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 1;
                    }
                }
                if (shifter10 == 3){
                    Left = gamepad1.left_stick_y * .3;
                    Right = gamepad1.right_stick_y * .3;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 4;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 2;
                    }
                }
                if (shifter10 == 4){
                    Left = gamepad1.left_stick_y * .4;
                    Right = gamepad1.right_stick_y * .4;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 5;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 3;
                    }
                }
                if (shifter10 == 5){
                    Left = gamepad1.left_stick_y * .5;
                    Right = gamepad1.right_stick_y * .5;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 6;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 4;
                    }
                }
                if (shifter10 == 6){
                    Left = gamepad1.left_stick_y * .6;
                    Right = gamepad1.right_stick_y * .6;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 7;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 5;
                    }
                }
                if (shifter10 == 7) {
                    Left = gamepad1.left_stick_y * .7;
                    Right = gamepad1.right_stick_y * .7;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 8;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 6;
                    }
                }
                if (shifter10 == 8) {
                    Left = gamepad1.left_stick_y * .8;
                    Right = gamepad1.right_stick_y * .8;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 9;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 7;
                    }
                }
                if (shifter10 == 9) {
                    Left = gamepad1.left_stick_y * .9;
                    Right = gamepad1.right_stick_y * .9;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_up) {
                        shifter10 = 10;
                    }
                    if (gamepad1.dpad_down) {
                        shifter10 = 8;
                    }
                }

                if (shifter10 == 10 ) {
                    Left = gamepad1.left_stick_y;
                    Right = gamepad1.right_stick_y;
                    robot.leftMotor.setPower(Left);
                    robot.rightMotor.setPower(Right);
                    if (gamepad1.dpad_down) {
                        shifter10 = 9;
                    }
                }




            } else {
                Left = gamepad1.left_stick_y;
                Right = gamepad1.right_stick_y;
                robot.leftMotor.setPower(Left);
                robot.rightMotor.setPower(Right);
            }
            }
        }
    }



