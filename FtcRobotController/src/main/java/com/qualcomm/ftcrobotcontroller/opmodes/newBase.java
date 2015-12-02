package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by subash on 11/17/2015.
 */
public class newBase extends OpMode
{
    float speed = 1.0f;
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor right;
    DcMotor left;
    DcMotor grabber;
    Servo diverter;
    PhoneGyrometer gyro;


    @Override
    public void stop()
    {
        right.close();
        left.close();
        frontRight.close();
        frontLeft.close();
        backRight.close();
        backLeft.close();
        gyro.onDestroy();
    }

    @Override
    public void init()
    {
        gyro = new PhoneGyrometer(hardwareMap);
        gamepad1.setJoystickDeadzone(0.05f);
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        grabber = hardwareMap.dcMotor.get("grabber");
        diverter = hardwareMap.servo.get("diverter");
        grabber.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.REVERSE);
        left.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        diverter.setDirection(Servo.Direction.FORWARD);
        gamepad1.setJoystickDeadzone(0.01f);
        gamepad2.setJoystickDeadzone(0.01f);
    }

    @Override
    public void loop()
    {
        setDrive(gamepad1.right_stick_y, gamepad1.left_stick_y);
        grabber.setPower(gamepad2.left_stick_y);
        if(gamepad2.right_stick_button)
        {
                diverter.setPosition(1.0f);
        }
        else if(gamepad2.left_stick_button)
        {
                diverter.setPosition(0.0f);
        }
        else
        {
                diverter.setPosition(0.5f);
        }

        if(gamepad1.a)
        {
            frontRight.setPower(-0.2f);
            frontLeft.setPower(-0.2f);
            backRight.setPower(0.2f);
            backLeft.setPower(0.2f);
        }
        else
        {

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
        telemetry.addData("gyroReading = ", gyro.getAzimuth());
    }

    void setDrive(double y1, double y2)
    {
        right.setPower(y1);
        left.setPower(y2);
    }

}
