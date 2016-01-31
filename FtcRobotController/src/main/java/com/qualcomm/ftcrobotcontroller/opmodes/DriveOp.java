package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Date;

public class DriveOp extends OpMode {

    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;


    public void init() {
        //driving wheel motors
        frontLeftMotor = hardwareMap.dcMotor.get("frontL");
        frontRightMotor = hardwareMap.dcMotor.get("frontR");
        backLeftMotor = hardwareMap.dcMotor.get("backL");
        backRightMotor = hardwareMap.dcMotor.get("backR");
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);

    }
    //Set Motors method
    public void setMotors(double FrontL, double FrontR, double BackL, double BackR){
        frontLeftMotor.setPower(FrontL);
        frontRightMotor.setPower(FrontR);
        backLeftMotor.setPower(BackL);
        backRightMotor.setPower(BackR);
    }

    public void loop() {
        //DON'T TOUCH(Driving and Joystick controls)
        float throttle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;
        float secondThrottle = gamepad2.left_stick_y;
        float secondRightThrottle = gamepad2.right_stick_y;
        //DON'T TOUCH(Dead zone code)
        throttle = (Math.abs(throttle) < 0.3) ? 0 : throttle;
        rightThrottle = (Math.abs(rightThrottle) < 0.05) ? 0 : rightThrottle;
        secondThrottle = (Math.abs(secondThrottle) < 0.3) ? 0 : secondThrottle;
        secondRightThrottle = (Math.abs(secondRightThrottle) < 0.05) ? 0 : secondRightThrottle;
        //DON'T TOUCH(Calling the Set Motors method)

        if (Math.abs(throttle) < .05 && Math.abs(rightThrottle) < .05)
        {
            setMotors(secondThrottle, secondRightThrottle, secondThrottle, secondRightThrottle);
        } else {
            setMotors(throttle, rightThrottle, throttle, rightThrottle);
        }

        }
    public void stop() {

    }

}