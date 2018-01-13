package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by Pahel and Rohan on 11/18/17.
 */


@TeleOp(name = "Delta_TeleOp_relic")
@Disabled
public class Delta_TeleOp_Relic extends OpMode {
/*Delta_TeleOp is designed for and tested with the Tile Runner robot. If this program is used with another robot it may not worked.
* This is specificly made for the Tile Runner and not another pushbot or competiotion robot. However, this program is the basic design for
* simple program and could work on a different robot with simple debugging and configuration.*/

    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor slideMotor;
    DcMotor relicMotor;
    Servo glyphServoRight;
    Servo glyphServoLeft;
    Servo clawServo;
    Servo moveRelicManipulatorServo;
    //Initial value for slide motor
    public int IVFSM;
/*
    public double openClaw = 0;
    public double closeClaw = 0.6;
    public double upClaw = 0;
    public double downClaw = 0.5;

*/

    

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


        //---------------------------------------------------------------------------------------------
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
        relicMotor = hardwareMap.dcMotor.get("relicMotor");
        clawServo = hardwareMap.servo.get("clawServo");
        moveRelicManipulatorServo = hardwareMap.servo.get("moveRelicManipulatorServo");
        IVFSM = slideMotor.getCurrentPosition();


        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

        glyphServoLeft.setPosition(0.5);
        glyphServoRight.setPosition(0.35);
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
        //This is what runs when the play button is hit

        FourWheelDrive();
        slideMove();
        glyphManipulator();
        slideIncrement();
        //relicSlides();
        //relicManipulatorClawServo();
        //relicManipulatorMovementServo();
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
        float leftY_gp1 = (-gamepad1.left_stick_y);
        float rightY_gp1 = (-gamepad1.right_stick_y);
        //
        //11/24/17 This edit was made by Colin Szeto. This was a test that we used to see if the triggers will work for the servos
        // float strafeStickLeft = (-gamepad1.left_trigger);//*leftWheelMotorFront.getMaxSpeed();
        // float strafeStickRight = (-gamepad1.right_trigger);//*leftWheelMotorFront.getMaxSpeed();
        //
        // run the motors by setting power to the motors with the game pad value

        if (gamepad1.left_trigger > 0) {
//Used for strafing to the left
            leftWheelMotorFront.setPower(-1);
            leftWheelMotorBack.setPower(1);
            rightWheelMotorFront.setPower(1);
            rightWheelMotorBack.setPower(-1);

        } else if (gamepad1.right_trigger > 0) {
//Used for strafing to the right
            leftWheelMotorFront.setPower(1);
            leftWheelMotorBack.setPower(-1);
            rightWheelMotorFront.setPower(-1);
            rightWheelMotorBack.setPower(1);

        } else {
            leftWheelMotorFront.setPower(leftY_gp1);
            leftWheelMotorBack.setPower(leftY_gp1);
            rightWheelMotorFront.setPower(rightY_gp1);
            rightWheelMotorBack.setPower(rightY_gp1);
            //run the motors by setting power to the motors with the game pad values
            //leftWheelMotorFront.setPower(leftY_gp1);
            //leftWheelMotorBack.setPower(leftY_gp1);
            //rightWheelMotorFront.setPower(rightY_gp1);
            //rightWheelMotorBack.setPower(rightY_gp1);
        }

