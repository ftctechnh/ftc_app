package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class Dino_auto extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor upperMotor;
    DcMotor bottomMotor;
    Servo leftsideServo;
    Servo rightsideServo;
    double leftservopos, rightservopos, deltapos = 0.01;
    boolean k=true;
    ElapsedTime timer;

    @Override
    public void init() {
        //initialize motors
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        upperMotor = hardwareMap.dcMotor.get("small_arm");
        bottomMotor = hardwareMap.dcMotor.get("big_arm");
        leftsideServo = hardwareMap.servo.get("left_servo");
        rightsideServo = hardwareMap.servo.get("right_servo");
        leftservopos = 0;
        rightservopos = 0;
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if (k) {
            timer=new ElapsedTime();
            timer.reset();
            k=false;
        }
        if (timer.time()<=2) {
                leftMotor.setPower(1);
                rightMotor.setPower(1);
            }
        else
            if ((timer.time()>2) && (timer.time()<3)) {
                leftMotor.setPower(-1);
                rightMotor.setPower(1);
            }
            else {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
            }
    }
}