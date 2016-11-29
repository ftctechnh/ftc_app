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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;

import java.util.List;

@TeleOp(name="PusherOpmode", group="Testing")  // @Autonomous(...) is the other common choice
//@Disabled
public class PusherOpmode extends LinearOpMode {

    static final double SLICER_UP   = 0.0; // top of the slicer
    static final double SLICER_DOWN = 1.0; // bottom of the slicer
    static final double SHOOT_DOWN = 0.7;  // starting position
    static final double SHOOT_UP = 1;      // shoot position

    private ElapsedTime runtime = new ElapsedTime();

    //The engine which controlls our drive motors
    DriveEngine engine = null;

    //Servos used to move particles
    Servo servoSlicer = null;
    Servo servoPusher = null;

    @Override
    public void runOpMode() {

        engine = new DriveEngine(DriveEngine.engineMode.pusherMode, hardwareMap, gamepad1);

        servoSlicer = hardwareMap.servo.get("ball_slicer");
        servoPusher = hardwareMap.servo.get("ball_pusher");

        // set the shoot open & ball slicer down/default position
        servoPusher.setDirection(Servo.Direction.FORWARD);
        servoPusher.setPosition(SHOOT_DOWN);
        servoSlicer.setPosition(SLICER_DOWN);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            engine.drive();

            if(gamepad1.x)
            {
                servoPusher.setPosition(SHOOT_DOWN);
            }
            else if(gamepad1.b)
            {
                servoPusher.setPosition(SHOOT_UP);
            }

            if (gamepad1.left_bumper)
            {
                servoSlicer.setPosition(SLICER_DOWN);
            }
            else if (gamepad1.left_trigger > .5)
            {
                servoSlicer.setPosition(SLICER_UP);
            }

            telemetry.addData("leftPower", engine.getLeftPower());
            telemetry.addData("rightPower", engine.getRightPower());
            telemetry.addData("pusher", servoPusher.getPosition());
            telemetry.addData("slicer", servoSlicer.getPosition());
            telemetry.update();

            idle();     // allow something else to run (aka, release the CPU)
        }
    }
}