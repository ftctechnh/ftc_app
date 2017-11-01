package org.firstinspires.ftc.teamcode.ftc2016to2017season.OldCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "#11183: TeleOp CompetitionTrigger", group = "Robot")
@Disabled

public class TeleOpCompetitionTrigger extends OpMode {


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

    Servo lift_servo;
    DcMotor lift_motor;

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
*/public enum cap_ball_arm_state_type {
    CAP_BALL_INIT_POS,
    CAP_BALL_ARM_OPEN,
    CAP_BALL_BALL_HOLD,
    CAP_BALL_LIFT_BALL,
    CAP_BALL_DROP_BALL
}
    cap_ball_arm_state_type cap_ball_arm_state;

    static final int CYCLE_MS = 5000;     // period of each cycle(mili seconds)


    private ElapsedTime runtime = new ElapsedTime();
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
            /* lets reverse the direction of the right wheel motor*/
            rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
            rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");
        ballShooterMotor = hardwareMap.dcMotor.get("ballShooterMotor");
        /* get a reference to our ColorSensor object */
        //colorSensor = hardwareMap.colorSensor.get("sensor_color");
        lift_servo = hardwareMap.servo.get("capBallServo"); //config name

        lift_motor = hardwareMap.dcMotor.get("capBallMotor"); //config name
        lift_motor.setDirection(DcMotor.Direction.REVERSE);

        cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_INIT_POS;
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
        //BallShooter();
        shoot();

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

    /*public void BallShooter(){
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



    }*/

/*---------------------------------------------------------------------------------------------
*/
    public void shoot(){

        if (gamepad2.right_bumper){
        ballShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ballShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ballShooterMotor.setTargetPosition((int)(ballShooterMotor.getCurrentPosition() + (1478* shooterGearRatio)));
        ballShooterMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ballShooterMotor.setPower(1);
        while (ballShooterMotor.isBusy()){

        }

        ballShooterMotor.setPower(0);
        ballShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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


    public void CapBallLift()
    {

        /*
        CAP_BALL_INIT_POS,
        CAP_BALL_ARM_OPEN,
        CAP_BALL_BALL_HOLD,
        CAP_BALL_LIFT_BALL,
        CAP_BALL_DROP_BALL
        */

        //       INITIAL STATE        TRIGGER         FINAL STATE         Description
        //       CAP_BALL_INIT_POS    gamepad.x        CAP_BALL_ARM_OPEN   opening the arm
        //       CAP_BALL_ARM_OPEN      gamepad.y       CAP_BALL_BALL_HOLD  holding the ball
        //
        //
        //
        //
        switch (cap_ball_arm_state)
        {
            case CAP_BALL_INIT_POS:
                if (gamepad2.x)
                {
                    lift_servo.setPosition(.2);
                    cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_ARM_OPEN;
                }
                break;
            case CAP_BALL_ARM_OPEN:
                if (gamepad2.y)
                {
                    lift_servo.setPosition(.4);
                    cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_BALL_HOLD;
                }
                break;
            case CAP_BALL_BALL_HOLD: {
                lift_motor.setDirection(DcMotor.Direction.FORWARD);
                //  encoderlift(1.0, -120, 6);
                float leftY_gp1 = -gamepad2.left_stick_y;
                //  float rightY_gp1 = -gamepad1.right_stick_y;

                //run the motors by setting power to the motors with the game pad values
                lift_motor.setPower(leftY_gp1);
            }
            if (gamepad2.b)
            {
                lift_motor.setPower(0);
                cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_LIFT_BALL;
            }
            break;
            case CAP_BALL_LIFT_BALL:
                if (gamepad2.a)
                {
                    lift_servo.setPosition(.2);
                    cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_DROP_BALL;
                }
                break;
            case CAP_BALL_DROP_BALL:
            {
                lift_motor.setDirection(DcMotor.Direction.REVERSE);
                //  encoderlift(1.0, -120, 6);
                float leftY_gp1 = -gamepad2.left_stick_y;
                //  float rightY_gp1 = -gamepad1.right_stick_y;

                //run the motors by setting power to the motors with the game pad values
                lift_motor.setPower(leftY_gp1);
            }
            if (gamepad2.x)
            {
                lift_motor.setPower(0);
                lift_motor.setDirection(DcMotor.Direction.REVERSE);
                lift_servo.setPosition(.8);
                //  encoderlift(1.0, 120, 6);
                cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_INIT_POS;
            }
            break;
        }
    }

    public void encoderlift(double liftSpeed,
                            double lift_cm,
                            double timeoutS){
        int newLiftTarget;

        // Ensure that the opmode is still active
        {

            // Determine new target position, and pass to motor controller
            newLiftTarget = lift_motor.getCurrentPosition() + (int)(lift_cm * COUNTS_PER_CM);
            lift_motor.setTargetPosition(newLiftTarget);


            // Turn On RUN_TO_POSITION
            lift_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            lift_motor.setPower(Math.abs(liftSpeed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            while (lift_motor.isBusy()) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d ", newLiftTarget);
                telemetry.addData("Path2",  "Running at %7d ", lift_motor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            lift_motor.setPower(0);


            // Turn off RUN_TO_POSITION
            lift_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
/*
---------------------------------------------------------------------------------------------
*/
}}

