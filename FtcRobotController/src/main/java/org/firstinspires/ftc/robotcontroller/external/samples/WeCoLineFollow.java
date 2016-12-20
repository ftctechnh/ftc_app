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

package org.firstinspires.ftc.robotcontroller.external.samples;

 import android.content.Context;
 import android.hardware.Sensor;
 import android.hardware.SensorEvent;
 import android.hardware.SensorEventListener;
 import android.hardware.SensorManager;

 import com.qualcomm.ftccommon.DbgLog;
 import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
 import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 import com.qualcomm.robotcore.eventloop.opmode.OpMode;
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.hardware.DigitalChannel;
 import com.qualcomm.robotcore.hardware.LightSensor;
 import com.qualcomm.robotcore.hardware.Servo;
 import com.qualcomm.robotcore.hardware.TouchSensor;
 import com.qualcomm.robotcore.util.ElapsedTime;
 import com.qualcomm.robotcore.util.Range;

 @Autonomous(name="WeCo: Line Follow", group="WeCo")
 //@Disabled
 public class WeCoLineFollow extends OpMode  {

   // orientation values
   DcMotor motorLeft1;
   DcMotor motorLeft2;
   DcMotor motorRight1 ;
   DcMotor motorRight2 ;
   float motorLeft1Power = 0;
   float motorLeft2Power = 0;
   float motorRight1Power = 0;
   float motorRight2Power = 0;
   float motorPowerMin = -1;
   float motorPowerMax = 1;

   static final float normalTurnSpeed = (float) 0.75;
   static final float normalSpeed = (float) 0.05;

   LightSensor centerLight;
   TouchSensor startstopTouch;

   int whattodo = 1; //0 do nothing; 1 moveforward; 2 turn

   public WeCoLineFollow() {

   }

   @Override
   public void init() {
   }

   @Override
   public void start() {

     motorLeft1 = hardwareMap.dcMotor.get("motorLeft1");
     motorLeft2 = hardwareMap.dcMotor.get("motorLeft2");
     motorRight1 = hardwareMap.dcMotor.get("motorRight1");
     motorRight2 = hardwareMap.dcMotor.get("motorRight2");

     motorLeft1.setDirection(DcMotor.Direction.REVERSE);
     motorLeft2.setDirection(DcMotor.Direction.REVERSE);

     centerLight = hardwareMap.lightSensor.get("lightSensor1");
     startstopTouch = hardwareMap.touchSensor.get("touchSensor1");

     centerLight.enableLed(true);
     whattodo = 1;

   }

   @Override
   public void loop() {

     switch (whattodo) {
       case 1:
         if(startstopTouch.isPressed())
           whattodo = 2;
         break;
       case 2:
         // gets current position and uses formula to find rotations or distance in inches
         if(startstopTouch.isPressed())
           whattodo = 1;
         followLine(centerLight,120, -normalSpeed);
         break;
       default:
         whattodo = 0;
         break;

     }
     //clips motor and servo power/position

     motorLeft1Power = Range.clip(motorLeft1Power, motorPowerMin, motorPowerMax);
     motorLeft2Power = Range.clip(motorLeft2Power, motorPowerMin, motorPowerMax);
     motorRight1Power = Range.clip(motorRight1Power, motorPowerMin, motorPowerMax);
     motorRight2Power = Range.clip(motorRight2Power, motorPowerMin, motorPowerMax);

     //sets motor and servo power/position
     motorLeft1.setPower(motorLeft1Power);
     motorLeft2.setPower(motorLeft2Power);
     motorRight1.setPower(motorRight1Power);
     motorRight2.setPower(motorRight2Power);

     telemetry.addData("CenterLight", centerLight.getLightDetected());
     telemetry.addData("CenterLightRaw", centerLight.getRawLightDetected());
     telemetry.addData("Motor Left1 Power", "Motor Left1 power is " + String.format("%.2f", motorLeft1Power));
     telemetry.addData("Motor Right1 Power", "Motor Right1 power is " + String.format("%.2f", motorRight1Power));
     telemetry.addData("Motor Left2 Power", "Motor Left2 power is " + String.format("%.2f", motorLeft2Power));
     telemetry.addData("Motor Right2 Power", "Motor Right2 power is " + String.format("%.2f", motorRight2Power));
     telemetry.addData("WhatToDo", "WhatToDo is " + String.format("%d", whattodo));
   }

   public void startLeftTurn(){
     motorLeft1Power = -normalTurnSpeed;
     motorLeft2Power = -normalTurnSpeed;
     motorRight1Power = normalTurnSpeed;
     motorRight2Power = normalTurnSpeed;


   }
   public void moveForward(){
     motorLeft1Power = normalSpeed;
     motorLeft2Power = normalSpeed;
     motorRight1Power = normalSpeed;
     motorRight2Power = normalSpeed;

   }
   public void moveForward(float leftpower, float rightpower){
     motorLeft1Power = leftpower;
     motorLeft2Power = leftpower;
     motorRight1Power = rightpower;
     motorRight2Power = rightpower;

   }
   public void stopMove(){
     motorLeft1Power = 0;
     motorLeft2Power = 0;
     motorRight1Power = 0;
     motorRight2Power = 0;

   }

   public void followLine(LightSensor inLightSensor, double targetLight, float speed) {
     double currentlightValue = inLightSensor.getRawLightDetected();
     double error = targetLight - currentlightValue;
     double constantProporionality = 0.01;

     double leftPower = speed - constantProporionality * error;
     double rightPower = speed + constantProporionality *error;
     DbgLog.msg("Line Follow" + leftPower + "Error: "+ error);

     moveForward((float) leftPower, (float) rightPower);
   }
   public float motorPosition(DcMotor motor, float resetValue) {
     return(motor.getCurrentPosition() - resetValue) ;

   }





   @Override
   public void stop() {
   }


 }
