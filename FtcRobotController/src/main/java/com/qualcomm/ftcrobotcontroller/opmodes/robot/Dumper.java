package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/13/2015.
 */
public class Dumper {
    public Servo servo;

    double leftPosition = 0.44;
    double neutralPosition = 0.49;
    double rightPosition = 0.54;

    public Dumper(){
    }

    public void init(HardwareMap hardwareMap){
        servo = hardwareMap.servo.get("dumperServo");
    }
    public void setLeft(){
        servo.setPosition(leftPosition);
    }

    public void setRight(){
        servo.setPosition(rightPosition);
    }

    public void setNeutral(){
        servo.setPosition(neutralPosition);
    }
}
