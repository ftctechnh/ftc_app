package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 2/9/2016.
 */
public class EncoderMotor extends LinearMotor implements Runnable, Auto{
    private double circumference;
    private double gearRatio;
    private double encoderRot;
    private int distance;
    //Constants
    public static final int ENCODERTOLERANCE = 10;

    EncoderMotor(DcMotor myMotor, String myName,  boolean isReversed,
                 double myDiameter,double myGearRatio, int myEncoder){
        super(myMotor, myName, true, isReversed);
        circumference = myDiameter*Math.PI;
        gearRatio = myGearRatio;
        encoderRot = myEncoder;

        //Values involving distances
        distance = 0;
        speedLimit = 0;

        runner = new Thread();
    }
    public void operate(double inches, double mySpeedLimit){
        //@TODO Does this handle Going backwards?
        distance = (int)((inches / circumference / gearRatio) * encoderRot * orientation);
        speedLimit = mySpeedLimit;
        //Start new process
        runner = new Thread(this);
        runner.start();
    }
    public void operate(double inches){
        if(inches *orientation > 0)
            this.operate(inches, 1);
        else if(inches *orientation < 0)
            this.operate(inches, -1);
    }
    public void run(){
        motor.setPower(speedLimit);
        boolean canProcess = true;
        while(canProcess){
            try{
                if(Math.abs(motor.getCurrentPosition())>= Math.abs(distance)-ENCODERTOLERANCE){
                    //Includes Reset command
                    this.fullStop();
                    canProcess = false;
                }
                else
                    motor.wait(WAITRESOLUTION);
            }
            catch(InterruptedException e){
                canProcess= false;
                Thread.currentThread().interrupt();
            }

        }

    }
    public void waitForCompletion() throws InterruptedException{
        runner.join();
    }
    public void setPower(double pow){
        motor.setPower(pow * orientation);
    }

}
