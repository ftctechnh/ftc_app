package com.walnutHillsEagles.WalnutLibrary;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    //Distance value used in operate
    private int distance;
    //Current value of encoders since it cannot restart
    private int currentPosition;
    //Create timer
    private LinearOpMode myOp;
    //Create Direction
    private boolean isForward;
    public DistanceMotor(DcMotor myMotor, String myName, boolean encoderCheck,boolean isReveresed,
                         double myDiameter,double myGearRatio, int myEncoder){
        super(myMotor, myName, encoderCheck,isReveresed);
        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        currentPosition = motor.getCurrentPosition();
        circumference = myDiameter*Math.PI;
        gearRatio = myGearRatio;
        encoderRot = myEncoder;
        distance = 0;
        speedLimit = 0;
        //Create for timer
        myOp = new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {
                return;
            }
        };
        isForward = true;
    }
    //Starts operation with given parameters
    public void operate(double inches, double mySpeedLimit){
        distance = (int)(inches * circumference * gearRatio)+currentPosition;
        isForward = true;
        if(inches<0)
            isForward=false;
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
        while((isForward&&checkForward())||(!isForward&&checkBackward())) {
            try {
                myOp.sleep(WAITRESOLUTION);
                motor.setPower(speedLimit);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            finally{
                currentPosition=motor.getCurrentPosition();
            }
        }
        //Keep if encoders have passed distance before continueing
        stopMotor();
        resetPoistion();
        //Reset Encoders NOTICE: Reset encoders is bugged
        //motor.setMode(DcMotorController.RunMode.valueOf("RESET_ENCODERS"));
    }
    public void resetPoistion(){
        motor.setTargetPosition(currentPosition);
    }
    private boolean checkForward(){
        return motor.getCurrentPosition()<distance;
    }


    private boolean checkBackward(){
        return motor.getCurrentPosition()>distance;
    }
}
