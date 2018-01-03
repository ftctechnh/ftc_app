package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by adi, inspirationteam on 11/20/2016.
 */

@TeleOp(name = "#11183: Beacon Press", group = "Robot")
@Disabled

public class BeaconPress extends OpMode {


    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor ballCollectorMotor;
    DcMotor ballShooterMotor;
    Servo beaconPress;
    ColorSensor colorSensor;

/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/


    // ColorSensor colorSensor;    // Hardware Device Object



/*
 ----------------------------------------------------------------------------------------------
Declare global variables here
*/


    /*---------------------------------------------------------------------------------------------
            Get references to the hardware installed on the robot and name them here
    */
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
        //Sets team!
        boolean teamRed = false;
        boolean teamBlue = true;

    }
    /*
    ---------------------------------------------------------------------------------------------

          Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */

    @Override
    public void init_loop() {

    }

    /*
     ---------------------------------------------------------------------------------------------

          Code to run ONCE when the driver hits PLAY

    */
    @Override
    public void start(){
        colorSensor.enableLed(true);
    }

    //enables the color sensor to work

    /*
    Code to run REPEATEDLY after the driver hit PLAY
    Main code loop goes here
     */

    @Override
    public void loop() {
        colorSensor.enableLed(true);


        FourWheelDrive();
        CollectBalls();
        BallShooter();
        servoMove();
        readColor();
    }
/*
---------------------------------------------------------------------------------------------

    Functions go here
 */


    public void FourWheelDrive(){
        /*
        read the gamepad values and put into variables
         */
        //telemetry.addData("leftWheel Motor front encoder value: %d ", leftWheelMotorFront.getCurrentPosition());
        //telemetry.update();
        float leftY_gp1 = -gamepad1.left_stick_y;
        float rightY_gp1 = -gamepad1.right_stick_y;

        //run the motors by setting power to the motors with the game pad values
        leftWheelMotorFront.setPower(leftY_gp1);
        leftWheelMotorBack.setPower(leftY_gp1);
        rightWheelMotorFront.setPower(rightY_gp1);
        rightWheelMotorBack.setPower(rightY_gp1);

    }
/*---------------------------------------------------------------------------------------------
*/

    public void CollectBalls(){
        boolean intake = gamepad1.right_bumper;
        boolean outtake = gamepad1.left_bumper;

        if (intake) {
            ballCollectorMotor.setPower(1);
        } else if (outtake) {
            ballCollectorMotor.setPower(-1);
        } else {
            ballCollectorMotor.setPower(0);
        }
    }

/*---------------------------------------------------------------------------------------------
*/

    /*public void servoMove(){
        float game = -gamepad2.left_stick_y;
        float gameMove = (float) (game * 0.1);
        if( (beaconPress.getPosition() + gameMove) > 0.0) {
            beaconPress.setPosition(beaconPress.getPosition() + gameMove);
        }
        if(beaconPress.getPosition() <= 0.0 && gameMove > 0.0) {
                beaconPress.setPosition(beaconPress.getPosition() + gameMove);
        }
    }
    */
    public void servoMove() {
        double up_step = 0.25;
        double down_step = -0.25;

        if(gamepad2.dpad_down && (beaconPress.getPosition() ) + down_step >= 0){
            beaconPress.setPosition(beaconPress.getPosition() + down_step);
            sleep(125);
        }

        if(gamepad2.dpad_up && (beaconPress.getPosition() ) + up_step >= 0){
            beaconPress.setPosition(beaconPress.getPosition() + up_step);
            sleep(125);
        }

        }

        public void sleep(long pauseInMS) {
            long time_sleepStart = System.currentTimeMillis();
            long endTime = time_sleepStart + pauseInMS;

            while(endTime - System.currentTimeMillis() > 0) {

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
        String currentColor = "blank";

        colorSensor.enableLed(true);

        if(red > blue && red > green) {
            currentColor = "red";
        } else if (blue > red && blue > green) {
            currentColor = "blue";
        } else if (green > red && green > blue) {
            currentColor = "green";
        }

        telemetry.addData("r value", colorSensor.red());
        telemetry.addData("g value", colorSensor.green());
        telemetry.addData("b value", colorSensor.blue());
        telemetry.addData("current beacon color", currentColor);
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Saturation", hsvValues[1]);
        telemetry.addData("Value", hsvValues[2]);

        telemetry.update();
    }

    public void BallShooter(){
        float shoot = -gamepad2.right_stick_y;//gets value from 2nd gamepad's joystick

        ballShooterMotor.setPower(Math.abs(shoot));//set power


       /*saving previous code sample if using only one gamepad*/
        /*

        boolean shoot = gamepad1.a;
        if (shot) {
            ballShooterMotor.setPower(1);
        } else {
            ballShooterMotor.setPower(-1);
            ballShooterMotor.setPower(0)
        }

        */

    }

/*---------------------------------------------------------------------------------------------
*/
    /* Read the color sensor and return "b" for Blue or "r" for Red */
   /* public char ReadColorSensor(){

    char redOrBlue = 'b';

    /* Variables used to store value of the color sensor
    /* hsvValues is an array that will hold the hue, saturation, and value information
    float hsvValues[] = {0F,0F,0F};

    /* bLedOn represents the state of the LED.
    boolean bLedOn = true;

    /* Set the LED in the beginning
    colorSensor.enableLed(bLedOn);

    /* convert the RGB values to HSV values
    Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

    /* send the info back to driver station using telemetry function.
    telemetry.addData("LED", bLedOn ? "On" : "Off");
    telemetry.addData("Clear", colorSensor.alpha());
    telemetry.addData("Red  ", colorSensor.red());
    telemetry.addData("Green", colorSensor.green());
    telemetry.addData("Blue ", colorSensor.blue());
    telemetry.addData("Hue", hsvValues[0]);
    telemetry.update();

    if (colorSensor.red()>1000) /*need to find out the treashold for
        redOrBlue='r';
    bLedOn = !bLedOn;
    colorSensor.enableLed(bLedOn);
    return redOrBlue;
    }*/


    /*
    ---------------------------------------------------------------------------------------------

         Code to run ONCE after the driver hits STOP
    */
    @Override
    public void stop() {
    }

/*
---------------------------------------------------------------------------------------------
 */

}

