package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class TimedMotor extends LinearMotor implements Runnable, Auto {
    public static final int MSECSINSEC = 1000;
    public static final int NSECSINSEC = 1000000;
    private int numMSecs;
    private Thread runner;



    public TimedMotor(DcMotor myMotor, String name,boolean encoderCheck, boolean isReversed){
        super(myMotor, name, encoderCheck,isReversed);
        numMSecs = 0;
    }

    //Seconds precise up to three decimal places
    //@ENSURE(Seconds > 0)
    public void operate(double seconds, double mySpeedLimit){
        numMSecs = (int) (seconds*MSECSINSEC);
        speedLimit = mySpeedLimit * orientation;
        //Start parrallel Process
        runner = new Thread(this);
        runner.start();
    }
    public void operate(double seconds){
        if(seconds < 0)
            this.operate(seconds,-1);
        else
            this.operate(seconds, 1);
    }

    public void run(){
        this.setPower(speedLimit);
        try{
            timer.sleep(numMSecs);
        }
        catch(InterruptedException e){
            //@TODO: Overkill?
            fullStop();
            Thread.currentThread().interrupt();
        }
        finally {
            fullStop();
        }
        fullStop();
    }
    public void waitForCompletion() throws InterruptedException{
        runner.join();
    }

}
