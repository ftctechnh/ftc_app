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
    DcMotor harvestMotor;

  //  Servo bucketServo;
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

    public void initMotors() {

        motorRight = hardwareMap.dcMotor.get("motor_right");
        motorLeft = hardwareMap.dcMotor.get("motor_left");
        motorRight.setDirection(DcMotor.Direction.REVERSE);


    }

    public void initScoreArm() {

        motorScore = hardwareMap.dcMotor.get("motor_score");
        motorScore.setDirection(DcMotor.Direction.REVERSE);

    }

    public void initTriggerArms() {

        rightTriggerArm = hardwareMap.servo.get("servo_right_trigger");
        leftTriggerArm = hardwareMap.servo.get("servo_left_trigger");
        rightTriggerArm.setDirection(Servo.Direction.REVERSE);
        leftTriggerArm.setDirection(Servo.Direction.REVERSE);

        rightTriggerArmPosition = 0;
        leftTriggerArmPosition = 0;

        rightTriggerArm.setPosition(0);
        leftTriggerArm.setPosition(0);

    }

    public void initHarvestMotor(){

        harvestMotor = hardwareMap.dcMotor.get("harvester_motor");
    }

 /*   public void initBucketServo(){

        bucketServo = hardwareMap.servo.get("bucket_servo");
        bucketServo.setPosition(.5);

    }
*/

    public void init() {

        initMotors();

        initScoreArm();

        initHarvestMotor();

//        initBucketServo();

        initTriggerArms();

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

    public void loopTriggerArm(){


        if (gamepad2.right_bumper) {

            rightTriggerArmPosition += rightTriggerArmDelta;
        }

        else {

            rightTriggerArmPosition -= rightTriggerArmDelta;

        }

        if (gamepad2.left_bumper) {

            leftTriggerArmPosition -= leftTriggerArmDelta;

        }

        else{

            leftTriggerArmPosition += leftTriggerArmDelta;

        }



        rightTriggerArmPosition = Range.clip(rightTriggerArmPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        leftTriggerArmPosition = Range.clip(leftTriggerArmPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);


        rightTriggerArm.setPosition(rightTriggerArmPosition);
        leftTriggerArm.setPosition(leftTriggerArmPosition);



    }

    public void loopHarvestMotor() {
        float throttle3 = -gamepad2.left_stick_y;
        float direction3 = gamepad2.left_stick_x;
        float right3 = throttle3 - direction3;
        float left3 = throttle3 + direction3;

        right3 = Range.clip(right3, -1, 1);
        left3 = Range.clip(left3, -1, 1);

        right3 = (float)scaleInput(right3);
        left3 =  (float)scaleInput(left3);

        harvestMotor.setPower(right3);


    }


    public void loop(){

        loopDriverMotors();

        loopScoreMotor();

        loopHarvestMotor();

        loopTriggerArm();

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