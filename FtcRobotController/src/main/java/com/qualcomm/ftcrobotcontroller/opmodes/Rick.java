package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by rayzhang on 11/10/15.
 */
public class Rick extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor leftClimb;
    DcMotor rightClimb;
    DcMotor upperMotor;
    DcMotor bottomMotor;
    @Override
    public void init(){
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftClimb = hardwareMap.dcMotor.get("left_climb");
        rightClimb = hardwareMap.dcMotor.get("right_climb");
        upperMotor= hardwareMap.dcMotor.get("small_arm");
        bottomMotor= hardwareMap.dcMotor.get("big_arm");
    }
    @Override
    public void loop(){
        float leftY = gamepad1.left_stick_y;
        float rightY= -gamepad1.right_stick_y;
        float rightTrigger=gamepad1.right_trigger;
        float leftTrigger=-gamepad1.left_trigger;
        boolean up=gamepad1.dpad_up;
        boolean down=gamepad1.dpad_down;
        boolean leftBumper=gamepad1.left_bumper;
        boolean rightBumper=gamepad1.right_bumper;
        boolean a=gamepad1.a;
        boolean b=gamepad1.b;
        boolean x=gamepad1.x;
        boolean y=gamepad1.y;
        if (rightBumper)
        {
            upperMotor.setDirection(DcMotor.Direction.FORWARD);
            upperMotor.setPower(1);
        }
        if (leftBumper)
        {
            upperMotor.setDirection(DcMotor.Direction.REVERSE);
            upperMotor.setPower(1);
        }
        if (!((leftTrigger<0)&&(rightTrigger>0)))
        {
            if (rightTrigger>0)
            {
                bottomMotor.setPower(0.25*rightTrigger);
            }
            if (leftTrigger<0)
            {
                bottomMotor.setPower(0.25*leftTrigger);
            }
        }
        if ((leftTrigger==0)&&(rightTrigger==0))
        {
            bottomMotor.setPower(0);
        }
        if ((leftBumper==false)&&(rightBumper==false))
        {
            upperMotor.setPower(0);
        }
        if (up)
        {
            leftClimb.setDirection(DcMotor.Direction.FORWARD);
            rightClimb.setDirection(DcMotor.Direction.FORWARD);
            rightClimb.setPower(1);
            leftClimb.setPower(1);
        }
        if (down)
        {
            leftClimb.setDirection(DcMotor.Direction.REVERSE);
            rightClimb.setDirection(DcMotor.Direction.REVERSE);
            rightClimb.setPower(1);
            leftClimb.setPower(1);
        }
        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
    }
}
