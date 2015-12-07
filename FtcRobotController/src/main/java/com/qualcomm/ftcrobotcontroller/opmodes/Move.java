package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//import com.qualcomm.ftcrobotcontroller.opmodes.MC;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

public class Move extends LinearOpMode{

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

    public void runOpMode(){
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor = hardwareMap.dcMotor.get("motor_left");
        while(true) {
            rightMotor.setPower(speed);
            leftMotor.setPower(speed);
        }
    }
}