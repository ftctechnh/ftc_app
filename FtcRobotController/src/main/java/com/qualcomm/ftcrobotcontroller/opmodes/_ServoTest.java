package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//TODO: Detect red/blue line, change ODS boundary DONE, but need calibration
//TODO: Get sweeper working
//TODO: Get button pushing working DONE
//TODO: do dumping
//TODO: Tune get out of the way?
public  class _ServoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor sweeper;
        sweeper = hardwareMap.dcMotor.get("sweeper");

        waitForStart();

        sweeper.setPower(-1);
        sleep(5000);


    }





}