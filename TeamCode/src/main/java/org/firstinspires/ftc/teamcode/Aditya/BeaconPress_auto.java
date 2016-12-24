package org.firstinspires.ftc.teamcode.Aditya;

/**
 * Created by adi, inspirationteam on 12/23/2016.
 */

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaCodecInfo;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "beacon press auto", group = "Robot")




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

    int currentColor = 0;

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
        beaconPress.setPosition(0.5);
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

        boolean teamRed = false;
        boolean teamBlue = true;
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        setColor(true, false);

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }

    public void beaconScan(String colorFirstLetterofAliance) {

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
        int currentColor = 0;
//declares the colors that it sees by default, in a different name!

        colorSensor.enableLed(true);

        if(red > blue && red > green) {
            currentColor = 1;
        } else if (blue > red && blue > green) {
            currentColor = 2;
        } else if (green > red && green > blue) {
            currentColor = 3;
        }
        //checks which color the side currently is

        telemetry.addData("r value", colorSensor.red());
        telemetry.addData("g value", colorSensor.green());
        telemetry.addData("b value", colorSensor.blue());
        telemetry.addData("current beacon color, (1 is red, 2 is blue)",currentColor);
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Saturation", hsvValues[1]);
        telemetry.addData("Value", hsvValues[2]);

        telemetry.update();
    }



    public void setColor(boolean isItRed, boolean isItBlue) {
        //sets team to what we put in the beginning input of the function
        boolean teamRed = isItRed;
        boolean teamBlue = isItBlue;

        //sets the colors for the side of the beacon
        boolean sideOne = false;
        boolean sideTwo = false;

        int readNumber = 0;

        readColor();

        if(currentColor == 1 && isItRed){
            sideOne = isItRed;
        } else if(currentColor == 2 && isItBlue) {
            sideOne = isItBlue;
        }

//reads first part of beacon

        encoderDrive(0.75, -10.5, -10.5);



        readColor();

        if(currentColor == 1 && isItRed){
            sideTwo = isItRed;
        } else if(currentColor == 2 && isItBlue) {
            sideTwo = isItBlue;
        }

        if(sideOne && sideTwo){

        } else if(sideOne == true && sideTwo == false) {

            sleep(250);
            encoderDrive(0.75, 6.5, 6.5);
            sleep(350);
            beaconPress.setPosition(0.1);
            sleep(1500);
            beaconPress.setPosition(0.55);
            sleep(150);

            //moves backward, as the color we need to press is behind, and then it presses the color we need
        } else if(sideOne == false && sideTwo == true) {

            encoderDrive(0.75,-5,-5);
            beaconPress.setPosition(0.1);
            sleep(1500);
            beaconPress.setPosition(0.55);
            sleep(150);

            //presses beacon
        } else if(sideOne == false && sideTwo == false){

            encoderDrive(0.75, -5, -5);
            beaconPress.setPosition(0);
            sleep(1500);
            beaconPress.setPosition(0.8);
            sleep(150);
            readColor();

            //presses beacon
        }
    }

    public void sleep(long pauseInMS) {
        long time_sleepStart = System.currentTimeMillis();
        long endTime = time_sleepStart + pauseInMS;

        while(endTime - System.currentTimeMillis() > 0) {

        }
    }

}
