package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class TitanArmTest extends OpMode {

    DcMotor ArmMotor;
    Servo arm;
    double armPosition;

    /**
     * Constructor
     */
    public TitanArmTest() {

    }


    public void init() {

        arm = hardwareMap.servo.get("servo");
        ArmMotor = hardwareMap.dcMotor.get("motor");

        armPosition = 0;
        arm.setPosition(0);
    }

    public void loop() {
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;
        float throttle1 = -gamepad1.right_stick_y;
        float direction1 = gamepad1.right_stick_x;
        float right1 = throttle1 - direction1;
        float left1 = throttle1 + direction1;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);
        right1 = Range.clip(right1, -1, 1);
        left1 = Range.clip(left1, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);
        right1 = (float)scaleInput(right1);
        left1 =  (float)scaleInput(left1);

        // write the values to the motors
        ArmMotor.setPower(right);


        // update the position of the arm.


    }
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