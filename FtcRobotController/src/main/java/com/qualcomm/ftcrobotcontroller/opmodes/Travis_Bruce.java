package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class Travis_Bruce extends OpMode {
    DcMotor leftMotor_1;
    DcMotor rightMotor_1;
    DcMotor leftMotor_2;
    DcMotor rightMotor_2;
    DcMotor upperMotor;
    DcMotor bottomMotor;
    //Servo leftsideServo;
    //Servo rightsideServo;
    double leftservopos,rightservopos,deltapos=0.01;
    @Override
    public void init()
    {
        leftMotor_1 = hardwareMap.dcMotor.get("left_motor_1");
        rightMotor_1 = hardwareMap.dcMotor.get("right_motor_1");
        leftMotor_2 = hardwareMap.dcMotor.get("left_motor_2");
        rightMotor_2 = hardwareMap.dcMotor.get("right_motor_2");
        //movement motors initialize
        upperMotor= hardwareMap.dcMotor.get("small_arm");
        bottomMotor= hardwareMap.dcMotor.get("big_arm");
        //arm motors initialize
        //leftsideServo = hardwareMap.servo.get("left_servo");
        //rightsideServo = hardwareMap.servo.get("right_servo");
        //leftservopos=0;
        //rightservopos=0;
    }
    @Override
    public void loop() {
        float leftY = -gamepad1.left_stick_y;
        float rightY = gamepad1.right_stick_y;
        float rightTrigger = gamepad1.right_trigger;
        float leftTrigger = -gamepad1.left_trigger;
        boolean up = gamepad1.dpad_up;
        boolean down = gamepad1.dpad_down;
        boolean left = gamepad1.dpad_left;
        boolean right = gamepad1.dpad_right;
        boolean rightBumper = gamepad1.right_bumper;
        boolean leftBumper = gamepad1.left_bumper;
        boolean a = gamepad1.a;
        boolean b = gamepad1.b;
        boolean x = gamepad1.x;
        boolean y = gamepad1.y;
        float leftY2 = -gamepad2.left_stick_y;
        float rightY2 = gamepad2.right_stick_y;
        float rightTrigger2 = gamepad2.right_trigger;
        float leftTrigger2 = -gamepad2.left_trigger;
        boolean up2 = gamepad2.dpad_up;
        boolean down2 = gamepad2.dpad_down;
        boolean left2 = gamepad2.dpad_left;
        boolean right2 = gamepad2.dpad_right;
        boolean rightBumper2 = gamepad2.right_bumper;
        boolean leftBumper2 = gamepad2.left_bumper;
        boolean a2 = gamepad2.a;
        boolean b2 = gamepad2.b;
        boolean x2 = gamepad2.x;
        boolean y2 = gamepad2.y;
        //get the information from gamepad. determine which one is pressing
        /*if (b)
        {
            rightservopos+=deltapos;
        }
        if (x)
        {
            rightservopos-=deltapos;
        }
        if (right)
        {
            leftservopos+=deltapos;
        }
        if (left)
        {
            leftservopos-=deltapos;
        }
        rightservopos = Range.clip(rightservopos, 0, 1);
        rightsideServo.setPosition(rightservopos);
        leftservopos = Range.clip(leftservopos, 0, 1);
        leftsideServo.setPosition(leftservopos);*/
        if (rightBumper) {
            upperMotor.setDirection(DcMotor.Direction.FORWARD);
            upperMotor.setPower(1);
        }
        if (leftBumper) {
            upperMotor.setDirection(DcMotor.Direction.REVERSE);
            upperMotor.setPower(1);
        }
        if (!((leftTrigger < 0) && (rightTrigger > 0))) {
            if (rightTrigger > 0) {
                bottomMotor.setPower(rightTrigger);
            }
            if (leftTrigger < 0) {
                bottomMotor.setPower(leftTrigger);
            }
        }
        if ((leftTrigger == 0) && (rightTrigger == 0)) {
            bottomMotor.setPower(0);
        }
        if (!leftBumper && !rightBumper) {
            upperMotor.setPower(0);
        }
        leftMotor_1.setPower(leftY);
        leftMotor_2.setPower(leftY);
        rightMotor_1.setPower(rightY);
        rightMotor_2.setPower(rightY);
    }
}
