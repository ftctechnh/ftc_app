package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by pahel and rohan on 11/18/17.
 */


/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "runMotors")
@Disabled
public class runMotors extends OpMode {


    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    //DcMotor slideMotor;

    //Initial value for slide motor
    //public int IVFSM;





/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/


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
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");
        //slideMotor = hardwareMap.dcMotor.get("slideMotor");
        //IVFSM = slideMotor.getCurrentPosition();

        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);


//This is closed-loop speed control. Encoders are required for this mode.
// SetPower() in this mode is actually requesting a certain speed, based on the top speed of
// encoder 4000 pulses per second.

        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

    /*
    ---------------------------------------------------------------------------------------------

          Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */

    public void init_loop() {
    }

    /*
     ---------------------------------------------------------------------------------------------

          Code to run ONCE when the driver hits PLAY

    */
    @Override
    public void start() {

    }

    /*
    Code to run REPEATEDLY after the driver hit PLAY
    Main code loop goes here
     */

    @Override
    public void loop() {
        //FourWheelDrive();
        //slideMove();
        runMotors();

    }

    /* Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

/*
---------------------------------------------------------------------------------------------
 */

/*
---------------------------------------------------------------------------------------------

    Functions go here
 */

    public void runMotors() {
        int x = 0;
        int y = 0;
        int b = 0;
        int a = 0;
        if(gamepad1.x){
            if(x%2 != 0) {
                leftWheelMotorFront.setPower(1);
            }
            else{
                leftWheelMotorFront.setPower(0);
            }
            x++;
        }
        if(gamepad1.y){
            if(y%2 != 0) {
                rightWheelMotorFront.setPower(1);
            }
            else{
                rightWheelMotorFront.setPower(0);
            }
            y++;
        }
        if(gamepad1.b){
            if(b%2 != 0) {
                rightWheelMotorBack.setPower(1);
            }
            else{
                rightWheelMotorBack.setPower(0);
            }
            b++;
        }
        if(gamepad1.a){
            if(a%2 != 0){
                leftWheelMotorBack.setPower(1);
            }
            else{
                leftWheelMotorBack.setPower(0);
            }
            a++;
        }


    }
}
