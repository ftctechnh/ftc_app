package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by mars on 1/7/16.
 */
public class ContinuousServo extends OpMode {

    Servo arm1;
    double armDelta = 0.5;
    double armPosition;
    final static double ARM_MIN_RANGE = 0;
    final static double ARM_MAX_RANGE = 1;

    /**
     * Constructor
     */
    public ContinuousServo() {

    }

    public void init() {

        arm1 = hardwareMap.servo.get("servo1");

        armPosition = 180;

        arm1.setPosition(180);


    }

    public void loop() {

        // update the position of the arm.
        if (gamepad1.a) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            armPosition += armDelta;

        }

        if (gamepad1.b) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            armPosition -= armDelta;

        }




        // clip the position values so that they never exceed their allowed range.
        //right = right * 2;
        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);


        arm1.setPosition(armPosition);


    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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
