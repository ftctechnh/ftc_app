package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by Rohan on 11/18/17.
 */


@TeleOp(name = "servo read position")
@Disabled
public class servo_valueRead extends OpMode {
/*Delta_TeleOp is designed for and tested with the Tile Runner robot. If this program is used with another robot it may not worked.
* This is specificly made for the Tile Runner and not another pushbot or competiotion robot. However, this program is the basic design for
* simple program and could work on a different robot with simple debugging and configuration.*/

    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */

    Servo glyphServoRight;
    Servo glyphServoLeft;
    Servo jewel_servo;

    public double glyphLeftPos;
    public double glyphRightPos;
    public double jewelPos;


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

        glyphServoRight = hardwareMap.servo.get("glyphServoRight");
        glyphServoLeft = hardwareMap.servo.get("glyphServoLeft");
        jewel_servo = hardwareMap.servo.get("jewelServo");

        glyphServoLeft.setPosition(0.5);
        glyphServoRight.setPosition(1.0);
        jewel_servo.setPosition(0.5);
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

      //  moveServos();
      incrementClose();
      incrementOpen();
        glyphLeftPos = glyphServoLeft.getPosition();
        glyphRightPos = glyphServoRight.getPosition();
        jewelPos = jewel_servo.getPosition();

        telemetry.addData("right stick is", gamepad1.right_stick_y);
        telemetry.addData("left stick is", gamepad1.left_stick_y);
       // telemetry.addData("jewel pos", jewel_servo.getPosition());
        telemetry.update();

    }

 /*   public void moveServos(){



        if (gamepad1.y){

            glyphServoRight.setPosition(glyphServoRight.getPosition() + 0.1);
            sleep(500);

        }
        else if (gamepad1.a){

            glyphServoRight.setPosition(glyphServoRight.getPosition() -0.1);
            sleep(500);

        }



        if (gamepad1.left_bumper){

            glyphServoLeft.setPosition(glyphServoLeft.getPosition() + 0.1);
            sleep(500);

        }
        else if (gamepad1.right_bumper){

            glyphServoLeft.setPosition(glyphServoLeft.getPosition() -0.1);
            sleep(500);

        }
    }*/

    public void sleep(long time){
        long startTime = System.currentTimeMillis();
        long  endTime = startTime + time;

        while(startTime < endTime){

        }

    }
    public void incrementOpen(){

        while (gamepad1.x){
            glyphServoLeft.setPosition(glyphServoLeft.getPosition()+0.05);
            glyphServoRight.setPosition(glyphServoRight.getPosition()-0.05);
            sleep(500);
        }
    }
    public void incrementClose(){

        while (gamepad1.y) {
            glyphServoLeft.setPosition(glyphServoLeft.getPosition()-0.05);
            glyphServoRight.setPosition(glyphServoRight.getPosition()+0.05);
            sleep(500);
        }
    }
}

//--------------------------------------------------------------------------------------------