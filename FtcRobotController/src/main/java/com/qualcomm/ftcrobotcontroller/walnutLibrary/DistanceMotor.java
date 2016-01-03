package com.qualcomm.ftcrobotcontroller.walnutLibrary;

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
    public DistanceMotor(DcMotor myMotor, String myName, boolean encoderCheck,boolean isReveresed,
                         double myDiameter,double myGearRatio, int myEncoder){
        super(myMotor, myName, encoderCheck,isReveresed);
        motor.setMode(DcMotorController.RunMode.valueOf("RUN_TO_POSITION"));
        circumference = myDiameter*Math.PI;
        gearRatio = myGearRatio;
        encoderRot = myEncoder;
    }
    public void operate(double inches, double mySpeedLimit){
        int distance = (int)(inches * circumference * gearRatio);
        speedLimit = mySpeedLimit*orientation;
        motor.setTargetPosition(distance);
        motor.setPower(speedLimit);
        new Thread(this).start();
    }
    public void run(){
        //Keep checking if the motor is busy before resseting encoders
        while(motor.isBusy()){
            try{
                Thread.sleep(WAITRESOLUTION);
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
