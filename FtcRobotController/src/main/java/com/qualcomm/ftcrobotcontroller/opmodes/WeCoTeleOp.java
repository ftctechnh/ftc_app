/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

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
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class WeCoTeleOp extends OpMode {


  @Override
  public void init() {

  }

  DcMotor motor1 ;
  DcMotor motor2 ;
  DcMotor motor3 ;
  DcMotor motor4 ;


  float motorPowerMin = -1 ;
  float motorPowerMax = 1  ;
  int scaleNum = 0 ;
  float wheelDiameter = 4 ;

  @Override
  public void start() {

    motor1 = hardwareMap.dcMotor.get("motor_1") ;
    motor2 = hardwareMap.dcMotor.get("motor_2") ;
    motor3 = hardwareMap.dcMotor.get("motor_3") ;
    motor4 = hardwareMap.dcMotor.get("motor_4") ;

    motor1.setDirection(DcMotor.Direction.REVERSE) ;
    motor2.setDirection(DcMotor.Direction.REVERSE) ;

  }

  @Override
  public void loop() {

    float motor1power = -gamepad1.left_stick_y + gamepad1.right_stick_x ;
    float motor2power = -gamepad1.left_stick_y + gamepad1.right_stick_x ;
    float motor3power = -gamepad1.left_stick_y - gamepad1.right_stick_x ;
    float motor4power = -gamepad1.left_stick_y - gamepad1.right_stick_x ;


    motor1power = Range.clip(motor1power, motorPowerMin, motorPowerMax) ;
    motor2power = Range.clip(motor2power, motorPowerMin, motorPowerMax) ;
    motor3power = Range.clip(motor3power, motorPowerMin, motorPowerMax) ;
    motor4power = Range.clip(motor4power, motorPowerMin, motorPowerMax) ;


    if (gamepad1.left_bumper) {

      scaleNum += 1 ;
    }

    if (scaleNum % 2 == 1) {
      motor1power = scale1(motor1power) ;
      motor2power = scale1(motor2power) ;
      motor3power = scale1(motor3power) ;
      motor4power = scale1(motor4power) ;

    }
    motor1.setPower(motor1power) ;
    motor2.setPower(motor2power) ;
    motor3.setPower(motor3power) ;
    motor4.setPower(motor4power) ;

    double position2 = -motor2.getCurrentPosition() ;
    double position4 = motor4.getCurrentPosition() ;

    position2 = (position2/2500) * (wheelDiameter*3.14159265358);
    position4 = (position4/2500) * (wheelDiameter*3.14159265358);


    telemetry.addData("Left Stick", "Left Stick is at " + String.format("%.2f", gamepad1.left_stick_y));
    telemetry.addData("Motor 1", "Motor 1 power is " + String.format("%.2f", motor1power));
    telemetry.addData("Motor 2", "Motor 2 power is " + String.format("%.2f", motor2power));
    telemetry.addData("Motor 3", "Motor 3 power is " + String.format("%.2f", motor3power));
    telemetry.addData("Motor 4", "Motor 4 power is " + String.format("%.2f", motor4power));
    telemetry.addData("Left Distance", "Left wheel has gone " + String.format("%.2f", position2) + " inches");
    telemetry.addData("Right Distance", "Right wheel has gone " + String.format("%.2f", position4) + " inches");

  }
  @Override
  public void stop() {

  }

  float scale1(float scaleInput1) {

    scaleInput1 /= 3 ;

    return  scaleInput1 ;
  }
}
