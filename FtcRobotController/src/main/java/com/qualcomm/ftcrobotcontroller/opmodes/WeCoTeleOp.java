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
  Servo servo1 ;

  final static double servoMinRange = 0.0 ;
  final static double servoMaxRange = 1.0 ;
  double servoDelta = 0.25 ;
  double servoPosition ;
  boolean BbuttonOn= false;
  ElapsedTime BbuttonTimmer = new ElapsedTime();
  boolean AbuttonOn= false;
  ElapsedTime AbuttonTimmer = new ElapsedTime();
  double buttonResetTime = 0.25 ;
  float motorPowerMin = -1 ;
  float motorPowerMax = 1  ;
  int scaleNum = 0 ;
  float wheelDiameter = 4 ;


  @Override
  public void start() {

    motor1 = hardwareMap.dcMotor.get("motor_1") ;
    motor2 = hardwareMap.dcMotor.get("motor_2") ;
    servo1 = hardwareMap.servo.get("servo_1") ;

    motor1.setDirection(DcMotor.Direction.REVERSE) ;

    servoPosition = 0.5 ;
  }

  @Override
  public void loop() {
//increase servo position by a set amount
    if (gamepad2.a || gamepad2.b) {

      if (gamepad2.a && !AbuttonOn) {

        servoPosition = servoPosition + servoDelta;
        DbgLog.msg("=====Decrease arm position=====");
        AbuttonOn = true;
        AbuttonTimmer.reset();

      }
      if (AbuttonOn == true && (AbuttonTimmer.time() > buttonResetTime)) {
        AbuttonOn = false;
        DbgLog.msg("=====Reset AbuttonOn=====");
      }


      //DbgLog.msg("=====servoPosition====="+String.format("%f", servoPositionClipped)) ;

      if (gamepad2.b && !BbuttonOn) {

        servoPosition = servoPosition - servoDelta;
        DbgLog.msg("=====Decrease arm position=====" + String.format("%f", servoPosition));
        BbuttonOn = true;
        BbuttonTimmer.reset();

      }
      if (BbuttonOn == true && (BbuttonTimmer.time() > buttonResetTime)) {
        BbuttonOn = false;
        DbgLog.msg("=====Reset BbuttonOn=====");
      }


      if ((gamepad2.a == false) && (gamepad2.b == false)) {
        DbgLog.msg("=====Not Pressed=====");
      }
    } else if ((gamepad2.left_trigger > 0) || (gamepad2.right_trigger > 0)) {
      //increase servo position proportionally
      servoPosition = servoPosition + (gamepad2.left_trigger / 100) - (gamepad2.right_trigger / 100);
    }

//sets motor power
    float motor1power = -gamepad1.left_stick_y + gamepad1.right_stick_x ;
    float motor2power = -gamepad1.left_stick_y - gamepad1.right_stick_x ;

    if (gamepad1.left_bumper) {

      scaleNum += 1 ;
    }

    if (scaleNum % 2 == 1) {
      motor1power = scale1(motor1power) ;
      motor2power = scale1(motor2power) ;

    }

    if (gamepad1.right_trigger > 0) {
      motor1power = scale2(motor1power, gamepad1.left_trigger) ;
      motor2power = scale2(motor2power, gamepad1.left_trigger) ;
    }

//clips motor and servo power/position
    double motor1powerClipped = Range.clip(motor1power, motorPowerMin, motorPowerMax) ;
    double motor2powerClipped = Range.clip(motor2power, motorPowerMin, motorPowerMax) ;
    servoPosition = Range.clip(servoPosition, servoMinRange, servoMaxRange) ;


//Defines scale

    //sets motor and servo power/position
    motor1.setPower(motor1powerClipped) ;
    motor2.setPower(motor2powerClipped) ;
    servo1.setPosition(servoPosition);

// gets current position and uses formula to find rotations or distance in inches
    double position1 = -motor1.getCurrentPosition() ;
    double position2 = motor2.getCurrentPosition() ;

    position1 = (position1/2500)  /* * (wheelDiameter*3.14159265358)*/;
    position2 = (position2/2500) /* * (wheelDiameter*3.14159265358)*/;

//telemetry data
    //telemetry.addData("Left Stick", "Left Stick is at " + String.format("%.2f", gamepad1.left_stick_y)); //left stick y-axis poition
    telemetry.addData("0 Motor 1", "Motor 1 power is " + String.format("%.2f", motor1powerClipped)); //motor 1 power
    telemetry.addData("0 Motor 2", "Motor 2 power is " + String.format("%.2f", motor2powerClipped)); // motor 2 power
    telemetry.addData("1 Left Distance", "Left motor has gone " + String.format("%.2f", position1) + " rotations"); //distance in rotations
    telemetry.addData("1 Right Distance", "Right motor has gone " + String.format("%.2f", position2) + " rotations"); //distance in rotations
    telemetry.addData("2 Left Trigger", "Left Trigger is at " + String.format("%.2f", gamepad2.left_trigger)); // right trigger position
    telemetry.addData("2 Right Trigger", "Right Trigger is at " + String.format("%.2f", gamepad2.right_trigger)); // right trigger position
    telemetry.addData("Servo Position", "Servo is at " + String.format("%f", servoPosition)) ; //servo position


  }
  @Override
  public void stop() {

  }
//the alternate scale
  float scale1(float scaleInput1) {

    scaleInput1 /= 3 ;

    return  scaleInput1 ;
  }
  //test alternate
  float scale2(float scale2Input, float scale2Input2) {

    scale2Input /= scale2Input2 ;

    return scale2Input ;
  }
}
