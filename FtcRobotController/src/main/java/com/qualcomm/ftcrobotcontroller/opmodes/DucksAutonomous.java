package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;

import java.util.Timer;

/**
 * Created by Dan on 11/12/2015.
 */

public class DucksAutonomous extends LinearOpMode{
    static int WINCH=1;
    static int WINCHPIVOT=2;
    static int WINCHWHEEL=1;
    static int RIGHT=2;
    static int LEFT=1;
    GyroSensor gyro;
    DcMotorController winchMC;
    DcMotorController driveMC;
    DcMotorController wheelMC;
    public DucksAutonomous(){

    }
    public void sleep(long MS){
        long start=System.currentTimeMillis();
        while(System.currentTimeMillis()<start+MS){
            telemetry.addData("waiting","");
        }
    }
    public void goForward(double speed, long MS)throws InterruptedException{
        long startTime=System.currentTimeMillis();
        int startHeading=gyro.getHeading();
        while(System.currentTimeMillis()<startTime+MS){
            int offset=gyro.getHeading()-startHeading;
                if(offset<-180){
                    offset=offset+360;
                }
                if(offset>180){
                    offset=offset-360;
                }
                telemetry.addData("offset",offset);
            if(Math.abs(offset)<=2 && driveMC.getMotorPower(RIGHT)!=speed) {
                driveMC.setMotorPower(RIGHT, speed);
                driveMC.setMotorPower(LEFT, -speed);
                telemetry.addData("offsettting",0);
            }
            if(2<Math.abs(offset) && Math.abs(offset)<=5){
                telemetry.addData("offsettting",1);
                if(offset<0 && driveMC.getMotorPower(RIGHT)!=speed-.5){
                    driveMC.setMotorPower(RIGHT, speed-.5);
                    driveMC.setMotorPower(LEFT,-speed);
                }
                if(offset>0 && driveMC.getMotorPower(LEFT)!=-speed+.5){
                    driveMC.setMotorPower(RIGHT, speed);
                    driveMC.setMotorPower(LEFT,-speed+.5);
                }
            }
            if(5<Math.abs(offset) && Math.abs(offset)<10){
                telemetry.addData("offsettting",2);
                if(offset<0 && driveMC.getMotorPower(RIGHT)!=speed-.7){
                    driveMC.setMotorPower(RIGHT, speed-.7);
                    driveMC.setMotorPower(LEFT,-speed);
                }
                if(offset>0 && driveMC.getMotorPower(LEFT)!=-speed+.7){
                    driveMC.setMotorPower(RIGHT, speed);
                    driveMC.setMotorPower(LEFT,-speed+.7);
                }
            }
            if(Math.abs(offset)>=10){
                telemetry.addData("offsettting",3);
                if(offset<0 && driveMC.getMotorPower(RIGHT)!=speed-.8){
                    driveMC.setMotorPower(RIGHT, speed-.8);
                    driveMC.setMotorPower(LEFT,-speed);
                }
                if(offset>0 && driveMC.getMotorPower(LEFT)!=-speed+.8){
                    driveMC.setMotorPower(RIGHT, speed);
                    driveMC.setMotorPower(LEFT,-speed+.8);
                }
            }
        }
        driveMC.setMotorPower(LEFT, 0);
        driveMC.setMotorPower(RIGHT, 0);
    }

    public void turnLeft (int degrees, double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading-degrees;
        if(targetHeading<0){
            targetHeading=targetHeading+360;
        }
        while(gyro.getHeading()<targetHeading-2 || gyro.getHeading()>targetHeading+2){
            if(driveMC.getMotorPower(LEFT)!=speed) {
                driveMC.setMotorPower(LEFT, speed);
                driveMC.setMotorPower(RIGHT, speed);
            }
            telemetry.addData("currentheading", gyro.getHeading());
            telemetry.addData("targetHeading", targetHeading);
        }
        driveMC.setMotorPower(LEFT, 0);
        driveMC.setMotorPower(RIGHT, 0);
    }

    public void turnRight (int degrees,double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading+degrees;
        if(targetHeading>360){
            targetHeading=targetHeading-360;
        }
        while(gyro.getHeading()<targetHeading-2 || gyro.getHeading()>targetHeading+2){
            if(driveMC.getMotorPower(LEFT)!=-speed) {
                driveMC.setMotorPower(LEFT, -speed);
                driveMC.setMotorPower(RIGHT, -speed);
            }
            telemetry.addData("currentheading", gyro.getHeading());
            telemetry.addData("targetHeading", targetHeading);
        }
        driveMC.setMotorPower(LEFT,0);
        driveMC.setMotorPower(RIGHT, 0);
    }
    @Override
    public void runOpMode() throws InterruptedException{
        driveMC=hardwareMap.dcMotorController.get("driveMC");
        winchMC=hardwareMap.dcMotorController.get("winchMC");
        wheelMC=hardwareMap.dcMotorController.get("wheelMC");
        gyro=hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        while(gyro.isCalibrating()){
            telemetry.addData("lol", "lol");
        }

        waitForStart();

        //assorted commands for testing gyro
        sleep(500);
        turnRight(90,.5);
        sleep(500);
        turnLeft(90,.5);
        sleep(500);
        goForward(.8, 1000);
        sleep(500);
        turnRight(180,1);
        sleep(500);
        goForward(.8,1000);
        sleep(500);
        turnLeft(180,1);
        driveMC.setMotorPower(LEFT, 0);
        driveMC.setMotorPower(RIGHT, 0);
//        goForward(1,1000);

//        //go forward after turning
//        left.setPower(.5);
//        right.setPower(-.5);
//        Thread.sleep(1000);
//        left.setPower(0);
//        right.setPower(0);
//
//        turnLeft(75,.3);
//
//        //go forwards onto mountatin
//        left.setPower(.6);
//        right.setPower(-.6);
//        Thread.sleep(2000);
//        left.setPower(0);
//        right.setPower(0);


    }
}