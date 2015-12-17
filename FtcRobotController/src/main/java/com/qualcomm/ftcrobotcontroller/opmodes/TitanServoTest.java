package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class TitanServoTest extends OpMode {

    Servo arm1;
    Servo arm2;
    Servo arm3;
    double armDelta = 0.5;
    double armPosition;
    double armDelta1 = 0.5;
    double armPosition1;
    double armDelta2 = 0.5;
    double armPosition2;
    final static double ARM_MIN_RANGE = 0;
    final static double ARM_MAX_RANGE = 1;

    /**
     * Constructor
     */
    public TitanServoTest() {

    }


    public void init() {

        arm1 = hardwareMap.servo.get("servo1");
        arm2 = hardwareMap.servo.get("servo2");
        arm3 = hardwareMap.servo.get("servo3");

        armPosition = 0;
        arm1.setPosition(0);
        arm2.setPosition(0);
        arm3.setPosition(0);
    }

    public void loop() {

        // update the position of the arm.
        if (gamepad1.dpad_up) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            armPosition += armDelta;
        }

        if (gamepad1.dpad_left) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            armPosition1 += armDelta1;
        }

        if (gamepad1.dpad_right) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            armPosition2 -= armDelta2;
        }

        if (gamepad1.dpad_down) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            armPosition -= armDelta;
            armPosition1 -= armDelta1;
            armPosition2 += armDelta2;

        }


        // clip the position values so that they never exceed their allowed range.
        //right = right * 2;
        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        armPosition1 = Range.clip(armPosition1, ARM_MIN_RANGE, ARM_MAX_RANGE);
        armPosition2 = Range.clip(armPosition2, ARM_MIN_RANGE, ARM_MAX_RANGE);


        // write position values to the wrist and claw servo
        arm1.setPosition(armPosition);
        arm2.setPosition(armPosition1);
        arm3.setPosition(armPosition2);

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

