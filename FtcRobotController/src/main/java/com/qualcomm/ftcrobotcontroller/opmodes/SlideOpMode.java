/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * A simple example of a linear op mode that will approach an IR beacon
 */
public class SlideOpMode extends LinearOpMode {

    final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster
    final static double HOLD_IR_SIGNAL_STRENGTH = 0.20; // Higher values will cause the robot to follow closer

    DcMotor motor_inner;
    DcMotor motor_outer;



    @Override
    public void runOpMode() throws InterruptedException {

        // set up the hardware devices we are going to use

        motor_inner = hardwareMap.dcMotor.get("motor_inner");
        motor_outer = hardwareMap.dcMotor.get("motor_outer");


        // wait for the start button to be pressed
        waitForStart();

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        float throttle_inner = gamepad1.left_stick_y;
        float throttle_outer = gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        throttle_inner = Range.clip(throttle_inner, -1, 1);
        throttle_outer = Range.clip(throttle_outer, -1, 1);

        if (java.lang.Math.abs (throttle_inner)<0.1){
            throttle_inner=0.0f;
        }
        if (java.lang.Math.abs (throttle_outer)<0.1){
            throttle_outer=0.0f;
        }

        // now approach the beacon
        motor_inner.setPower(throttle_inner);
        motor_outer.setPower(throttle_outer);

        telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", throttle_inner));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", throttle_outer));


        waitForNextHardwareCycle();
    }
}
