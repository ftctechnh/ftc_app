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

package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//Edited in order to have all of the important constants as final, so that no unintentional modifications are made.
//This class should be used so that any changes made to the robot configuration propagates through all parts of the code that has been written.

public abstract class RobotBase extends LinearOpMode
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors
    protected DcMotor frontRight, frontLeft, backRight, backLeft;

    //Start timer
    protected long startTime = System.currentTimeMillis();

    // Called on initialization (once)
    @Override
    public void runOpMode() throws InterruptedException
    {
        //This all happens during init()

        //Define driving motors
        frontRight = hardwareMap.dcMotor.get("Front Right");
        frontLeft = hardwareMap.dcMotor.get("Front Left");
        backRight = hardwareMap.dcMotor.get("Back Right");
        backLeft = hardwareMap.dcMotor.get("Back Left");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //Custom Initialization steps.
        customInitialization();

        //Wait for the start button to be pressed.
        waitForStart();

        runInstructions(); //This is where the child classes differ.
    }

    //This is used when custom initialization has to be used.  Optional overload.
    protected void customInitialization() throws InterruptedException
    {}

    //This method is called when the start button has been pressed (all init() stuff is handled above).
    protected abstract void runInstructions() throws InterruptedException;
}