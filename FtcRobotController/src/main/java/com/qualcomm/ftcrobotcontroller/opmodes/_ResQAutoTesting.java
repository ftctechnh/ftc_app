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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class _ResQAutoTesting extends LinearOpMode {

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
    Servo zipLineServo;

    OpticalDistanceSensor opticalDistanceSensor;
    UltrasonicSensor ultrasonicSensor;
    ColorSensor colorsensor;

    //Array for sensor values
    List<String> debugValues = new ArrayList<String>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss:SSS ");

    @Override
    public void runOpMode() throws InterruptedException {

        //No need to tune these unless calibration is broken.
        double reflectance = 0;
        double BLACKVALUE = 0.01;
        double WHITEVALUE = 0.4;
        double REDVALUE = 0.3;
        double BLUEVALUE = 0.05;
        // double MARGIN = 0.03;

        String date;
        //load calibration values
        try {
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
        if (getRedAlliance() == 0) {
            debugValues.add(formatter.format(new Date()) + " Blue ");
        } else {
            debugValues.add(formatter.format(new Date()) + " Red ");
        }
        double EOPDThreshold = 0.2 * BLACKVALUE + 0.8 * WHITEVALUE;
        debugValues.add(formatter.format(new Date()) + " EOPDThreshold:" + EOPDThreshold);

        //map the code to hardware map
        double value;
        frontRightWheel = hardwareMap.dcMotor.get("frontR");
        frontLeftWheel = hardwareMap.dcMotor.get("frontL");
        backRightWheel = hardwareMap.dcMotor.get("backR");
        backLeftWheel = hardwareMap.dcMotor.get("backL");
        frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        backRightWheel.setDirection(DcMotor.Direction.FORWARD);
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        /*
        sweeper = hardwareMap.dcMotor.get("sweeper");

        buttonServo = hardwareMap.servo.get("leftbutton");
        buttonServo.setPosition(0.9);
        button2Servo = hardwareMap.servo.get("rightbutton");
        button2Servo.setPosition(0.5);
        twistServo = hardwareMap.servo.get("twist");
        twistServo.setPosition(1);
        zipLineServo = hardwareMap.servo.get("zipline");
        zipLineServo.setPosition(1);
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
        */
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("light");
        // colorsensor = hardwareMap.colorSensor.get("color");
        // colorsensor.enableLed(false);

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

        debugValues.add(formatter.format(new Date()) + "Starting Run");
        //turn on sweeper, move forward
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        encoderDrive(4750, 0.3);
        frontLeftWheel.setPower(-0.3);
        backLeftWheel.setPower(-0.3);
        frontRightWheel.setPower(0.3);
        backRightWheel.setPower(0.3);
        sleep(450);
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        encoderDrive(4000, 0.3);

        //Drive forward until reach line
        //Slow down to 0.1
        while (true) {
            waitOneFullHardwareCycle();
            reflectance = opticalDistanceSensor.getLightDetected();
            telemetry.addData("Reflectance Value", reflectance);
            debugValues.add(formatter.format(new Date()) + "Reflectance Value: " + reflectance);

            if (Math.abs(reflectance - WHITEVALUE) < 0.03) { //found white tape
                debugValues.add(formatter.format(new Date()) + "Found white tape");
                frontRightWheel.setPower(0);
                frontLeftWheel.setPower(0);
                backRightWheel.setPower(0);
                backLeftWheel.setPower(0);
                sleep(300);
                if (getRedAlliance() == 1) {
                    frontLeftWheel.setPower(0.3);
                    backLeftWheel.setPower(0.3);
                    frontRightWheel.setPower(-0.3);
                    backRightWheel.setPower(-0.3);
                    sleep(50);
                } else {
                    frontLeftWheel.setPower(-0.3);
                    backLeftWheel.setPower(-0.3);
                    frontRightWheel.setPower(0.3);
                    backRightWheel.setPower(0.3);
                    sleep(50);

                }
                break;
            }
            //set speed here for driving to line
            frontLeftWheel.setPower(-0.1);
            backLeftWheel.setPower(-0.1);
            frontRightWheel.setPower(-0.1);
            backRightWheel.setPower(0.1);

        }

        //follow the left edge of the line
        Date lineFollowerStart = new Date();
        debugValues.add(formatter.format(lineFollowerStart) + "Entering Line Follower");
        while (true) {
            waitOneFullHardwareCycle();
            //  sweeper.setPower(1);
            // double distance = ultrasonicSensor.getUltrasonicLevel();
            reflectance = opticalDistanceSensor.getLightDetected();
            debugValues.add(formatter.format(new Date()) + "Reflectance:" + reflectance);
            // debugValues.add(formatter.format(new Date()) + "Distance:" + distance);

            telemetry.addData("Current Status: Linefollower", "Current Status: Linefollower");
            //Line Follower

            double valueB;
            double valueS;

            value = reflectance - EOPDThreshold;
            debugValues.add(formatter.format(new Date()) + "Correction Error:" + value);
            //Set values for line follower
            valueB = .12;
            if (value > 0) {
                valueS = .18;
            } else {
                valueS = -.18;
            }
            debugValues.add(formatter.format(new Date()) + "Correction values(after):" + valueS + "/" + valueB);
            if (getRedAlliance() == 0) {
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
            //telemetry.addData("Ultrasonic Value", distance);

            /*
            Date now = new Date();
            //Wait until ultrasonic distance <=22 and 3.5 seconds after line follower starts
            //Ignore the if/else
            if(distance <= 22 && distance > 1 && now.getTime() - lineFollowerStart.getTime() > 3500) {
                    frontRightWheel.setPower(0);
                    backRightWheel.setPower(0);
                    frontLeftWheel.setPower(0);
                    backLeftWheel.setPower(0);
                break;
            }
        }
        */
            debugValues.add(formatter.format(new Date()) + "Finish line follower");
          /*  colorsensor.enableLed(false);
            sleep(500);
            telemetry.addData("Red", colorsensor.red());
            telemetry.addData("Blue", colorsensor.blue());
            sleep(500);*/
        }
    }
    private void encoderDrive(int distance, double power) throws InterruptedException
    {
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        int startDistance = backRightWheel.getCurrentPosition();

        while (-1*(startDistance-backRightWheel.getCurrentPosition())<distance){
            telemetry.addData("Distance", -1*(startDistance-backRightWheel.getCurrentPosition()));
            frontRightWheel.setPower(power);
            frontLeftWheel.setPower(power);
            backRightWheel.setPower(power);
            backLeftWheel.setPower(power);
            waitOneFullHardwareCycle();
    }
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        backLeftWheel.setPower(0);
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    protected int getDelay(){
        return 0;
    }


    protected int getRedAlliance(){
        return 1;
    }

}