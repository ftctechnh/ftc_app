package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

import java.util.Timer;

/**
 * Created by Dan on 11/12/2015.
 */

public class DucksAutonomous extends LinearOpMode{
    DcMotor left;
    DcMotor right;
    DcMotor winch;
    DcMotor winchpivot;
    DcMotor winchwheel;
    Timer timer=new Timer();
//    GyroSensor gyro;

//    public void turnLeft (int degrees, double speed){
//        int startHeading=gyro.getHeading();
//        int targetHeading=startHeading-degrees;
//        if(targetHeading<0){
//            targetHeading=targetHeading+360;
//        }
//        while(gyro.getHeading()<targetHeading-3 || gyro.getHeading()>targetHeading+3){
//            left.setPower(speed);
//            right.setPower(speed);
//        }
//        left.setPower(0);
//        right.setPower(0);
//    }
//
//    public void turnRight(int degrees,double speed){
//        int startHeading=gyro.getHeading();
//        int targetHeading=startHeading+degrees;
//        telemetry.addData("targetHeading",targetHeading);
//        if(targetHeading>360){
//            targetHeading=targetHeading-360;
//        }
//        while(gyro.getHeading()<targetHeading-3 || gyro.getHeading()>targetHeading+3){
//            printHeading();
//            telemetry.addData("targetHeading", targetHeading);
//            left.setPower(-speed);
//            right.setPower(-speed);
//        }
//        left.setPower(0);
//        right.setPower(0);
//    }
//    public void printHeading(){
//        telemetry.addData("currentHeading",gyro.getHeading());
//    }

    @Override
    public void runOpMode() throws InterruptedException{
        left= hardwareMap.dcMotor.get("left");
        right= hardwareMap.dcMotor.get("right");
        winch=hardwareMap.dcMotor.get("winch");
        winchpivot=hardwareMap.dcMotor.get("winchpivot");
        winchwheel=hardwareMap.dcMotor.get("winchwheel");
        waitForStart();

//        gyro=hardwareMap.gyroSensor.get("gyro");
//        gyro.calibrate();
//        while(gyro.isCalibrating()){
//            telemetry.addData("You are calibrating", "lmao");
//        }

//        winch.setPower(-1);
//        winchwheel.setPower(-1);
//        Thread.sleep(5000);

        winchwheel.setPower(.8);
        winch.setPower(1);
        Thread.sleep(5000);

        winchwheel.setPower(0);
        winch.setPower(0);
    }

}

