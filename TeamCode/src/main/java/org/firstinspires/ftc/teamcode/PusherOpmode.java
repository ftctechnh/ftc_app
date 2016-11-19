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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;

import java.util.List;

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

@TeleOp(name="PusherOpmode", group="Testing")  // @Autonomous(...) is the other common choice
//@Disabled
public class PusherOpmode extends LinearOpMode {

    /* Declare OpMode members. */
    static final double SLICER_UP   = 0.0;      // top of the slicer
    static final double SLICER_DOWN = 1.0;      // bottom of the slicer


    static final double SHOOT_DOWN = 0.5;      // starting position
    static final double SHOOT_UP = 0.9;

    static final long RETURN_TIME_MS = 5000;   // time required to shoot the ball

    static final int SAMPLE_SIZE = 15;

    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor = null;
    DcMotor rightMotor = null;
    Servo servoSlicer, servoPusher;

    double slicePosition = SLICER_UP;
    List<Float> rightJoyStickValues = new ArrayList();
    List<Float> leftJoyStickValues = new ArrayList();

    @Override
    public void runOpMode() {

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        leftMotor  = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        servoSlicer = hardwareMap.servo.get("ball_slicer");
        servoPusher = hardwareMap.servo.get("ball_pusher");
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // set the shoot open & ball slicer down/default position
        servoPusher.setDirection(Servo.Direction.FORWARD);
        servoPusher.setPosition(SHOOT_DOWN);
        servoSlicer.setPosition(SLICER_DOWN);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        for(int i=0; i<SAMPLE_SIZE; i++)
        {
            rightJoyStickValues.add(0f);
            leftJoyStickValues.add(0f);
        }

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//            telemetry.addData("Run Time: " + runtime.toString());

            if(gamepad1.x)
            {
                servoPusher.setPosition(.7);
            }
            else if(gamepad1.b)
            {
                servoPusher.setPosition(1);
            }


            rightJoyStickValues.remove(0);
            rightJoyStickValues.add(gamepad1.right_stick_y);

            leftJoyStickValues.remove(0);
            leftJoyStickValues.add(gamepad1.left_stick_y);

            float sumRight = 0;
            float sumLeft = 0;
            for(int i=0; i<SAMPLE_SIZE; i++)
            {
                sumRight += rightJoyStickValues.get(i);
                sumLeft += leftJoyStickValues.get(i);
            }
            float rightPower = sumRight/SAMPLE_SIZE;
            float leftPower = sumLeft/SAMPLE_SIZE;

            //This is backwards on purpose...
            leftMotor.setPower(leftPower); //gamepad1.right_stick_y);//
            rightMotor.setPower(rightPower); //gamepad1.left_stick_y);//

            if (gamepad1.left_bumper) {
                servoSlicer.setPosition(SLICER_DOWN);
            }
            else if (gamepad1.left_trigger > .5){
                servoSlicer.setPosition(SLICER_UP);
            }

//            if (gamepad1.right_bumper) {
//                servoPusher.setPosition(SHOOT_UP);
//                sleep(RETURN_TIME_MS);
//                servoPusher.setPosition(SHOOT_DOWN);
//            }

            telemetry.addData("x", rightPower);
            telemetry.addData("y", leftPower);
            telemetry.addData("pusher", servoPusher.getPosition());
            telemetry.addData("direction", servoPusher.getDirection());
            telemetry.update();

            idle();     // allow something else to run (aka, release the CPU)
        }
    }
}