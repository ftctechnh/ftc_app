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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 *
 * This is an example LinearOpMode that shows how to use
 * a Modern Robotics Color Sensor.
 *
 * The op mode assumes that the color sensor
 * is configured with a name of "sensor_color".
 *
 * You can use the X button on gamepad1 to toggle the LED on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Sensor: MR Color", group = "Pushbot")
//@Disabled
public class SensorMRColor_TT extends LinearOpMode {

  //ColorSensor colorSensor;    // Hardware Device Object
  HardwarePushbot_TT         robot   = new HardwarePushbot_TT();   // Use a Pushbot's hardware
  private ElapsedTime runtime = new ElapsedTime();
  ColorSensor colorLocal = null;


  @Override
  public void runOpMode() {

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F,0F,0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    robot.init(hardwareMap);
    colorLocal = robot.color ;

    // bLedOn represents the state of the LED.
    boolean bLedOn = false;

    // get a reference to our ColorSensor object.
    // Set the LED in the beginning
//    colorLocal.enableLed(bLedOn);

    // while the op mode is active, loop and read the RGB data.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.

    // wait for the start button to be pressed.
    telemetry.addData("Initiatilized", "Press Start");
    telemetry.update();
    waitForStart();

    while (opModeIsActive()) {

      if (robot.color.red() > robot.color.blue()) {
        telemetry.addData("Detecting", "Red");
        telemetry.update();
      }
      else if (robot.color.red() < robot.color.blue()){
        telemetry.addData("Detecting", "Blue");
        telemetry.update();
      }
      else {
        telemetry.addData("Detecting", "Neither");
        telemetry.update();
      }

      // send the info back to driver station using telemetry function.
//      telemetry.addData("LED", bLedOn ? "On" : "Off");
//      telemetry.addData("2 Clear", colorLocal.alpha());
//      telemetry.addData("3 Red  ", colorLocal.red());
//      telemetry.addData("4 Green", colorLocal.green());
//      telemetry.addData("5 Blue ", colorLocal.blue());
//      telemetry.addData("Hue", hsvValues[0]);
//      telemetry.addData("Saturation", hsvValues[1]);
//      telemetry.addData("Values", hsvValues[2]);
//      //telemetry.update();


      // convert the RGB values to HSV values.
//      Color.RGBToHSV(colorLocal.red() * 8, colorLocal.green() * 8, colorLocal.blue() * 8, hsvValues);
//      waitForStart();
    }

  }
}
