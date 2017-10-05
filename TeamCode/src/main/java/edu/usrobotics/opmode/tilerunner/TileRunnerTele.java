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
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package edu.usrobotics.opmode.tilerunner;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.usrobotics.opmode.tilerunner.TileRunnerHardware;

@TeleOp(name="TileRunner Teleop", group="Tilerunner")
public class TileRunnerTele extends OpMode {

    private TileRunnerHardware robot = new TileRunnerHardware();

    @Override
    public void init() {

        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void loop() {

        float leftY = gamepad1.left_stick_y;
        float rightY = gamepad1.right_stick_y;

        float creepyness = gamepad1.left_trigger;
        float multiplier = 1f - (creepyness * 0.66f);

        float speedL = -rightY * multiplier;
        float speedR = -leftY * multiplier;

        float gripperLiftPower = -gamepad2.right_stick_y * 0.5f;

        if(gripperLiftPower < 0.01f && gripperLiftPower > -0.01f){

            gripperLiftPower = 0.1f;

        }

        // 0.2 is the down position?
        // 1 is the up position

        // open pos: right, left, .75, .36
        // closed: .53, .45
        // x is close, b is open

        if(gamepad1.a){

            robot.ballKnocker.setPosition(0);

        }

        if(gamepad1.b){

            robot.ballKnocker.setPosition(0.2);

        }

        if(gamepad2.b){

            robot.openGripper();

        }

        if(gamepad2.x){

            robot.closeGripper();

        }

        robot.leftMotor1.setPower(speedL);
        robot.leftMotor2.setPower(speedL);

        robot.rightMotor1.setPower(speedR);
        robot.rightMotor2.setPower(speedR);

        robot.gripperLift.setPower(gripperLiftPower);

        telemetry.addData("Ball knocker pos", robot.ballKnocker.getPosition());
        telemetry.addData("Gripper right pos", robot.gripperRight.getPosition());
        telemetry.addData("Gripper left pos", robot.gripperLeft.getPosition());

        telemetry.update();

    }

}
