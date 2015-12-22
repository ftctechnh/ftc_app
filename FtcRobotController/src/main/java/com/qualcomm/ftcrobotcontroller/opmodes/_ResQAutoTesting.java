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


public class _ResQAutoTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor rightWheel;
        DcMotor leftWheel;
        DcMotor sweeper;
        Servo buttonServo;
        Servo button2Servo;
        Servo climberservo;
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

       double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);


        double value;
        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        leftWheel.setDirection(DcMotor.Direction.FORWARD);
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        sweeper = hardwareMap.dcMotor.get("sweeper");

        buttonServo = hardwareMap.servo.get("buttonservo");
        buttonServo.setPosition(0.9);
        button2Servo = hardwareMap.servo.get("button2servo");
        button2Servo.setPosition(0);
        climberservo = hardwareMap.servo.get("climberservo");
        climberservo.setPosition(0.0);

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        colorsensor = hardwareMap.colorSensor.get("colorsensor");
        colorsensor.enableLed(false);

        int getRedAlliance = 0;
        int getDelay=0;


        waitForStart();

        //do we need delay
        telemetry.addData("InDelay", "yes");
        sleep(getDelay);
        telemetry.addData("InDelay", "no");

        rightWheel.setPower(0.2);
        leftWheel.setPower(0.2);
        sweeper.setPower(-1);

        if (getDelay == 0)
            sleep(0);
            //sleep(3500);
        else //with delay (2nd start), robot is placed farther
            sleep(4500);
        rightWheel.setPower(0);
        leftWheel.setPower(0);

        while(true){
            reflectance = opticalDistanceSensor.getLightDetected();
            telemetry.addData("Reflectance Value", reflectance);

            if (Math.abs(reflectance - WHITEVALUE) < 0.05) { //found white tape
                leftWheel.setPower(0);
                rightWheel.setPower(0);
                sleep(200);
                if (getRedAlliance == 1) {
                    leftWheel.setPower(0.3);
                    rightWheel.setPower(-0.3);
                    sleep(3200);
                } else {
                    //Overshoot to left side of line only as BLUE alliance
                    leftWheel.setPower(0.1);
                    rightWheel.setPower(0.1);
                    sleep(800);
                    leftWheel.setPower(-0.3);
                    rightWheel.setPower(0.3);
                    sleep(700);

                }
                break;
            }

            leftWheel.setPower(0.2);
            rightWheel.setPower(0.2);
            waitForNextHardwareCycle();
        }
        leftWheel.setPower(0);
        rightWheel.setPower(0);
        sleep(700);
        //follow the left edge of the line

        while(true) {
            waitOneFullHardwareCycle();
            sweeper.setPower(1);
            double distance = ultrasonicSensor.getUltrasonicLevel();
            reflectance = opticalDistanceSensor.getLightDetected();

            telemetry.addData("Current Status: Linefollower", "Current Status: Linefollower");

            double valueB;
            double valueS;

                value = reflectance - EOPDThreshold ;
                valueB = .07-0.5*value;
                valueS = .07+0.5*value;
                if (Math.abs(valueB) < 0.2)
                    valueS = (Math.signum(valueS) * 0.2);

                valueS = Range.clip(valueS, -1, 1);
                valueB = Range.clip(valueB, -1, 1);
                leftWheel.setPower(valueS);
                rightWheel.setPower(valueB);

            telemetry.addData("valueB", valueB);
            telemetry.addData("valueC", valueS);
            telemetry.addData("Reflectance Value", reflectance);
            telemetry.addData("Ultrasonic Value", distance);


            if(distance < 30 && distance>1) {
                if (getRedAlliance == 0) {
                    leftWheel.setPower(0.3);
                    rightWheel.setPower(0);
                    sleep(200);
                    leftWheel.setPower(0);
                }
                break;
            }
        }

        colorsensor.enableLed(false);
        sleep(1500);
        telemetry.addData("Red", colorsensor.red());
        telemetry.addData("Blue", colorsensor.blue());
        if(colorsensor.red()<0.1&&colorsensor.blue()>0.1){
            if( getRedAlliance == 1){
                //If Alliance is red and the button is red
                //Servo Down
                buttonServo.setPosition(0.3);
                sleep(1000);

            } else if (getRedAlliance == 0){
                button2Servo.setPosition(0.8);
                sleep(1000);
            }

        }else if (colorsensor.red()>0.1&&colorsensor.blue()<0.1){
          /*  //go back
            leftWheel.setPower(-0.1);
            rightWheel.setPower(-0.1);
            sleep(300);*/

            if( getRedAlliance == 1){
                button2Servo.setPosition(0.6);
                sleep(1000);

            } else if (getRedAlliance == 0){
                buttonServo.setPosition(0.3);
                sleep(1000);
            }
        } else {
            button2Servo.setPosition(0.6);
            buttonServo.setPosition(0.3);
            sleep(1000);
        }

        leftWheel.setPower(0.1);
        rightWheel.setPower(0.1);
        sleep(200);

        //Dump climbers
        climberservo.setPosition(1);
        sleep(1500);
        climberservo.setPosition(0);

        leftWheel.setPower(-0.1);
        rightWheel.setPower(-0.1);
        sleep(800);

        //End of Autonomous
        if (getDelay == 0) {
            leftWheel.setPower(-0.2);
            rightWheel.setPower(-0.2);
            sleep(400);
            if (getRedAlliance == 1) {
                leftWheel.setPower(0.2);
                rightWheel.setPower(-0.2);
                sleep(500);
                leftWheel.setPower(0.3);
                leftWheel.setPower(0.3);
                sleep(700);
                leftWheel.setPower(0.2);
                rightWheel.setPower(-0.2);
                sleep(500);
                leftWheel.setPower(0.3);
                rightWheel.setPower(0.3);
                sleep(2000);
                leftWheel.setPower(0);
                rightWheel.setPower(0);
            } else {
                leftWheel.setPower(-0.4);
                rightWheel.setPower(0.4);
                sleep(1500);
                leftWheel.setPower(0.3);
                leftWheel.setPower(0.3);
                sleep(700);
                leftWheel.setPower(-0.4);
                rightWheel.setPower(0.4);
                sleep(700);
                leftWheel.setPower(0.3);
                rightWheel.setPower(0.3);
                sleep(1400);
                leftWheel.setPower(0);
                rightWheel.setPower(0);
            }
        }
    }




}