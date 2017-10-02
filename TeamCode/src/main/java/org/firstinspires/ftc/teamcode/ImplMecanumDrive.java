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
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * algorithm for calculation of magnitude
 * for use with advanced mecanum drives
 * jlemke 9/26/2017 @ 7:26 AM
 */

@TeleOp(name="Mecanum Drive Test", group="TeleOp")
public class ImplMecanumDrive extends OpMode
{
    // move that gear up
    Hardware750 robot = new Hardware750();
    // setup runtime timer
    private ElapsedTime runtime = new ElapsedTime();

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
        telemetry.addData("LeftTrig", gamepad1.left_trigger);
        telemetry.addData("RightTrig", gamepad1.right_trigger);
        telemetry.addData("lstick X", gamepad1.left_stick_x);
        telemetry.addData("lstick Y", (-1 * gamepad1.left_stick_y));
        double presquare = Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2);
        telemetry.addData("presquare", presquare);
        double magnitude = ((-1 * gamepad1.left_stick_y) < 0) ? -1*Math.sqrt(presquare) : Math.sqrt(presquare);
        if (magnitude > 1) {
            magnitude = 1;
        }
        telemetry.addData("magnitude", magnitude);
        double angle = Math.atan2((-1*gamepad1.left_stick_y),gamepad1.left_stick_x);
        if (angle < 0) {
            angle += Math.PI*2;
        }
        double triggerVal = (-1 * gamepad1.left_trigger) + gamepad1.right_trigger;
        telemetry.addData("ang le", angle);
        telemetry.addData("number of pi", (angle / Math.PI));
        double extraPi = angle / Math.PI;
        //Makes variables for the Voltage Magnitude
        double VM1 = ((magnitude) * (Math.sin(extraPi + (Math.PI / 4))) + triggerVal);
        double VM2 = ((magnitude) * (Math.cos(extraPi + (Math.PI / 4))) - triggerVal);
        double VM3 = ((magnitude) * (Math.cos(extraPi + (Math.PI / 4))) + triggerVal);
        double VM4 = (((magnitude) * (Math.sin(extraPi + (Math.PI / 4))) - triggerVal));
        //finds the largest Voltage Magnitude above 1 or below -1 and records it
        double [] thing = {1, VM1, VM2, VM3, VM4};
        int idk = 0;
        for (int i = 1; i < 5; i++) {
          if ((Math.abs(thing [i]) > 1) && (Math.abs(thing [i]) > Math.abs(thing [i - 1]))) {
                idk = i;
          }
        }
        //Create an array
        double [] VM20 = new double [4];
        VM20 [0] = 0;
        VM20 [1] = 0;
        VM20 [2] = 0;
        VM20 [3] = 0;
        //Set the final values and make sure they're under 1
        for (int i = 1; i < 5; i++) {
            VM20[i - 1] = (thing[i]/thing[idk]);
        }
        if(VM20[0] > 1){
            VM20[0] = 1;
        } else if (VM20[0] < -1){
            VM20[0] = -1;
        }
        if(VM20[1] > 1){
            VM20[1] = 1;
        } else if (VM20[1] < -1){
            VM20[1] = -1;
        }
        if(VM20[2] > 1){
            VM20[2] = 1;
        } else if (VM20[2] < -1){
            VM20[2] = -1;
        }
        if(VM20[3] > 1){
            VM20[3] = 1;
        } else if (VM20[3] < -1){
            VM20[3] = -1;
        }
        telemetry.addData("VM1", VM20 [0]);
        telemetry.addData("VM2", VM20 [1]);
        telemetry.addData("VM3", VM20 [2]);
        telemetry.addData("VM4", VM20 [3]);
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
