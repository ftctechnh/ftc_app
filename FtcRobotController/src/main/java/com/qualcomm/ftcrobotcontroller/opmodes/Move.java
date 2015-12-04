package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//import com.qualcomm.ftcrobotcontroller.opmodes.MC;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

public class Move extends OpMode{

    public Move(){
    }

    final static double speed = .5;
    static DcMotor armLowerMotor;
    DcMotor armUpperMotor;
    boolean peopleAndLight = true;
    boolean climbing = false;
    static boolean isBlue = true;
    final static int distance = 1000;
    DcMotor rightMotor;
    DcMotor leftMotor;

    public void init(){
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        leftMotor = hardwareMap.dcMotor.get("motor_left");
    }

    public void start(){
        while(true) {
            rightMotor.setPower((float).9);
            leftMotor.setPower((float).9);
            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("Motor target Pos", "Pos: " + rightMotor.getTargetPosition());
            telemetry.addData("Motor current Pos", "Pos: " + rightMotor.getCurrentPosition());
        }
        /*
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();
        rightMotor.setTargetPosition(distance + rightPos);
        leftMotor.setTargetPosition(distance + leftPos);
        while(rightMotor.getTargetPosition() > rightMotor.getCurrentPosition()){
            rightMotor.setPower(speed);
            leftMotor.setPower(speed);
        }


        */
    }

    public void loop(){

    }

}