package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

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
    int i = 0;
    int j = 0;
    public void init(){
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        leftMotor = hardwareMap.dcMotor.get("motor_left");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        rightMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

    }


    public void loop(){

        if(rightMotor.getCurrentPosition() == 0){

            i = 1;
            rightMotor.setDirection(DcMotor.Direction.REVERSE);
            rightMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            leftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

            rightMotor.setTargetPosition(-10000);
            leftMotor.setTargetPosition(-10000);
            if (rightMotor.getCurrentPosition() > rightMotor.getTargetPosition()){
                i = 2;
                rightMotor.setPower(-.5);
                leftMotor.setPower(-.5);
            }else {
                rightMotor.setPower(0);
                leftMotor.setPower(0);
                i = 3;
            }
        }
        /*
        if(leftMotor.getCurrentPosition() == 0){
            j = 1;
            leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            leftMotor.setTargetPosition(1000);

            if (leftMotor.getCurrentPosition() < leftMotor.getTargetPosition()){
                j = 2;
                leftMotor.setPower(.75);
            }else {
                leftMotor.setPower(0);
                j = 3;
            }
        }
        */

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Did the right motor work?", "Right Motor work? " + i);
        //telemetry.addData("Did the left motor work?", "Left Motor work? " + j);
        telemetry.addData("RightMotor pos: ", "RightMotor pos: " + rightMotor.getCurrentPosition());
        //telemetry.addData("Leftmotor pos: ", "LeftMotor pos: " + leftMotor.getCurrentPosition());
    }

}