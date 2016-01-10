package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public abstract class _ResQAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor frontRightWheel;
        DcMotor frontLeftWheel;
        DcMotor backRightWheel;
        DcMotor backLeftWheel;
        DcMotor sweeper;
        Servo buttonServo;
        Servo button2Servo;
        Servo climberservo;
        Servo twistServo;
        Servo releaseServo;
        Servo hookServo;
        OpticalDistanceSensor opticalDistanceSensor;
        UltrasonicSensor ultrasonicSensor;
        ColorSensor colorsensor;

        double reflectance = 0;
        double BLACKVALUE = 0.01;
        double WHITEVALUE = 0.4;
        double REDVALUE = 0.3;
        double BLUEVALUE = 0.05;
        // double MARGIN = 0.03;//needs to be tuned

        String date;

        try
        {
            File file = new File("/sdcard/FIRST/calibration.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            date = br.readLine();
            REDVALUE = Double.parseDouble(br.readLine());
            BLUEVALUE = Double.parseDouble(br.readLine());
            WHITEVALUE = Double.parseDouble(br.readLine());
            BLACKVALUE = Double.parseDouble(br.readLine());
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        double EOPDThreshold = 0.4 * BLACKVALUE + 0.6* WHITEVALUE;


        double value;
        frontRightWheel = hardwareMap.dcMotor.get("frontR");
        frontLeftWheel = hardwareMap.dcMotor.get("frontL");
        backRightWheel = hardwareMap.dcMotor.get("backR");
        backLeftWheel = hardwareMap.dcMotor.get("backL");
        frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        backRightWheel.setDirection(DcMotor.Direction.FORWARD);
        sweeper = hardwareMap.dcMotor.get("sweeper");

        buttonServo = hardwareMap.servo.get("leftbutton");
        buttonServo.setPosition(0.9);
        button2Servo = hardwareMap.servo.get("rightbutton");
        button2Servo.setPosition(0.7);
        //climberservo = hardwareMap.servo;.get("climber");
        //climberservo.setPosition(0.0);
        twistServo = hardwareMap.servo.get("twist");
        twistServo.setPosition(1);
        releaseServo = hardwareMap.servo.get("release");
        releaseServo.setPosition(0);
        hookServo = hardwareMap.servo.get("signal");
        hookServo.setPosition(1);

        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("light");
        colorsensor = hardwareMap.colorSensor.get("color");
        colorsensor.enableLed(false);

        //Actual Stuff Starts Here
        waitForStart();
        //do we need delay
        telemetry.addData("InDelay", "yes");
        sleep(getDelay());
        telemetry.addData("InDelay", "no");

        if (getDelay() == 0)
            sleep(0);
            //sleep(3500);
        else //with delay (2nd start), robot is placed farther
            sleep(4500);

        sweeper.setPower(-0.8);
        frontRightWheel.setPower(0.3);
        frontLeftWheel.setPower(0.3);
        backRightWheel.setPower(0.3);
        backLeftWheel.setPower(0.3);
        sleep(3000);
        //sweeper.setPower(-1);


        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        backLeftWheel.setPower(0);

        //Drive forward until reach line
        while(true){
            reflectance = opticalDistanceSensor.getLightDetected();
            telemetry.addData("Reflectance Value", reflectance);

            if (Math.abs(reflectance - WHITEVALUE) < 0.05) { //found white tape
                frontRightWheel.setPower(0);
                frontLeftWheel.setPower(0);
                backRightWheel.setPower(0);
                backLeftWheel.setPower(0);
                sleep(200);
                if (getRedAlliance() == 1) {
                    /*frontRightWheel.setPower(-.25);
                    frontLeftWheel.setPower(-.25);
                    backRightWheel.setPower(-.25);
                    backLeftWheel.setPower(-.25);
                    sleep(450);
                    frontLeftWheel.setPower(0.9);
                    backLeftWheel.setPower(0.9);
                    frontRightWheel.setPower(-0.50);
                    backRightWheel.setPower(-0.50);
                    sleep(750);
                    */
                    /*frontLeftWheel.setPower(0.2);
                    backLeftWheel.setPower(0.2);
                    frontRightWheel.setPower(0.2);
                    backRightWheel.setPower(0.2);
                    sleep(200);*/
                } else {
                    //Overshoot to left side of line only as BLUE alliance
                    /*frontLeftWheel.setPower(0.1);
                    backLeftWheel.setPower(0.1);
                    frontRightWheel.setPower(0.1);
                    backRightWheel.setPower(0.1);
                    sleep(80);*/
                    frontLeftWheel.setPower(-0.3);
                    backLeftWheel.setPower(-0.3);
                    frontRightWheel.setPower(0.3);
                    backRightWheel.setPower(0.3);
                    sleep(50);

                }
                break;
            }

            frontLeftWheel.setPower(0.1);
            backLeftWheel.setPower(0.1);
            frontRightWheel.setPower(0.1);
            backRightWheel.setPower(0.1);
            waitForNextHardwareCycle();
        }
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        backLeftWheel.setPower(0);
        sleep(700);

        //follow the left edge of the line
        while(true) {
            waitOneFullHardwareCycle();
            sweeper.setPower(1);
            double distance = ultrasonicSensor.getUltrasonicLevel();
            reflectance = opticalDistanceSensor.getLightDetected();

            telemetry.addData("Current Status: Linefollower", "Current Status: Linefollower");
            //Line Follower

            double valueB;
            double valueS;

            value = reflectance - EOPDThreshold ;
            valueB = .07-0.5*value;
            valueS = .07+0.5*value;
            if (Math.abs(valueB) < 0.2)
                valueS = (Math.signum(valueS) * 0.2);

            valueS = Range.clip(valueS, -1, 1);
            valueB = Range.clip(valueB, -1, 1);
            if (getRedAlliance()==0) {
                frontLeftWheel.setPower(valueS);
                backLeftWheel.setPower(valueS);
                frontRightWheel.setPower(valueB);
                backRightWheel.setPower(valueB);
            } else {
                frontLeftWheel.setPower(valueB);
                backLeftWheel.setPower(valueB);
                frontRightWheel.setPower(valueS);
                backRightWheel.setPower(valueS);
            }

            telemetry.addData("valueB", valueB);
            telemetry.addData("valueC", valueS);
            telemetry.addData("Reflectance Value", reflectance);
            telemetry.addData("Ultrasonic Value", distance);

            //Wait until ultrasonic distance <24
            if(distance < 24 && distance>1) {
                if (getRedAlliance() == 0) {
                    /*frontLeftWheel.setPower(0.3);
                    backLeftWheel.setPower(0.3);*/
                    frontRightWheel.setPower(0);
                    backRightWheel.setPower(0);
                    //sleep(200);
                    frontLeftWheel.setPower(0);
                    backLeftWheel.setPower(0);
               }else{
                    frontRightWheel.setPower(0);
                    backRightWheel.setPower(0);
                    frontLeftWheel.setPower(0);
                    backLeftWheel.setPower(0);
                }
                break;
            }
        }

        colorsensor.enableLed(false);
        sleep(500);
        telemetry.addData("Red", colorsensor.red());
        telemetry.addData("Blue", colorsensor.blue());
        sleep(500);

        //Color Sensor Logic
        if(colorsensor.red()<0.1&&colorsensor.blue()>0.1){
            if (getRedAlliance() == 0){
                //button2Servo.setPosition(0.8);
                //sleep(1000);
                frontLeftWheel.setPower(-0.1);
                backLeftWheel.setPower(-0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(100);
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(350);
                frontLeftWheel.setPower(-0.2);
                backLeftWheel.setPower(-0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(350);
                frontLeftWheel.setPower(0);
                backLeftWheel.setPower(0);
                frontRightWheel.setPower(0);
                backRightWheel.setPower(0);
                sleep(350);
                button2Servo.setPosition(0);
                buttonServo.setPosition(0.7);
                sleep(1000);
            } else {
                button2Servo.setPosition(0);
                buttonServo.setPosition(0.4);
                sleep(1000);
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(400);
            }

        }else if (colorsensor.red() > 0.1 &&colorsensor.blue()<0.1){
            if (getRedAlliance() == 1){
                //buttonServo.setPosition(0.7);
                //sleep(1000);
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(400);
                frontLeftWheel.setPower(-0.2);
                backLeftWheel.setPower(-0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(350);
                frontLeftWheel.setPower(0);
                backLeftWheel.setPower(0);
                frontRightWheel.setPower(0);
                backRightWheel.setPower(0);
                sleep(500);
                button2Servo.setPosition(0);
                buttonServo.setPosition(0.3);
                sleep(1000);
            } else {
                frontLeftWheel.setPower(-0.1);
                backLeftWheel.setPower(-0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(100);
                button2Servo.setPosition(0);
                buttonServo.setPosition(0.7);
                sleep(1000);
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(400);

            }
        } else {

        }



        //Drive off to the side
        frontLeftWheel.setPower(-0.1);
        backLeftWheel.setPower(-0.1);
        frontRightWheel.setPower(-0.1);
        backRightWheel.setPower(-0.1);
        //button2Servo.setPosition(0.6);
        //buttonServo.setPosition(0.3);
        sleep(1100);

        //End of Autonomous
        if (getDelay() == 0) {
            frontLeftWheel.setPower(-0.2);
            backLeftWheel.setPower(-0.2);
            frontRightWheel.setPower(-0.2);
            backRightWheel.setPower(-0.2);
            sleep(300);
            if (getRedAlliance() == 1) {
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(1500);
                frontLeftWheel.setPower(0.3);
                backLeftWheel.setPower(0.3);
                frontRightWheel.setPower(0.3);
                backRightWheel.setPower(0.3);
                sleep(1450);
                frontRightWheel.setPower(0);
                frontLeftWheel.setPower(0);
                backRightWheel.setPower(0);
                backLeftWheel.setPower(0);
            } else {
                frontLeftWheel.setPower(-0.4);
                backLeftWheel.setPower(-0.4);
                frontRightWheel.setPower(0.4);
                backRightWheel.setPower(0.4);
                sleep(900);
                frontLeftWheel.setPower(0.3);
                backLeftWheel.setPower(0.3);
                frontRightWheel.setPower(0.3);
                backRightWheel.setPower(0.3);
                sleep(500);
                frontLeftWheel.setPower(0.3);
                backLeftWheel.setPower(0.3);
                frontRightWheel.setPower(0.3);
                backRightWheel.setPower(0.3);
                sleep(450);
                frontRightWheel.setPower(0);
                frontLeftWheel.setPower(0);
                backRightWheel.setPower(0);
                backLeftWheel.setPower(0);
                buttonServo.setPosition(0);
                button2Servo.setPosition(0.9);
            }
        }
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        backLeftWheel.setPower(0);
        sweeper.setPower(0);
    }


    abstract protected int getDelay();


    abstract protected int getRedAlliance();

}