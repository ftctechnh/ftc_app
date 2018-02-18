package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;


import android.transition.Slide;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


/**
 * Created by Team Inspiration on 1/21/18.
 */
@TeleOp(name = "Curious George")
public class curious_george_teleop extends OpMode {


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
    Servo glyphServoRight;
    Servo glyphServoLeft;
    Servo jewel_servo;
    DcMotor relicMotor;
    Servo relicMain;
    Servo relicLeft;
    Servo relicRight;
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
        jewel_servo = hardwareMap.servo.get("jewelServo");
        IVFSM = slideMotor.getCurrentPosition();
        relicMain = hardwareMap.servo.get("relicMain");
        relicLeft = hardwareMap.servo.get("relicLeft");
        relicRight = hardwareMap.servo.get("relicRight");
        relicMotor = hardwareMap.dcMotor.get("relicMotor");


        leftWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        leftWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        slideMotor.setDirection(DcMotor.Direction.REVERSE);
        relicMotor.setDirection(DcMotor.Direction.REVERSE);

        openGlyph();
        jewel_servo.setPosition(0.9);

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
       Glyph();
       Relic();
       Drive();
       Slides();

        telemetry.addData("glyph left pos", glyphServoLeft.getPosition());
        telemetry.addData("glyph right pos", glyphServoRight.getPosition());

        //  telemetry.addData("jewel pos", jewel_servo.getPosition());

        telemetry.update();

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
public void Slides(){
    slideMove();
    slideIncrement();
}
public void Drive(){
    FourWheelDrive();
}
public void Relic() {
    relicManipulator();
}
public void Glyph() {
    glyphManipulator();
    incrementOpen();
    incrementClose();
}
    public void relicManipulator() {

        boolean rightBumper = gamepad2.right_bumper;
        boolean leftBumper = gamepad2.left_bumper;
        float leftY_gp2 = (-gamepad2.left_stick_y);
        boolean rightButtonY = gamepad2.y;
        boolean rightButtonA = gamepad2.a;
        boolean rightButtonX = gamepad2.x;


        relicMotor.setPower(leftY_gp2);

        // this is what grabs the relic
        if (leftBumper) {
            //relicLeft.setPosition(0.75);
            relicLeft.setPosition(0.2);
            telemetry.addData("bumper value",relicLeft.getPosition());
            telemetry.update();

            //relicRight.setPosition(0.1);
            //relicRight is in charge of moving the relic gripper
        }

        // this is what releases the claw
        if (rightBumper) {

            relicLeft.setPosition(0.5);
            telemetry.addData("bumper value",relicLeft.getPosition());
            telemetry.update();


            //relicRight.setPosition(1);
            //relicRight.setPosition(0);
            
        }
        // this is the most extended postion
        if (rightButtonY) {
            //change the value to make the srm move lower
            relicMain.setPosition(0.3); //0.2
            //relicMain is the servo that releases the relic arm out out

            //relicLeft.setPosition(0.75)
            //this was the claw not all the way close
            //relicLeft.setPosition(0.1);
            telemetry.addData("button y pressed", relicMain.getPosition());
            telemetry.update();
        }
        //below was the code for the old collector with two claw arms
       // if (gamepad2.b) {
       //     relicLeft.setPosition(1);
       //     relicRight.setPosition(0.25);
       // }

        //below was the middle position
        if (rightButtonX) {
            relicMain.setPosition(0.45); //0.45

            telemetry.addData("button x pressed", relicMain.getPosition());
            telemetry.update();
        }

        // this is the stowed position
        if (rightButtonA) {
            relicMain.setPosition(1);
            //adding this line to move relic up at most stowed position
           // relicLeft.setPosition();

        }


    }

