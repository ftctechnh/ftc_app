package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class FourWheelDriveTrainOp extends OpMode {

    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor motorRightBack;
    DcMotor motorLeftBack;

    /**
     * Constructor
     */
    public FourWheelDriveTrainOp() {
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
		 *   "motor_1" is on the front left side of the bot.
		 *   "motor_2" is on the back left side of the bot.
		 *   "motor_3" is on the front right side of the bot.
		 *   "motor_4" is on the back right side of the bot.
		 */
        motorLeftFront = hardwareMap.dcMotor.get("motor_left_front");
        motorLeftBack = hardwareMap.dcMotor.get("motor_left_back");
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        motorRightFront = hardwareMap.dcMotor.get("motor_right_front");
        motorRightBack = hardwareMap.dcMotor.get("motor_right_back");
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
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float leftpower = -gamepad1.left_stick_y;
        float rightpower = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        rightpower = Range.clip(rightpower, -1, 1);
        leftpower = Range.clip(leftpower , -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        rightpower = (float)scaleInput(rightpower);
        leftpower =  (float)scaleInput(leftpower);

        // write the values to the motors
        motorLeftFront.setPower(leftpower);
        motorLeftBack.setPower(leftpower);
        motorRightFront.setPower(rightpower);
        motorRightBack.setPower(rightpower);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftpower));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightpower));
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
