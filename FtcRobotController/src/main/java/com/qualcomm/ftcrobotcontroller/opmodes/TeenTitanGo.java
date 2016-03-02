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

    DcMotor rightWinchMotor;
    DcMotor leftWinchMotor;

    Servo rightTriggerArm;
    Servo leftTriggerArm;

    Servo bucketServo;

    double rightTriggerArmDelta = 0.5;
    double rightTriggerArmPosition;
    double leftTriggerArmDelta = 0.5;
    double leftTriggerArmPosition;

    double bucketServoDelta = 0.01;
    double bucketServoPosition;

    final static double ARM_MIN_RANGE  = 0;
    final static double ARM_MAX_RANGE  = 1;

    final static double BUCKET_MIN_RANGE  = 0;
    final static double BUCKET_MAX_RANGE  = 1;

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
//        leftTriggerArm.setDirection(Servo.Direction.REVERSE);

        rightTriggerArmPosition = 0.0;
        leftTriggerArmPosition = 0.3;

        rightTriggerArm.setPosition(rightTriggerArmPosition);
        leftTriggerArm.setPosition(leftTriggerArmPosition);
    }

    public void initBucketServo() {

        bucketServo = hardwareMap.servo.get("bucket_servo");
        bucketServo.setDirection(Servo.Direction.REVERSE);

        bucketServoPosition = 0.55;

        bucketServo.setPosition(bucketServoPosition);

    }

    public void initHarvestMotor() {

        harvestMotor = hardwareMap.dcMotor.get("harvester_motor");
    }

    public void initWinchMotors() {
  /*      try {
            rightWinchMotor = hardwareMap.dcMotor.get("right_winch");
            rightWinchMotor.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception fooExcep) {
            telemetry.addData("Failed to init rightWinchMotor");
            rightWinchMotor = null;
        }
        try {
            leftWinchMotor = hardwareMap.dcMotor.get("left_winch");
        } catch (Exception barExcep) {
            telemetry.addData("Failed to init leftWinchMotor");
            leftWinchMotor = null;
        }
    */
        rightWinchMotor = hardwareMap.dcMotor.get("right_winch");
        rightWinchMotor.setDirection(DcMotor.Direction.REVERSE);
        leftWinchMotor = hardwareMap.dcMotor.get("left_winch");
    }

    public void init() {

        initMotors();
        initScoreArm();
        initBucketServo();
        initHarvestMotor();
        initTriggerArms();
        initWinchMotors();
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

    public void loopBucketServo() {

        if (gamepad2.b) {
            bucketServoPosition += bucketServoDelta;
            bucketServoPosition = Range.clip(bucketServoPosition, BUCKET_MIN_RANGE, BUCKET_MAX_RANGE);
            bucketServo.setPosition(bucketServoPosition);
        }
        if (gamepad2.x) {
            bucketServoPosition -= bucketServoDelta;
            bucketServoPosition = Range.clip(bucketServoPosition, BUCKET_MIN_RANGE, BUCKET_MAX_RANGE);
            bucketServo.setPosition(bucketServoPosition);
        }
        if (gamepad2.y) {
            bucketServoPosition = .55;
            bucketServo.setPosition(bucketServoPosition);
        }

        telemetry.addData("Bucket Pos", bucketServo.getPosition());
    }

    public void loopTriggerArm() {

        if (gamepad2.dpad_right) {
            rightTriggerArmPosition += rightTriggerArmDelta;
            rightTriggerArmPosition = Range.clip(rightTriggerArmPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
            rightTriggerArm.setPosition(rightTriggerArmPosition);
        }
        else if (gamepad2.dpad_left) {
            leftTriggerArmPosition += leftTriggerArmDelta;
            leftTriggerArmPosition = Range.clip(leftTriggerArmPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
            leftTriggerArm.setPosition(leftTriggerArmPosition);
        } else {
            if (leftTriggerArm.getPosition() != 0.3) {
                leftTriggerArm.setPosition(0.3);
            }
            if (rightTriggerArm.getPosition() != 0.0) {
                rightTriggerArm.setPosition(0.0);
            }
        }

        telemetry.addData("Right Triggerarm", rightTriggerArm.getPosition());
        telemetry.addData("Left Triggerarm", leftTriggerArm.getPosition());

/*        if (gamepad2.x) {
            rightTriggerArm.setPosition(rightTriggerArmPosition);
            leftTriggerArm.setPosition(leftTriggerArmPosition);
        }
        else if (gamepad2.b) {
            rightTriggerArm.setPosition(rightTriggerArmPosition);
            leftTriggerArm.setPosition(leftTriggerArmPosition);
        } else {
            rightTriggerArm.setPosition(.4);
            leftTriggerArm.setPosition(.4);
        }
*/    }

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

    public void loopWinchMotors() {
/*
        float throttle4 = -gamepad2.left_trigger;
        float direction4 = gamepad2.right_trigger;
        float right4 = throttle4 - direction4;
        float left4 = throttle4 + direction4;

        right4 = (float) Range.clip(right4, 0, 0.01);
        left4 = (float) Range.clip(left4, 0, 0.01);

        right4 = (float)scaleInput(right4);
        left4 =  (float)scaleInput(left4);
*/

//        (this && that) this and that
//        (this || that) this or that
        // (this || that || those) this or that or those
        // (this && (that || those)) this and either that or those

/*        if (leftWinchMotor != null && gamepad2.left_trigger > 0) {
            leftWinchMotor.setPower(0.5);
        }
        if (leftWinchMotor != null && gamepad2.dpad_left) {
            leftWinchMotor.setPower(-0.5);
        }
        if (rightWinchMotor != null && gamepad2.right_bumper) {
            rightWinchMotor.setPower(0.5);
        }
        if (rightWinchMotor != null && gamepad2.dpad_right) {
            rightWinchMotor.setPower(-0.5);
        }
                /* Work In Progress
        if (leftWinchMotor != null && rightWinchMotor != null && gamepad2.dpad_up) {
            leftWinchMotor.setPower(0.0);
            rightWinchMotor.setPower(0.0);
        }
*/
        if (gamepad2.left_bumper) {
            leftWinchMotor.setPower(0.5);
        }
        else if (gamepad2.left_trigger > 0) {
            leftWinchMotor.setPower(-0.5);
        }
        else if (gamepad2.right_bumper) {
            rightWinchMotor.setPower(0.5);
        }
       else  if (gamepad2.right_trigger > 0) {
            rightWinchMotor.setPower(-0.5);
        }
        else {
            leftWinchMotor.setPower(0.0);
            rightWinchMotor.setPower(0.0);
        }
    }

    public void loop(){

        loopDriverMotors();
        loopScoreMotor();
        loopBucketServo();
        loopHarvestMotor();
        loopTriggerArm();
        loopWinchMotors();
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
