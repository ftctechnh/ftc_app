package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Nikhil on 9/19/2015.
 */
public class TeleOp extends OpMode {

    DcMotor leftMotor1; //Assigns motor1 as a DC Motor
    DcMotor leftMotor2; //Assigns motor2 as a DC Motor
    DcMotor rightMotor1;
    DcMotor rightMotor2;


    @Override
    public void init() {
        leftMotor1 = hardwareMap.dcMotor.get("leftMotor1"); //This command acquires the right addresses (location) of where to send the commands to
        leftMotor2 = hardwareMap.dcMotor.get("leftmotor2");
        rightMotor1 = hardwareMap.dcMotor.get("rightMotor1");
        rightMotor2 = hardwareMap.dcMotor.get("rightMotor2");

        rightMotor1.setDirection(DcMotor.Direction.REVERSE);
        rightMotor2.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {

        leftMotor1.setPower(gamepad1.left_stick_y); //Assigns motor1 to the y-axis of the left stick on the gamepad
        leftMotor2.setPower(gamepad1.left_stick_y);//Assigns motor2 to the y-axis of the right stick on the gamepad
        rightMotor1.setPower(gamepad1.right_stick_y);
        rightMotor2.setPower(gamepad1.right_stick_y);

    }
}
