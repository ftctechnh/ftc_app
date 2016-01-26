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
    //Debuging variable
    private LinearOpMode myOp;



    public TimedMotor(DcMotor myMotor, String name,boolean encoderCheck, boolean isReversed){
        super(myMotor, name, encoderCheck,isReversed);
        numMSecs = 0;
        myOp = new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {
                return;
            }
        };
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
        motor.setPower(speedLimit);
        try{
            myOp.sleep(numMSecs);
        }
        catch(InterruptedException e){
            //@TODO: Overkill?
            stop();
            Thread.currentThread().interrupt();
        }
        finally {
            stop();
        }
        stop();
    }
    public void waitForCompletion() throws InterruptedException{
        runner.join();
    }

}