    public void FourWheelDrive() {
        /*

        read the gamepad values and put into variables
         */
        double threshold = 0.2;
        float leftY_gp1 = (-gamepad1.left_stick_y);
        float rightY_gp1 = (-gamepad1.right_stick_y);
        telemetry.addData("right power input", rightY_gp1);
        telemetry.addData("left power input", leftY_gp1);
        //
        //11/24/17 This edit was made by Colin Szeto. This was a test that we used to see if the triggers will work for the servos
        // float strafeStickLeft = (-gamepad1.left_trigger);//*leftWheelMotorFront.getMaxSpeed();
        // float strafeStickRight = (-gamepad1.right_trigger);//*leftWheelMotorFront.getMaxSpeed();
        //
        // run the motors by setting power to the motors with the game pad value

        if (gamepad1.right_trigger > 0) {

            leftWheelMotorFront.setPower(1);
            leftWheelMotorBack.setPower(-1);
            rightWheelMotorFront.setPower(-1);
            rightWheelMotorBack.setPower(1);

        } else if (gamepad1.left_trigger > 0) {

            leftWheelMotorFront.setPower(-1);
            leftWheelMotorBack.setPower(1);
            rightWheelMotorFront.setPower(1);
            rightWheelMotorBack.setPower(-1);

        } else if (Math.abs(gamepad1.left_stick_y) > threshold || Math.abs(gamepad1.right_stick_y) > threshold){
            leftWheelMotorFront.setPower(leftY_gp1);
            leftWheelMotorBack.setPower(leftY_gp1);
            rightWheelMotorFront.setPower(rightY_gp1);
            rightWheelMotorBack.setPower(rightY_gp1);
        }
        else{
            leftWheelMotorBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftWheelMotorFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightWheelMotorBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightWheelMotorFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftWheelMotorFront.setPower(0);
            leftWheelMotorBack.setPower(0);
            rightWheelMotorFront.setPower(0);
            rightWheelMotorBack.setPower(0);
        }


    }

    public void slideMove() {

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        IVFSM = slideMotor.getCurrentPosition();

        if (gamepad2.right_stick_y != 0) {
            slideMotor.setPower(gamepad2.right_stick_y);

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
        if (gamepad1.right_bumper&&gamepad1.left_bumper){
            middleGlyph();

        }
        else if (gamepad1.left_bumper) {

//opening the claw

            openGlyph();
        } else if (gamepad1.right_bumper) {

            closeGlyph();
        }


/*        telemetry.addData("The value of the right servo is", left_claw);
        telemetry.addData("The value of the left servo is", right_claw);
        telemetry.update();

        */
    }
    public void wait(int mSec){
        double startTime;
        double endTime;

        startTime = System.currentTimeMillis();
        endTime = startTime+mSec;

        while(endTime >= System.currentTimeMillis()){

        }
    }
    public void incrementOpen(){

        while (gamepad1.x){
            glyphServoLeft.setPosition(glyphServoLeft.getPosition()+0.05);
            glyphServoRight.setPosition(glyphServoRight.getPosition()-0.05);
            wait(300);
        }
    }
    public void incrementClose(){

        while (gamepad1.y) {
            glyphServoLeft.setPosition(glyphServoLeft.getPosition()-0.05);
            glyphServoRight.setPosition(glyphServoRight.getPosition()+0.05);
            wait(300);
        }
    }
    public void slideIncrement() {

        if (gamepad2.dpad_up) {

            moveUpInch(2.54);

        } else if (gamepad2.dpad_right) {
            moveUpInch(17.78);
        } else if (gamepad2.dpad_down){
            moveUpInch(33.02);
        }
    }

    public void moveUpInch(double cm) {
        double target_Position;
        double countsPerCM = 609.6;
        double finalTarget = cm * countsPerCM;
        target_Position = slideMotor.getCurrentPosition() - finalTarget;

        slideMotor.setTargetPosition((int) target_Position);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideMotor.setPower(-0.6);

        while (slideMotor.isBusy()) {
            telemetry.addData("In while loop in moveUpInch", slideMotor.getCurrentPosition());
            telemetry.update();

        }

        slideMotor.setPower(0);

    }

    public void moveDownInch(double cm) {
        double target_Position;
        double countsPerCM = 609.6;
        double finalTarget = cm * countsPerCM;
        target_Position = slideMotor.getCurrentPosition() + finalTarget;

        slideMotor.setTargetPosition((int) target_Position);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideMotor.setPower(0.6);

        while (slideMotor.isBusy()) {
            telemetry.addData("In while loop in moveDownInch", slideMotor.getCurrentPosition());
            telemetry.update();

        }

        slideMotor.setPower(0);

    }
    public void openGlyph(){

        //switching values with closeGlyph
        //reversed values
        glyphServoRight.setPosition(0.5);
        glyphServoLeft.setPosition(0.4);
    }

    public void closeGlyph(){
        //reversed values
        glyphServoRight.setPosition(0.75);
        glyphServoLeft.setPosition(0.15);
    }

    public void middleGlyph(){
        glyphServoRight.setPosition(0.65);
        glyphServoLeft.setPosition(0.25);
    }
}
