package com.walnutHillsEagles.WalnutLibrary;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class TimedMotor extends LinearMotor implements Runnable{
    public static final int MSECSINSEC = 1000;
    public static final int NSECSINSEC = 1000000;
    private int numMSecs;
    private Thread runner;
    //Debuging variable
    private boolean hasStopped;
    private LinearOpMode myOp;



    public TimedMotor(DcMotor myMotor, String name,boolean encoderCheck, boolean isReversed){
        super(myMotor, name, encoderCheck,isReversed);
        numMSecs = 0;
        hasStopped = false;
        myOp = new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {
                return;
            }
        };
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
            myOp.sleep(numMSecs);
        }
        catch(InterruptedException e){
            //@TODO: Overkill?
            stopMotor();
            hasStopped=true;
            Thread.currentThread().interrupt();
        }
        finally {
            stopMotor();
            hasStopped=true;
        }
        stopMotor();
        hasStopped=true;
    }
    //Nasty Debugging stuff
    public boolean getStopped(){
        return hasStopped;
    }
    public void setStopped(boolean val){
        hasStopped=val;
    }
}
