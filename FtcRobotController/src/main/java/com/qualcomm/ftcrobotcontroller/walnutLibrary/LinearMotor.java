package com.qualcomm.ftcrobotcontroller.walnutLibrary;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public abstract class LinearMotor{
    //Field
    protected DcMotor motor;
    protected String name;
    protected boolean hasEncoders;
    protected double speedLimit;
    //Value for a full encoder rotation
    protected int encoderRot;
    protected int orientation;

    //Constructor
    LinearMotor(DcMotor myMotor, String myName, boolean encoderCheck, boolean isReversed){
        motor = myMotor;
        name = myName;
        hasEncoders = encoderCheck;
        speedLimit = 0;
        if(encoderCheck){
            motor.setMode((DcMotorController.RunMode.valueOf("RUN_USING_ENCODERS")));
            motor.setMode((DcMotorController.RunMode.valueOf("RESET_ENCODERS")));
        }
        if(isReversed){
            orientation = -1;
        }
        else{
            orientation = 1;
        }
    }
    public abstract void operate(double val, double mySpeedLimit);
    public void stopMotor(){
        motor.setPower(0);
    }
}
