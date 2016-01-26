package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class DistanceMotor extends LinearMotor implements Runnable, Auto {
    //Constants
    //How often the code will check if its current operation is done
    public static final long WAITRESOLUTION = 100;
    //Precisions of wheels. Smaller = better/ less consistant
    public static final int RANGEVAL = 30;
    //Please measure in Inches
    private double circumference;
    private double gearRatio;
    //Distance value used in operate
    private int distance;
    //Current value of encoders since it cannot restart
    private int currentPosition;
    //Create timer
    private LinearOpMode myOp;
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
        currentPosition = motor.getCurrentPosition();
        distance = 0;
        speedLimit = 0;
        //Create timer
        myOp = new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {
                return;
            }
        };
        runner = new Thread();
    }
    //Starts operation with given parameters
    public void operate(double inches, double mySpeedLimit){
        distance = (int)(inches * circumference * gearRatio)+ currentPosition;
        speedLimit = mySpeedLimit*orientation;
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
                myOp.sleep(WAITRESOLUTION);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            finally{
                currentPosition=motor.getCurrentPosition();
            }
        }
        //Keep if encoders have passed distance before continueing
        stop();
        //Reset Encoders NOTICE: Reset encoders is bugged
        //motor.setMode(DcMotorController.RunMode.valueOf("RESET_ENCODERS"));
    }
    @Override
    public void stop(){
        motor.setPower(0);
        resetPoistion();
    }
    //Timers
    public void waitForCompletion() throws InterruptedException{
        runner.join();
    }
    //Private helper methods
    private void resetPoistion(){
        currentPosition=motor.getCurrentPosition();
        motor.setTargetPosition(currentPosition);
    }
    private boolean inRange(int target, int current){
        return (current > target-RANGEVAL) && (current < target+RANGEVAL);
    }
}
