package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by rayzhang on 10/26/15.
 */
public class Jonathan extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor upperMotor;
    DcMotor bottomMotor;
    @Override
    public void init()
    {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        upperMotor= hardwareMap.dcMotor.get("small_arm");
        bottomMotor= hardwareMap.dcMotor.get("big_arm");
    }
    @Override
    public void loop()
    {
        float leftY = -gamepad1.left_stick_y;
        float rightY= gamepad1.right_stick_y;
        float rightTrigger=gamepad1.right_trigger;
        float leftTrigger=-gamepad1.left_trigger;
        boolean rightBumper=gamepad1.right_bumper;
        boolean leftBumper=gamepad1.left_bumper;
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
        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
    }
}
