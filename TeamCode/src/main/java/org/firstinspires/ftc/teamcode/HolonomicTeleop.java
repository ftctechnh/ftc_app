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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// Created by Roma Bhatia  on 9/21/17
//
//
// Last edit: 10/21/17 by Mrinaal Ramachandran

/**
 * This file provides basic Teleop driving for a Holonomic robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Holonomic hardware class to define the devices on the robot.
 * All device access is managed through the HolonomicHardware class.
 *
 * This particular OpMode executes a basic Holonomic Drive Teleop for a Holonomic bot with omniwheels
 * facing at 45 degree angles.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
    /*
    * Created by Roma Bhatia (@sugarcrystals01) on 9/21/17
    * Last edit: 10/29/17
    */
@TeleOp(name="Holonomic_TeleOp", group="We Love Pi")

public class HolonomicTeleop extends OpMode {

    // DECLARE OPMODE MEMBERS
    HolonomicHardware robot = new HolonomicHardware(); // use the class created to define a Pushbot's hardware

    double limiter = 1;

    @Override
    public void init() {
        // INIT robot
        robot.init(hardwareMap);
        robot.dropper.setPosition(0);

        // TELL DRIVER STATION THAT ROBOT IS INIT
        telemetry.addData("Status", "Initialized");    //
    }

    @Override
    public void loop() {

        // THE X AND Y AXIS FOR EACH SIDE (LEFT AND RIGHT)
        double left_y;
        double left_x;
        double right_y;
        double right_x;

        // SETTING VALUES FOR LEFT X AND Y
        left_y = -gamepad1.left_stick_y * limiter;
        left_x = gamepad1.left_stick_x * limiter;
        // SETTING VALUES FOR RIGHT X AND Y
        right_y = -gamepad1.right_stick_y * limiter;
        right_x = gamepad1.right_stick_x * limiter;


        // SETTING THE POWER TO MOVE THE ROBOT WITH EACH MOTOR

        if ((right_x == 0) && gamepad1.dpad_down == false && gamepad1.dpad_left == false && gamepad1.dpad_right == false && gamepad1.dpad_up == false) {

            robot.F_L.setPower(left_y + left_x);
            robot.F_R.setPower(left_x - left_y);
            robot.R_R.setPower(-left_y - left_x);
            robot.R_L.setPower(left_y - left_x);
        }

        if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0) {
            limiter = 0.2;

            telemetry.addData("limiter is", limiter);
        }

         else if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0) {
            limiter = 0.3;
            telemetry.addData("limiter is", limiter);

        } else {
            limiter = 0.5;
            telemetry.addData("Limiter is ", limiter);
        }


        if (gamepad2.a) {
            robot.clamp.setPower(0.2);
        } else if (gamepad2.b) {
            robot.clamp.setPower(-0.2);
        } else {
            robot.clamp.setPower(0);
        }


        if (gamepad2.right_trigger > 0) {
            robot.elevator.setPower(gamepad2.right_trigger * 0.65);
        } else if (gamepad2.left_trigger > 0) {
            robot.elevator.setPower(-gamepad2.left_trigger * 0.2);
        } else {
            robot.elevator.setPower(0);
        }

        // MAKE THE ROBOT ROTATE
        if ((left_y + left_x == 0) && (right_x != 0)) {
            int dir = 1;
            if (right_x < 0) dir = -1;

            robot.F_L.setPower(dir * limiter);
<<<<<<< HEAD
            robot.F_R.setPower(dir * limiter);
            robot.R_R.setPower(dir * limiter);
            robot.R_L.setPower(dir * limiter);
=======
            robot.F_R.setPower(dir *limiter);
            robot.R_R.setPower(dir *limiter);
            robot.R_L.setPower(dir *limiter);
>>>>>>> 67ecde76a2a33d97f83233d006cf13147434501b
        }

        //DPAD DIRECTIONS
         if (gamepad1.dpad_up) {
            //DPAD FORWARD 0.5
            robot.F_L.setPower(0.5*limiter);
            robot.F_R.setPower(-0.5*limiter);
            robot.R_L.setPower(0.5*limiter);
            robot.R_R.setPower(-0.5*limiter);
        } else if (gamepad1.dpad_down) {
            //DPAD BACKWARD 0.5
            robot.F_L.setPower(-0.5*limiter);
            robot.F_R.setPower(0.5*limiter);
            robot.R_L.setPower(-0.5*limiter);
            robot.R_R.setPower(0.5*limiter);
        } else if (gamepad1.dpad_left) {
            //DPAD LEFT 0.5
            robot.F_L.setPower(-0.5*limiter);
            robot.F_R.setPower(-0.5*limiter);
            robot.R_L.setPower(0.5*limiter);
            robot.R_R.setPower(0.5*limiter);
        } else if (gamepad1.dpad_right) {
            //DPAD RIGHT 0.5
            robot.F_L.setPower(0.5*limiter);
            robot.F_R.setPower(0.5*limiter);
            robot.R_L.setPower(-0.5*limiter);
            robot.R_R.setPower(-0.5*limiter);
        } else if (gamepad1.dpad_up && gamepad1.dpad_right) {
            //DPAD NORTHEAST (RIGHT FRONT DIAGONAL)
            robot.F_L.setPower(0.5*limiter);
            robot.F_R.setPower(0*limiter);
            robot.R_L.setPower(0*limiter);
            robot.R_R.setPower(-0.5*limiter);
        } else if (gamepad1.dpad_up && gamepad1.dpad_left) {
            //DPAD NORTHWEST (LEFT FRONT DIAGONAL
            robot.F_L.setPower(0*limiter);
            robot.F_R.setPower(-0.5*limiter);
            robot.R_L.setPower(0.5*limiter);
            robot.R_R.setPower(0*limiter);
        } else if (gamepad1.dpad_down && gamepad1.dpad_right) {
            robot.F_L.setPower(0*limiter);
            robot.F_R.setPower(0.5*limiter);
            robot.R_L.setPower(-0.5*limiter);
            robot.R_R.setPower(0*limiter);
        } else if (gamepad1.dpad_down && gamepad1.dpad_left) {
            robot.F_L.setPower(-0.5*limiter);
            robot.F_R.setPower(0*limiter);
            robot.R_L.setPower(0*limiter);
            robot.R_R.setPower(0.5*limiter);
        }

    // TELEMETRY WITH INFO ABOUT POWER, AND VALUES OF (X,Y)
        telemetry.addData("left stick y","%.2f",left_y);
        telemetry.addData("right_y stick y","%.2f",right_y);
        telemetry.addData("left stick x","%.2f",left_x);
        telemetry.addData("right_y stick x","%.2f",right_x);
        telemetry.addData("left front drive","%.2f",robot.F_L.getPower());
        telemetry.addData("right_y front drive","%.2f",robot.F_R.getPower());
        telemetry.addData("left back drive","%.2f",robot.R_L.getPower());
        telemetry.addData("right_y back drive","%.2f",robot.R_R.getPower());
        telemetry.addData("robot clamp","%.2f",robot.clamp.getPower());
        telemetry.addData("elevator","%.2f",robot.elevator.getPower());
        telemetry.addData("a button","%b",gamepad2.a);
    }
}
