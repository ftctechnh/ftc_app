/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------

=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp.Main;


import android.transition.Slide;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Rohan on 2/23/18.
 * Created for testing purposes in slide increment
 */
@TeleOp(name = "1 Use this for competition George")
public class
General_George_TeleOp extends OpMode {


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
    Servo jewel_servo_rotate;
    DcMotor relicMotor;
    Servo relicMain;
    Servo relicClaw;
    public int IVFSM;
    ElapsedTime runtime = new ElapsedTime();



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
        jewel_servo_rotate = hardwareMap.servo.get("jewelServoRotate");
        IVFSM = slideMotor.getCurrentPosition();
        relicMain = hardwareMap.servo.get("relicMain");
        relicClaw = hardwareMap.servo.get("relicClaw");
        relicMotor = hardwareMap.dcMotor.get("relicMotor");


        leftWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        leftWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        slideMotor.setDirection(DcMotor.Direction.REVERSE);
        relicMotor.setDirection(DcMotor.Direction.REVERSE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//in this mode, the motors actively fight any movement when their power is set to 0

        openGlyph();
        jewel_servo.setPosition(0.1);
        jewel_servo_rotate.setPosition(0.74);
        relicMain.setPosition(1);




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
        telemetry.addData("Slide Motor Value is ", slideMotor.getCurrentPosition());
        telemetry.update();
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

    public void IncrementMain(){
        while (gamepad2.dpad_up){
            relicMain.setPosition(relicMain.getPosition()+0.05);
        }
        while (gamepad2.dpad_down){
            relicMain.setPosition(relicMain.getPosition()-0.05);
        }
    }
    public void relicManipulator() {
        if (gamepad2.a){
            relicMain.setPosition(0.1);
        }
        else if (gamepad2.x){
            relicMain.setPosition(0.6);
        }
        else if (gamepad2.y){
            relicMain.setPosition(1);
        }
        else if (gamepad2.left_bumper){
            relicClaw.setPosition(1.0);
        }
        else if (gamepad2.right_bumper){
            relicClaw.setPosition(0.2);
        }
        else if (gamepad2.left_bumper&&gamepad2.right_bumper){
            relicClaw.setPosition(0.5);
        }
        else{
            relicMotor.setPower(gamepad2.left_stick_y);
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
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
//This was done by Rohan and Steven on 2/23/18 because the regionals are soon and the slides malfunction if they reach the very top. This causes the most ascension, therefore, it is disabled. TEMPORARY!!
            //moveUpInch(33.02);

        } else if (gamepad2.dpad_right) {
            moveUpInch(20.32);
        } else if (gamepad2.dpad_down){
            moveUpInch(5.08);
        }
    }

    public void moveUpInch(double cm) {
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double target_Position;
        double countsPerCM = 50;
        double finalTarget = cm * countsPerCM;
        target_Position = slideMotor.getCurrentPosition() - finalTarget;

        slideMotor.setTargetPosition((int) target_Position);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideMotor.setPower(-0.6);
        double begintime = runtime.seconds();
        while (slideMotor.isBusy() && (runtime.seconds() - begintime) < 1.5) {
            telemetry.addData("In while loop in moveUpInch", slideMotor.getTargetPosition());
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
