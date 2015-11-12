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
    @Override
    public void init(){
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftClimb= hardwareMap.dcMotor.get("left_arm");
        rightClimb= hardwareMap.dcMotor.get("right_arm");
    }
    @Override
    public void loop(){
        float leftY = gamepad1.left_stick_y;
        float rightY= -gamepad1.right_stick_y;
        float rightTrigger=gamepad1.right_trigger;
        float leftTrigger=-gamepad1.left_trigger;
        boolean leftBumper=gamepad1.left_bumper;
        boolean rightBumper=gamepad1.right_bumper;
        boolean a=gamepad1.a;
        boolean b=gamepad1.b;
        boolean x=gamepad1.x;
        boolean y=gamepad1.y;
        if ((leftBumper)||(rightBumper))
        {
            if ((leftBumper) && (leftTrigger == 0))
            {
                leftClimb.setDirection(DcMotor.Direction.REVERSE);
                leftClimb.setPower(1);
            }
            else
            {
                leftClimb.setDirection(DcMotor.Direction.FORWARD);
                leftClimb.setPower(leftTrigger);
            }
            if ((rightBumper) && (rightTrigger == 0))
            {
                rightClimb.setDirection(DcMotor.Direction.REVERSE);
                rightClimb.setPower(1);
            }
            else
            {
                rightClimb.setDirection(DcMotor.Direction.FORWARD);
                rightClimb.setPower(rightTrigger);
            }
        }
        else
        {
            rightClimb.setDirection(DcMotor.Direction.FORWARD);
            rightClimb.setPower(rightTrigger);
            leftClimb.setDirection(DcMotor.Direction.FORWARD);
            leftClimb.setPower(leftTrigger);
        }
        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
    }
}
