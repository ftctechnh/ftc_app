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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="MotorOpmode", group="Testing")  // @Autonomous(...) is the other common choice
@Disabled
public class MotorOpmode extends LinearOpMode {

    /* Declare OpMode members. */
    static final double SLICER_UP   = 0.6;      // top of the slicer
    static final double SLICER_DOWN = 0.0;      // bottom of the slicer

    static final double SHOOT_OPEN = 0.9;
    static final double SHOOT_TRAP = 0.45;      // .6 is too high
    static final double SHOOT_FIRE = 0.2;       // .3 vertical

    static final long SHOOTING_TIME_MS = 1000;   // time required to shoot the ball
    static final double SHOOTING_POWER = 1;

    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor = null;
    DcMotor rightMotor = null;
    DcMotor ballMotor = null;
    Servo servoSlicer, servoPusher;

    public enum pusherState {OPEN, TRAP, FIRE};
    pusherState pState;

    @Override
    public void runOpMode() {
        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        ballMotor = hardwareMap.dcMotor.get("ball_motor");
        leftMotor  = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        servoSlicer = hardwareMap.servo.get("ball_slicer");
        servoPusher = hardwareMap.servo.get("ball_pusher");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        ballMotor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // set the shoot open & ball slicer down/default position
        servoPusher.setPosition(SHOOT_OPEN);
        servoSlicer.setPosition(SLICER_DOWN);
        pState = pusherState.TRAP;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//            telemetry.addData("Run Time: " + runtime.toString());
            telemetry.addData("x", gamepad1.left_stick_y);
            telemetry.addData("y", gamepad1.right_stick_y);
            telemetry.update();

            leftMotor.setPower(-gamepad1.left_stick_y);
            rightMotor.setPower(-gamepad1.right_stick_y);

            //state transitions
            switch (pState) {
                case OPEN:
                    if(gamepad1.left_bumper){
                        pState = pusherState.TRAP;
                    }
                    break;
                case TRAP:
                    if(gamepad1.right_bumper){
                        pState = pusherState.FIRE;
                    }
                    else if(gamepad1.left_trigger> 0.5){
                        pState = pusherState.OPEN;
                    }
                    break;
                default:
                    telemetry.addData("State Transition", "Error default");
                    break;
            }

            //motor actions
            switch (pState) {
                case OPEN:
                    servoPusher.setPosition(SHOOT_OPEN);
                    servoSlicer.setPosition(SLICER_DOWN);
                    telemetry.addData("State", "OPEN");
                    break;
                case TRAP:
                    servoPusher.setPosition(SHOOT_TRAP);
                    servoSlicer.setPosition(SLICER_DOWN);
                    telemetry.addData("State", "TRAP");
                    break;
                case FIRE:
                    leftMotor.setPower(0);      // stop the robot before shooting
                    rightMotor.setPower(0);
                    telemetry.addData("State", "FIRE");
                    telemetry.update();

                    // spin up the ball shooter motors
                    ballMotor.setPower(SHOOTING_POWER/2);
                    sleep(SHOOTING_TIME_MS/2);
                    ballMotor.setPower(SHOOTING_POWER);

                    // lift the ball gate
                    servoSlicer.setPosition(SLICER_UP);
                    sleep(SHOOTING_TIME_MS);
                    sleep(SHOOTING_TIME_MS);

                    // push/load the ball
                    servoPusher.setPosition(SHOOT_FIRE);

                    // waits for the pusher to push the ball before returning to open position
                    sleep(SHOOTING_TIME_MS);
                    ballMotor.setPower(0);
                    //Automatically transition to Open State
                    pState = pusherState.OPEN;
                    // no need to reset the motors, just wait for the next opModeIsActive loop iteration
                    break;
                default:
                    break;
            }
            telemetry.update();

            idle();     // allow something else to run (aka, release the CPU)
        }
    }
}