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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

abstract class RobotOpModes extends OpMode
{
    // Variable/ hardware declarations
    protected DcMotor motorFrontLeft;
    protected DcMotor motorBackLeft;
    protected DcMotor motorFrontRight;
    protected DcMotor motorBackRight;

    protected float frontLeftPow;
    protected float backLeftPow;
    protected float frontRightPow;
    protected float backRightPow;

    @Override
    public void init()
    {
        hardwareMap();
        resetPower();
    }

    // No use in overloading loop(), that's like calling an infinite loop inside an infinite loop

    // Called when opmode terminated, basically destructor
    @Override
    public void stop()
    {

    }

    protected void hardwareMap()
{
    motorFrontLeft = hardwareMap.dcMotor.get("fl");
    motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    motorBackLeft = hardwareMap.dcMotor.get("bl");
    motorFrontRight = hardwareMap.dcMotor.get("fr");
    motorBackRight = hardwareMap.dcMotor.get("br");

    return;
    }

    protected void resetPower()
    {
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);

        return;
    }

    protected double scaleInput(final double power)
    {
        double scaledPower;

        scaledPower = Math.pow(power , 3);

        return scaledPower;
    }
}