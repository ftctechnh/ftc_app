package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
//import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
//import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

/**
 * Created by Cormac on 11/2/2015.
 */
public class BasicDrive extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void init(){
        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        //reverse right motor so forward is forward
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }
    
    @Override
    public void loop(){
        float xValue = gamepad1.left_stick_y;
        float yValue = -gamepad1.left_stick_y;

        //calc value for each motor
        float leftPower = yValue + xValue;
        float rightPower = yValue - xValue;

        //clip range aka set max and min
        leftPower = Range.clip(leftPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);

        //set power
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

    }
}