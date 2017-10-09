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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file provides basic Telop driving for a Holonomic robot.
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
    * Last edit: 10/7/17
    */
@TeleOp(name="Holonomic: Teleop Holo", group="We Love Pi")

public class HolonomicTeleop extends OpMode{

    /* Declare OpMode members. */
    HolonomicHardware robot       = new HolonomicHardware(); // use the class created to define a Pushbot's hardware
    // could also use HardwarePushbotMatrix class.

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Initialized");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */

    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //define variables for a cleaner code.
        // 2 is for the X AXIS on the JOYSTICK.
        double left;
        double left2;
        double right;
        double right2;


        // Run wheels in holonomic mode (note: The joystick goes negative when pushed forwards, so negate it)

        //For Joystick 1(LEFT JOYSTICK): direction moving forward, backward, left, right & straight at an angle
        left = -gamepad1.left_stick_y;
        left2 = gamepad1.left_stick_x;
        //For Joystick 2 (RIGHT JOYSTICK): rotation clockwise and counterclockwise
        right = -gamepad1.right_stick_y;
        right2 = gamepad1.right_stick_x;


        //Use gamepad 1 LEFT JOYSTICK for right, left, forward, and back movement
        robot.leftDrive.setPower(left + left2);
        robot.rightDrive.setPower(left2 - left);
        robot.rightbackdrive.setPower(-left - left2);
        robot.leftbackdrive.setPower(left - left2);

        //Use gamepad 1 RIGHT JOYSTICK for counterclockwise and clockwise rotation
        //NOTE: this is untested, but in theory it should work

        if((left+left2==0) && (right2!=0)) {
            int dir = 1;
            if(right2<0) dir =-1;
            robot.leftDrive.setPower(dir*0.3);
            robot.rightDrive.setPower(dir*0.3);
            robot.rightbackdrive.setPower(dir*0.3);
            robot.leftbackdrive.setPower(dir*0.3);
        }


        

        // Send telemetry message to signify robot running;
        telemetry.addData("left stick y",  "%.2f", left);
        telemetry.addData("right stick y", "%.2f", right);
        telemetry.addData("left stick x", "%.2f",left2);
        telemetry.addData("right stick x", "%.2f", right2);
        telemetry.addData("left front drive", "%.2f", robot.leftDrive.getPower());
        telemetry.addData("right front drive", "%.2f", robot.rightDrive.getPower());
        telemetry.addData("left back drive", "%.2f", robot.leftbackdrive.getPower());
        telemetry.addData("right back drive", "%.2f", robot.rightbackdrive.getPower());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
