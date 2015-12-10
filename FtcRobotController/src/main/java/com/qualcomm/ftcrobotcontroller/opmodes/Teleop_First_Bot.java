package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the game_pad
 */

public class Teleop_First_Bot extends OpMode {

    final static double Bucket_servo_MIN_RANGE  = 0.00;
    final static double Bucket_servo_MAX_RANGE = 0.90;

    // position of the servo bucket.
    double Bucket_servoPosition;

    // amount to change the bucket servo position.
    double Bucket_servoDelta = 0.1;

    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    DcMotor motorArm;
    DcMotor motorSweeper;
    Servo Bucket_servo;
    /**
     * Constructor
     */
    public Teleop_First_Bot() {
    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    @Override
    public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 *   There are six motors.
		 *   "motor_1" is on the front right side of the bot.
		 *   "motor_2" is on the back right side of the bot.
		 *   "motor_3" is on the front left side of the bot.
		 *   "motor_4" is on the back left side of the bot.
		 *   "motor 3 and 4 are reversed"
		 *   "motor_5" is the motor powering the sweeper.
		 *   "motor_6" is the motor powering the arm.
		 *   "servo_1" is the servo that controls the bucket up and down.
		 *   ]
		 */

        motorLeftFront = hardwareMap.dcMotor.get("motor_left_front");
        motorLeftBack = hardwareMap.dcMotor.get("motor_left_back");
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        motorRightFront = hardwareMap.dcMotor.get("motor_right_front");
        motorRightBack = hardwareMap.dcMotor.get("motor_right_back");
        motorArm = hardwareMap.dcMotor.get("motor_arm");
        motorSweeper = hardwareMap.dcMotor.get("motor_sweeper");
        Bucket_servo = hardwareMap.servo.get("servo_bucket");

        // assign the starting position of the bucket.
        Bucket_servoPosition = 1;

    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Game_pad 1 controls the left motors via left stick (Y).
		 * Game_pad 1 controls the right motors via right stick (Y).
		 *
		 * Game_pad 2 controls the sweeper via forwards game pad "left bumper".
		 * Game_pad 2 controls the sweeper backwards via game pad " right bumper".
		 * Game_pad 2 controls the arm forward via game pad "dpad up"
		 * Game_pad 2 controls the arm backwards via game pad "dpad down"
		 * Game_pad 2 controls the bucket servo forward via game pad "a"
		 * Game_pad 2 controls the bucket servo backwards via game pad "b"
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left_power = gamepad1.left_stick_y;
        float right_power = gamepad1.right_stick_y;

        // note that the arm power is set to 50 %.
        boolean arm_power = gamepad2.dpad_up;
        boolean arm_power_one = gamepad2.dpad_down;

        // note that the sweeper can go backwards in case something  gets stuck.
        boolean sweeper_power = gamepad2.left_bumper;
        boolean sweeper_power1 = gamepad2.right_bumper;

        boolean bucket_servo_power_open = gamepad2.a;
        boolean bucket_servo_power_closed = gamepad2.b;

        // clip the right/left values so that the values never exceed +/- 1
        right_power = Range.clip(right_power, -1, 1);
        left_power = Range.clip(left_power, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right_power = (float) scaleInput(right_power);
        left_power = (float) scaleInput(left_power);

        // write the values to the motors
        motorLeftFront.setPower(left_power);
        motorLeftBack.setPower(left_power);
        motorRightBack.setPower(right_power);
        motorRightFront.setPower(right_power);

        // update the position of the arm.
        if (arm_power) {

            motorArm.setPower(-.50);
            telemetry.addData("The Arm Works ","Going Up");
        }
        else if (arm_power_one) {

            motorArm.setPower(.50);
            telemetry.addData("The Arm Works","Going Down");
        }
        else {
            motorArm.setPower(0);
            telemetry.addData( " The Arm ", " Power Is Set To Zero ");
        }

        // update the position of the sweeper
        if (sweeper_power) {

            motorSweeper.setPower(-1);
            telemetry.addData("The Sweeper","works Going Forward");
        }
        else if (sweeper_power1) {

            motorSweeper.setPower(1);
            telemetry.addData("The Sweeper " , "Works Going Backward" );
        }
        else {
            motorSweeper.setPower(0);
            telemetry.addData( " The sweeper", " Power Is Set To Zero" );
        }

        //open or close the bucket
        if (bucket_servo_power_open) {
            // if the A button is pushed on game_pad_1, increment the position of
            // the arm servo.
            Bucket_servoPosition += Bucket_servoDelta;
            telemetry.addData("servo","is open");
        }

        else if (bucket_servo_power_closed) {
            // if the b button is pushed on game_pad_1, decrease the position of
            // the arm servo.
            Bucket_servoPosition -= Bucket_servoDelta;
            telemetry.addData("servo"," is closed ");
        }

        // clip the position values so that they never exceed their allowed range.
        Bucket_servoPosition = Range.clip(Bucket_servoPosition, Bucket_servo_MIN_RANGE, Bucket_servo_MAX_RANGE);

        // write position values to the bucket servo
        Bucket_servo.setPosition(Bucket_servoPosition);

		/*
		 * Send telemetry data back to driver station.
		 */

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left_power));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right_power));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
