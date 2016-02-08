package com.walnuthillseagles.walnutlibrary;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearMotor extends SimpleMotor implements Runnable, Auto {
    //Field
    protected DcMotor motor;
    protected String name;
    protected boolean hasEncoders;
    protected double speedLimit;
    protected boolean isDone;
    //Value for a full encoder rotation
    protected int encoderRot;
    protected int orientation;
    protected Thread runner;
    //Encoder Stuff
    protected int target;
    //Constants
    public static final double WHEEL_SIZE =4;
    public static final double GEAR_RATIO = 1;
    public static final int TOLERANCE = 50;
    //Constructor
    LinearMotor(DcMotor myMotor, String myName, boolean encoderCheck, boolean isReversed){
        super(myMotor, myName, encoderCheck);
        orientation = 1;
        if(isReversed)
            orientation = -1;
        runner = new Thread(this);
        target = 0;
        speedLimit = 0;
    }
    //Val = inches
    public void operate(double val, double mySpeedLimit){
        speedLimit = Math.abs(mySpeedLimit);
        if(val < 0)
            speedLimit = -speedLimit;
        if(hasEncoders)
            target = (int)((val / WHEEL_SIZE) * GEAR_RATIO * orientation);
        runner.start();
    }
    public void operate(double val){
        operate(val, 1);
    }
    public void run(){
        motor.setPower(speedLimit);
        boolean notStopped = true;
        while(!inRange(target, motor.getCurrentPosition()) && notStopped){
            try{
                motor.wait(WAITRESOLUTION);
            }
            catch(InterruptedException e){
                this.stop();
                notStopped = false;
            }
        }
        this.stop();
    }
    protected boolean inRange(int target, int current){
        return (current > target-TOLERANCE) && (current < target+TOLERANCE);
    }
    public void waitForCompletion() throws InterruptedException{
        runner.join();
    }
    public DcMotor getMotor(){
        return motor;
    }
}
