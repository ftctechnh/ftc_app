/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification,
 * are permitted (subject to the limitations in the disclaimer below)
 * provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to
  * endorse or
 * promote products derived from this software without specific prior written
  * permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
  * THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/*
 * This is an example LinearOpMode that shows how to use a Modern Robotics
 * Color Sensor. It uses the Trainerbot paddle to deploy the sensor, reads
 * the color of a Relic Recovery game piece, and reports.
 *
 * Physical setup:
 *   Put the Trainerbot on a Relic Recovery Field. It's probably best to stay
 * off the Balancing Stone for now; let's keep it simple to begin with.
 *   Put a Platform (black piece of wood with two depressions and a white
 * stripe in the middle) in front of the Trainerbot. Put a Red and a Blue
 * Jewel in the Platform depressions.
 *   Adjust the Trainerbot so its paddle deployed forward would put the color
 * sensor right between the two Jewels. When this opmode runs, the paddle
 * will begin stowed back into the Trainerbot, but will quickly deploy
 * forward to the position you adjusted. That's where it will try to get the
 * color of the left Jewel.
 *   The Modern Robotics color sensor is not very sensitive, so it may help
 * to put it closer to the left Jewel, inside 2cm. Beyond that, it's almost
 * blind.
 *
 *   It is assumed that the color sensor is configured with a name of
 * "colorSensor". That's done in TrainerbotN.xml, of which there are copies in
 * the hardware folder in the root of this project. But the one that counts is
 * the one that rides on the Robot Controller phone.
 *
 * This example is copied from sample SensorMRColor and modified to suit a
 * Trainerbot.
 */
@Autonomous(name = "Sensor: MR Color", group = "Trainerbot")
//@Disabled
public class SensorMRColor extends LinearOpMode {

  @Override
  public void runOpMode() {
    ColorSensor colorSensor;    // Hardware Device Object
    colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

    // wait for the start button to be pressed.
    waitForStart();

    // Turn the LED on.
    colorSensor.enableLed(true);

    // Read the RGB data, and report colors to driver station.
    telemetry.addData("Red  ", colorSensor.red());
    telemetry.addData("Green", colorSensor.green());
    telemetry.addData("Blue ", colorSensor.blue());
    telemetry.update();
    sleep (3000);
  }
}
