package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ExampleServos extends OpMode {
    Servo servo1;
    Servo servo2;

    //Position constants for the buttons
    double DOWN_POSITION = 0.6;
    double UP_POSITION = 0.4;

    //Scale constant to scale the joystick value by
    double SCALE = 0.01;
    //Variable for the servo's current position
    double currentPosition;

    @Override
    public void init() {
      servo1 = hardwareMap.servo.get("left_hand");
      servo2 = hardwareMap.servo.get("right_hand");
      //Set the current position to a known value
      currentPosition = 0.5;
    }


    @Override
    public void loop() {

      //**** Move a Servo with Buttons ****
      //Move servo 1 to the up position when a button is pressed
      if(gamepad1.a) {
          servo1.setPosition(UP_POSITION);
      }
      //Move servo 1 to the down position when a button is pressed
      if(gamepad1.b) {
          servo1.setPosition(DOWN_POSITION);
      }

      //**** Move a Servo with a Joystick ****
      //Get the joystick value and scale it to set how much the servo should change
      double delta = (-gamepad1.left_stick_y)*SCALE;
      //Add the amount to change by to the current position, and
      //limit the values to a range the servo can accept
      currentPosition = Range.clip((currentPosition + delta), 0, 1.0);
      //apply the value to the servo
      servo2.setPosition(currentPosition);
    }
}
