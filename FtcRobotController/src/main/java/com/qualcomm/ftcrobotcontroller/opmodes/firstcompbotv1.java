package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class firstcompbotv1 extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the arm servo approaches 0, the arm position moves up (away from the floor).
     * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
     */
    // TETRIX VALUES.

    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    DcMotor motorArm;
    DcMotor motorSweeper;

    /**
     * Constructor
     */
    public firstcompbotv1() {
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
		 *   "motor_2" is on the back right side of the bot.
		 *   There are six motors "motor_1","motor_2","motor_3","motor_4","motor_5" and "motor_6".
		 *   "motor_1" is on the front right side of the bot.
		 *   "motor_3" is on the front left side of the bot.
		 *   "motor_4" is on the back left side of the bot.
		 *   "motor 3 and 4 are reversed"
		 *   "motor_5" is the motor powering the sweeper.
		 *   "motor_6" is the motor powering the arm.
		 */
        motorLeftFront = hardwareMap.dcMotor.get("lf");
        motorLeftBack = hardwareMap.dcMotor.get("lb");
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        motorRightFront = hardwareMap.dcMotor.get("fr");
        motorRightBack = hardwareMap.dcMotor.get("rb");
        motorArm = hardwareMap.dcMotor.get("ma");
        motorSweeper = hardwareMap.dcMotor.get("ms");

    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1 controls the left motors via left stick (Y)
		 * Gamepad 1 controls the right motors via right stick (Y)
		 * Gamepad 1 controls the sweeper via game pad "a"
		 * Gamepad 1 controls the arm forward via game pad "left bumper"
		 * Gamepad 1 controls the are backwards via game pad "right bumper"
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float leftpower = -gamepad1.left_stick_y;
        float rightpower = -gamepad1.right_stick_y;

        boolean armpower = gamepad1.right_bumper;
        boolean armpower1 = gamepad1.left_bumper;
        boolean sweeperpower = gamepad1.b;

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
        motorRightBack.setPower(rightpower);
        motorRightFront.setPower(rightpower);
        // update the position of the arm.
        if (armpower) {

            motorArm.setPower(.5);
        }
        else if (armpower1) {

            motorArm.setPower(-.5);
        }
        else {
            motorArm.setPower(0);
        }
        // update the position of the sweeper


        if (sweeperpower) {
            motorSweeper.setPower(1);
        }
        else {
            motorSweeper.setPower(0);
        }

		/*
		 * Send telemetry data back to driver station.
		 */

        //telemetry.addData("Text", "*** Robot Data***");
        //telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftpower));
        //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightpower));
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
