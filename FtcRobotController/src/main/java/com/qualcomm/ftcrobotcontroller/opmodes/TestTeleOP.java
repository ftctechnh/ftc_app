package com.qualcomm.ftcrobotcontroller.opmodes;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Robotics on 10/28/2015.
 */




public class TestTeleOP extends OpMode {
    DcMotor frontLeftMotor; //motor declarations, actual motor names will be later on
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;

    private static final String frontLeft =  "front_left";  //motor name defines
    private static final String frontRight = "front_right";
    private static final String backLeft = "back_left";
    private static final String backRight = "back_right";
    private static final String armName = "arm";
    private static final String clawName = "claw";

    private static final double armRest = 1.0;
    private static final double clawRest = 0.7;
    private static final double armStage1 = 0.8;
    private static final double armStage2 = 0.3;
    private static final double clawStage1 = 0.2;
    private static final double frontMotorMultiple = 0.75;
    private static final double backMotorMultiple = 1.0;



    //constructor
    public TestTeleOP() {
        //nope
    }

    //copy pasted from k9TeleOP
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        frontLeftMotor = hardwareMap.dcMotor.get(frontLeft);
        frontRightMotor = hardwareMap.dcMotor.get(frontRight);
        backRightMotor = hardwareMap.dcMotor.get(backLeft);
        backLeftMotor = hardwareMap.dcMotor.get(backRight);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        //backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        //frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection((DcMotor.Direction.REVERSE));
        //arm = hardwareMap.servo.get("servo_1");
        //claw = hardwareMap.servo.get("servo_6");
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        /* TANK STEERING MOTHERFUCKAH
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;
        */

        float leftThrottle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;


        // clip the right/left values so that the values never exceed +/- 1
        //right = Range.clip(right, -1, 1);
        //left = Range.clip(left, -1, 1); NO!

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        //leftThrottle = (float)scaleInput(leftThrottle);
        //rightThrottle =  (float)scaleInput(rightThrottle);

        // write the values to the motors

        backLeftMotor.setPower(leftThrottle * backMotorMultiple);
        frontLeftMotor.setPower(leftThrottle * frontMotorMultiple);
        backRightMotor.setPower(rightThrottle * backMotorMultiple);
        frontRightMotor.setPower(rightThrottle * frontMotorMultiple);
        // update the position of the arm
		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** F***YEAH!!!***");
        //telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        //telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftThrottle));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightThrottle));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
        telemetry.addData("Text", "****ROBOT IS STOPPED****");
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

        // return scaled value.
        return dScale;
    }
}
