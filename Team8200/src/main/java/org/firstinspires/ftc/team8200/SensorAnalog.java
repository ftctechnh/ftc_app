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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
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
@TeleOp(name = "Sensor: Ultrasonic Distance Sensor", group = "Sensor")
//@Disabled
public class SensorAnalog extends LinearOpMode {

  DeviceInterfaceModule dim;                  // Device Object
  AnalogInput distanceSensor;
  double voltage, maxVoltage;
  private ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() {

    dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");   //  Use generic form of device mapping
    distanceSensor = hardwareMap.get(AnalogInput.class, "distance");

    /* THIS should not be in this analog distance sensor test file
      Please create a NEW op mode without any of these

    HardwareK9bot robot = new HardwareK9bot();
    LightExceed lineFinder = new LightExceed();
    ColorSensorRed colorSensor = new ColorSensorRed();
    */

    // wait for the start button to be pressed.
    waitForStart();

    // while the op mode is active, loop
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive()) {

      voltage = distanceSensor.getVoltage();
      maxVoltage = distanceSensor.getMaxVoltage();

      // send the info back to driver station using telemetry function.
      telemetry.addData("Voltage", voltage);
      telemetry.addData("Max Voltage", maxVoltage);

      telemetry.update();

      // go from angle, go straight after X seconds, detect line and activate colorsensor file, done

      /* THIS should not be in this file
        Create a new class for your autonomous opmode

      runtime.reset();
      if(runtime.seconds() < 10) {
        robot.leftMotor.setPower(1);
        robot.rightMotor.setPower(1);
      } else {
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        lineFinder.MoveToBeacon();
      }
      */
    }
  }
}
