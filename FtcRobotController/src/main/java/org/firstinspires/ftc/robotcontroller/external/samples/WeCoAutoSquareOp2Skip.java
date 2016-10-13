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
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.hardware.LightSensor;
 import com.qualcomm.robotcore.hardware.TouchSensor;
 import com.qualcomm.robotcore.util.Range;


 public class WeCoAutoSquareOp2Skip extends OpMode  {
   // Initialize HW Data Objects
     TouchSensor touchSensor1;  // Hardware Device Object
     TouchSensor touchSensor2;  // Hardware Device Object
   LightSensor lightSensor1;  // Hardware Device Object
   DcMotor motorLeft1;
   DcMotor motorLeft2;
   DcMotor motorRight1 ;
   DcMotor motorRight2 ;

   //Drive Control Values
   static final float normalTurnSpeed = (float) 0.75;
   static final float normalSpeed = (float) 0.75;
   static final float normalLine = 1;
   static final double normalturn = 0.5;
   float resetValueLeft, resetValueRight = 0;
   float motorPowerMin = -1;
   float motorPowerMax = 1;
   double positionLeft, positionRight;
   float motorLeft1Power = 0;
   float motorLeft2Power = 0;
   float motorRight1Power = 0;
   float motorRight2Power = 0;

   public WeCoAutoSquareOp2Skip() {
   }

   @Override
   public void init() {
     whattodo = 1;
     count = 0;
   }
  //Event Control Value
   double count;
   double etime;
   int whattodo = 1; //0 do nothing; 1 moveforward; 2 turn

   @Override
   public void start() {
     // get a reference to our Hardware objects
       touchSensor1 = hardwareMap.touchSensor.get("touchSensor1");
       touchSensor2 = hardwareMap.touchSensor.get("touchSensor2");
       lightSensor1 = hardwareMap.lightSensor.get("lightSensor1");
       motorLeft1 = hardwareMap.dcMotor.get("motorLeft1");
       motorLeft2 = hardwareMap.dcMotor.get("motorLeft2");
       motorRight1 = hardwareMap.dcMotor.get("motorRight1");
       motorRight2 = hardwareMap.dcMotor.get("motorRight2");

     //Setup Hardware
     motorLeft1.setDirection(DcMotor.Direction.REVERSE);
     motorLeft2.setDirection(DcMotor.Direction.REVERSE);
     lightSensor1.enableLed(true);
   }

   @Override
   public void loop() {
       if(touchSensor1.isPressed())
           whattodo = 6;
       if(touchSensor2.isPressed())
            whattodo++;
     switch (whattodo) {
       case 1:
         resetValueLeft = -motorLeft1.getCurrentPosition();
         resetValueRight = motorRight1.getCurrentPosition();
         moveForward();
         whattodo = 2;
         break;
       case 2:
           // gets current position and uses formula to find rotations or distance in inches
           positionLeft = -motorLeft1.getCurrentPosition() - resetValueLeft;
           positionRight = motorRight1.getCurrentPosition() - resetValueRight;
           positionLeft = (positionLeft / 2500);//(wheelDiameter*3.14159265358)
           positionRight = (positionRight / 2500); //(wheelDiameter*3.14159265358)

         if((Math.abs(positionLeft) > normalLine) && (positionRight > normalLine))
           whattodo =3;
         break;
       case 3:
         stopMove();
         resetValueLeft = -motorLeft1.getCurrentPosition();
         resetValueRight = motorRight1.getCurrentPosition();
         startLeftTurn();
         whattodo = 4;
         break;
       case 4:
         // gets current position and uses formula to find rotations or distance in inches
         positionLeft = -motorLeft1.getCurrentPosition()- resetValueLeft;
         positionRight = motorRight1.getCurrentPosition() - resetValueRight;

         positionLeft = (positionLeft / 2500);//(wheelDiameter*3.14159265358)
         positionRight = (positionRight / 2500); //(wheelDiameter*3.14159265358)
         if((Math.abs(positionLeft) > normalturn) && (positionRight > normalturn))
           whattodo = 5;
         break;
       case 5:
         stopMove();
         resetValueLeft = -motorLeft1.getCurrentPosition();
         resetValueRight = motorRight1.getCurrentPosition();
         count ++;
         if (count == 4) {
           whattodo = 6;
         }
         else {
           whattodo = 1;
         }
         break;
       case 6:
         stopMove();
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

     // servoHookPosition = Range.clip(servoHookPosition, servoMinRange, servoMaxRange);
     //servoCDPosition = Range.clip(servoCDPosition, servoMinRange, servoMaxRange);


     //sets motor and servo power/position
     motorLeft1.setPower(motorLeft1Power);
     motorLeft2.setPower(motorLeft2Power);
     motorRight1.setPower(motorRight1Power);
     motorRight2.setPower(motorRight2Power);
     // motorLifter.setPower(motorLifterPower);
     //servoHook.setPosition(servoHookPosition);
       //servoCD.setPosition(servoCDPosition);
       // servoTail.setPosition(servoTailPosition);


       // gets current position and uses formula to find rotations or distance in inches
       positionLeft = -motorLeft1.getCurrentPosition();
       positionRight = motorRight1.getCurrentPosition();

       positionLeft = (positionLeft / 2500);  //(wheelDiameter*3.14159265358)
       positionRight = (positionRight / 2500); //(wheelDiameter*3.141592653)



       motorLeft1.setPower(motorLeft1Power);

       //Telemetry Data
       telemetry.addData("Light1Raw", lightSensor1.getLightDetectedRaw());
       telemetry.addData("Light1Normal", lightSensor1.getLightDetected());
       telemetry.addData("Motor Left1 Power", "Motor Left1 power is " + String.format("%.2f", motorLeft1Power));
       telemetry.addData("Motor Right1 Power", "Motor Right1 power is " + String.format("%.2f", motorRight1Power));
       telemetry.addData("Motor Left2 Power", "Motor Left2 power is " + String.format("%.2f", motorLeft2Power));
       telemetry.addData("Motor Right2 Power", "Motor Right2 power is " + String.format("%.2f", motorRight2Power));
       telemetry.addData("positionLeft", "positionLeft is " + String.format("%.2f", positionLeft));
       telemetry.addData("Count", "Count is " + String.format("%.2f", count));
       telemetry.addData("PositionRight", "PositionRight is " + String.format("%.2f", positionRight));
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
   public void stopMove(){
     motorLeft1Power = 0;
     motorLeft2Power = 0;
     motorRight1Power = 0;
     motorRight2Power = 0;
   }

   public float motorPosition(DcMotor motor, float resetValue) {
     return(motor.getCurrentPosition() - resetValue) ;
   }

   @Override
   public void stop() {
   }

 }