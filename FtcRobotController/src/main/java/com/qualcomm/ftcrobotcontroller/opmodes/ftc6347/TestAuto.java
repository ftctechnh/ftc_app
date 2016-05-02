package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc6347.DriveFunctions.DIRECTION;

/**
 * Created by FTCGearedUP on 2/15/2016.
 */
public class TestAuto extends LinearOpMode {

    //DriveFunctions df;
    DriveFunctions df;

    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotor motor6;
    DcMotorController winch;
    Servo climbers;
    Servo red;
    Servo blue;
    Servo arm1;
    UltrasonicSensor ultrasonicSensor;
    LightSensor reflectedLight;

    @Override
    public void runOpMode() throws InterruptedException {

        waitOneFullHardwareCycle();

        df = new DriveFunctions(0,hardwareMap,telemetry);

        /////////////////////////Motors////////////////////////////
       
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");

        winch = hardwareMap.dcMotorController.get("c1");

        /////////////////////////////Servos/////////////////////////////
        climbers = hardwareMap.servo.get("sr2");// 2
        red = hardwareMap.servo.get("sr3");// 3
        blue = hardwareMap.servo.get("sr1");// 1
        arm1 = hardwareMap.servo.get("sr5");// 5

        climbers.setPosition(1.0); //initialize arm
        red.setPosition(1); //initialize red arm
        blue.setPosition(0); //initialize blue arm
        arm1.setPosition(0.5); //initialize hang arm1, higher servo

        //////////////////////////////Sensors///////////////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("s4");

        ////////////////////////////Variables//////////////////////////

        waitForStart();

        df.drive(0, DIRECTION.STOP);

        sleep(50);

        df.drive(0, DIRECTION.FORWARD);

        sleep(50);

        df.drive(0, DIRECTION.STOP);


    }
}
