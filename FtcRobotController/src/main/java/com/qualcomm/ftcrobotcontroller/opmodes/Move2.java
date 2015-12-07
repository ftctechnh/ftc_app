package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//import com.qualcomm.ftcrobotcontroller.opmodes.MC;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

public class Move2 extends OpMode{
    public Move2(){
    }

    final static float speed = .5f;
    static DcMotor armLowerMotor;
    DcMotor armUpperMotor;
    boolean peopleAndLight = true;
    boolean climbing = false;
    static boolean isBlue = true;
    DcMotor rightMotor;
    DcMotor leftMotor;
    boolean doOnce = true;

    public void init(){
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        leftMotor = hardwareMap.dcMotor.get("motor_left");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }


    public void loop(){

        while(doOnce){
            for(int i = 0; i <= 100000; i++) {
                rightMotor.setPower(speed);
                leftMotor.setPower(speed);
            }
            doOnce=false;
        }
    }

}