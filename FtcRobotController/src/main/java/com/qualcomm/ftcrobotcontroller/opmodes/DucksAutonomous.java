package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Dan on 11/12/2015.
 */

public class DucksAutonomous extends LinearOpMode{
    DcMotor left;
    DcMotor right;
    DcMotor winch;
    DcMotor winchpivot;
    DcMotor winchwheel;
    GyroSensor gyro;
    public DucksAutonomous(){

    }


    public void turnLeft (int degrees, double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading-degrees;
        if(targetHeading<0){
            targetHeading=targetHeading+360;
        }
        while(gyro.getHeading()<targetHeading-3 || gyro.getHeading()>targetHeading+3){
            left.setPower(-speed);
            right.setPower(-speed);
        }
        left.setPower(0);
        right.setPower(0);
    }

    public void turnRight (int degrees,double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading+degrees;
        telemetry.addData("targetHeading",targetHeading);
        if(targetHeading>360){
            targetHeading=targetHeading-360;
        }
        while(gyro.getHeading()<targetHeading-5 || gyro.getHeading()>targetHeading+5){
            printHeading();
            telemetry.addData("targetHeading", targetHeading);
            left.setPower(speed);
            right.setPower(speed);
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void printHeading(){
        telemetry.addData("currentHeading",gyro.getHeading());
    }
    @Override
    public void runOpMode() throws InterruptedException{
        left= hardwareMap.dcMotor.get("left");
        right= hardwareMap.dcMotor.get("right");
        winch=hardwareMap.dcMotor.get("winch");
        winchpivot=hardwareMap.dcMotor.get("winchpivot");
        winchwheel=hardwareMap.dcMotor.get("winchwheel");
        gyro=hardwareMap.gyroSensor.get("gyro");

        gyro.calibrate();
        while(gyro.isCalibrating()){
            telemetry.addData("lol","lol");
        }

        waitForStart();

        //go forward from starting position
        left.setPower(-.5);
        right.setPower(  .5);
        Thread.sleep(4000);
        left.setPower(0);
        right.setPower(0);

        turnLeft(45, .3);

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