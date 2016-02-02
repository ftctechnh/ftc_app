package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by rayzhang on 11/10/15.
 */
public class Thor extends OpMode {
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
    public void init(){
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftClimb = hardwareMap.dcMotor.get("left_climb");
        rightClimb = hardwareMap.dcMotor.get("right_climb");
        upperMotor= hardwareMap.dcMotor.get("small_arm");
        bottomMotor= hardwareMap.dcMotor.get("big_arm");
        //leftsideServo = hardwareMap.servo.get("left_servo");
        //rightsideServo = hardwareMap.servo.get("right_servo");
        //leftservopos=0;
        //rightservopos=0;
    }
    @Override
    public void loop(){
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
        /*if (b2)
        {
            rightservopos+=deltapos;
        }
        if (x2)
        {
            rightservopos-=deltapos;
        }
        if (right2)
        {
            leftservopos+=deltapos;
        }
        if (left2)
        {
            leftservopos-=deltapos;
        }
        rightservopos = Range.clip(rightservopos, 0, 1);
        rightsideServo.setPosition(rightservopos);
        leftservopos = Range.clip(leftservopos, 0, 1);
        leftsideServo.setPosition(leftservopos);*/
        if (rightBumper2)
        {
            upperMotor.setDirection(DcMotor.Direction.FORWARD);
            upperMotor.setPower(1);
        }
        if (leftBumper2)
        {
            upperMotor.setDirection(DcMotor.Direction.REVERSE);
            upperMotor.setPower(1);
        }
        if (!((leftTrigger2<0)&&(rightTrigger2>0))) {
            if (rightTrigger2 > 0) {
                bottomMotor.setPower(0.75*rightTrigger2);
            }
            if (leftTrigger2 < 0) {
                bottomMotor.setPower(0.75*leftTrigger2);
            }
        }
        if ((leftTrigger2==0)&&(rightTrigger2==0))
        {
            bottomMotor.setPower(0);
        }
        if ((!leftBumper2)&&(!rightBumper2))
        {
            upperMotor.setPower(0);
        }
        if (up)
        {
            leftClimb.setDirection(DcMotor.Direction.REVERSE);
            leftClimb.setPower(-1);
        }
        if (a)
        {
            rightClimb.setDirection(DcMotor.Direction.REVERSE);
            rightClimb.setPower(-1);
        }
        if (y)
        {
            rightClimb.setDirection(DcMotor.Direction.FORWARD);
            rightClimb.setPower(-1);
        }
        if (down)
        {
            leftClimb.setDirection(DcMotor.Direction.FORWARD);
            leftClimb.setPower(-1);
        }
        if ((!a)&&(!y))
        {
            rightClimb.setPower(0);
        }
        if ((!up)&&(!down))
        {
            leftClimb.setPower(0);
        }
        leftMotor.setPower(-leftY);
        rightMotor.setPower(-rightY);
    }
}
