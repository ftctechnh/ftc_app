package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;

public class DragonoidsTeleOp extends DragonoidsOpMode {
    @Override
    public void init() {
        super.init();
    }
    @Override
    public void loop() {
        // Joystick values range from -1 to 1
        float forwardAmount = -gamepad1.left_stick_y;
        float turningAmount = -gamepad1.right_stick_x;

        forwardAmount = Range.clip(forwardAmount, -1, 1);
        turningAmount = Range.clip(turningAmount, -1, 1);
        forwardAmount = (float) scaleInput(forwardAmount);
        turningAmount = (float) scaleInput(turningAmount);

        // TEMPORARY HACK OF A FIX OH GOD NO PLEASE FIX THIS
        driveMotors.get("right").setPower(Range.clip(forwardAmount - turningAmount, (float) -1.0, (float)1.0));
        driveMotors.get("left").setPower(Range.clip(forwardAmount + turningAmount, (float) -1.0, (float)1.0));

        if (gamepad2.right_bumper) {
            // Turn on the conveyor
            auxMotors.get("conveyor").setPower(0.45);
        }
        else {
            //set conveyor motor to 0
            auxMotors.get("conveyor").setPower(0.0);
        }

        if (gamepad2.left_bumper) {
            //reverse the conveyor
            auxMotors.get("conveyor").setPower(-0.25);
        }
        else {
            //set conveyor motor to 0
            auxMotors.get("conveyor").setPower(0.0);
        }

        if (gamepad2.dpad_left) {
            //reverse the conveyor
            auxMotors.get("knocker").setPower(0.45);
        }
        else {
            //set conveyor motor to 0
            auxMotors.get("knocker").setPower(0.0);
        }
        if (gamepad2.dpad_right) {
            //reverse the conveyor
            auxMotors.get("knocker").setPower(-0.45);
        }
        else {
            //set conveyor motor to 0
            auxMotors.get("knocker").setPower(0.0);
        }
        if (gamepad2.a){
            //opens the gate
            servos.get("gate").setPosition(0.5);
        }
        else{
            //closes the gate
            servos.get("gate").setPosition(0.0);
        }

        super.loop();
    }
    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
    private float scaleInputOriginal(double dVal)  {
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
        return (float)dScale;
    }
    private double scaleInput (double value) {
        // Return the value (from -1 to 1) squared to scale it quadratically
        double magnitude = Math.pow(value, 2);
        if (value < 0) {
            return -1 * magnitude;
        }
        else {
            return magnitude;
        }
    }
}