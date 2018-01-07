package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Beta")
@Disabled
public class Beta extends OpMode {

    // Beta is the tester robot that Team Inspiration is using. This program is similar to Delta_TeleOp. It has a few changes.

     /*   ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor slideMotor;
    Servo glyphServoRight;
    Servo glyphServoLeft;
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
        glyphServoRight = hardwareMap.servo.get("glyphServoRight");
        glyphServoLeft = hardwareMap.servo.get("glyphServoLeft");
        slideMotor = hardwareMap.dcMotor.get("slideMotor");
        IVFSM = slideMotor.getCurrentPosition();


        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

        glyphServoLeft.setPosition(0.0);
        glyphServoRight.setPosition(1.0);



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
        float leftY_gp1 = (gamepad1.left_stick_y);
        float rightY_gp1 = (gamepad1.right_stick_y);
        //
        //11/24/17 This edit was made by Colin Szeto. This was a test that we used to see if the triggers will work for the servos
        // float strafeStickLeft = (gamepad1.left_trigger);//*leftWheelMotorFront.getMaxSpeed();
        // float strafeStickRight = (gamepad1.right_trigger);//*leftWheelMotorFront.getMaxSpeed();
        //
        // run the motors by setting power to the motors with the game pad value

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

        // telemetry.addData("Left Front value is", leftWheelMotorFront.getPower());
        //  telemetry.addData("Left Back value is", leftWheelMotorBack.getPower());
        // telemetry.addData("Right Front value is", rightWheelMotorFront.getPower());
        //  telemetry.addData("Right Back value is", rightWheelMotorBack.getPower());
        //  telemetry.update();
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

        //lift motor

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
       /* Boolean Right_Bumper = (gamepad1.right_bumper);
        Boolean Left_Bumper = (gamepad1.left_bumper);
        double right_claw = (glyphServoRight.getPosition());
        double left_claw = (glyphServoLeft.getPosition());
       */

        if (gamepad1.left_bumper) {

//opening the claw
            //commented out so that it wouldn't ruin the old code
            glyphServoRight.setPosition(0.7);
            /*
            try {
                glyphServoRight.setPosition(0.5);
            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            glyphServoLeft.setPosition(0.2);
        }
        else if (gamepad1.right_bumper){

            glyphServoRight.setPosition(0.9);

            glyphServoLeft.setPosition(0.0);

            telemetry.addData("The value of the right servo is", glyphServoRight.getPortNumber());
            telemetry.addData("The value of the left servo is", glyphServoLeft.getPortNumber());
            telemetry.update();
        }

/*        telemetry.addData("The value of the right servo is", left_claw);
        telemetry.addData("The value of the left servo is", right_claw);
        telemetry.update();

        */
    }
}
//--------------------------------------------------------------------------------------------