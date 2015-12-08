package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Thor_auto extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor leftClimb;
    DcMotor rightClimb;
    DcMotor upperMotor;
    DcMotor bottomMotor;
    //Servo leftsideServo;
    //Servo rightsideServo;
    //double leftservopos,rightservopos,deltapos=0.01;
    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftClimb = hardwareMap.dcMotor.get("left_climb");
        rightClimb = hardwareMap.dcMotor.get("right_climb");
        upperMotor = hardwareMap.dcMotor.get("small_arm");
        bottomMotor = hardwareMap.dcMotor.get("big_arm");
        //leftsideServo = hardwareMap.servo.get("left_servo");
        //rightsideServo = hardwareMap.servo.get("right_servo");
        //leftservopos=0;
        //rightservopos=0;
    }

    @Override
    public void loop() {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        if (timer.time() - timer.startTime() < 30) {
            leftMotor.setPower(1);
            rightMotor.setPower(1);
        }
    }
}