        // telemetry.addData("Left Front value is", leftWheelMotorFront.getPower());
        //  telemetry.addData("Left Back value is", leftWheelMotorBack.getPower());
        // telemetry.addData("Right Front value is", rightWheelMotorFront.getPower());
        //  telemetry.addData("Right Back value is", rightWheelMotorBack.getPower());
        //  telemetry.update();



    }

    public void slideMove() {
//This moves the glyph manipulator slides up and down.
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        IVFSM = slideMotor.getCurrentPosition();

        if (gamepad2.right_stick_y > 0) {
            slideMotor.setPower(gamepad2.right_stick_y);
            //If the right stick is pushed up move the slides up
        } else if (gamepad2.right_stick_y < 0) {
            slideMotor.setPower(gamepad2.right_stick_y);
            //If the right stick is pushed down move the slides down
        } else {
            slideMotor.setPower(0);
            //Otherwise do nothing
        }
    }

    public void glyphManipulator() {
       /*
        Boolean Right_Bumper = (gamepad1.right_bumper);
        Boolean Left_Bumper = (gamepad1.left_bumper);
        double right_claw = (glyphServoRight.getPosition());
        double left_claw = (glyphServoLeft.getPosition());
       */

        if (gamepad1.left_bumper) {
//If the left bumper is hit then move the servos to these values or open the claw
//opening the claw
            glyphServoRight.setPosition(0.35);
            glyphServoLeft.setPosition(0.5);
        } else if (gamepad1.right_bumper) {
//If the right bumper is hit move the servos the the below positions or close the claw
            glyphServoRight.setPosition(0.05);
            glyphServoLeft.setPosition(0.85);

           /* telemetry.addData("The value of the right servo is", glyphServoRight.getPosition());
            telemetry.addData("The value of the left servo is", glyphServoLeft.getPosition());
            telemetry.update();    */
        }

/*      telemetry.addData("The value of the right servo is", left_claw);
        telemetry.addData("The value of the left servo is", right_claw);
        telemetry.update();

        */
    }

    public void slideIncrement() {

       /* if (gamepad2.dpad_down)
        {

            moveUpInch(2.54);

        }*/
        if (gamepad2.dpad_up)
        {
            //Makes a nice function for the other long function moveUpInch()
            moveUpInch(-2);
        }
        else {

        }
    }

   /*  public void moveUpInch(double cm) {
        double target_Position;
        double countsPerCM = 609.6;
        double finalTarget = cm*countsPerCM;
        target_Position = slideMotor.getCurrentPosition() + finalTarget;


        slideMotor.setTargetPosition((int)target_Position);

        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        slideMotor.setPower(-0.6);

        while (slideMotor.isBusy()){
            telemetry.addData("In while loop in moveUpInch", slideMotor.getCurrentPosition());
            telemetry.update();

        }

        slideMotor.setPower(0);

    }*/

    public void moveUpInch(double cm) {
        double target_Position;
        double countsPerCM = 609.6;
        double finalTarget = cm*countsPerCM;
        target_Position = slideMotor.getCurrentPosition() + finalTarget;
//This is the math that is used to determine how much to move the motor

        slideMotor.setTargetPosition((int)target_Position);

        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        slideMotor.setPower(-0.6);
//This part sets power to the motor and stops it when the motor is done running
        while (slideMotor.isBusy()){
            telemetry.addData("In while loop in moveDownInch", slideMotor.getCurrentPosition());
            telemetry.update();

        }

        slideMotor.setPower(0);
//This stops the motor
    }

   /* public void relicSlides() {
      double g2lsx = gamepad2.left_stick_x;
//If there is movement on the x-axis then move the motor proportionally to the amount of movement on the joystick
        if (g2lsx != 0) {
            relicMotor.setPower(g2lsx);
        }
        else{
            relicMotor.setPower(0);
        }
    }*/

  /*  public void relicManipulatorClawServo() {
      if (gamepad2.left_bumper){
          clawServo.setPosition(openClaw);
      }
      else if (gamepad2.right_bumper){
          clawServo.setPosition(closeClaw);
          //This says that if you press a bumper open or close the claw
      }
      telemetry.addData("In the function relicManipulatorClawServo and the value of that servo is", clawServo.getPosition());
      telemetry.update();
    }
*/
  /*  public void relicManipulatorMovementServo(){
        if (gamepad2.x){
            moveRelicManipulatorServo.setPosition(upClaw);
        }
        else if (gamepad2.y){
            moveRelicManipulatorServo.setPosition(downClaw);
            //This controls the overall movement of the claw
        }

    }
*/
    }


//--------------------------------------------------------------------------------------------