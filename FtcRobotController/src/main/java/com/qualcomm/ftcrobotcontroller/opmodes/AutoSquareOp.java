package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by akhil on 8/30/2015.
 */
public class AutoSquareOp extends OpMode {


    @Override
    public void init() {

    }

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;

    int scaleDegree = 1;

    public AutoSquareOp() {

    }

    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void start() {
        if (gamepad1.left_bumper == true){
        motor1 = hardwareMap.dcMotor.get("motor_1");
        motor2 = hardwareMap.dcMotor.get("motor_2");
        motor3 = hardwareMap.dcMotor.get("motor_3");
        motor4 = hardwareMap.dcMotor.get("motor_4");

        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor4.setDirection(DcMotor.Direction.REVERSE);


    }

    @Override
    public void loop() {
         {
        
        }
    }


}

