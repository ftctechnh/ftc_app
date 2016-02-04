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
        waitOneFullHardwareCycle();
        winchwheelMC=hardwareMap.dcMotorController.get("winchwheelMC");
        leftsweepMC=hardwareMap.dcMotorController.get("leftsweepMC");
        rightpivotMC=hardwareMap.dcMotorController.get("rightpivotMC");
        gyro=hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        while(gyro.isCalibrating()){
            telemetry.addData("lol", "lol");
        }
        waitForStart();

        goForward(.5,2000);
        turnRight(45,.5);
        goForward(.5,3000);
        turnRight(45,.5);
        goForward(.3,10000);
    }
}