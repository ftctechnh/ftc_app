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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.annotation.Target;


@TeleOp(name="BlockGrabber Linear OpMode", group="Linear Opmode")
//@Disabled
public class BlockGrabber extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor GrabberMotor = null;
    private Servo s1, s2 = null;

    private int initial_angle = 0;
    private int final_angle = 45;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        GrabberMotor = hardwareMap.get(DcMotor.class, "Grabber1");
        s1 = hardwareMap.get(Servo.class, "Servo1");
        s2 = hardwareMap.get(Servo.class, "Servo2");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        GrabberMotor.setDirection(DcMotor.Direction.FORWARD);
        double initial_angle_value = Range.clip(initial_angle, 0,1 );
        double final_angle_value = Range.clip(final_angle, 0,1 );

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        GrabberMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        GrabberMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        GrabberMotor.setTargetPosition(87);

        GrabberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            while (GrabberMotor.isBusy()){
                GrabberMotor.setPower(-0.2);

            }
            GrabberMotor.setPower(0.0);

            s1.setPosition(initial_angle_value);
            s2.setPosition(initial_angle_value);
            boolean pressed = false;
            if (gamepad1.x && !pressed){
                s1.setPosition(final_angle_value);
                s2.setPosition(final_angle_value);

                GrabberMotor.setTargetPosition(87);
                GrabberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (GrabberMotor.isBusy()){
                    GrabberMotor.setPower(0.2);

                }
                GrabberMotor.setPower(0.0);

                pressed = true;
            }
            if (gamepad1.x && pressed) {
                s1.setPosition(initial_angle_value);
                s2.setPosition(initial_angle_value);

                GrabberMotor.setTargetPosition(10);
                GrabberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (GrabberMotor.isBusy()){
                    GrabberMotor.setPower(-0.2);

                }
                GrabberMotor.setPower(0.0);


            }



        }
    }
}
