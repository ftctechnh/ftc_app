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

package org.overlake.ftc.team_7330.Testing;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/*
 *
 * This is an example LinearOpMode that shows how to use
 * the Adafruit RGB Sensor.  It assumes that the I2C
 * cable for the sensor is connected to an I2C port on the
 * Core Device Interface Module.
 *
 * It also assuems that the LED pin of the sensor is connected
 * to the digital signal pin of a digital port on the
 * Core Device Interface Module.
 *
 * You can use the digital port to turn the sensor's onboard
 * LED on or off.
 *
 * The op mode assumes that the Core Device Interface Module
 * is configured with a name of "dim" and that the Adafruit color sensor
 * is configured as an I2C device with a name of "color".
 *
 * It also assumes that the LED pin of the RGB sensor
 * is connected to the signal pin of digital port #5 (zero indexed)
 * of the Core Device Interface Module.
 *
 * You can use the X button on either gamepad to turn the LED on and off.
 *
 */
@TeleOp(name="AdafruitRGBExample")
public class TestColor extends SynchronousOpMode
{

  ColorSensor sensorRGBLeft;
    ColorSensor sensorRGBRight;

  float hueLeft;
    float hueRight;

  DeviceInterfaceModule cdim;

  // we assume that the LED pin of the RGB sensor is connected to
  // digital port 5 (zero indexed).
  static final int LED_CHANNEL_L = 5;
    static final int LED_CHANNEL_R = 4;

  @Override
  public void main() throws InterruptedException {

    // write some device information (connection info, name and type)
    // to the log file.
    hardwareMap.logDevices();

    // get a reference to our DeviceInterfaceModule object.
    cdim = hardwareMap.deviceInterfaceModule.get("dim");

    // set the digital channel to output mode.
    // remember, the Adafruit sensor is actually two devices.
    // It's an I2C sensor and it's also an LED that can be turned on or off.
    cdim.setDigitalChannelMode(LED_CHANNEL_L, DigitalChannelController.Mode.OUTPUT);
      cdim.setDigitalChannelMode(LED_CHANNEL_R, DigitalChannelController.Mode.OUTPUT);


      // get a reference to our ColorSensor object.
    sensorRGBLeft = hardwareMap.colorSensor.get("colorLeft");
      sensorRGBRight = hardwareMap.colorSensor.get("colorRight");

    // bEnabled represents the state of the LED.
    boolean bEnabledLeft = true;
      boolean bEnabledRight = true;

    // turn the LED on in the beginning, just so user will know that the sensor is active.
    cdim.setDigitalChannelState(LED_CHANNEL_L, bEnabledLeft);
      cdim.setDigitalChannelState(LED_CHANNEL_R, bEnabledRight);

    // Set up our dashboard computations
    composeDashboard();

    // wait for the start button to be pressed.
    waitForStart();

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValuesLeft[] = {0F,0F,0F};
      float hsvValuesRight[] = {0F,0F,0F};

    // values is a reference to the hsvValues arrays.
    final float valuesLeft[] = hsvValuesLeft;
      final float valuesRight[] = hsvValuesRight;

    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    // final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);
    // ^^^Ruthie thinks this is really cool

    // bPrevState and bCurrState represent the previous and current state of the button.
    // boolean bPrevState = false;
    // boolean bCurrState = false;

    // while the op mode is active, loop and read the RGB data.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive())
    {
      // convert the RGB values to HSV values.
      Color.RGBToHSV((sensorRGBLeft.red() * 255) / 800, (sensorRGBLeft.green() * 255) / 800, (sensorRGBLeft.blue() * 255) / 800, hsvValuesLeft);
        Color.RGBToHSV((sensorRGBRight.red() * 255) / 800, (sensorRGBRight.green() * 255) / 800, (sensorRGBRight.blue() * 255) / 800, hsvValuesRight);

      this.hueLeft = hsvValuesLeft[0];
        this.hueRight = hsvValuesRight[0];

      // change the background color to match the color detected by the RGB sensor.
      // pass a reference to the hue, saturation, and value array as an argument
      // to the HSVToColor method.
      /*
        relativeLayout.post(new Runnable() {
        public void run() {
          relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
        }
      });
      */

      this.telemetry.update();
      this.idle();
    }
  }

  //----------------------------------------------------------------------------------------------
  // dashboard configuration
  //----------------------------------------------------------------------------------------------

  void composeDashboard()
  {
    // The default dashboard update rate is a little to slow for us, so we update faster
    telemetry.setUpdateIntervalMs(200);

    // send the info back to driver station using telemetry function.
    telemetry.addLine(
            telemetry.item("Red Left: ", new IFunc<Object>()
            {
              public Object value()
              {
                  return sensorRGBLeft.red();
              }
            }),
            telemetry.item("Green Left: ", new IFunc<Object>()
            {
              public Object value()
              {
                  return sensorRGBLeft.green();
              }
            }),
            telemetry.item("Blue Left: ", new IFunc<Object>()
            {
              public Object value()
              {
                  return sensorRGBLeft.blue();
              }
            })
    );

      telemetry.addLine(
              telemetry.item("Red Right: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return sensorRGBRight.red();
                  }
              }),
              telemetry.item("Green Right: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return sensorRGBRight.green();
                  }
              }),
              telemetry.item("Blue Right: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return sensorRGBRight.blue();
                  }
              })
      );

      telemetry.addLine(
              telemetry.item("Hue Left: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return hueLeft;
                  }
              }),
              telemetry.item("Alpha Left: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return sensorRGBLeft.alpha();
                  }
              })
      );
      telemetry.addLine(
              telemetry.item("Hue Right: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return hueRight;
                  }
              }),
              telemetry.item("Alpha Right: ", new IFunc<Object>()
              {
                  public Object value()
                  {
                      return sensorRGBRight.alpha();
                  }
              })
      );
  }
}
