package com.powerstackers.resq.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import static com.qualcomm.robotcore.hardware.DcMotor.Direction.FORWARD;

/**
 * Created by Derek on 10/10/2015.
 */
public class ProtobotTeleop extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the arm servo approaches 0, the arm position moves up (away from the floor).
     * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
     */
    // TETRIX VALUES.
    final static double servoRight_MIN_RANGE  = 0.20;
    final static double servoRight_MAX_RANGE  = 1.00;
    final static double servoLeft_MIN_RANGE  = 0.80;
    final static double servoLeft_MAX_RANGE  = 0.00;

    // position of the arm servo.
    double servoRightPosition;

    // amount to change the arm servo position.
    double servoRightDelta = 0.1;

    // position of the claw servo
    double servoLeftPosition;

    // amount to change the claw servo position by
    double servoLeftDelta = 0.1;

    DcMotor motorBrush;
    DcMotor motorLift;
    DcMotor motorFRight;
    DcMotor motorFLeft;
    DcMotor motorBRight;
    DcMotor motorBLeft;
    Servo servoLeft;
    Servo servoRight;

    public ProtobotTeleop() {

    }

    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        motorBrush = hardwareMap.dcMotor.get("motorBrush");
        motorLift = hardwareMap.dcMotor.get("motorLift");
        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorFRight = hardwareMap.dcMotor.get("motorFRight");
        motorFLeft = hardwareMap.dcMotor.get("motorFLeft");
        motorFRight.setDirection(DcMotor.Direction.REVERSE);
        motorBRight = hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);

        servoLeft = hardwareMap.servo.get("servoLeft");
        servoRight = hardwareMap.servo.get("servoRight");

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
		 * lift/Brushes via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) scaleInput(right);
        left = (float) scaleInput(left);


        // lift controls
        // Makes Lift go In
        if (gamepad1.a) {           //If A Is Pressed Make Lift Go Up
            motorLift.setPower(-1);
        } else if (gamepad1.y) {    //If Y Is Pressed Make Lift Go Down
            motorLift.setPower(1);
        } else {                    //Otherwise Stop Motor
            motorLift.setPower(0);
        }

        if (gamepad1.x) {           //If X Is Pressed Make Brushes Push Debris
            motorBrush.setPower(1);
        } else if (gamepad1.b) {    //If B Is Pressed Make Brushes Pickup Debris
            motorBrush.setPower(-1);
        } else {                    //Otherwise Stop Motor
            motorBrush.setPower(0);
        }

        // update the position of the arm.
        if (gamepad2.y) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            servoRightPosition += servoRightDelta;
        }

        if (gamepad2.x) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            servoRightPosition -= servoRightDelta;
        }

        // update the position of the claw
        if (gamepad2.b) {
            servoLeftPosition += servoLeftDelta;
        }

        if (gamepad2.a) {
            servoLeftPosition -= servoLeftDelta;
        }

        // clip the position values so that they never exceed their allowed range.
        servoRightPosition = Range.clip(servoRightPosition, servoRight_MIN_RANGE, servoRight_MAX_RANGE);
        servoLeftPosition = Range.clip(servoLeftPosition, servoLeft_MIN_RANGE, servoLeft_MAX_RANGE);

        // write position values to the wrist and claw servo
        servoRight.setPosition(servoRightPosition);
        servoLeft.setPosition(servoLeftPosition);

        // write the values to the motors
        motorBRight.setPower(right);
        motorBLeft.setPower(left);

		/*
		 * Send telemetry data back to driver station. N
        motorFRight.setPower(right);
        motorFLeft.setPower(left);ote that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        Object Leftposition = servoLeft.getPosition();
        Object Rightposition = servoRight.getPosition();
        telemetry.addData("Servo Left", "Position: " + String.valueOf(Leftposition));
        telemetry.addData("Servo Right", "Position: " + String.valueOf(Rightposition));
        //Object direction = motorLift.getDirection();
        //telemetry.addData("direction", "direction: " + String.valueOf(direction));

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

        // return scaled value.
        return dScale;
    }

}
