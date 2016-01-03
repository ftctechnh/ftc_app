package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Date;

public class Robot4Teleop extends OpMode {

    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor adjustMotor;
    DcMotor linearMotor;
    DcMotor harvesterMotor;
    //DcMotor hangMotor;

    Servo clampServo;
    Servo twistServo;
    Servo releaseServo;
    Servo lButtonServo;
    Servo rButtonServo;
    //Servo hookServo;

    private static int ADJUST_MOTOR_TARGET = 0;
    private static int LINEAR_MOTOR_TARGET = 0;
    private static double RELEASE_SERVO_INIT = 0.5;
    private static double CLAMP_SERVO_INIT = 0.28;
    private static double TWIST_SERVO_INIT = 0.5;
    private Date resetStartingTime;
    private Date hangStartingTime;

    boolean reset_state = false;
    boolean hang_back_state = false;
    boolean up_pressed = false;

    public void init() {
        frontLeftMotor = hardwareMap.dcMotor.get("frontL");
        frontRightMotor = hardwareMap.dcMotor.get("frontR");
        backLeftMotor = hardwareMap.dcMotor.get("backL");
        backRightMotor = hardwareMap.dcMotor.get("backR");
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        adjustMotor = hardwareMap.dcMotor.get("adjust");
        adjustMotor.getController().setMotorChannelMode(1, DcMotorController.RunMode.RESET_ENCODERS);
        adjustMotor.getController().setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        adjustMotor.setDirection(DcMotor.Direction.FORWARD);
        linearMotor = hardwareMap.dcMotor.get("linear");
        linearMotor.getController().setMotorChannelMode(2, DcMotorController.RunMode.RESET_ENCODERS);
        linearMotor.getController().setMotorChannelMode(2, DcMotorController.RunMode.RUN_USING_ENCODERS);
        linearMotor.setDirection(DcMotor.Direction.FORWARD);
        harvesterMotor = hardwareMap.dcMotor.get("sweeper");
        harvesterMotor.setDirection(DcMotor.Direction.FORWARD);
        //hangMotor = hardwareMap.dcMotor.get("hang");
        //hangMotor.setDirection(DcMotor.Direction.FORWARD);

        twistServo = hardwareMap.servo.get("twist");
        twistServo.setPosition(TWIST_SERVO_INIT);
        releaseServo = hardwareMap.servo.get("release");
        releaseServo.setPosition(RELEASE_SERVO_INIT);
        clampServo = hardwareMap.servo.get("clamp");
        clampServo.setPosition(CLAMP_SERVO_INIT);
        lButtonServo = hardwareMap.servo.get("leftbutton");
        lButtonServo.setPosition(0);
        rButtonServo = hardwareMap.servo.get("rightbutton");
        rButtonServo.setPosition(1);
        //hookServo = hardwareMap.servo.get("hook");
       // hookServo.setPosition(1);

    }

    public void setMotors(double FrontL, double FrontR, double BackL, double BackR){
        frontLeftMotor.setPower(FrontL);
        frontRightMotor.setPower(FrontR);
        backLeftMotor.setPower(BackL);
        backRightMotor.setPower(BackR);
    }

