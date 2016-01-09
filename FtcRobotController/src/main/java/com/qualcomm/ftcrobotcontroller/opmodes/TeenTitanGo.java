package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by mars on 1/9/16.
 */
public class TeenTitanGo extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorScore;

    Servo rightTriggerArm;
    Servo leftTriggerArm;
    double rightTriggerArmDelta = 0.5;
    double rightTriggerArmPosition;
    double leftTriggerArmDelta = 0.5;
    double leftTriggerArmPosition;
    final static double ARM_MIN_RANGE  = 0;
    final static double ARM_MAX_RANGE  = 1;

    public TeenTitanGo() {

    }

    public void initTriggerArms() {



    }

    public void initMotors() {

        motorRight = hardwareMap.dcMotor.get("motor_right");
        motorLeft = hardwareMap.dcMotor.get("motor_left");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    public void initScoreArm() {

        motorScore = hardwareMap.dcMotor.get("motor_score");
        motorScore.setDirection(DcMotor.Direction.REVERSE);
    }

    public void init() {

        initMotors();

        initScoreArm();

    }

    public void loopDriverMotors(){

        float throttle1 = -gamepad1.left_stick_y;
        float direction1 = gamepad1.left_stick_x;
        float right1 = throttle1 - direction1;
        float left1 = throttle1 + direction1;

        float throttle2 = -gamepad1.right_stick_y;
        float direction2 = gamepad1.right_stick_x;
        float right2 = throttle2 - direction2;
        float left2 = throttle2 + direction2;

        right1 = Range.clip(right1, -1, 1);
        left1 = Range.clip(left1, -1, 1);
        right2 = Range.clip(right2, -1, 1);
        left2 = Range.clip(left2, -1, 1);

        right1 = (float)scaleInput(right1);
        left1 =  (float)scaleInput(left1);
        right2 = (float)scaleInput(right2);
        left2 =  (float)scaleInput(left2);

        motorLeft.setPower(right1);
        motorRight.setPower(right2);


    }

    public void loopScoreMotor(){

        float throttle = -gamepad2.right_stick_y;
        float direction = gamepad2.right_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        motorScore.setPower(right);

    }

    public void loop(){

        loopDriverMotors();

        loopScoreMotor();

    }

    @Override
    public void stop() {

    }

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}