/*
 * Copyright (c) 2014 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

/**
 * Follow an IR Beacon
 * <p>
 * How to use: <br>
 * Make sure the Modern Robotics IR beacon is off. <br>
 * Set it to 1200 at 180.  <br>
 * Make sure the side of the beacon with the LED on is facing the robot. <br>
 * Turn on the IR beacon. The robot will now follow the IR beacon. <br>
 * To stop the robot, turn the IR beacon off. <br>
 */
public class OmniBotIrSeekerOp extends OpMode {

  final static double motorPower = 0.15 ;

  final static double irSignalStrength = 0.20 ;

  IrSeekerSensor irSeeker ;
  DcMotor motor1 ;
  DcMotor motor2 ;
  DcMotor motor3 ;
  DcMotor motor4 ;

  public OmniBotIrSeekerOp() {

  }

  @Override
  public void start() {

    motor1 = hardwareMap.dcMotor.get("motor_1") ;
    motor2 = hardwareMap.dcMotor.get("motor_2") ;
    motor3 = hardwareMap.dcMotor.get("motor_3") ;
    motor4 = hardwareMap.dcMotor.get("motor_4") ;

    motor1.setDirection(DcMotor.Direction.REVERSE) ;
    motor4.setDirection(DcMotor.Direction.REVERSE) ;

  }

  @Override
  public void loop() {

    if (irSeeker.signalDetected()) {

      double angle = irSeeker.getAngle() ;
      double strength = irSeeker.getStrength() ;

      if (angle < 0) {

        motor1.setPower(-motorPower);
        motor2.setPower(-motorPower);
        motor3.setPower(motorPower);
        motor4.setPower(motorPower);

        DbgLog.msg("===Signal To the left===") ;

      } else if (angle > 0) {

        motor1.setPower(motorPower);
        motor2.setPower(motorPower);
        motor3.setPower(-motorPower);
        motor4.setPower(-motorPower);

        DbgLog.msg("===Signal To the right===") ;

      } else if (strength < irSignalStrength) {

        motor1.setPower(motorPower);
        motor2.setPower(motorPower);
        motor3.setPower(-motorPower);
        motor4.setPower(-motorPower);

        DbgLog.msg("===Signal is weak===") ;

      } else {

        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor3.setPower(0.0);
        motor4.setPower(0.0);

        DbgLog.msg("===Signal right on===") ;

      }
    } else {

      motor1.setPower(0.0);
      motor2.setPower(0.0);
      motor3.setPower(0.0);
      motor4.setPower(0.0);
      DbgLog.msg("===No Signal===");
    }
    DbgLog.msg(irSeeker.toString());
  }

  @Override
  public void stop() {
    // no action needed
  }
}
