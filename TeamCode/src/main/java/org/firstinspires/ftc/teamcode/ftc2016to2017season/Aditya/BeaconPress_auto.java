package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

/**
 * Created by adi, inspirationteam on 12/23/2016.
 */

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "beacon press auto", group = "Robot")
@Disabled



public class BeaconPress_auto extends OpMode {

    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor ballCollectorMotor;
    DcMotor ballShooterMotor;
    Servo beaconPress;
    ColorSensor colorSensor;

    static final double COUNTS_PER_MOTOR_REV = 757;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.3333;     // 56/24
    static final double WHEEL_PERIMETER_CM = 29;     // For figuring circumference
    static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV) /
            (WHEEL_PERIMETER_CM * DRIVE_GEAR_REDUCTION);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    static int INITIAL_SHOOTERPOS;
    String currentColor = "blank";
    int currentColorInt = 0;

    @Override
    public void init() {
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");
            /* lets reverse the direction of the right wheel motor*/
        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");
        ballShooterMotor = hardwareMap.dcMotor.get("ballShooterMotor");
        beaconPress = hardwareMap.servo.get("beaconPress");
        /* get a reference to our ColorSensor object */
        //colorSensor = hardwareMap.colorSensor.get("sensor_color");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");


        leftWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        INITIAL_SHOOTERPOS = ballShooterMotor.getCurrentPosition();
        telemetry.addData("Inital Shooter Position", INITIAL_SHOOTERPOS);



    }


    @Override
    public void start() {
        // setColor("blue");
        // beaconRead(1);
        // beaconread decalres the team. 1 is for blue, 2 is for red :)
        // justServo("red");
        newBeacon("red");
    }

    public void loop() {

    }

    public void stop() {

    }



    public void straightDrive(double power){
        leftWheelMotorBack.setPower(power);
        leftWheelMotorFront.setPower(power);
        rightWheelMotorBack.setPower(power);
        rightWheelMotorFront.setPower(power);
    }

    public void encoderDrive(double speed, double leftInches, double rightInches) {

        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;


        // Determine new target position, and pass to motor controller
        newLeftTarget = leftWheelMotorBack.getCurrentPosition() + (int) (leftInches * COUNTS_PER_CM);
        newRightTarget = rightWheelMotorBack.getCurrentPosition() + (int) (rightInches * COUNTS_PER_CM);
        leftWheelMotorFront.setTargetPosition(newLeftTarget);
        rightWheelMotorFront.setTargetPosition(newRightTarget);
        leftWheelMotorBack.setTargetPosition(newLeftTarget);
        rightWheelMotorBack.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        if (Math.abs(leftInches) > Math.abs(rightInches)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightInches) / leftInches;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftInches) / rightInches;
        }

        leftWheelMotorFront.setPower(Math.abs(leftSpeed));
        leftWheelMotorBack.setPower(Math.abs(leftSpeed));
        rightWheelMotorFront.setPower(Math.abs(rightSpeed));
        rightWheelMotorBack.setPower(Math.abs(rightSpeed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        while ((leftWheelMotorBack.isBusy() && rightWheelMotorBack.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    leftWheelMotorBack.getCurrentPosition(),
                    rightWheelMotorBack.getCurrentPosition());
            telemetry.update();
        }
    }

     public void readColor() {

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;


        //convert the RGB values to HSV values
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();
//declares the colors that it sees by default, in a different name!

        colorSensor.enableLed(true);

        if(red > blue && red > green) {
            currentColor = "red";
            currentColorInt = 1;
        } else if (blue > red && blue > green) {
            currentColor = "blue";
            currentColorInt = 2;
        } else if (green > red && green > blue) {
            currentColor = "green";
            currentColorInt = 0;
        } else {
            currentColor = "other";
            currentColorInt = 0;
        }
        //checks which color the side currently is

        telemetry.addData("r value", colorSensor.red());
        telemetry.addData("g value", colorSensor.green());
        telemetry.addData("b value", colorSensor.blue());
        telemetry.addData("current beacon color",currentColor);
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Saturation", hsvValues[1]);
        telemetry.addData("Value", hsvValues[2]);

        telemetry.update();
    }



    public void setColor(String team) {

        colorSensor.enableLed(true);
        //sets team to what we put in the beginning input of the function
        int teamRed = 1;
        int teamBlue = 2;
        int currentTeam = 0;


        if(team.equals("red")) {

            currentTeam = teamRed;

        } else if(team.equals("blue")) {

            currentTeam = teamBlue;

        }
        //the 2 "if statements" make the teams picked form the input.

        //sets the colors for the side of the beacon
        int sideOne = 0;
        int sideTwo = 0;



        readColor();
        String colorRead = currentColor;
        if(colorRead.equals("red")){
            telemetry.addData("color seen is RED ", colorSensor.red());
            telemetry.update();

            sideOne = teamRed;

        } else if(colorRead.equals("blue")){

            sideOne = teamBlue;
        }


        sleep(1750);
        encoderDrive(0.8,-20,-20);
        sleep(750);

        readColor();

        String colorReadtwo = currentColor;
        if(colorReadtwo.equals("red")){
            telemetry.addData("color seen is RED ", colorSensor.red());
            telemetry.update();

            sideTwo = teamRed;

        } else if(!colorReadtwo.equals("blue")){

            sideTwo = teamBlue;
        }


        if(sideOne == currentTeam && sideTwo == currentTeam) {
            //do nothing
        }
        else if(sideOne != currentTeam && sideTwo == currentTeam) {
            encoderDrive(0.5, -6.5, -6.5);
            sleep(1000);
            beaconPress.setPosition(0.1);
            sleep(4000);
            beaconPress.setPosition(0.3);
        }
        else if(sideOne != currentTeam && sideTwo == currentTeam) {
            encoderDrive(0.5, 2.5, 2.5);
            sleep(1000);
            beaconPress.setPosition(0.1);
            sleep(4000);
            beaconPress.setPosition(0.3);
        }
        else if(sideOne != currentTeam && sideTwo != currentTeam) {
            encoderDrive(0.5, 2.5, 2.5);
            sleep(1000);
            beaconPress.setPosition(0.1);
            sleep(4000);
            beaconPress.setPosition(0.3);
        }



    }

    public void beaconRead(int team /* 1 = blue, 2 = red */) {

        while(currentColorInt == 0){
            straightDrive(-0.7);
            readColor();
        }

        readColor();
        int readOne = currentColorInt;
        if(readOne == team) {
            encoderDrive(0.6,-4,-4);
            beaconPress.setPosition(0.15);
        } else if(readOne != team){
            encoderDrive(0.6,-20,-20);
        }

        readColor();
        int readTwo = currentColorInt;

        if(readTwo == team){
            encoderDrive(0.6,-5,-5);
            beaconPress.setPosition(0.15);
        }

    }

    public void sleep(long pauseInMS) {
        long time_sleepStart = System.currentTimeMillis();
        long endTime = time_sleepStart + pauseInMS;

        while(endTime - System.currentTimeMillis() > 0) {

        }
    }

    public void justServo(String team){

        int currentTeam = 0;
        int teamRed = 1;
        int teamBlue = 2;

        if(team.equals("red")){
            currentTeam = teamRed;
        }

        if(team.equals("blue")){
            currentTeam = teamBlue;
        }

        readColor();

        while(currentColorInt != currentTeam) {
            straightDrive(-0.3);
            readColor();
        }

        if(team == currentColor){
            sleep(200);
            beaconPress.setPosition(0.0);
            sleep(5000);
            beaconPress.setPosition(0.5);
        }

        readColor();

        while(currentColorInt == currentTeam) {
            straightDrive(-0.75);
            readColor();
        }

        while(currentColorInt != currentTeam) {
            straightDrive(-0.3);
            readColor();
        }

        if(currentTeam == currentColorInt){
            sleep(200);
            beaconPress.setPosition(0.0);
            sleep(3000);
            beaconPress.setPosition(0.5);
        } }

        public void newBeacon(String team){

            int teamRed = 1;
            int teamBlue = 2;
            int currentTeam = 0;

            if(team.equals("red")) {
                currentTeam = teamRed;
            }

            if(team.equals("blue")) {
                currentTeam = teamBlue;
            }
            readColor();

            while(!currentColor.equals(team)) {
                straightDrive(0.25);
                readColor();
            }

            if(currentColor.equals(team)) {
                encoderDrive(0.4, 3, 3);
                sleep(750);
                beaconPress.setPosition(0);
                sleep(5500);
                beaconPress.setPosition(0.5);
            }

            readColor();

            telemetry.addData("pressed!", currentColor);
            telemetry.update();

            if(currentColor.equals(team)) {
                encoderDrive(0.5, 30, 30);
            }


    }
    }



