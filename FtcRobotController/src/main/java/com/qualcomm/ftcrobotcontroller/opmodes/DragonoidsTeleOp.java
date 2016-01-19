package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;

public class DragonoidsTeleOp extends OpMode {
    @Override
    public void init() {
        DragonoidsGlobal.init(hardwareMap);
    }
    @Override
    public void loop() {
        // Joystick values range from -1 to 1
        float forwardAmount = -gamepad1.right_stick_x;
        float turningAmount = -gamepad1.left_stick_y;

        forwardAmount = Range.clip(scaleInput(forwardAmount), -1, 1);
        turningAmount = Range.clip(scaleInput(turningAmount), -1, 1);

        // TEMPORARY HACK OF A FIX OH GOD NO PLEASE FIX THIS
        double rightDrivePower = Range.clip(forwardAmount - turningAmount, -1.0, 1.0);
        double leftDrivePower = Range.clip(forwardAmount + turningAmount, -1.0, 1.0);
        DragonoidsGlobal.setDrivePower(rightDrivePower, leftDrivePower);

        if (gamepad2.right_bumper) {
            // Turn on the conveyor
            DragonoidsGlobal.conveyor.setPower(0.45);
        }
        else if (gamepad2.left_bumper) {
            // Reverse the conveyor
            DragonoidsGlobal.conveyor.setPower(-0.25);
        }
        else {
            // Stop conveyor motor
            DragonoidsGlobal.conveyor.setPower(0.0);
        }

        if (gamepad2.dpad_left) {
            DragonoidsGlobal.knocker.setPower(0.45);
        }
        else if (gamepad2.dpad_right) {
            DragonoidsGlobal.knocker.setPower(-0.45);
        }
        else {
            DragonoidsGlobal.knocker.setPower(0.0);
        }
//      Commented cause we don't have it
        if (gamepad2.a){
            // Open the gate
            DragonoidsGlobal.gate.setPosition(0.5);
        }
        else{
            // Close the gate
            DragonoidsGlobal.gate.setPosition(0.0);
        }

        this.outputTelemetry();
    }
    private void outputTelemetry() {
        //telemetry.addData("Right drive motor power", driveMotors.get("rightOneDrive").getPower());
        //telemetry.addData("Left drive motor power", driveMotors.get("leftOneDrive").getPower());
        telemetry.addData("Conveyor motor power", DragonoidsGlobal.conveyor.getPower());
        telemetry.addData("Knocker motor power", DragonoidsGlobal.knocker.getPower());
    }
    @Override
    public void stop() {
        // Stop all motors
        DragonoidsGlobal.stopAll();
        super.stop();
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
    private float scaleInput (double value) {
        // Return the value (from -1 to 1) squared to scale it quadratically
        float magnitude = (float) Math.pow(value, 2);
        if (value < 0) {
            return -1 * magnitude;
        }
        else {
            return magnitude;
        }
    }
}