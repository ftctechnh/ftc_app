package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Yan Vologzhanin on 2/7/2016.
 */
public class SimpleMotor {
    //Field
    protected DcMotor motor;
    //Used for telemetry
    protected String name;
    protected boolean hasEncoders;
    
    //Timer used for each object
    protected LinearOpMode timer;
    public static final long WAITRESOLUTION = 200;
    public static final double MAXPOW = 0.9;
    SimpleMotor(DcMotor myMotor, String myName, boolean encoderCheck){
        motor = myMotor;
        name = myName;
        if(encoderCheck){
            hasEncoders = true;
            //@TODO Can this be handled better?
            try{
                resetEncoder();
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        else{
            hasEncoders = false;
            motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
        timer = new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {
                return;
            }
        };

    }
    public DcMotor getMotor(){
        return motor;
    }
    //@TODO Do I need this?
    public boolean checkEncoders(){
        return hasEncoders;
    }
    @Override
    public String toString(){
        return this.name;
    }
    public void setName(String myName){
        name = myName;
    }
    //Methods
    public void fullStop(){
        motor.setPower(0);
        if(hasEncoders){
            try{
                resetEncoder();
            }
            catch(InterruptedException e){
                motor.setPower(0);
                Thread.currentThread().interrupt();
            }

        }
    }
    public void stop(){
        motor.setPower(0);
    }
    public void setPower(double pow){
        //First statement should catch pow=0, but just to be sure...
        if(pow>=-1.0 * MAXPOW&&pow <=MAXPOW)
            motor.setPower(pow);
        else if(pow!=0)
            motor.setPower(MAXPOW * (pow/Math.abs(pow)));
        //val/|val| = 1 or -1, depending if val is negetive
    }
    protected void resetEncoder() throws InterruptedException {

        if (motor != null) {
            motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            while (motor.getCurrentPosition() != 0) {
                motor.wait(WAITRESOLUTION);
            }
            motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }


    }
    protected void sleep(long time) throws InterruptedException{
        motor.wait(WAITRESOLUTION);
    }
}
