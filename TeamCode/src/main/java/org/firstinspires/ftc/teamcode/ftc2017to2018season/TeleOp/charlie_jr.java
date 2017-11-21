package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by pahel and rohan on 11/18/17.
 */


/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "charlie_jr")
//@Disabled
public class charlie_jr extends OpMode {


    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor slideMotor;
    Servo glyphServo1;
    Servo glyphServo2;
    //Initial value for slide motor
    public int IVFSM;





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
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");
        glyphServo1 = hardwareMap.servo.get("glyphServo1");
        glyphServo2 = hardwareMap.servo.get("glyphServo2");
        slideMotor = hardwareMap.dcMotor.get("slideMotor");
        IVFSM = slideMotor.getCurrentPosition();

        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);


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
        FourWheelDrive();
        slideMove();
       glyphManipulator();


    }

    /* Code to run ONCE after the driver hits STOP
     */


/*
---------------------------------------------------------------------------------------------
 */

/*
---------------------------------------------------------------------------------------------

    Functions go here
 */

    public void FourWheelDrive() {
        /*

        read the gamepad values and put into variables
         */
        float leftY_gp1 = (-gamepad1.left_stick_y);//*leftWheelMotorFront.getMaxSpeed();
        float rightY_gp1 = (-gamepad1.right_stick_y);//*leftWheelMotorFront.getMaxSpeed();
        float strafeStickLeft = (-gamepad1.left_trigger);//*leftWheelMotorFront.getMaxSpeed();
        float strafeStickRight = (-gamepad1.right_trigger);//*leftWheelMotorFront.getMaxSpeed();
        //run the motors by setting power to the motors with the game pad value

        if (gamepad1.right_trigger > 0) {

            leftWheelMotorFront.setPower(-1);
            leftWheelMotorBack.setPower(1);
            rightWheelMotorFront.setPower(1);
            rightWheelMotorBack.setPower(-1);

        } else if (gamepad1.left_trigger > 0) {

            leftWheelMotorFront.setPower(1);
            leftWheelMotorBack.setPower(-1);
            rightWheelMotorFront.setPower(-1);
            rightWheelMotorBack.setPower(1);

        } else {
            leftWheelMotorFront.setPower(leftY_gp1);
            leftWheelMotorBack.setPower(leftY_gp1);
            rightWheelMotorFront.setPower(rightY_gp1);
            rightWheelMotorBack.setPower(rightY_gp1);
        }

        telemetry.addData("Left Front value is", leftWheelMotorFront.getPower());
        telemetry.addData("Left Back value is", leftWheelMotorBack.getPower());
        telemetry.addData("Right Front value is", rightWheelMotorFront.getPower());
        telemetry.addData("Right Back value is", rightWheelMotorBack.getPower());
        telemetry.update();
        //telemetry.addData("",)
        //telemetry.update();
        //These were going to be used to find the values of triggers but we couldn't acomplish it
        //run the motors by setting power to the motors with the game pad values
        //leftWheelMotorFront.setPower(leftY_gp1);
        //leftWheelMotorBack.setPower(leftY_gp1);
        //rightWheelMotorFront.setPower(rightY_gp1);
        //rightWheelMotorBack.setPower(rightY_gp1);


    }

    public void slideMove() {

        IVFSM = slideMotor.getCurrentPosition();

        if (gamepad2.right_stick_y > 0) {
            slideMotor.setPower(gamepad2.right_stick_y);

        } else if (gamepad2.right_stick_y < 0) {
            slideMotor.setPower(-1);
        } else {
            slideMotor.setPower(0);
        }
    }

    public void glyphManipulator() {

       if (gamepad1.left_bumper = false) {
            glyphServo2.setPosition(-0.5);
            glyphServo1.setPosition(0.5);
        } else if (gamepad1.right_bumper = false) {
            glyphServo1.setPosition(-0.5);
            glyphServo2.setPosition(0.5);
        } else {
        }
    }
}
//---------------------------------------------------------------------------------------------
