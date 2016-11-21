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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This is an example LinearOpMode that shows how to use
 * the Modern Robotics Gyro.
 *
 * The op mode assumes that the gyro sensor
 * is attached to a Device Interface Module I2C channel
 * and is configured with a name of "gyro".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
*/
@Autonomous(name = "Sensor: MR Gyro Function TT", group = "Sensor")
//@Disabled
public class SensorMRGyroFunction_TT extends LinearOpMode {

  HardwarePushbot_TT robot = new HardwarePushbot_TT();   // Use a Pushbot's hardware
  private ElapsedTime runtime = new ElapsedTime();


  @Override
  public void runOpMode() {

    robot.init(hardwareMap);

    // start calibrating the gyro.
    calibrateGyro();

    // wait for the start button to be pressed.
    waitForStart();

    makeGyroTurn(30);

  }

  public void calibrateGyro() {

    // Ensure that the opmode is still active
      // start calibrating the gyro.
      telemetry.addData(">", "Gyro Calibrating. Do Not move!");
      telemetry.update();
      robot.gyro.calibrate();

      // make sure the gyro is calibrated.
      while (!isStopRequested() && robot.gyro.isCalibrating()) {
        sleep(50);
        idle();
      }

      telemetry.addData(">", "Gyro Calibrated.  Press Start.");
      telemetry.update();

      // wait for the start button to be pressed.
//      waitForStart();

  }

  public void makeGyroTurn(int turnAngle) {
      boolean lastResetState = false;
      boolean curResetState = false;

      int xVal, yVal, zVal = 0;     // Gyro rate Values
      int heading = 0;              // Gyro integrated heading
      int angleZ = 0;

      while (opModeIsActive()) {
          robot.gyro.resetZAxisIntegrator();

          lastResetState = curResetState;

          // get the x, y, and z values (rate of change of angle).
          xVal = robot.gyro.rawX();
          yVal = robot.gyro.rawY();
          zVal = robot.gyro.rawZ();

          // get the heading info.
          // the Modern Robotics' gyro sensor keeps
          // track of the current heading for the Z axis only.
          heading = robot.gyro.getHeading();
          angleZ = robot.gyro.getIntegratedZValue();

          telemetry.addData("0", "Heading %03d", heading);
          telemetry.addData("1", "Int. Ang. %03d", angleZ);
          telemetry.addData("2", "X av. %03d", xVal);
          telemetry.addData("3", "Y av. %03d", yVal);
          telemetry.addData("4", "Z av. %03d", zVal);
          telemetry.update();
      }
      //sleep (10000);

  }
}