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

public class DucksAutonomous extends AutonomousCommands{
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