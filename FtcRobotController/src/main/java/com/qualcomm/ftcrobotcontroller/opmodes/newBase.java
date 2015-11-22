package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by subash on 11/17/2015.
 */
public class newBase extends OpMode
{
    float speed = 0.75f;
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor right;
    DcMotor left;

    @Override
    public void stop()
    {
        right.close();
        left.close();
        frontRight.close();
        frontLeft.close();
        backRight.close();
        backLeft.close();
    }

    @Override
    public void init()
    {
        gamepad1.setJoystickDeadzone(0.05f);
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        right.setDirection(DcMotor.Direction.REVERSE);
        left.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);

    }

    @Override
    public void loop()
    {
        setDrive(gamepad1.right_stick_y, gamepad1.left_stick_y);
        if(gamepad1.right_bumper)
        {
            frontRight.setPower(speed);
            frontLeft.setPower(speed);
        }
        else if(gamepad1.right_trigger > 0.05f)
        {
            frontRight.setPower(-speed);
            frontLeft.setPower(-speed);
        }
        else
        {
            frontRight.setPower(0.0f);
            frontLeft.setPower(0.0f);
        }
        if(gamepad1.left_bumper)
        {
            backRight.setPower(speed);
            backLeft.setPower(speed);
        }
        else if(gamepad1.left_trigger > 0.05f)
        {
            backRight.setPower(-speed);
            backLeft.setPower(-speed);
        }
        else
        {
            backRight.setPower(0.0f);
            backLeft.setPower(0.0f);
        }
    }

    void setDrive(double y1, double y2)
    {
        right.setPower(y1);
        left.setPower(y2);
    }


}
