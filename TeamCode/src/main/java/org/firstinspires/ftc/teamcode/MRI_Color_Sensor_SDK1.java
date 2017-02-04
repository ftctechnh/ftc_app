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

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.TouchSensor;

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
@Autonomous(name = "Sensor: MRI Color Sensor", group = "Sample")
//@Disabled
public class MRI_Color_Sensor_SDK1 extends LinearOpMode {

  //ColorSensor colorSensor;    // Hardware Device Object
  HardwarePushbot_TT         robot   = new HardwarePushbot_TT();   // Use a Pushbot's hardware
  private ElapsedTime runtime = new ElapsedTime();
  ColorSensor colorLocal = null;

  TouchSensor touch;         //Instance of TouchSensor - for changing color sensor mode

  @Override
  public void runOpMode() throws InterruptedException {

    // hsvValues is an array that will hold the hue, saturation, and value information.
//    float hsvValues[] = {0F,0F,0F};

    // values is a reference to the hsvValues array.
//    final float values[] = hsvValues;

    boolean touchState = false;  //Tracks the last known state of the touch sensor
    boolean LEDState = true;     //Tracks the mode of the color sensor; Active = true, Passive = false

    robot.init(hardwareMap);
//    colorLocal = robot.color ;

    telemetry.addData("Initiatilized", "Press Start");
    telemetry.update();

    // wait for the start button to be pressed.
    waitForStart();

    robot.color.enableLed(LEDState);  //Set the mode of the LED; Active = true, Passive = false
    //Active - For measuring reflected light. Cancels out ambient light
    //Passive - For measuring ambient light, eg. the FTC Color Beacon

    float hsvValues[] = {0, 0, 0};  //used to get Hue

    // while the op mode is active, loop and read the RGB data.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive()) {

      //The below two if() statements ensure that the mode of the color sensor is changed only once each time the touch sensor is pressed.
      //The mode of the color sensor is saved to the sensor's long term memory. Just like flash drives, the long term memory has a life time in the 10s or 100s of thousands of cycles.
      //This seems like a lot but if your program wrote to the long term memory every time though the main loop, it would shorten the life of your sensor.

      if (!touchState && robot.touch.isPressed()) {  //If the touch sensor is just now being pressed (was not pressed last time through the loop but now is)
        touchState = true;                   //Change touch state to true because the touch sensor is now pressed
        LEDState = !LEDState;                //Change the LEDState to the opposite of what it was
        robot.color.enableLed(LEDState);     //Set the mode of the color sensor using LEDState
      }
      if (!robot.touch.isPressed()) {                //If the touch sensor is now pressed
        touchState = false;                  //Set the touchState to false to indicate that the touch sensor was released
      }

      //calculate hue
      Color.RGBToHSV(robot.color.red() * 8, robot.color.green() * 8, robot.color.blue() * 8, hsvValues);

      //display values
      telemetry.addData("2 Clear", robot.color.alpha());
      telemetry.addData("3 Red  ", robot.color.red());
      telemetry.addData("4 Green", robot.color.green());
      telemetry.addData("5 Blue ", robot.color.blue());
      telemetry.addData("6 Hue", hsvValues[0]);
      telemetry.addData("7 Touch", robot.touch.isPressed());
      telemetry.update();

      //illuminate the RED/BLUE LED on the Core Device Interface if the RED/BLUE value is greatest
      if (robot.color.red() > robot.color.blue() && robot.color.red() > robot.color.green()) {
        robot.cdim.setLED(1, true);           //Red ON
        robot.cdim.setLED(0, false);          //Blue OFF
      } else if (robot.color.blue() > robot.color.red() && robot.color.blue() > robot.color.green()) {
        robot.cdim.setLED(1, false);          //Red OFF
        robot.cdim.setLED(0, true);           //Blue ON
      } else {
        robot.cdim.setLED(1, false);           //Red OFF
        robot.cdim.setLED(0, false);           //Blue OFF
      }

      waitOneFullHardwareCycle();  //wait for all new data to go from the phone to the controllers and from the controllers to the phone.

    }

  }
}
