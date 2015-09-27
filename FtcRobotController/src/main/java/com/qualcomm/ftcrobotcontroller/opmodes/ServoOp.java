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
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class ServoOp extends OpMode {

  Servo servo1 ;

  final static double servoMinRange = .1 ;
  final static double servoMaxRange = 0.9 ;
  double servoDelta = 0.1 ;
  double servoPosition ;
  double servoPositionClipped ;
  String gamepad = "Gamepad is not pressed";
  boolean BbuttonOn= false;
  ElapsedTime BbuttonTimmer = new ElapsedTime();

  @Override
  public void init() {
      servo1 = hardwareMap.servo.get("servo_1") ;

      servoPosition = 0.5 ;

  }

  @Override
  public void loop() {
/*
    if (gamepad1.a) {

      servoPosition = servoPosition + servoDelta ;
      DbgLog.msg("=====Decrease arm position=====") ;
      gamepad = "A button is pressed" ;

    }

*/
    //DbgLog.msg("=====servoPosition====="+String.format("%f", servoPositionClipped)) ;

    if (gamepad1.b && !BbuttonOn) {

        servoPosition = servoPosition - servoDelta ;
        DbgLog.msg("=====Decrease arm position====="+String.format("%f", servoPosition)) ;
        gamepad = "b button is pressed" ;
        BbuttonOn = true;
        BbuttonTimmer.reset();

    }
    if(BbuttonOn == true && (BbuttonTimmer.time() > 0.5) ) {
        BbuttonOn = false;
        DbgLog.msg("=====Reset BbuttonOn=====") ;
        gamepad = "Gamepad is not pressed" ;
    }


    if (gamepad1.a && gamepad1.b == false) {
        gamepad = "Gamepad is not pressed" ;
        DbgLog.msg("=====Not Pressed=====") ;
    }

    servoPositionClipped = Range.clip(servoPosition, servoMinRange, servoMaxRange) ;

    servo1.setPosition(servoPositionClipped);


    telemetry.addData("Servo Pressed", gamepad);
    telemetry.addData("Servo Position", "Servo is at " + String.format("%f", servoPositionClipped)) ;

  }
  @Override
  public void stop() {

  }
}

/*
    if (gamepad1.a || gamepad1.b) {
      gamepad = "Gamepad a or b is pressed" ;
    } else {
      gamepad = "Gamepad is not pressed" ;
    }
 */
