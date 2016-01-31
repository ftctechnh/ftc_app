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


public abstract class _ResQAuto extends LinearOpMode {

    //Array for sensor values
    List<String> debugValues = new ArrayList<String>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss:SSS ");

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
        Servo zipLineServo;

        OpticalDistanceSensor opticalDistanceSensor;
        UltrasonicSensor ultrasonicSensor;
        ColorSensor colorsensor;

        //No need to tune these unless calibration is broken.
        double reflectance = 0;
        double BLACKVALUE = 0.01;
        double WHITEVALUE = 0.4;
        double REDVALUE = 0.3;
        double BLUEVALUE = 0.05;
        // double MARGIN = 0.03;

        String date;
        //load calibration values
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
        if (getRedAlliance()==0) {
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
        sweeper = hardwareMap.dcMotor.get("sweeper");

        buttonServo = hardwareMap.servo.get("leftbutton");
        buttonServo.setPosition(0.9);
        button2Servo = hardwareMap.servo.get("rightbutton");
        button2Servo.setPosition(0.5);
        //climberservo = hardwareMap.servo;.get("climber");
        //climberservo.setPosition(0.0);
        twistServo = hardwareMap.servo.get("twist");
        twistServo.setPosition(1);
        //releaseServo = hardwareMap.servo.get("release");
        //releaseServo.setPosition(0);
        //hookServo = hardwareMap.servo.get("signal");
        //hookServo.setPosition(1);
        zipLineServo = hardwareMap.servo.get("zipline");
        zipLineServo.setPosition(1);

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

        debugValues.add(formatter.format(new Date()) + "Starting Run");
        //turn on sweeper, move forward
        //Goes quickly at 0.3 for 3 seconds, then goes slower
       //sweeper.setPower(-0.8);
        frontRightWheel.setPower(0.1);
        frontLeftWheel.setPower(0.1);
        backRightWheel.setPower(0.1);
        backLeftWheel.setPower(0.1);
        sleep(3000);
        sweeper.setPower(-1);


        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        backLeftWheel.setPower(0);

        //Drive forward until reach line
        //Slow down to 0.1
        while(true){
            reflectance = opticalDistanceSensor.getLightDetected();
            telemetry.addData("Reflectance Value", reflectance);
            debugValues.add(formatter.format(new Date()) + "Reflectance Value: " + reflectance);

            if (Math.abs(reflectance - WHITEVALUE) < 0.03) { //found white tape
                debugValues.add(formatter.format(new Date()) + "Found white tape");
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
            //set speed here for driving to line
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
        Date lineFollowerStart = new Date();
        debugValues.add(formatter.format(lineFollowerStart) + "Entering Line Follower");
        while(true) {
            waitOneFullHardwareCycle();
            sweeper.setPower(1);
            double distance = ultrasonicSensor.getUltrasonicLevel();
            reflectance = opticalDistanceSensor.getLightDetected();
            debugValues.add(formatter.format(new Date()) + "Reflectance:" + reflectance);
            debugValues.add(formatter.format(new Date()) + "Distance:" + distance);

            telemetry.addData("Current Status: Linefollower", "Current Status: Linefollower");
            //Line Follower

            double valueB;
            double valueS;

            value = reflectance - EOPDThreshold ;
            debugValues.add(formatter.format(new Date()) + "Correction Error:" + value);
            /*valueB = .07-0.5*value;
            valueS = .07+0.5*value;
            debugValues.add(formatter.format(new Date()) + "Correction values(before):" + valueS + "/" + valueB);
            if (Math.abs(valueS) < 0.2)
                valueS = (Math.signum(valueS) * 0.2);

            valueS = Range.clip(valueS, -1, 1);
            valueB = Range.clip(valueB, -1, 1);*/
            //Set values for line follower
            valueB = .12;
            if (value > 0) {
                valueS = .18;
            } else {
                valueS = -.18;
            }
            debugValues.add(formatter.format(new Date()) + "Correction values(after):" + valueS + "/" + valueB);
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

            Date now = new Date();
            //Wait until ultrasonic distance <=22 and 3.5 seconds after line follower starts
            //Ignore the if/else
            if(distance <= 22 && distance > 1 && now.getTime() - lineFollowerStart.getTime() > 3500) {
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
        debugValues.add(formatter.format(new Date()) + "Finish line follower");
        colorsensor.enableLed(false);
        sleep(500);
        telemetry.addData("Red", colorsensor.red());
        telemetry.addData("Blue", colorsensor.blue());
        sleep(500);

        //Color Sensor Logic
        //For detect blue
        //if(colorsensor.red()<0.1&&colorsensor.blue()>0.1){
        if(colorsensor.red()<colorsensor.blue()) {
            debugValues.add(formatter.format(new Date()) + "Detect Blue Value" + colorsensor.blue() + ", Red" + colorsensor.red());
            if (getRedAlliance() == 0) {
                //forward
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(650);
                //backward
                frontLeftWheel.setPower(-0.2);
                backLeftWheel.setPower(-0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(350);
                //tilt to the right
                frontLeftWheel.setPower(-0.1);
                backLeftWheel.setPower(-0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(400);
                //stop
                frontLeftWheel.setPower(0);
                backLeftWheel.setPower(0);
                frontRightWheel.setPower(0);
                backRightWheel.setPower(0);
                sleep(350);
                //forward
                frontLeftWheel.setPower(0.1);
                backLeftWheel.setPower(0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(100);
                //dump climbers
                button2Servo.setPosition(0.05);
                buttonServo.setPosition(0.7);
                sleep(1500);
                //forward
                frontLeftWheel.setPower(0.1);
                backLeftWheel.setPower(0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(300);
            } else {
                //go backward
                frontLeftWheel.setPower(-0.2);
                backLeftWheel.setPower(-0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(100);
                //stop
                frontLeftWheel.setPower(0);
                backLeftWheel.setPower(0);
                frontRightWheel.setPower(0);
                backRightWheel.setPower(0);
                //Tilt robot to the right
                frontLeftWheel.setPower
                        (-0.1);
                backLeftWheel.setPower(-0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(450);
                //servoMove(button2Servo, 0.7, 0.05, -0.1, 200);
                //position servo
                button2Servo.setPosition(0.05);
                buttonServo.setPosition(0.5);
                sleep(800);
                //go forward
                frontLeftWheel.setPower(0.15);
                backLeftWheel.setPower(0.15);
                frontRightWheel.setPower(0.15);
                backRightWheel.setPower(0.15);
                sleep(500);
            }
            //If detect red
            //  }else if (colorsensor.red() > 0.1 &&colorsensor.blue()<0.1){
        } else if (colorsensor.red()>colorsensor.blue()) {
            debugValues.add(formatter.format(new Date()) + "Detect Red Value" + colorsensor.red() + ", Blue" + colorsensor.blue());
            if (getRedAlliance() == 1){
                //forward
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(400);
                //backwards
                frontLeftWheel.setPower(-0.2);
                backLeftWheel.setPower(-0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(350);
                //stop
                frontLeftWheel.setPower(0);
                backLeftWheel.setPower(0);
                frontRightWheel.setPower(0);
                backRightWheel.setPower(0);
                sleep(500);
                //activate servos
                button2Servo.setPosition(0.05);
                sleep(800);
                //forward
                frontLeftWheel.setPower(0.2);
                backLeftWheel.setPower(0.2);
                frontRightWheel.setPower(0.2);
                backRightWheel.setPower(0.2);
                sleep(240);
            } else {
                //Tilt robot to the right
                frontLeftWheel.setPower(-0.1);
                backLeftWheel.setPower(-0.1);
                frontRightWheel.setPower(0.1);
                backRightWheel.setPower(0.1);
                sleep(300);
                //backward
                frontLeftWheel.setPower(-0.2);
                backLeftWheel.setPower(-0.2);
                frontRightWheel.setPower(-0.2);
                backRightWheel.setPower(-0.2);
                sleep(200);
                //stop
                frontLeftWheel.setPower(0);
                backLeftWheel.setPower(0);
                frontRightWheel.setPower(0);
                backRightWheel.setPower(0);
                //set servo
                buttonServo.setPosition(0.8);
                button2Servo.setPosition(0);
                sleep(1600);
                //forward
                frontLeftWheel.setPower(0.15);
                backLeftWheel.setPower(0.15);
                frontRightWheel.setPower(0.15);
                backRightWheel.setPower(0.15);
                sleep(450);

            }
        } else {
            debugValues.add(formatter.format(new Date()) + "None Detected, Red" + colorsensor.red() + ", Blue" + colorsensor.blue());
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
        debugValues.add(formatter.format(new Date()) + "End of Autonomous, Navigation start");
        if (getDelay() == 0) {
            //move to the side only if first
            //go back
            frontLeftWheel.setPower(-0.2);
            backLeftWheel.setPower(-0.2);
            frontRightWheel.setPower(-0.2);
            backRightWheel.setPower(-0.2);
            sleep(300);
            if (getRedAlliance() == 1) {
                //turn left
                frontLeftWheel.setPower(0.4);
                backLeftWheel.setPower(0.4);
                frontRightWheel.setPower(-0.4);
                backRightWheel.setPower(-0.4);
                sleep(1000);
                //forward
                frontLeftWheel.setPower(0.3);
                backLeftWheel.setPower(0.3);
                frontRightWheel.setPower(0.3);
                backRightWheel.setPower(0.3);
                sleep(1550);
                //stop
                frontRightWheel.setPower(0);
                frontLeftWheel.setPower(0);
                backRightWheel.setPower(0);
                backLeftWheel.setPower(0);
            } else {
                //Turn right(on blue alliance)
                frontLeftWheel.setPower(-0.4);
                backLeftWheel.setPower(-0.4);
                frontRightWheel.setPower(0.4);
                backRightWheel.setPower(0.4);
                sleep(900);
                //forward
                frontLeftWheel.setPower(0.3);
                backLeftWheel.setPower(0.3);
                frontRightWheel.setPower(0.3);
                backRightWheel.setPower(0.3);
                sleep(500);
                //forward more
                frontLeftWheel.setPower(0.3);
                backLeftWheel.setPower(0.3);
                frontRightWheel.setPower(0.3);
                backRightWheel.setPower(0.3);
                sleep(450);
                //stop
                frontRightWheel.setPower(0);
                frontLeftWheel.setPower(0);
                backRightWheel.setPower(0);
                backLeftWheel.setPower(0);
                //buttonServo.setPosition(0);
               // button2Servo.setPosition(0.9);
            }
        }
        //stop
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        backLeftWheel.setPower(0);
        sweeper.setPower(0);
        debugValues.add(formatter.format(new Date()) + "Autonomous Completed");

        //Write sensor values to file
        try {
            SimpleDateFormat f = new SimpleDateFormat("MMdd_HHmm");
            File file = new File("/sdcard/FIRST/debugFor" + f.format(new Date()) + ".txt");
            FileOutputStream fileoutput = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fileoutput);
            for (String s: debugValues) {
                ps.println(s);
            }
            ps.close();
            fileoutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void encoderDrive(double distance, double power) throws InterruptedException
    {
        DcMotor frontRightWheel;
        DcMotor frontLeftWheel;
        DcMotor backRightWheel;
        DcMotor backLeftWheel;
        frontRightWheel = hardwareMap.dcMotor.get("frontR");
        frontLeftWheel = hardwareMap.dcMotor.get("frontL");
        backRightWheel = hardwareMap.dcMotor.get("backR");
        backLeftWheel = hardwareMap.dcMotor.get("backL");
        frontLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
      /*  while (encoder.distance<distance){
        frontRightWheel.setPower(power);
        frontLeftWheel.setPower(power);
        backRightWheel.setPower(power);
        backLeftWheel.setPower(power);
        waitForNextHardwareCycle();
    }*/

    }
    private void servoMove(Servo s1, double initPos, double endPos, double delta, long delay) throws InterruptedException
    {
        double tempPos = initPos;
        while (Math.abs(tempPos - endPos) > delta) {
            s1.setPosition(tempPos);
            sleep(delay);
            tempPos += delta;
        }
        s1.setPosition(endPos);
    }
    abstract protected int getDelay();


    abstract protected int getRedAlliance();

}