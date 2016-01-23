package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by alex on 1/21/16.
 */
public class OhmIZeus extends OpMode {

    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;

    DcMotor motorLazySusan;

    DcMotor motorArmExtend;
    DcMotor motorArmVertical;

    Servo wrist;
    double wristDelta = 0.5;
    double wristPosition;
    final static double WRIST_MIN_RANGE  = 0;
    final static double WRIST_MAX_RANGE  = 1;


    public OhmIZeus() {


    }

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

        motorArmExtend = hardwareMap.dcMotor.get("motor_arm_extend");
        motorArmVertical = hardwareMap.dcMotor.get("motor_arm_vertical");

    }

    public void initWristServo() {

        wrist = hardwareMap.servo.get("servo_wrist");

        wristPosition = 0;
        wrist.setPosition(0);

    }



    public void init() {

        initMotors();
        initSusanMotors();
        initArmMotors();
        initWristServo();

    }

    /*
    private void setupThrottle(float x, float y) {
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
    }
    */

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

    public void loopSusanMotors(){

        if (gamepad1.right_bumper) {
            motorLazySusan.setPower(1.0);
        }
        else

        if (gamepad1.left_bumper){
            motorLazySusan.setPower(-1.0);
        }
        else
        {
            motorLazySusan.setPower(0.0);

        }



    }

    /*
        float throttle1 = -gamepad2.left_stick_y;
        float direction1 = gamepad2.left_stick_x;
        float right1 = throttle1 - direction1;
        float left1 = throttle1 + direction1;

        float throttle2 = -gamepad2.right_stick_y;
        float direction2 = gamepad2.right_stick_x;
        float right2 = throttle2 - direction2;
        float left2 = throttle2 + direction2;


        left1 = Range.clip(left1, -1, 1);
        right1 = Range.clip(right1, -1, 1);
        left2 = Range.clip(left2, -1, 1);
        right2 = Range.clip(right2, -1, 1);


        left1 =  (float)scaleInput(left1);
        right1 = (float)scaleInput(right1);
        left2 =  (float)scaleInput(left2);
        right2 = (float)scaleInput(right2);
    */

    public void loopArmMotors(){

        float throttle1 = -gamepad2.left_stick_y;
        float direction1 = gamepad2.left_stick_x;
        float right1 = throttle1 - direction1;
        float left1 = throttle1 + direction1;

        float throttle2 = -gamepad2.right_stick_y;
        float direction2 = gamepad2.right_stick_x;
        float right2 = throttle2 - direction2;
        float left2 = throttle2 + direction2;


        left1 = Range.clip(left1, -1, 1);
        right1 = Range.clip(right1, -1, 1);
        left2 = Range.clip(left2, -1, 1);
        right2 = Range.clip(right2, -1, 1);


        left1 =  (float)scaleInput(left1);
        right1 = (float)scaleInput(right1);
        left2 =  (float)scaleInput(left2);
        right2 = (float)scaleInput(right2);


        motorArmExtend.setPower(left1);
        motorArmExtend.setPower(right1);
        motorArmVertical.setPower(left2);
        motorArmVertical.setPower(right2);

    }


    public void loopWristServo() {


        if (gamepad2.dpad_up) {

            wristPosition = wristDelta;

        }

        if (gamepad2.dpad_left) {

            wristPosition += wristDelta;
        }

        if (gamepad2.dpad_right) {

            wristPosition -= wristDelta;
        }

        wristPosition = Range.clip(wristPosition, WRIST_MIN_RANGE, WRIST_MAX_RANGE);

        wrist.setPosition(wristPosition);

    }


    public void loop() {

        loopDriverMotors();
        loopSusanMotors();
        loopArmMotors();
        loopWristServo();
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