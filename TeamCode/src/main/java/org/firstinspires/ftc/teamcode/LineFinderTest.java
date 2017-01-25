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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

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
 * You can use the X button on gamepad1 to toggle the LED on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@TeleOp(name = "Test Line Finder", group = "Sensor")
@Disabled                           // Comment this out to add to the opmode list
public class LineFinderTest extends LinearOpMode {

  HardwareDM robot = new HardwareDM();

  @Override
  public void runOpMode() throws InterruptedException {

    robot.init(hardwareMap);
      // adaHSV is an array that will hold the hue, saturation, and value information.
      float[] adaHSV = {0F, 0F, 0F};

    // adaValues is a reference to the adaHSV array.
    final float adaValues[] = adaHSV;

    float WHITE_THRESHOLD = 2.0F;


    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

    // wait for the start button to be pressed.
    waitForStart();

    // Set Stripe finder LED on
    robot.stripeColor.enableLed(true);

    robot.setDriveZeroPower(DcMotor.ZeroPowerBehavior.BRAKE);
    // loop and read the RGB data.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive() && robot.stripeColor.alpha() < WHITE_THRESHOLD)  {


      // Drive til we see the stripe
      robot.lfDrive.setPower(0.8);
      robot.lrDrive.setPower(0.8);
      robot.rfDrive.setPower(0.8);
      robot.rrDrive.setPower(0.8);

      // convert the RGB adaValues to HSV adaValues.
      Color.RGBToHSV((robot.beaconColor.red() * 255) / 800, (robot.beaconColor.green() * 255) / 800,
              (robot.beaconColor.blue() * 255) / 800, adaHSV);

      // send the info back to driver station using telemetry function.
      telemetry.addData("Beacon Alpha", robot.beaconColor.alpha());
      telemetry.addData("Beacon Hue", adaHSV[0]);
        telemetry.addData("Stripe Alpha", robot.stripeColor.alpha());

      // change the background color to match the color detected by the RGB sensor.
      // pass a reference to the hue, saturation, and value array as an argument
      // to the HSVToColor method.
      relativeLayout.post(new Runnable() {
        public void run() {
          relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, adaValues));
        }
      });

      telemetry.update();
      idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
    }
    robot.setDriveZeroPower(DcMotor.ZeroPowerBehavior.FLOAT);
    robot.lfDrive.setPower(0.0);
    robot.lrDrive.setPower(0.0);
    robot.rfDrive.setPower(0.0);
    robot.rrDrive.setPower(0.0);

    while(opModeIsActive()) {
      // Keep displaying colors
      // convert the RGB adaValues to HSV adaValues.
      Color.RGBToHSV((robot.beaconColor.red() * 255) / 800, (robot.beaconColor.green() * 255) / 800,
              (robot.beaconColor.blue() * 255) / 800, adaHSV);

      // send the info back to driver station using telemetry function.
      telemetry.addData("Beacon Alpha", robot.beaconColor.alpha());
      telemetry.addData("Beacon Hue", adaHSV[0]);
      telemetry.addData("Stripe Alpha", robot.stripeColor.alpha());

      // change the background color to match the color detected by the RGB sensor.
      // pass a reference to the hue, saturation, and value array as an argument
      // to the HSVToColor method.
      relativeLayout.post(new Runnable() {
        public void run() {
          relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, adaValues));
        }
      });

      telemetry.update();
      idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop

    }
  }
}
