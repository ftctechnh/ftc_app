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

        DcMotor rightWheel;
        DcMotor leftWheel;
        DcMotor sweeper;
        Servo buttonServo;
        Servo button2Servo;
        Servo leftsweeper;
        Servo rightsweeper;
        Servo climberservo;
        OpticalDistanceSensor opticalDistanceSensor;
        UltrasonicSensor ultrasonicSensor;
        ColorSensor colorsensor;

        double reflectance = 0;
        //double TARGET_REFLECTANCE = 0.1;
        double BLACKVALUE = 0.01;
        double WHITEVALUE = 0.3;
        double REDVALUE = 0.2;
        double BLUEVALUE = 0.05;
       // double MARGIN = 0.03;//needs to be tuned

        double SECOND_SWEEP_DISTANCE = 40;
        double PUSH_BUTTON_DISTANCE = 20;


        double ultrasonicThreshold = 15;
        String date;

        //boolean var to control the action happens only once
        boolean firstSweep = false;
        boolean secondSweep = false;
        boolean thirdSweep = false;

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
            BLACKVALUE = 0.01;
            WHITEVALUE = 0.3;
            REDVALUE = 0.2;
            BLUEVALUE = 0.05;
        }

        double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);
        final double POWER = 0.3;
        final double BASEPOWER = 0.2;

        final double BUTTONSERVO_MIN_RANGE  = 0.01;
        final double BUTTONSERVO_MAX_RANGE  = 1;
        final double BUTTON2SERVO_MIN_RANGE  = 0.01;
        final double BUTTON2SERVO_MAX_RANGE  = 1;
        final double leftsweeper_MIN_RANGE  = 0.01;
        final double leftsweeper_MAX_RANGE  = 1;
        final double rightsweeper_MIN_RANGE  = 0.01;
        final double rightsweeper_MAX_RANGE  = 1;
        final double climberservo_MIN_RANGE  = 0.01;
        final double climberservo_MAX_RANGE  = 1;

        double buttonservoPosition;
        double button2servoPosition;
        double leftsweeperPosition;
        double rightsweeperPosition;
        double climberservoPosition;

        boolean sweep = false;

        double value;
        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        sweeper = hardwareMap.dcMotor.get("sweeper");

        buttonServo = hardwareMap.servo.get("buttonservo");
        buttonservoPosition = 0.9;
        buttonServo.setPosition(buttonservoPosition);
        button2Servo = hardwareMap.servo.get("button2servo");
        button2servoPosition = 0;
        button2Servo.setPosition(button2servoPosition);


        //leftsweeper = hardwareMap.servo.get("//leftsweeper");
        //leftsweeperPosition = 0.8;
        //leftsweeper.setPosition(//leftsweeperPosition);
        //rightsweeper = hardwareMap.servo.get("//rightsweeper");
        //rightsweeperPosition = 0.2;
        //rightsweeper.setPosition(//rightsweeperPosition);
        climberservo = hardwareMap.servo.get("climberservo");
        climberservoPosition = 0.0;
        climberservo.setPosition(climberservoPosition);

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        colorsensor = hardwareMap.colorSensor.get("colorsensor");
        colorsensor.enableLed(false);


        waitForStart();
      /* buttonServoPosition = 0.7;
       buttonServoPosition = Range.clip(buttonServoPosition, BUTTONSERVO_MIN_RANGE, BUTTONSERVO_MAX_RANGE);
        buttonServo.setPosition(buttonServoPosition);*/
        //rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        //rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //rightWheel.setTargetPosition(5000);

        //do we need delay
        telemetry.addData("InDelay", "yes");
        sleep(getDelay());
        telemetry.addData("InDelay", "no");

        /*
        rightWheel.setPower(0.5);

        leftWheel.setPower(0.5);
        if (getDelay() == 0)
            sleep(3500);
        else //with delay (2nd start), robot is placed farther
            sleep(4500);
        rightWheel.setPower(0);
        leftWheel.setPower(0);
        //telemetry.addData("Encoder Value", rightWheel.getCurrentPosition());


        if (!firstSweep) {
            //if (getRedAlliance() == 1 && Math.abs(reflectance - REDVALUE) < 0.03) { //found the red tape before white tape, perfect place to sweep the debris into scoring region
            if (getRedAlliance() == 1){
                leftWheel.setPower(0);
                rightWheel.setPower(0);
                //Sweeps the balls and boxes
                //rightsweeper.setPosition(1);
                sleep(700);
                //leftsweeper.setPosition(0);
                sleep(700);
                //rightsweeper.setPosition(0.2);
                sleep(300);
                //leftsweeper.setPosition(1);
                //sleep(500); //may need to move twice if too much debris accumulated
            //}else if (getRedAlliance() == 0 && Math.abs(reflectance - BLUEVALUE) < 0.03) { //found the blue tape before white tape, perfect place to sweep the debris into scoring region
            }else{
                leftWheel.setPower(0);
                rightWheel.setPower(0);
                //Sweeps the balls and boxes
                //leftsweeper.setPosition(0);
                sleep(700);
                //rightsweeper.setPosition(1);
                sleep(700);
                //leftsweeper.setPosition(1);
                sleep(300);
                //rightsweeper.setPosition(0.2);

                // sleep(500); //may need to move twice if too much debris accumulated
            }
            firstSweep = true;
        }
*/
        //This finds the white line
        //while(reflectance < (WHITEVALUE+REDVALUE)/2+0.02) {
