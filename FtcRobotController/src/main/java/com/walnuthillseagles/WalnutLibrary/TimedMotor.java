package com.walnutHillsEagles.WalnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class TimedMotor extends LinearMotor implements Runnable{
    public static final int MSECSINSEC = 1000;
    public static final int NSECSINSEC = 1000000;
    private int numMSecs;
    private Thread runner;

    public TimedMotor(DcMotor myMotor, String name,boolean encoderCheck, boolean isReversed){
        super(myMotor, name, encoderCheck,isReversed);
        numMSecs = 0;
    }

    //Seconds precise up to three decimal places
    public void operate(double seconds, double mySpeedLimit){
        numMSecs = (int) (seconds*MSECSINSEC);
        speedLimit = mySpeedLimit * orientation;
        //Start parrallel Process
        runner = new Thread(this);
        runner.start();
    }

    public void run(){
        motor.setPower(speedLimit);
        try{
            Thread.sleep(numMSecs);
        }
        catch(InterruptedException e){
            //@TODO: Overkill?
            stopMotor();
            Thread.currentThread().interrupt();
        }
        finally {
            stopMotor();
        }
    }
}