    public void loop() {
        float throttle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;
        float secondThrottle = gamepad2.left_stick_y;
        float secondRightThrottle = gamepad2.right_stick_y;

        throttle = (Math.abs(throttle) < 0.3) ? 0 : throttle;
        rightThrottle = (Math.abs(rightThrottle) < 0.05) ? 0 : rightThrottle;
        setMotors(throttle, rightThrottle, throttle, rightThrottle);
        if (reset_state) {
            twistServo.setPosition(0.5);
            releaseServo.setPosition(0.58);
            if (new Date().getTime() - resetStartingTime.getTime() > 2000) {
                releaseServo.setPosition(RELEASE_SERVO_INIT);
                if (adjustMotor.getCurrentPosition() > ADJUST_MOTOR_TARGET + 25) {
                    adjustMotor.setPower(-0.05);
                } else if (adjustMotor.getCurrentPosition() < ADJUST_MOTOR_TARGET - 25) {
                    adjustMotor.setPower(0.05);
                } else {
                    adjustMotor.setPower(0);
                    if (Math.abs(linearMotor.getPower()) < .01) {
                        reset_state = false;
                    }
                }
                if (linearMotor.getCurrentPosition() > LINEAR_MOTOR_TARGET + 25) {
                    linearMotor.setPower(-0.5);
                } else if (linearMotor.getCurrentPosition() < LINEAR_MOTOR_TARGET - 25) {
                    linearMotor.setPower(0.5);
                } else {
                    linearMotor.setPower(0);
                    if (Math.abs(adjustMotor.getPower()) < .01) {
                        reset_state = false;
                    }
                }
            }
        } /*else if (hang_back_state) {
            while (new Date().getTime() - hangStartingTime.getTime() > 4000) {
            hangMotor.setPower(-0.8);
            }
            hangMotor.setPower(0);
            hang_back_state = false;
        } */else {
            if (gamepad1.x  || gamepad2.x) {
                    //hang_back_state = true;
                    //hangStartingTime = new Date();
                //hangMotor.setPower(0.5);
            }
            if (gamepad1.left_trigger == 0 && gamepad2.left_trigger == 0 && gamepad1.right_trigger == 0 && gamepad2.right_trigger == 0
                    && gamepad1.x == false && gamepad2.x == false) {
               // hangMotor.setPower(0);
            }
            if (gamepad1.b || gamepad2.b) {
                //hookServo.setPosition(0.05);
            }
            if (gamepad1.y || gamepad2.y) {
                clampServo.setPosition(1);
            }
            if (gamepad1.a || gamepad2.a) {
                clampServo.setPosition(CLAMP_SERVO_INIT);
            }
            if (gamepad1.right_bumper || gamepad2.right_bumper) {
                harvesterMotor.setPower(0.3);
            }
            if (gamepad1.left_bumper || gamepad1.left_bumper) {
                harvesterMotor.setPower(0);
            }
            if (gamepad1.dpad_left || gamepad2.dpad_left) {
                //adjustMotor.setTargetPosition(500);
                adjustMotor.setPower(0.05);
                twistServo.setPosition(0.6);
            }
            if (gamepad1.dpad_up || gamepad2.dpad_up) {
                //releaseServo.setPosition(RELEASE_SERVO_INIT);
                //hookServo.setPosition(0.85);
                up_pressed = true;
            }
            if (gamepad1.dpad_right || gamepad2.dpad_right) {
                adjustMotor.setPower(-0.05);
                twistServo.setPosition(0.4);
            }
            if (gamepad1.dpad_right == false && gamepad2.dpad_right == false && gamepad1.dpad_left == false && gamepad2.dpad_left == false) {
                adjustMotor.setPower(0);
            }
            if (gamepad1.dpad_down || gamepad2.dpad_down) {
                reset_state = true;
                resetStartingTime = new Date();
            }
            if (gamepad1.left_trigger == 1 || gamepad2.left_trigger == 1) {
                linearMotor.setPower(-0.5);
                if (up_pressed) {
                   // hangMotor.setPower(-0.5);
                }
            } else if (gamepad1.right_trigger == 1 || gamepad2.right_trigger == 1) {
                linearMotor.setPower(0.5);
                if (up_pressed) {
                   // hangMotor.setPower(0.5);
                }
            } else {
                linearMotor.setPower(0);
                //hangMotor.setPower(0);
            }
            telemetry.addData("Current Adjust Encoder Value: ", adjustMotor.getCurrentPosition());
            telemetry.addData("Current Linear Encoder Value: ", linearMotor.getCurrentPosition());
            //telemetry.addData("Current Hang Power: ", hangMotor.getPower());
        }
    }
    public void stop() {

    }

}