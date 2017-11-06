package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "strafe basic")
//@Disabled
public class charlieTeleOp extends OpMode {


/*
    ---------------------------------------------------------------------------------------------

   Define the actuators we use in the robot here
*/
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;


/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/


    ColorSensor bColorSensorLeft;    // Hardware Device Object





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

    mecanumStrafe();

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

    public void mecanumStrafe(){

        //Joystick 1 Left X-axis
        double J1LX = gamepad1.left_stick_x;

        leftWheelMotorFront.setPower(J1LX);
        leftWheelMotorBack.setPower(-J1LX);
        rightWheelMotorFront.setPower(-J1LX);
        rightWheelMotorBack.setPower(J1LX);
    }
/*
---------------------------------------------------------------------------------------------
*/
}

