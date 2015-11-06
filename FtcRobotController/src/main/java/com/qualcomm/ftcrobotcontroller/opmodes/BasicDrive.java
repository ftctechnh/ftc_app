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
    }
    
    @Override
    public void loop(){

    }
}