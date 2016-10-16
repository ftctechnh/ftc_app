/* Copyright (c) 2014 Qualcomm Technologies Inc

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

package org.firstinspires.ftc.teamcode.VelocityVortex;

// Import stuff if you need it

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Mecanum Teleop", group="MattTest")

public class TeleOpMain extends RobotOpModes
{
    private float j1leftx;
    private float j1lefty;
    private float j1rightx;

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void loop()
    {
        j1leftx = gamepad1.left_stick_x;
        j1lefty = gamepad1.left_stick_y;
        j1rightx = gamepad1.right_stick_x;
/*
        frontLeftPow = (float)(scaleInput(j1leftx + j1lefty + j1rightx));
        backLeftPow = (float)(scaleInput(j1leftx + j1lefty - j1rightx));
        frontRightPow = (float)(scaleInput(j1lefty - j1leftx - j1rightx));
        backRightPow = (float)(scaleInput(j1lefty - j1leftx + j1rightx));
*/
        frontLeftPow = (float)(scaleInput(j1leftx + j1lefty));
        backLeftPow = (float)(scaleInput(j1leftx + j1lefty));
        frontRightPow = (float)(scaleInput(j1lefty - j1leftx));
        backRightPow = (float)(scaleInput(j1lefty - j1leftx));

        motorFrontLeft.setPower(frontLeftPow);
        motorBackLeft.setPower(backLeftPow);
        motorFrontRight.setPower(frontRightPow);
        motorBackRight.setPower(backRightPow);
    }

    @Override
    public void stop()
    {

    }
}
