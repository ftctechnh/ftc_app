package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaCodecInfo;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "#11183: TeleOp Competition", group = "Robot")

public class TeleOpCompetition extends OpMode {


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

    double shooterGearRatio = 2.333;
//asdfasdf

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
        /* get a reference to our ColorSensor object */
        //colorSensor = hardwareMap.colorSensor.get("sensor_color");
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

    }

    /*
    Code to run REPEATEDLY after the driver hit PLAY
    Main code loop goes here
     */

    @Override
    public void loop() {
        FourWheelDrive();
        CollectBalls();
        BallShooter();
        shoot();

    }
/*
---------------------------------------------------------------------------------------------

    Functions go here
 */


    public void FourWheelDrive(){
        /*
        read the gamepad values and put into variables
         */
        telemetry.addData("leftWheel Motor front encoder value: %d ", leftWheelMotorFront.getCurrentPosition());
        telemetry.update();
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
    public void shoot(){

        if (gamepad2.a){
        ballShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ballShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ballShooterMotor.setTargetPosition(ballShooterMotor.getCurrentPosition() + (1478* shooterGearRatio);
        ballShooterMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ballShooterMotor.setPower(1);
        while (ballShooterMotor.isBusy()){
            telemetry.addData("", "Shooting...");
            telemetry.update();
        }
        telemetry.addData("", "Done Shooting");
        telemetry.update();
        ballShooterMotor.setPower(0);
        }

    }
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

