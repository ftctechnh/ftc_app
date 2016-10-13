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

package  org.firstinspires.ftc.robotcontroller.external.samples;

 import android.content.Context;
 import android.hardware.Sensor;
 import android.hardware.SensorEvent;
 import android.hardware.SensorEventListener;
 import android.hardware.SensorManager;

 import com.qualcomm.ftccommon.DbgLog;
 import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 import com.qualcomm.robotcore.eventloop.opmode.OpMode;
 import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.hardware.DigitalChannel;
 import com.qualcomm.robotcore.hardware.Servo;
 import com.qualcomm.robotcore.util.Range;

 @TeleOp(name="WeCo: TeleOpGyro", group="WeCo")
 //@Disabled
 public class WeCoTeleOpGyro extends OpMode implements SensorEventListener {

   // orientation values

   private float[] mGravity;       // latest sensor values
   private float[] mGeomagnetic;   // latest sensor values

   private float azimuth = 0.0f;      // value in radians
   private float pitch = 0.0f;        // value in radians
   private float roll = 0.0f;

   private String startDate;
   private SensorManager mSensorManager;
   Sensor accelerometer;
   Sensor magnetometer;

   public WeCoTeleOpGyro() {

   }

   @Override
   public void init() {

   }

   DcMotor motorLeft1;
   DcMotor motorLeft2;
   DcMotor motorRight1 ;
   DcMotor motorRight2 ;
   //DcMotor motorLifter;
   //DcMotor motorLifterTilt;
   //DigitalChannel lifterHolofectSensor;
   //Servo servoHook;
   //Servo servoCD;
   //Servo servoTail ;

   final static double servoMinRange = 0.0;
   final static double servoMaxRange = 1.0;

   public enum POSITION {TOP, MIDDLE, BOTTOM}

   public POSITION currentPosition = POSITION.BOTTOM;

   public enum DIRECTION {DOWN, UP}

   DIRECTION stickDirection = DIRECTION.DOWN;
   double servoHookPosition = 1;
   boolean hookRB = false ;
   boolean hookRT = false ;
   double servoCDPosition = 0.5;
   String halleffectState = "On";

   float motorLeft1power;
   float motorLeft2power;
   float motorRight1power;
   float motorRight2power;
   float motorPowerMin = -1;
   float motorPowerMax = 1;
   float motorScalar = 1 ;
   String dpadPosition = "Off" ;
   //float motorLifterPower;
   //String motorLifterInverse = "Off" ;
   //String motorLifterNull = "Off" ;
   //float motorLifterTiltPower;
   //float tilterPower = (float) 0.5;
   //float lifterScalarFactor =  (float) 10 ;
   //String lifterFloat = "Off" ;
   //boolean scaleLB = false ;
   //boolean scaleRB = false ;
   //float servoTailPosition = (float) 1.0;
   //String servoTPString = "Up" ;
   int scaleNum = 0;
   float wheelDiameter = 4;
   double positionLeft;
   double positionRight
           ;
   //double lifterPosition = 90;
   //double lifterPositionIdeal = 90;



   @Override
   public void start() {

     motorLeft1 = hardwareMap.dcMotor.get("motorLeft1");
     motorLeft2 = hardwareMap.dcMotor.get("motorLeft2");
     motorRight1 = hardwareMap.dcMotor.get("motorRight1");
     motorRight2 = hardwareMap.dcMotor.get("motorRight2");
     //motorLifter = hardwareMap.dcMotor.get("motorLifter");
     //motorLifterTilt = hardwareMap.dcMotor.get("motorLifterTilt");
     //lifterHolofectSensor = hardwareMap.digitalChannel.get("halleffect_1");
     //servoCD = hardwareMap.servo.get("servoCD");
     //servoHook = hardwareMap.servo.get("servoHook");
     //servoTail = hardwareMap.servo.get("servoTail") ;
     mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
     accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
     magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


     motorLeft1.setDirection(DcMotor.Direction.REVERSE);
     motorLeft2.setDirection(DcMotor.Direction.REVERSE);
     //motorLifterTilt.setDirection(DcMotor.Direction.REVERSE);
   }

   @Override
   public void loop() {
     /**  Remove Lifter
     if (lifterHolofectSensor.getState() == false) {

       DbgLog.msg("==== Hall Effect On ===");
     }
     if (lifterHolofectSensor.getState() == false) {
       if (currentPosition == POSITION.TOP || stickDirection == DIRECTION.UP && currentPosition != POSITION.BOTTOM) {
         currentPosition = POSITION.TOP ;
         if (stickDirection == DIRECTION.DOWN) {
           motorLifterPower = -gamepad2.left_stick_y;
         } else {
           motorLifterPower = 0;
         }
       } else if (currentPosition == POSITION.BOTTOM || stickDirection == DIRECTION.DOWN && currentPosition != POSITION.TOP) {
         currentPosition = POSITION.BOTTOM ;
         if (stickDirection == DIRECTION.UP) {
           motorLifterPower = -gamepad2.left_stick_y;
         } else {
           motorLifterPower = 0;
         }
       }
     } else {
       currentPosition = POSITION.MIDDLE ;
       motorLifterPower = -gamepad2.left_stick_y ;
     }
     if (lifterHolofectSensor.getState() == false) {
       halleffectState = "On";
     } else {
       halleffectState = "Off";
     }
     if (gamepad2.left_stick_button == true) {
       motorLifterPower = -gamepad2.left_stick_y ;
       motorLifterNull = "On" ;
     } else {
       motorLifterNull = "Off" ;
     }

 //Opens and closes hook
     if (gamepad1.right_trigger == 1 && !hookRT) {
       servoHookPosition = servoHookPosition + 0.5;
       hookRT = true ;
     } else if (gamepad1.right_bumper == true && !hookRB) {
       servoHookPosition = servoHookPosition - 0.5;
       hookRB = true ;
     }
     if (gamepad1.right_trigger != 1) {
       hookRT = false ;
     }
     if (!gamepad1.right_bumper) {
       hookRB = false ;
     }

 //Climber dropper position
     if (gamepad1.left_trigger == 1) {
       servoCDPosition = 0.5;
     } else if (gamepad1.left_bumper == true) {
       servoCDPosition = 0.0;
     }

   //sets tail position
     if (gamepad1.a == true) {
       servoTailPosition = (float) 1.0 ;
     }
     if ( gamepad1.b == true) {
       servoTailPosition = (float) 0.0 ;
     }
     if (servoTailPosition == 0) {
       servoTPString = "Up" ;
     } else if (servoTailPosition == 0.5) {
       servoTPString = "Down" ;
     }

 //Sets Lifter Tilt power
     motorLifterTiltPower = gamepad2.right_stick_y ;
     /*if (lifterPosition > lifterPositionIdeal) {
       motorLifterTiltPower = -tilterPower ;
     } else if (lifterPosition < lifterPositionIdeal) {
       motorLifterTiltPower = tilterPower ;
     } else if ( Math.abs(lifterPosition-lifterPositionIdeal) < 2.5) {
       motorLifterTiltPower = 0 ;
     }
     lifterPositionIdeal = lifterPositionIdeal - (gamepad2.right_stick_y/2) ;


     if (gamepad2.left_bumper == true && !scaleLB) {
       lifterScalarFactor = lifterScalarFactor + 1 ;
       scaleLB = true ;
     }
     if (gamepad2.right_bumper == true && !scaleRB) {
       lifterScalarFactor = lifterScalarFactor - 1 ;
       scaleLB = true ;
     }
     if (!gamepad2.left_bumper) {
       scaleLB = false ;
     }
     if (!gamepad2.right_bumper) {
       scaleRB = false ;
     }
     motorLifterTiltPower = lifterTiltScale(motorLifterTiltPower, lifterScalarFactor) ;
     lifterScalarFactor = Range.clip(lifterScalarFactor, 1, 10) ;

     if (gamepad2.b == true) {
       motorLifterTilt.setPower(0);
     } else if (gamepad2.a == true) {
       motorLifterTilt.setPowerFloat();
     }

     if (gamepad2.dpad_up) {
       motorLifterInverse = "On" ;
     } else if ( gamepad2.dpad_down) {
       motorLifterInverse = "Off" ;
     }
     if (motorLifterInverse.equals("On")) {
       motorLifterPower = -motorLifterPower ;
     }


     if ( motorLifterTilt.getPowerFloat() == true) {
       lifterFloat = "On" ;
     } else {
       lifterFloat = "Off" ;
     }
     End Remove LIfter**/

 //sets motor power
     motorLeft1power = (-gamepad1.left_stick_y - gamepad1.right_stick_x);
     motorLeft2power = (-gamepad1.left_stick_y - gamepad1.right_stick_x);
     motorRight1power = (-gamepad1.left_stick_y + gamepad1.right_stick_x);
     motorRight2power = (-gamepad1.left_stick_y + gamepad1.right_stick_x);

 //Makes lifter stay within desired area


     //goes slower if turning
     if (gamepad1.right_stick_x  != 0) {
       motorLeft1power = 2*(motorLeft1power)/3 ;
        motorLeft2power = 2*(motorLeft2power)/3 ;
     }

     if (gamepad1.dpad_up == false && gamepad1.dpad_down == false) {
       dpadPosition = "Off" ;
     }

     if (gamepad1.dpad_up == true && dpadPosition.equals("Off")) {
       motorScalar = motorScalar/2 ;
       dpadPosition = "Up" ;
     }

     if (gamepad1.dpad_down == true && dpadPosition.equals("Off")) {
       motorScalar = motorScalar * 2 ;
       dpadPosition = "Down" ;
     }

     motorLeft1power = motorLeft1power/motorScalar ;
     motorLeft2power = motorLeft2power/motorScalar ;
     motorRight1power = motorRight1power/motorScalar ;
     motorRight2power = motorRight2power/motorScalar ;

 //clips motor and servo power/position
     //motorLifterPower = motorLifterPower/3 ;//scales final lifter power
     motorLeft1power = Range.clip(motorLeft1power, motorPowerMin, motorPowerMax);
     motorLeft2power = Range.clip(motorLeft2power, motorPowerMin, motorPowerMax);
     motorRight1power = Range.clip(motorRight1power, motorPowerMin, motorPowerMax);
     motorRight2power = Range.clip(motorRight2power, motorPowerMin, motorPowerMax);
     //motorScalar = Range.clip(motorScalar, 1, 8) ;
    // motorLifterTiltPower = Range.clip(motorLifterTiltPower, motorPowerMin, motorPowerMax);
    // motorLifterPower = Range.clip(motorLifterPower, motorPowerMin, motorPowerMax);
    // servoHookPosition = Range.clip(servoHookPosition, servoMinRange, servoMaxRange);
     //servoCDPosition = Range.clip(servoCDPosition, servoMinRange, servoMaxRange);

 //sets motor and servo power/position
     motorLeft1.setPower(motorLeft1power);
     motorLeft2.setPower(motorLeft2power);
     motorRight1.setPower(motorRight1power);
     motorRight2.setPower(motorRight2power);
     //motorLifter.setPower(motorLifterPower);
     //servoHook.setPosition(servoHookPosition);
     //servoCD.setPosition(servoCDPosition);
     //servoTail.setPosition(servoTailPosition);

     //if (!motorLifterTilt.getPowerFloat() ) {
       //motorLifterTilt.setPower(motorLifterTiltPower) ;
     //}

 // gets current position and uses formula to find rotations or distance in inches
     positionLeft = -motorLeft1.getCurrentPosition();
     positionRight
             = motorRight1.getCurrentPosition();

     positionLeft = (positionLeft / 2500);//(wheelDiameter*3.14159265358)
     positionRight
             = (positionRight
             / 2500); //(wheelDiameter*3.14159265358)
 //
     if (-gamepad2.left_stick_y > 0) {
       stickDirection = DIRECTION.UP;
     } else if (-gamepad2.left_stick_y < 0) {
       stickDirection = DIRECTION.DOWN;
     }


 //telemetry data
     //telemetry.addData("Left Stick", "Left Stick is at " + String.format("%.2f", gamepad1.left_stick_y)); //left stick y-axis poition
     //telemetry.addData("0 Lifter Scalar", "The lifter is " + String.format("%.0f", lifterScalarFactor) + " tenths power"); // tells scale
     //telemetry.addData("0 Tilt Lifter Float", "The lifter float is " + lifterFloat);
     telemetry.addData("1 Motor 1", "Motor 1 power is " + String.format("%.2f", motorLeft1power)); //motor 1 power
     telemetry.addData("1 Motor 2", "Motor 2 power is " + String.format("%.2f", motorLeft2power)); // motor 2 power
     telemetry.addData("1 Motor 3", "Motor 3 power is " + String.format("%.2f", motorRight1power)); //motor 1 power
     telemetry.addData("1 Motor 4", "Motor 4 power is " + String.format("%.2f", motorRight2power)); // motor 2 power
     telemetry.addData("1.1 Motor Scalar", "Motor Scale is " + String.format("%f", motorScalar));
     telemetry.addData("1.1 Dpad Position", "Dpad is " + dpadPosition) ;
     telemetry.addData("1.2 Left Distance", "Left motor has gone " + String.format("%.2f", positionLeft) + " rotations"); //distance in rotations
     telemetry.addData("1.2 Right Distance", "Right motor has gone " + String.format("%.2f", positionRight) + " rotations"); //distance in rotations
     //telemetry.addData("2 Motor Lifter", "Motor Lifter power is " + String.format("%.2f", motorLifterPower)); //motor Lifter power
    // telemetry.addData("3 Servo Hook Position", "Servo Hook is at " + String.format("%.1f", servoHookPosition)); //servo hook position
    // telemetry.addData("3 Servo Tail", "Servo tail is " + servoTPString);//tells if servo tail is down/up
     //telemetry.addData("4 HalleffectSensor", "Halleffect sensor is " + halleffectState); //hallefect state
     telemetry.addData("4 CurrentPosition", "Current position is " + currentPosition.name());
     telemetry.addData("4 StickDirection", "Stick is moving " + stickDirection.name());
    // telemetry.addData("4 Motor Lifter Null", "Motor Lifter Null is " + motorLifterNull);// allows you to move lifter no matter what
     telemetry.addData("5 azimuth", Math.round(Math.toDegrees(azimuth)));
     telemetry.addData("5 pitch", Math.round(Math.toDegrees(pitch)));
     telemetry.addData("5 roll", Math.round(Math.toDegrees(roll)));


   }

   @Override
   public void stop() {
     mSensorManager.unregisterListener(this);
   }

   public void onAccuracyChanged(Sensor sensor, int accuracy) {
     // not sure if needed, placeholder just in case
   }

   //float lifterTiltScale(float scaleInput/*motor power*/, float scaleInput2/*lifter scale factor*/) {
     //scaleInput = (scaleInput2 *scaleInput)/10 ;

     //return scaleInput ;
   //}

   public void onSensorChanged(SensorEvent event) {
     // we need both sensor values to calculate orientation
     // only one value will have changed when this method called, we assume we can still use the other value.
     if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
       mGravity = event.values;
     }
     if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
       mGeomagnetic = event.values;
     }
     if (mGravity != null && mGeomagnetic != null) {  //make sure we have both before calling getRotationMatrix
       float R[] = new float[9];
       float I[] = new float[9];
       boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
       if (success) {
         float orientation[] = new float[3];
         SensorManager.getOrientation(R, orientation);
         azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
         pitch = orientation[1];
         roll = orientation[2];
       }
     }
   }
 }