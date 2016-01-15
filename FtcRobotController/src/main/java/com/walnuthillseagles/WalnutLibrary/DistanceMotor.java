package com.walnuthillseagles.WalnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class DistanceMotor extends LinearMotor implements Runnable{
    //How often the code will check if its current operation is done
    public static final long WAITRESOLUTION = 100;
    //Please measure in Inches
    private double circumference;
    private double gearRatio;
    private int distance;
    public DistanceMotor(DcMotor myMotor, String myName, boolean encoderCheck,boolean isReveresed,
                         double myDiameter,double myGearRatio, int myEncoder){
        super(myMotor, myName, encoderCheck,isReveresed);
        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        circumference = myDiameter*Math.PI;
        gearRatio = myGearRatio;
        encoderRot = myEncoder;
        distance = 0;
        speedLimit = 0;
    }
    //Starts operation with given parameters
    public void operate(double inches, double mySpeedLimit){
        distance = (int)(inches * circumference * gearRatio);
        speedLimit = mySpeedLimit*orientation;
        motor.setTargetPosition(distance);
        motor.setPower(speedLimit);
        new Thread(this).start();
    }
    //Allows other methods to change speed midway through method
    public void changeSpeedLimit(double mySpeedLimit){
        speedLimit = mySpeedLimit;
    }
    public void run(){
        //Keep if encoders have passed distance before continueing
        while(motor.getCurrentPosition()<distance){
            try{
                Thread.sleep(WAITRESOLUTION);
                motor.setPower(speedLimit);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        stopMotor();
        resetPoistion();
        //Reset Encoders
        motor.setMode(DcMotorController.RunMode.valueOf("RESET_ENCODERS"));
    }
    public void resetPoistion(){
        motor.setTargetPosition(0);
    }
}
