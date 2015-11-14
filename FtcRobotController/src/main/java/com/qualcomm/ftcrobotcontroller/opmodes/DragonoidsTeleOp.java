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
        // If y equals -1 then joystick is pushed all the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);
        right = scaleInput(right);
        left = scaleInput(left);

        driveMotors.get("right").setPower(right);
        driveMotors.get("left").setPower(left);

        super.loop();
    }
    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
    private float scaleInput(double dVal)  {
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
}