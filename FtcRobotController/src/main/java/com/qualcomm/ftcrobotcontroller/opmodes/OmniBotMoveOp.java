/* Copyright (c) 2014 Qualcomm Technologies Inc

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

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.lang.Override;

public class OmniBotMoveOp extends OpMode {

  DcMotor motor1 ;
  DcMotor motor2 ;
  DcMotor motor3 ;
  DcMotor motor4 ;

  int scaleDegree = 1 ;

  public OmniBotMoveOp () {

  }

  @Override
  public void start () {

    motor1 = hardwareMap.dcMotor.get("motor_1") ;
    motor2 = hardwareMap.dcMotor.get("motor_2") ;
    motor3 = hardwareMap.dcMotor.get("motor_4") ;
    motor4 = hardwareMap.dcMotor.get("motor_3") ;

    motor1.setDirection(DcMotor.Direction.REVERSE) ;
    motor4.setDirection(DcMotor.Direction.REVERSE) ;
  }

  @Override
  public void loop () {

    float motor1Power = -gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
    float motor2Power = -gamepad1.left_stick_y + -gamepad1.left_stick_x + -gamepad1.right_stick_x;
    float motor3Power = -gamepad1.left_stick_y + gamepad1.left_stick_x + -gamepad1.right_stick_x;
    float motor4Power = -gamepad1.left_stick_y + -gamepad1.left_stick_x + gamepad1.right_stick_x;

    motor1Power = Range.clip(motor1Power, -1, 1);
    motor2Power = Range.clip(motor2Power, -1, 1);
    motor3Power = Range.clip(motor3Power, -1, 1);
    motor4Power = Range.clip(motor4Power, -1, 1);

    if (gamepad1.left_bumper == true) {
      int scaleDegree = 0;
    }

    if (gamepad1.right_bumper == true) {
      int scaleDegree = 1;
    }

    if (scaleDegree == 0) {
      motor1Power = (float) scaleInput(motor1Power);
      motor2Power = (float) scaleInput(motor2Power);
      motor3Power = (float) scaleInput(motor3Power);
      motor4Power = (float) scaleInput(motor4Power);
    }

    motor1.setPower(motor1Power);
    motor2.setPower(motor2Power);
    motor3.setPower(motor3Power);
    motor4.setPower(motor4Power);

    telemetry.addData("Left Joystick", "Left Joystick at" + String.format("(%.2f,%.2f)", gamepad1.left_stick_x, gamepad1.left_stick_y));
    telemetry.addData("Motor Power 1", "Motor 1 power is" + String.format("%.2f", motor1Power));
    telemetry.addData("Motor Power 2", "Motor 2 power is" + String.format("%.2f", motor2Power));
    telemetry.addData("Motor Power 3", "Motor 3 power is" + String.format("%.2f", motor3Power));
    telemetry.addData("Motor Power 4", "Motor 4 power is" + String.format("%.2f", motor4Power));

  /*
   * Code to run when the op mode is first disabled goes here
   * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
   */
  }
  @Override
  public void stop() {

  }




/*
* This method scales the joystick input so for low joystick values, the
* scaled value is less than linear.  This is to make it easier to drive
* the robot more precisely at slower speeds.
*/
  double scaleInput(double dVal)  {
    double[] scaleArray = { 0.0, 0.025, 0.05, 0.075, 0.1, 0.125, 0.15, 0.175, 0.2, 0.225, 0.25, 0.275,
            0.3, 0.325, 0.35, 0.375, 0.4, 0.425, 0.45, 0.475, 0.5 };

    // get the corresponding index for the scaleInput array.
    int index = (int) (dVal * 20.0);
    if (index < 0) {
      index = -index;
    } else if (index > 20) {
      index = 20;
    }

    double dScale = 0.0;
    if (dVal < 0) {
      dScale = -scaleArray[index];
    } else {
      dScale = scaleArray[index];
    }

    return dScale;
  }
}
