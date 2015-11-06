package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Cormac on 11/2/2015.
 */
public class BasicDrive extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void init(){
        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcmotor.get("left_drive");
        rightMotor = hardwareMap.dcmotor.get("right_drive");

        //reverse right motor so forward is forward
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }
    
    @Override
    public void loop(){
        float xValue = gamepad.left_stick.y;
        float yValue = -gamepad.left_stick.y;

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