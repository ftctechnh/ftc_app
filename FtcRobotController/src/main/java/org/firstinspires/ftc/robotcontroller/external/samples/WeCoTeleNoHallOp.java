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

 import com.qualcomm.ftccommon.DbgLog;
 import com.qualcomm.robotcore.eventloop.opmode.OpMode;
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.hardware.DigitalChannel;
 import com.qualcomm.robotcore.hardware.Servo;
 import com.qualcomm.robotcore.util.Range;

 public class WeCoTeleNoHallOp extends OpMode {


   @Override
   public void init() {

   }

   DcMotor motor1;
   DcMotor motor2;
   DcMotor motorLifter;
   DcMotor motorLifterTilt;
   DigitalChannel lifterHolofectSensor;
   Servo servoHook;
   Servo servoCD;

   final static double servoMinRange = 0.0;
   final static double servoMaxRange = 1.0;

   public enum POSITION {TOP, MIDDLE, BOTTOM}

   public POSITION currentPosition = POSITION.BOTTOM;

   public enum DIRECTION {DOWN, UP}

   DIRECTION stickDirection = DIRECTION.DOWN;
   double servoHookPosition = 1;
   double servoCDPosition = 0.5;
   String halleffectState = "On";

   float motor1power;
   float motor2power;
   float motorPowerMin = -1;
   float motorPowerMax = 1;
   float motorLifterPower;
   float motorLifterTiltPower;
   float tilterPower = (float) 0.5;
   int scaleNum = 0;
   float wheelDiameter = 4;
   double position1;
   double position2;
   double lifterPosition = 90;
   double lifterPositionIdeal = 90;


   @Override
   public void start() {

     motor1 = hardwareMap.dcMotor.get("motorRight");
     motor2 = hardwareMap.dcMotor.get("motorLeft");
     motorLifter = hardwareMap.dcMotor.get("motorLifter");
     motorLifterTilt = hardwareMap.dcMotor.get("motorLifterTilt");
     lifterHolofectSensor = hardwareMap.digitalChannel.get("halleffect_1");
     servoCD = hardwareMap.servo.get("servoCD");
     servoHook = hardwareMap.servo.get("servoHook");
     motor2.setDirection(DcMotor.Direction.REVERSE);
     motorLifterTilt.setDirection(DcMotor.Direction.REVERSE);
   }

   @Override
   public void loop() {

 //sets motor power
     motor1power = (-gamepad1.left_stick_y - gamepad1.right_stick_x)/2;
     motor2power = (-gamepad1.left_stick_y + gamepad1.right_stick_x)/2;
     motorLifterPower = 1/((-gamepad2.left_stick_y*-gamepad2.left_stick_y)+1) ;




 //Opens and closes hook
     if (gamepad2.right_trigger == 1) {
       DbgLog.msg("===== Set Hook to 1 =====");
       servoHookPosition = 1.0;
     } else if (gamepad2.right_bumper == true) {
       DbgLog.msg("===== Set Hook to 0 =====");
       servoHookPosition = 0.0;
     }

 //Climber dropper position
     if (gamepad2.left_trigger == 1) {
       servoCDPosition = 0.5;
     } else if (gamepad2.left_bumper == true) {
       servoCDPosition = 0.0;
     }

 //Sets Lifter Tilt power
     motorLifterTiltPower = gamepad2.right_stick_y;
     /*if (lifterPosition > lifterPositionIdeal) {
       motorLifterTiltPower = -tilterPower ;
     } else if (lifterPosition < lifterPositionIdeal) {
       motorLifterTiltPower = tilterPower ;
     } else if ( Math.abs(lifterPosition-lifterPositionIdeal) < 2.5) {
       motorLifterTiltPower = 0 ;
     }

     lifterPositionIdeal = lifterPositionIdeal - (gamepad2.right_stick_y/2) ;
     */
     //goes slower if turning
     if (gamepad1.right_stick_x  != 0) {
       motor1power = 2*(motor1power)/3 ;
        motor2power = 2*(motor2power)/3 ;
     }



 //clips motor and servo power/position
     motor1power = Range.clip(motor1power, motorPowerMin, motorPowerMax);
     motor2power = Range.clip(motor2power, motorPowerMin, motorPowerMax);
     motorLifterTiltPower = Range.clip(motorLifterTiltPower, motorPowerMin, motorPowerMax);
     motorLifterPower = Range.clip(motorLifterPower, motorPowerMin, motorPowerMax);
     servoHookPosition = Range.clip(servoHookPosition, servoMinRange, servoMaxRange);
     servoCDPosition = Range.clip(servoCDPosition, servoMinRange, servoMaxRange);

 //sets motor and servo power/position
     motor1.setPower(motor1power);
     motor2.setPower(motor2power);
     motorLifter.setPower(motorLifterPower);
     motorLifterTilt.setPower(motorLifterTiltPower);
     servoHook.setPosition(servoHookPosition);
     servoCD.setPosition(servoCDPosition);

 // gets current position and uses formula to find rotations or distance in inches
     position1 = -motor1.getCurrentPosition();
     position2 = motor2.getCurrentPosition();

     position1 = (position1 / 2500);//(wheelDiameter*3.14159265358)
     position2 = (position2 / 2500); //(wheelDiameter*3.14159265358)
 //
     if (-gamepad2.left_stick_y > 0) {
       stickDirection = DIRECTION.UP;
     } else if (-gamepad2.left_stick_y < 0) {
       stickDirection = DIRECTION.DOWN;
     }

     //lifterPosition = Yaxis


 //telemetry data
     //telemetry.addData("Left Stick", "Left Stick is at " + String.format("%.2f", gamepad1.left_stick_y)); //left stick y-axis poition
     telemetry.addData("0 Motor 1", "Motor 1 power is " + String.format("%.2f", motor1power)); //motor 1 power
     telemetry.addData("0 Motor 2", "Motor 2 power is " + String.format("%.2f", motor2power)); // motor 2 power
     telemetry.addData("1 Left Distance", "Left motor has gone " + String.format("%.2f", position1) + " rotations"); //distance in rotations
     telemetry.addData("1 Right Distance", "Right motor has gone " + String.format("%.2f", position2) + " rotations"); //distance in rotations
     telemetry.addData("2 Motor Lifter", "Motor Lifter power is " + String.format("%.2f", motorLifterPower)); //motor Lifter power
     telemetry.addData("2 Servo Hook Position", "Servo Hook is at " + String.format("%f", servoHookPosition)); //servo hook position
     telemetry.addData("3 HalleffectSensor", "Halleffect sensor is " + halleffectState); //hallefect state
     telemetry.addData("3 CurrentPosition", "Current position is " + currentPosition.name());
     telemetry.addData("3 StickDirection", "Stick is moving " + stickDirection.name());


   }

   @Override
   public void stop() {

   }
 }
