package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.GyroSensor;


/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
@Deprecated
public class GyroDrive implements Runnable{
    public static final long WAITRESOLUTION = 300;
    //Hare
    private GyroSensor gyro;
    private EncoderMotor left;
    private EncoderMotor right;
    private Thread runner;
    private int targetHeading;
    //Constatns that allow you to modify behaviors
    //Value used when motors need to be realigned
    public static final double MOTORADJUSTMENTPOW = 0.8;
    public static final int GYROTOLERANCE = 3;
    public static final double MAX_SPEED = 0.95;

    public GyroDrive(EncoderMotor myLeft, EncoderMotor myRight, GyroSensor myGyro){
        left = myLeft;
        right = myRight;
        gyro = myGyro;
        runner = new Thread(this);
        targetHeading = 0;
        gyro.calibrate();
        while(gyro.isCalibrating()){
            try{
                Thread.sleep(WAITRESOLUTION);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
    public void LinearDrive(double inches){
        left.operate(inches);
        right.operate(inches);
    }
    public void tankTurn(int degrees){
        double turnOrientation = (degrees < 0) ? -1: 1;
        int tempHeading = degrees;
        int currentPos = gyro.getHeading();
        while(currentPos +tempHeading >= 360){
            tempHeading -= 360;
        }
        while(currentPos + tempHeading < 0){
            tempHeading += 360;
        }
        targetHeading = currentPos + tempHeading;
        left.setPower(MAX_SPEED * turnOrientation);
        right.setPower(-MAX_SPEED * turnOrientation);
        runner = new Thread(this);
        runner.start();
    }
    public void waitForCompletion() throws InterruptedException{
        left.waitForCompletion();
        right.waitForCompletion();
        runner.join();
    }
    public void run(){
        boolean canProcess = true;
        while(!(Math.abs(gyro.getHeading()-targetHeading)>=GYROTOLERANCE) && canProcess){
            try{
                left.sleep(WAITRESOLUTION);
            }
            catch(InterruptedException e){
                left.stop();
                right.stop();
                canProcess = false;
                Thread.currentThread().interrupt();
            }
        }
        left.stop();
        right.stop();
        try{
            left.resetEncoder();
            right.resetEncoder();
        }
        catch(InterruptedException e){
            Thread.currentThread();
        }

    }
    //Stuff I worry about
}
