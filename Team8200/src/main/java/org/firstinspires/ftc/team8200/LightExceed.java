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

package org.firstinspires.ftc.team8200;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 *
 * This is an example LinearOpMode that shows how to use
 * a legacy (NXT-compatible) Light Sensor.
 * It assumes that the light sensor is configured with a name of "sensor_light".
 *
 * You can use the X button on gamepad1 to turn Toggle the LED on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "8200: Line Follower", group = "Sensor")
//@Disabled
public class LightExceed extends LinearOpMode {

    LightSensor lightSensor;
    HardwareK9bot robot   = new HardwareK9bot(); // Hardware Device Object
    private ElapsedTime runtime = new ElapsedTime();

    static final double     WHITE_THRESHOLD        = 0.7;


    @Override
    public void runOpMode() {

        robot.init(hardwareMap);


//        // bLedOn represents the state of the LED.
         boolean bLedOn = true;

//        // get a reference to our Light Sensor object.
         lightSensor = hardwareMap.lightSensor.get("light");

//        // Set the LED state in the beginning.
         lightSensor.enableLed(bLedOn);

//        // wait for the start button to be pressed.
         waitForStart();

         MoveToBeacon();
    }

    public void MoveToBeacon() {
        // Move to white line
        while(lightSensor.getLightDetected() < WHITE_THRESHOLD) {
            robot.leftMotor.setPower(0.5);
            robot.rightMotor.setPower(0.5);
        }

        //step 2
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);

        //step 3 waiting for 1 second
        long waitTime = 1L;
        try {
            wait(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) { //while the touch sensor is not touching the wall (or proximity sensor is not touching wall)
            // step 4 turning for ___ seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.25)) {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0.5);
            }

            //step 5 follow the line
            while (lightSensor.getLightDetected() >= WHITE_THRESHOLD) { //follows white light is above threshold AND touch sensor is not touching
                robot.leftMotor.setPower(0.5);
                robot.rightMotor.setPower(0.5);
            }

        }
    }
}
