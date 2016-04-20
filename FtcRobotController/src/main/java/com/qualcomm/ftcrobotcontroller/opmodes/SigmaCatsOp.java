package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Stigma_Cats extends OpMode {

    double penPosition;
    double penMove = 0.1;
    DcMotor motorRight;
    DcMotor motorLeft;
    Servo pen;

    public Stigma_Cats() {

    }

    public void init(){


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
   }
   if(gamepad1.b){
   Triangle();
   }
   if(gamepad1.x){
   Circle();
   }
  
   }
   
   if(gamepad1.dpad_up){
   penPosition += penMove; 
      }
      
   if(gamepad1.dpad_down){
   penPosition -= penMove; 
      }
   Pen.setPosition(penPosition);

    }
    public void stop(){


    }
    public void goStraight(double time){
    ElapsedTime timer = new ElapsedTime();    
    while(timer.time <= time){
    motorLeft.setPower(1);
    motorRight.setPower(1);
       }
    }
   
   
    public void turnRight(double time){
    ElapsedTime timer = new ElapsedTime();
    while(timer.time <= time){
    motorLeft.setPower(1);
    motorRight.setPower(0);
       }
    }
   
   
    public void Circle(){
    ElapsedTime timer = new ElapsedTime();
    while(timer.time <= 2.01{
    motorLeft.setPower(1);
    motorRight.setPower(0);
      }
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

}
