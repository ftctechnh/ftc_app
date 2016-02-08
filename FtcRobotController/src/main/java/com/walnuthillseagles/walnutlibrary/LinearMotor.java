package com.walnuthillseagles.walnutlibrary;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public abstract class LinearMotor extends SimpleMotor implements Runnable, Auto {
    //Field
    protected DcMotor motor;
    protected String name;
    protected boolean hasEncoders;
    protected double speedLimit;
    protected boolean isDone;
    //Value for a full encoder rotation
    protected int encoderRot;
    protected int orientation;
    //Constructor
    LinearMotor(DcMotor myMotor, String myName, boolean encoderCheck, boolean isReversed){
        super(myMotor, myName, encoderCheck);
        orientation = 1;
        if(isReversed)
            orientation = -1;
    }
    public abstract void operate(double val, double mySpeedLimit);
    public DcMotor getMotor(){
        return motor;
    }
    public void stop(){
        motor.setPower(0);
    }
}
