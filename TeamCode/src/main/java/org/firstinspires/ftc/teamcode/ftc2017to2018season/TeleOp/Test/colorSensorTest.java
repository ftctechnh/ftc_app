package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by Rohan on 11/18/17.
 */


@TeleOp(name = "color sensor test new")
@Disabled
public class colorSensorTest extends OpMode {


    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */

    ColorSensor colorSensor;




    //Initial value for slide motor




/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/


    //  ColorSensor bColorSensorLeft;    // Hardware Device Object


    /*

         //----------------------------------------------------------------------------------------------
        //Declare global variables here


        org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.charlieTeleOp.cap_ball_arm_state_type cap_ball_arm_state;

        //static final int CYCLE_MS = 5000;     // period of each cycle(mili seconds)


        //private ElapsedTime runtime = new ElapsedTime();
        static final double     COUNTS_PER_MOTOR_REV    = 757 ;    // eg: TETRIX Motor Encoder
        static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // 56/24
        static final double     WHEEL_PERIMETER_CM   = 9;     // For figuring circumference
        static final double     COUNTS_PER_CM         = (COUNTS_PER_MOTOR_REV ) /
                (DRIVE_GEAR_REDUCTION*WHEEL_PERIMETER_CM);
        // Define class members
        //   Servo right_servo;


        /*---------------------------------------------------------------------------------------------
                Get references to the hardware installed on the robot and name them here
        */
    @Override
    public void init() {

        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        colorSensor.enableLed(true);

        telemetry.addData("ready to go", "");
        telemetry.update();

        /*telemetry.addData("glyph left pos", glyphServoLeft.getPosition());
        telemetry.addData("glyph right pos", glyphServoRight.getPosition());
        telemetry.addData("jewel pos", jewel_servo.getPosition());
        telemetry.update();*/
//This is closed-loop speed control. Encoders are required for this mode.
// SetPower() in this mode is actually requesting a certain speed, based on the top speed of
// encoder 4000 pulses per second.

        // leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }


    /*
    ---------------------------------------------------------------------------------------------

          Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */



    /*
     ---------------------------------------------------------------------------------------------

          Code to run ONCE when the driver hits PLAY

    */

    /*
    Code to run REPEATEDLY after the driver hit PLAY
    Main code loop goes here
     */

    @Override
    public void loop() {

       telemetry.addData("red value", colorSensor.red());
       telemetry.addData("blue value", colorSensor.blue());
       telemetry.update();



    }

}

//--------------------------------------------------------------------------------------------