package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;


public class SigmaCatsOp extends OpMode
{

    double penPosition;
    double penMove = 0.1;
    DcMotor motorRight;
    DcMotor motorLeft;
    Servo pen;

    public SigmaCatsOp() {

    }

    public void init(){
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        pen = hardwareMap.servo.get("pen");

    }
    public void loop(){
   
   float throttle = -gamepad1.left_stick_y;
   float direction = gamepad1.left_stick_x;
   float right = throttle - direction;
   float left = throttle + direction;
   right = Range.clip(right, -1, 1);
   left = Range.clip(left, -1, 1);
   right = (float)scaleInput(right);
    left =  (float)scaleInput(left);
   motorRight.setPower(right);
   motorLeft.setPower(left);


   if(gamepad1.a){
   Square();
       motorLeft.setPower(0);
       motorRight.setPower(0);
   }
   if(gamepad1.b){
   Triangle();
       motorLeft.setPower(0);
       motorRight.setPower(0);
   }
   if(gamepad1.x){
   Circle();
   }
        if(gamepad1.dpad_up){
            penPosition += penMove;
        }

        if(gamepad1.dpad_down){
            penPosition -= penMove;
        }
        pen.setPosition(penPosition);

    }

   

    public void stop(){


    }
    public void goStraight(double time){
    ElapsedTime timer = new ElapsedTime();    
    while(timer.time() <= time){
    motorLeft.setPower(1);
    motorRight.setPower(1);
       }
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
   
   
    public void turnRight(double time){
    ElapsedTime timer = new ElapsedTime();
    while(timer.time() <= time){
    motorLeft.setPower(1);
    motorRight.setPower(0);
       }
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
   
   
    public void Circle(){
    ElapsedTime timer = new ElapsedTime();
    while(timer.time () <= 0.5)
    {
    motorLeft.setPower(1);
    motorRight.setPower(0);
      }
        motorLeft.setPower(0);
        motorRight.setPower(0);
   }
   
   
    public void Triangle(){
    int x = 0;
    while(x <= 2){
    goStraight(.05);
    turnRight(.71);
    x++;
       }
   }
   
   
    public void Square(){
    int x = 0;
    while(x <= 3){
    goStraight(.05);
    turnRight(.66);

       }
   }
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
