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
    DcMotor ballCollectorMotor;
    DcMotor ballShooterMotor;

    Servo lift_servo;
    DcMotor lift_motor;

    public Servo autoBeaconPresser;
    int servoCount = 0;
    double shooterGearRatio = 2.333;
    public String currentColorBeaconLeft = "blank";
    double servoLeftPos = 0;
    double servoRightPos = 1;
//asdfasdf

/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/


    ColorSensor bColorSensorLeft;    // Hardware Device Object



    /*
     ----------------------------------------------------------------------------------------------
    Declare global variables here
    */public enum cap_ball_arm_state_type {
        CAP_BALL_INIT_POS,
        CAP_BALL_ARM_OPEN,
        CAP_BALL_BALL_HOLD,
        CAP_BALL_LIFT_BALL,
        CAP_BALL_DROP_BALL;
    }

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
            /* lets reverse the direction of the right wheel motor*/
        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");
        ballShooterMotor = hardwareMap.dcMotor.get("ballShooterMotor");
        /* get a reference to our ColorSensor object */
        //colorSensor = hardwareMap.colorSensor.get("sensor_color");
//        lift_servo = hardwareMap.servo.get("capBallServo"); //config name

        //lift_motor = hardwareMap.dcMotor.get("capBallMotor"); //config name
        //lift_motor.setDirection(DcMotor.Direction.REVERSE);

        //cap_ball_arm_state = cap_ball_arm_state_type.CAP_BALL_BALL_HOLD;

//This is closed-loop speed control. Encoders are required for this mode.
// SetPower() in this mode is actually requesting a certain speed, based on the top speed of
// encoder 4000 pulses per second.
        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        autoBeaconPresser = hardwareMap.servo.get("ServoPress");
        bColorSensorLeft = hardwareMap.colorSensor.get("bColorSensorLeft");
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

    public void FourWheelDriveEncoder(){
        /*
        read the gamepad values and put into variables
         */
        float leftY_gp1 = (gamepad1.left_stick_y);//*leftWheelMotorFront.getMaxSpeed();
        float rightY_gp1 = (gamepad1.right_stick_y);//*leftWheelMotorFront.getMaxSpeed();
        float strafeStickLeft = (-gamepad1.left_trigger);//*leftWheelMotorFront.getMaxSpeed();
        float strafeStickRight = (-gamepad1.right_trigger);//*leftWheelMotorFront.getMaxSpeed();
        //run the motors by setting power to the motors with the game pad value

        if (Math.abs(strafeStickLeft) > 0){

            leftWheelMotorFront.setPower(-strafeStickLeft);
            leftWheelMotorBack.setPower(strafeStickLeft);
            rightWheelMotorFront.setPower(strafeStickLeft);
            rightWheelMotorBack.setPower(-strafeStickLeft);
            telemetry.addData("left front encoder value", leftWheelMotorFront.getCurrentPosition());
            telemetry.addData("right front encoder value", rightWheelMotorFront.getCurrentPosition());
            telemetry.update();
        }

        else if (Math.abs(strafeStickRight) > 0){

            leftWheelMotorFront.setPower(strafeStickRight);
            leftWheelMotorBack.setPower(-strafeStickRight);
            rightWheelMotorFront.setPower(-strafeStickRight);
            rightWheelMotorBack.setPower(strafeStickRight);
            telemetry.addData("left front encoder value", leftWheelMotorFront.getCurrentPosition());
            telemetry.addData("right front encoder value", rightWheelMotorFront.getCurrentPosition());
            telemetry.update();
        }

        else {
            leftWheelMotorFront.setPower(leftY_gp1);
            leftWheelMotorBack.setPower(leftY_gp1);
            rightWheelMotorFront.setPower(rightY_gp1);
            rightWheelMotorBack.setPower(rightY_gp1);
        }
    }



        //run the motors by setting power to the motors with the game pad values
        //leftWheelMotorFront.setPower(leftY_gp1);
        //leftWheelMotorBack.setPower(leftY_gp1);
        //rightWheelMotorFront.setPower(rightY_gp1);
        //rightWheelMotorBack.setPower(rightY_gp1);



    }


/*---------------------------------------------------------------------------------------------
*/



