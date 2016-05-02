package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FTCGearedUP on 8/6/2015.
 */
public class Super_Auto extends LinearOpMode {

    DcMotor motor1; //right
    DcMotor motor2; //left
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotor motor6;
    DcMotor motor7;
    DcMotorController winch;
    DcMotorController wheelie;
    Servo red;
    Servo blue;
    Servo arm1;
    Servo climberExtend;
    Servo climberDump;
    UltrasonicSensor ultrasonic4;
    UltrasonicSensor ultrasonic5;
    LightSensor reflectedLight;
    TouchSensor touch1;
    GyroSensor xAxis;
    GyroSensor zAxis;


    private void reset() throws InterruptedException{

        motor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);//reset encoders
        motor2.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        while (motor2.getCurrentPosition() != 0 && motor1.getCurrentPosition() != 0) {
            waitOneFullHardwareCycle(); //wait for encoders to reset
        }

    }

    void repeat() throws InterruptedException {

        int forward = 3000;
        int back = 750;

        motor1.setPower(0.25);//forward
        motor2.setPower(0.25);
        sleep(forward);

        motor1.setPower(0);//stop all
        motor2.setPower(0);

        motor1.setPower(-0.5);//back
        motor2.setPower(-0.5);
        sleep(back);

        motor1.setPower(0);//stop all
        motor2.setPower(0);
    }
    ///////////////////////////////////////////////////RUN///////////////////////////////////////////////
    @Override
    public void runOpMode() throws InterruptedException{

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");
        motor7 = hardwareMap.dcMotor.get("7");

        winch = hardwareMap.dcMotorController.get("c1"); //0
        wheelie = hardwareMap.dcMotorController.get("c2"); //1

        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor2.setDirection(DcMotor.Direction.FORWARD);
        motor7.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());

        motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos////////////////////////////
        red = hardwareMap.servo.get("sr3");// 3
        blue = hardwareMap.servo.get("sr1");// 1
        arm1 = hardwareMap.servo.get("sr6");// 6
        climberExtend = hardwareMap.servo.get("sr4");// 4
        climberDump = hardwareMap.servo.get("sr5"); // 5

        //////////////////////////////Sensors///////////////////////////
        //////////////Legacy/////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3");
        reflectedLight.enableLed(true);
        ultrasonic4 = hardwareMap.ultrasonicSensor.get("s4"); //s4
        ultrasonic5 = hardwareMap.ultrasonicSensor.get("s5"); //s5
        ////////////CDI////////////
        touch1 = hardwareMap.touchSensor.get("t1"); //D 0
        xAxis = hardwareMap.gyroSensor.get("g2");//I2c 1
        zAxis = hardwareMap.gyroSensor.get("g1");//I2c 0
        xAxis.calibrate(); //calibrate both sensors
        //zAxis.calibrate();

        ////////////////////////////Variables//////////////////////////


        /////////////////////////////Start//////////////////////////

        waitForStart();

        ////////////////////////Initialize Servos//////////////////////

        red.setPosition(0.91); //initialize red arm
        blue.setPosition(0.02); //initialize blue arm
        arm1.setPosition(0.85); //initialize hang arm1, higher servo
        climberExtend.setPosition(0.6); //initialize climber extender
        climberDump.setPosition(0.82); //initialize climbers to be facing up

        //repeat 2 times

        repeat();
        repeat();

        motor3.setPower(-0.5);//intake on, repeat 3 times

        repeat();
        repeat();
        repeat();

        motor3.setPower(0);//stop intake

        //repeat 4 times

        repeat();
        repeat();
        repeat();
        repeat();

        motor1.setPower(0);//stop all
        motor2.setPower(0);
        motor3.setPower(0);
        reset(); //reset encoders

    }
}

