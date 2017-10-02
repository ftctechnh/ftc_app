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
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.android.dex.EncodedValueReader;


/**
 *This moves the vertical arm up and down with a motor~~
 */

@TeleOp(name = "Vertical", group = "BACONbot")
//@Disabled
public class VerticalArmCode extends LinearOpMode {

    /* This says to use BACONbot hardware */
    armclass robot = new armclass();


    /* Allow This Teleop to run! */
    @Override
    public void runOpMode() throws InterruptedException {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        /*These values are used for the drive*/


        /* Send telemetry message to signify that the robot's ready to play! */
        telemetry.addLine("♥ ♪ Ready to Run ♪ ♥");
        telemetry.update();

        /* Wait for the game to start (driver presses PLAY)*/
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up) {
                robot.verticalArmMotor.setPower(1);
            }
            if (gamepad1.dpad_down) {
                robot.verticalArmMotor.setPower(-1);
            }

            telemetry.addData("FrontLeft: ", robot.verticalArmMotor.getCurrentPosition());
        }
    }

}


