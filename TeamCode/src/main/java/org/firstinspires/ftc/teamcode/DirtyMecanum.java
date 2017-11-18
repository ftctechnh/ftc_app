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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * 'Dirty' Mecanum TeleOp
 * jlemke @ ~11:20 AM 9/25/17
 *
 * This is my _first_ iteration on a Mecanum drive opmode for p r o t o t y p i n g.
 * Enjoy responsibly.
 */

@TeleOp(name="Dirty Mecanum TeleOp", group="Iterative Opmode")
@Disabled

public class DirtyMecanum extends OpMode
{
    // move that gear up
    Hardware750 robot = new Hardware750();
    // setup our cute little dpad array
    // according to language spec, these should all default to false anyway.
    boolean[] dpadStates = new boolean[4];
    // setup runtime timer
    private ElapsedTime runtime = new ElapsedTime();
    static double SPEED = 1;

    @Override
    public void init() {
        telemetry.addData("Status", "Uninitialized...");
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // TODO: find a better way to do this
        dpadStates[0] = gamepad1.dpad_up;
        dpadStates[1] = gamepad1.dpad_right;
        dpadStates[2] = gamepad1.dpad_down;
        dpadStates[3] = gamepad1.dpad_left;



        if (dpadStates[0]){         // full forward
            robot.setAllDriveMotors(SPEED);
        } else if (dpadStates[1]) { // right strafe
            robot.flDrive.setPower(SPEED);
            robot.frDrive.setPower(-1 * SPEED);
            robot.rlDrive.setPower(-1 * SPEED);
            robot.rrDrive.setPower(SPEED);
        } else if (dpadStates[2]) { // full reverse
            robot.setAllDriveMotors(-1 * SPEED);
        } else if (dpadStates[3]) { // left strafe
            robot.flDrive.setPower(-1 * SPEED);
            robot.frDrive.setPower(SPEED);
            robot.rlDrive.setPower(SPEED);
            robot.rrDrive.setPower(-1 * SPEED);
        } else if (gamepad1.right_bumper) {
            robot.flDrive.setPower(SPEED);
            robot.frDrive.setPower(-1 * SPEED);
            robot.rlDrive.setPower(SPEED);
            robot.rrDrive.setPower(-1 * SPEED);
        } else if (gamepad1.left_bumper) {
            robot.flDrive.setPower(-1 * SPEED);
            robot.frDrive.setPower(SPEED);
            robot.rlDrive.setPower(-1 * SPEED);
            robot.rrDrive.setPower(SPEED);
        } else if (gamepad1.a){
            robot.flDrive.setPower(SPEED);
        } else if (gamepad1.b){
            robot.frDrive.setPower(SPEED);
        } else if (gamepad1.x){
            robot.rlDrive.setPower(SPEED);
        } else if (gamepad1.y){
            robot.rrDrive.setPower(SPEED);
        } else {

            robot.setAllDriveMotors(0);
        }

        telemetry.addData("D-Pad up", dpadStates[0]);
        telemetry.addData("D-Pad right", dpadStates[1]);
        telemetry.addData("D-Pad down", dpadStates[2]);
        telemetry.addData("D-Pad left", dpadStates[3]);
        telemetry.addData("ENCODERS", "");
        telemetry.addData("FL", robot.flDrive.getCurrentPosition());
        telemetry.addData("FR", robot.frDrive.getCurrentPosition());
        telemetry.addData("RL", robot.rlDrive.getCurrentPosition());
        telemetry.addData("RR", robot.rrDrive.getCurrentPosition());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