/*
        while(true){
            reflectance = opticalDistanceSensor.getLightDetected();
            telemetry.addData("Reflectance Value", reflectance);

            if (Math.abs(reflectance - WHITEVALUE) < 0.05) { //found white tape
                leftWheel.setPower(0);
                rightWheel.setPower(0);
                sleep(200);
                if (getRedAlliance() == 1) {
                    //leftWheel.setPower(-0.1);
                    //rightWheel.setPower(-0.1);
                    //sleep(800);
                    leftWheel.setPower(0.3);
                    rightWheel.setPower(-0.3);
                    sleep(3200);
                } else {
                    //Overshoot to left side of line only as BLUE alliance
                    leftWheel.setPower(0.1);
                    rightWheel.setPower(0.1);
                    sleep(1500);
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

  */      while(true) {
            waitOneFullHardwareCycle();
            sweeper.setPower(1);
            double distance = ultrasonicSensor.getUltrasonicLevel();
            reflectance = opticalDistanceSensor.getLightDetected();

            telemetry.addData("linefollower", "linefollower");

            double valueB;
            double valueS;
            if (reflectance > EOPDThreshold) {
                value = reflectance - EOPDThreshold ;
                valueB = .1+1*value;
                valueS = .1-1*value;
                //valueB = (valueB)<1?valueB:1;
                //valueS=(valueS)<1?valueS:1;
                if (Math.abs(valueS) < 0.25)
                  valueS = (Math.signum(valueS) * 0.25);

                valueS = Range.clip(valueS, -1, 1);
                valueB = Range.clip(valueB, -1, 1);
                leftWheel.setPower(valueS);
                rightWheel.setPower(valueB);

            } else {
                value = EOPDThreshold - reflectance;
                valueB = .1+1*value;
                valueS = .1-1*value;
                if (Math.abs(valueS) < .25)
                  valueS = (Math.signum(valueS) * 0.25);

                valueS = Range.clip(valueS, -1, 1);
                valueB = Range.clip(valueB, -1, 1);
                rightWheel.setPower(valueS);
                leftWheel.setPower(valueB);
            }

            telemetry.addData("valueB", valueB);
            telemetry.addData("valueC", valueS);
            telemetry.addData("Reflectance Value", reflectance);
            telemetry.addData("Ultrasonic Value", distance);
/*
            if (!secondSweep && 34 > distance) {
                leftWheel.setPower(0);
                rightWheel.setPower(0);
                //Sweeps the balls and boxes
                if(getRedAlliance()==1) {
                    //Sweeps the balls and boxes
                    //leftsweeper.setPosition(0);
                    sleep(700);
                    //rightsweeper.setPosition(0.2);
                    sleep(700);
                    //leftsweeper.setPosition(1);
                    sleep(300);
                    //rightsweeper.setPosition(0);
                    ////rightsweeper.setPosition(0.2);
                } else {
                    //Sweeps the balls and boxes
                    //rightsweeper.setPosition(1);
                    sleep(700);
                    //leftsweeper.setPosition(0.8);
                    sleep(700);
                    //rightsweeper.setPosition(0.2);
                    sleep(300);
                    //leftsweeper.setPosition(1);
                    //leftWheel.setPower(-0.3);
                    //rightWheel.setPower(0.3);
                    //sleep(250);
                }
                secondSweep=true;
            }
            if (!thirdSweep && 28 > distance) {
                leftWheel.setPower(0);
                rightWheel.setPower(0);
                ////leftsweeper.setPosition(0);
               // sleep(700);
                ////rightsweeper.setPosition(1);
                //sleep(700);
                ////rightsweeper.setPosition(0.2);
                //sleep(300);
                ////leftsweeper.setPosition(1);
                thirdSweep = true;
            }*/
            if(distance < 18 && distance>1) {
                if (getRedAlliance() == 0) {
                    leftWheel.setPower(0.3);
                    rightWheel.setPower(0);
                    sleep(200);
                    leftWheel.setPower(0);
                    rightWheel.setPower(0);
                } else {

                }
                break;
            }
        }

        colorsensor.enableLed(false);
        sleep(500);
        telemetry.addData("Red", colorsensor.red());
        telemetry.addData("Blue", colorsensor.blue());
        if(colorsensor.red()<0.1&&colorsensor.blue()>0.1){
            if( getRedAlliance() == 1){
                //If Alliance is red and the button is red
                //Servo Down
                buttonServo.setPosition(0.3);
                sleep(1000);

            } else if (getRedAlliance() == 0){
                button2Servo.setPosition(0.8);
                sleep(1000);
            }

        }else if (colorsensor.red()>0.1&&colorsensor.blue()<0.1){
          /*  //go back
            leftWheel.setPower(-0.1);
            rightWheel.setPower(-0.1);
            sleep(300);*/

            if( getRedAlliance() == 1){
                button2Servo.setPosition(0.6);
                sleep(1000);

            } else if (getRedAlliance() == 0){
                buttonServo.setPosition(0.3);
                sleep(3000);
            }
        }

        leftWheel.setPower(0.1);
        rightWheel.setPower(0.1);
        sleep(200);

        //Dump climbers
        climberservo.setPosition(1);
        sleep(1500);
        climberservo.setPosition(0);

        leftWheel.setPower(0.1);
        rightWheel.setPower(0.1);
        sleep(1200);

        //End of Autonomous
        if (getDelay() == 0) {
            leftWheel.setPower(-0.3);
            rightWheel.setPower(-0.3);
            sleep(1000);
            if (getRedAlliance() == 1) {
                leftWheel.setPower(0.4);
                rightWheel.setPower(-0.4);
                sleep(500);
                leftWheel.setPower(0.3);
                leftWheel.setPower(0.3);
                sleep(1500);
                leftWheel.setPower(0.4);
                rightWheel.setPower(-0.4);
                sleep(500);
                leftWheel.setPower(0.3);
                rightWheel.setPower(0.3);
                sleep(2000);
                leftWheel.setPower(0);
                rightWheel.setPower(0);
            } else {
                leftWheel.setPower(-0.4);
                rightWheel.setPower(0.4);
                sleep(3000);
                leftWheel.setPower(0.3);
                leftWheel.setPower(0.3);
                sleep(1500);
                leftWheel.setPower(-0.4);
                rightWheel.setPower(0.4);
                sleep(1500);
                leftWheel.setPower(0.3);
                rightWheel.setPower(0.3);
                sleep(1500);
                leftWheel.setPower(0);
                rightWheel.setPower(0);
            }
        }
    }


    abstract protected int getDelay();


    abstract protected int getRedAlliance();

}