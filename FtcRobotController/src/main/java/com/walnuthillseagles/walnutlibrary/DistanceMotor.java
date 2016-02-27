package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class DistanceMotor extends LinearMotor implements Runnable, Auto {
    //Constants
    //How often the code will check if its current operation is done

    //Precisions of wheels.
    public static final int RANGEVAL = 30;
    //Fields
    //Please measure in Inches
    private double circumference;
    private double gearRatio;
    //Distance value used in operate
    private int distance;
    //Parrallel Thread
    private Thread runner;
    public DistanceMotor(DcMotor myMotor, String myName, boolean encoderCheck,boolean isReveresed,
                         double myDiameter,double myGearRatio, int myEncoder){
        //Create Motor
        super(myMotor, myName, encoderCheck, isReveresed);
        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        //Values involving bot
        circumference = myDiameter*Math.PI;
        gearRatio = myGearRatio;
        encoderRot = myEncoder;

        //Values involving distances
        distance = 0;
        speedLimit = 0;

        runner = new Thread();
    }
    //Starts operation with given parameters
    public void operate(double inches, double mySpeedLimit){
        //@TODO Does this handle Going backwards?
        distance = (int)((inches / circumference / gearRatio) * encoderRot * orientation);
        speedLimit = mySpeedLimit;
        //Start new process
        runner = new Thread(this);
        runner.start();
    }
    public void operate(double inches){
        this.operate(inches, 1);
    }
    //Allows other methods to change speed midway through method
    public void changeSpeedLimit(double mySpeedLimit){
        speedLimit = mySpeedLimit;
    }
    public void run(){
        //Go for it
        motor.setTargetPosition(distance);
        motor.setPower(speedLimit);
        //Wait until in Pos
        //@TODO Better way to do this?
        while(!inRange(distance,motor.getCurrentPosition())) {
            try {
                motor.wait(WAITRESOLUTION);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        //Keep if encoders have passed distance before continueing
        //@TODO Should I just throw the exception up at this point?
        try{
            this.fullStop();
        } catch (InterruptedException e){
            this.stop();
            Thread.currentThread().interrupt();
        }

        //Reset Encoders NOTICE: Reset encoders is bugged
        //motor.setMode(DcMotorController.RunMode.valueOf("RESET_ENCODERS"));
    }
    //Timers
    public void waitForCompletion() throws InterruptedException{
        runner.join();
        motor.wait(WAITRESOLUTION);
    }
    //Private helper methods

}
