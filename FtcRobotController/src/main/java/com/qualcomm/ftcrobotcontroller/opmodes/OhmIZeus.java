package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by mars on 1/21/16.
 */
public class OhmIZeus extends OpMode {

    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;

    DcMotor motorLazySusan;
    DcMotor motorArmMovement;
    DcMotor motorWrist;

    Servo contServo;

    Servo bucketServo;
    double bucketDelta = 0.5;
    double bucketPosition;
    final static double BUCKET_MIN_RANGE  = 0;
    final static double BUCKET_MAX_RANGE  = 1;

    Servo guardServo;
    double guardDelta = 0.5;
    double guardPosition;
    final static double GUARD_MIN_RANGE  = 0;
    final static double GUARD_MAX_RANGE  = 1;

    public OhmIZeus() {}

    public void initMotors() {

        motorRight1 = hardwareMap.dcMotor.get("motor_1_right");
        motorRight2 = hardwareMap.dcMotor.get("motor_2_right");

        motorLeft1 = hardwareMap.dcMotor.get("motor_1_left");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2_left");
       
    }

    public void initSusanMotors() {

        motorLazySusan = hardwareMap.dcMotor.get("motor_lazy_susan");
    }

    public void initArmMotors() {

        motorArmMovement = hardwareMap.dcMotor.get("motor_arm_movement");
    }

    public void initWristMotor() {

        motorWrist = hardwareMap.dcMotor.get("motor_wrist");
    }

    public void initContinuousServo() {

        contServo = hardwareMap.servo.get("servo_continuous");
        contServo.setPosition(.5);
    }

    public void initBucketServo(){

        bucketServo = hardwareMap.servo.get("servo_bucket");

        bucketPosition = 1;
        bucketServo.setPosition(0);
    }

    public void initGuardServo(){

        guardServo = hardwareMap.servo.get("servo_guard");

        guardPosition = .5;
        guardServo.setPosition(.5);

    }

    public void init() {

        initMotors();
        initSusanMotors();
//        initArmMotors();
        initWristMotor();
//        initContinuousServo();
        initBucketServo();
        initGuardServo();
    }

   /* private void setupThrottle(float x, float y) {
        float throttle1 = -y;
        float direction1 = x;
        float right1 = throttle1 - direction1;
        float left1 = throttle1 + direction1;

        right1 = Range.clip(right1, -1, 1);
        left1 = Range.clip(left1, -1, 1);

        right1 = (float)scaleInput(right1);
        left1 =  (float)scaleInput(left1);

        return {
                right1, left1
        }
    } */

    public void loopDriverMotors() {

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

        motorRight1.setPower(right1);
        motorRight2.setPower(left1);
        motorLeft1.setPower(right2);
        motorLeft2.setPower(left2);
    }

    public void loopSusanMotor() {

        if (gamepad1.right_bumper || gamepad2.right_bumper) {
            motorLazySusan.setPower(-0.5);
        } else {
            if (gamepad1.left_bumper || gamepad2.left_bumper) {
                motorLazySusan.setPower(0.5);
            } else {
                motorLazySusan.setPower(0.0);
            }
        }
    }

    public void loopArmMotors() {        // this is the motor for the linear acuator

        if (gamepad1.x || gamepad2.x) {
            motorArmMovement.setPower(1);
        }
        else {
            if (gamepad1.y || gamepad2.y) {
                motorArmMovement.setPower(-1);
            } else {
                motorArmMovement.setPower(0.0);
            }
        }
        /*float throttle1 = -gamepad2.right_stick_y;
        float direction1 = gamepad2.right_stick_x;
        float right1 = throttle1 - direction1;
        float left1 = throttle1 + direction1;

        left1 = Range.clip(left1, -1, 1);
        right1 = Range.clip(right1, -1, 1);

        left1 =  (float)scaleInput(left1);
        right1 = (float)scaleInput(right1);

        motorArmMovement.setPower(left1);
        motorArmMovement.setPower(right1);
*/
    }


    public void loopWristMotor() {             // this is the motor for the wrist of our robot

        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            motorWrist.setPower(0.40);
        }
        else {
            if (gamepad1.dpad_down || gamepad2.dpad_down) {
                motorWrist.setPower(-0.40);
            } else {
                motorWrist.setPower(0.0);
            }
        }
    }

    public void loopContinuousServo() {          // this is how to use a continuous servo

        if (gamepad1.y){
            contServo.setPosition(1);
        }
        if (gamepad1.a){
            contServo.setPosition(0);
        }
        if (gamepad1.b){
            contServo.setPosition(.5);
        }

        telemetry.addData("Servo Pos", String.format("%.2f", contServo.getPosition()));
        telemetry.addData("Servo Dir", contServo.getDirection());   // this prints out what the servo is doing on th phones
    }

    public void loopBucketServo() {

        if (gamepad1.b || gamepad2.b) {
            bucketPosition -= bucketDelta;
        }
        if (gamepad1.a || gamepad2.a) {
            bucketPosition += bucketDelta;
        }

        bucketPosition = Range.clip(bucketPosition, BUCKET_MIN_RANGE, BUCKET_MAX_RANGE);

        telemetry.addData("bucket Pos",bucketPosition);

        bucketServo.setPosition(bucketPosition);

    }

    public void loopGuardServo() {

        if (gamepad1.right_stick_button || gamepad2.right_stick_button) {
            guardPosition += guardDelta;
        }
        if (gamepad1.left_stick_button || gamepad2.left_stick_button) {
            guardPosition -= guardDelta;
        }

        guardPosition = Range.clip(guardPosition, GUARD_MIN_RANGE, GUARD_MAX_RANGE);

        //telemetry.addData("bucket Pos",bucketPosition);

        guardServo.setPosition(guardPosition);
    }

    public void loop() {

        loopDriverMotors();
        loopSusanMotor();
//        loopArmMotors();
        loopWristMotor();
//        loopContinuousServo();
        loopBucketServo();
        loopGuardServo();
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
