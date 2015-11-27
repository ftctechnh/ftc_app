package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;

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

    public void goForward(double speed, long MS) throws InterruptedException{
        driveMC.setMotorPower(RIGHT,speed);
        driveMC.setMotorPower(LEFT,-speed);
        Thread.sleep(MS);
        driveMC.setMotorPower(LEFT, 0);
        driveMC.setMotorPower(RIGHT,0);
        Thread.sleep(500);
    }

    public void turnLeft (int degrees, double speed) throws InterruptedException{
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading-degrees;
        if(targetHeading<0){
            targetHeading=targetHeading+360;
        }
        while(gyro.getHeading()<targetHeading-3 || gyro.getHeading()>targetHeading+3){
            driveMC.setMotorPower(LEFT, speed);
            driveMC.setMotorPower(RIGHT,speed);
            telemetry.addData("currentheading", gyro.getHeading());
            telemetry.addData("targetHeading",targetHeading);
        }
        driveMC.setMotorPower(LEFT, 0);
        driveMC.setMotorPower(RIGHT, 0);
        Thread.sleep(500);
    }

    public void turnRight (int degrees,double speed) throws InterruptedException{
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading+degrees;
        if(targetHeading>360){
            targetHeading=targetHeading-360;
        }
        while(gyro.getHeading()<targetHeading-5 || gyro.getHeading()>targetHeading+5){
            printHeading();
            telemetry.addData("currentheading", gyro.getHeading());
            telemetry.addData("targetHeading",targetHeading);
            driveMC.setMotorPower(LEFT, -speed);
            driveMC.setMotorPower(RIGHT, -speed);
        }
        driveMC.setMotorPower(LEFT,0);
        driveMC.setMotorPower(RIGHT,0);
        Thread.sleep(500);
    }
    public void printHeading(){
        telemetry.addData("currentHeading",gyro.getHeading());
    }
    @Override
    public void runOpMode() throws InterruptedException{
        driveMC=hardwareMap.dcMotorController.get("driveMC");
        winchMC=hardwareMap.dcMotorController.get("winchMC");
        wheelMC=hardwareMap.dcMotorController.get("wheelMC");
        gyro=hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        while(gyro.isCalibrating()){
            telemetry.addData("lol","lol");
        }

        waitForStart();

        //go forward from starting position
        goForward(1, 1000);
        turnLeft(90, 1);

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