package com.qualcomm.ftcrobotcontroller.opmodes.OurPrograms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class CRLift extends OpMode{

    //double CRValue = 0.5;

    Servo rightCR;
    Servo leftCR;

    @Override
    public void init(){

        leftCR = hardwareMap.servo.get("servo_1");
        rightCR = hardwareMap.servo.get("servo_2");
    }

    @Override
    public void loop(){

        if(gamepad1.a){
            leftCR.setPosition(1.0);
            rightCR.setPosition(0.0);
        }
        else if(gamepad1.b){
            leftCR.setPosition(0.0);
            rightCR.setPosition(1.0);
        }
        else{
            leftCR.setPosition(0.5);
            rightCR.setPosition(0.5);
        }
    }

    public void stop() {
    }

}
