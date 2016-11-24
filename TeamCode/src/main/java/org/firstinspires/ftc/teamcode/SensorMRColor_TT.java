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

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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


  @Override
  public void runOpMode() {

    // bPrevState and bCurrState represent the previous and current state of the button.
    boolean bPrevState = false;
    boolean bCurrState = false;
    robot.init(hardwareMap);

    // bLedOn represents the state of the LED.
    boolean bLedOn = false;

    // get a reference to our ColorSensor object.
    // Set the LED in the beginning
    robot.color2.enableLed(bLedOn);


    // wait for the start button to be pressed.
    waitForStart();

    // while the op mode is active, loop and read the RGB data.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive()) {


      // send the info back to driver station using telemetry function.
//      telemetry.addData("LED", bLedOn ? "On" : "Off");
//      telemetry.addData("Clear", robot.mrColorSensor.alpha());
//      telemetry.addData("Red  ", robot.mrColorSensor.red());
//      telemetry.addData("Green", robot.mrColorSensor.green());
//      telemetry.addData("Blue ", robot.mrColorSensor.blue());
//      telemetry.addData("Hue", hsvValues[0]);


      telemetry.addData("2 Clear", robot.color2.alpha());
      telemetry.addData("3 Red  ", robot.color2.red());
      telemetry.addData("4 Green", robot.color2.green());
      telemetry.addData("5 Blue ", robot.color2.blue());
      telemetry.addData("6 I2C address", robot.color2.

      if (robot.color.red() > robot.color.blue()) {
        telemetry.addData("Detecting", "Red");
      }
      else{
        telemetry.addData("Detecting", "Blue");
      }
      telemetry.update();

//      if(robot.color.red()>robot.color.blue() || robot.color.blue()>robot.color.green())
//      {
//        robot.cdim.setLED(1, true);
//        robot.cdim.setLED(0, false);
//      }
//      else if(robot.color.blue()> robot.color.red() ||robot.color.blue()>robot.color.green())
//      {
//        robot.cdim.setLED(1, false);
//        robot.cdim.setLED(0, true);
//
//      }
//      else
//      {
//        robot.cdim.setLED(1, false);
//        robot.cdim.setLED(0, false);
//      }
//      waitForStart();
    }

  }
}
