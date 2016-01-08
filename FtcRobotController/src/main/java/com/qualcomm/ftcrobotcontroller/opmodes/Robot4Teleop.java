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
    Servo hookServo;

    private static int ADJUST_MOTOR_TARGET = 0;
    private static int LINEAR_MOTOR_TARGET = 0;

    private static double RELEASE_SERVO_INIT = 0; //Lower is left, higher is right
    private static double CLAMP_SERVO_INIT = 0.32;
    private static double TWIST_SERVO_INIT = 1;
    private static double HOOK_SERVO_INIT = 1;
    //Timing
    private Date resetStartingTime;
    private Date hangStartingTime;
    //States
    boolean reset_state = false;
    boolean hang_back_state = false;
    boolean up_pressed = false;

    public void init() {
        //driving wheel motors
        frontLeftMotor = hardwareMap.dcMotor.get("frontL");
        frontRightMotor = hardwareMap.dcMotor.get("frontR");
        backLeftMotor = hardwareMap.dcMotor.get("backL");
        backRightMotor = hardwareMap.dcMotor.get("backR");
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        //linear slide tilt motor
        adjustMotor = hardwareMap.dcMotor.get("adjust");
        adjustMotor.getController().setMotorChannelMode(1, DcMotorController.RunMode.RESET_ENCODERS);
        adjustMotor.getController().setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        adjustMotor.setDirection(DcMotor.Direction.FORWARD);
        //linear slide extend motor
        linearMotor = hardwareMap.dcMotor.get("linear");
        linearMotor.getController().setMotorChannelMode(2, DcMotorController.RunMode.RESET_ENCODERS);
        linearMotor.getController().setMotorChannelMode(2, DcMotorController.RunMode.RUN_USING_ENCODERS);
        linearMotor.setDirection(DcMotor.Direction.FORWARD);
        //sweeper motor
        harvesterMotor = hardwareMap.dcMotor.get("sweeper");
        harvesterMotor.setDirection(DcMotor.Direction.FORWARD);
        //Hang Motor
        //hangMotor = hardwareMap.dcMotor.get("hang");
        //hangMotor.setDirection(DcMotor.Direction.FORWARD);

        //Box servos
        twistServo = hardwareMap.servo.get("twist");
        twistServo.setPosition(TWIST_SERVO_INIT);
        releaseServo = hardwareMap.servo.get("release");
        releaseServo.setPosition(RELEASE_SERVO_INIT);
        //Mountain clamp servo
        clampServo = hardwareMap.servo.get("clamp");
        clampServo.setPosition(CLAMP_SERVO_INIT);
        //Button Servos(AUTONOMOUS ONLY)
        lButtonServo = hardwareMap.servo.get("leftbutton");
        lButtonServo.setPosition(0.2);
        rButtonServo = hardwareMap.servo.get("rightbutton");
        rButtonServo.setPosition(1);
        //All clear servo
        hookServo = hardwareMap.servo.get("signal");
        hookServo.setPosition(HOOK_SERVO_INIT);
    }
    //Set Motors method
    public void setMotors(double FrontL, double FrontR, double BackL, double BackR){
        frontLeftMotor.setPower(FrontL);
        frontRightMotor.setPower(FrontR);
        backLeftMotor.setPower(BackL);
        backRightMotor.setPower(BackR);
    }

    public void loop() {
        //DON'T TOUCH(Driving and Joystick controls)
        float throttle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;
        float secondThrottle = gamepad2.left_stick_y;
        float secondRightThrottle = gamepad2.right_stick_y;
        //DON'T TOUCH(Dead zone code)
        throttle = (Math.abs(throttle) < 0.3) ? 0 : throttle;
        rightThrottle = (Math.abs(rightThrottle) < 0.05) ? 0 : rightThrottle;
        //DON'T TOUCH(Calling the Set Motors method)
        setMotors(throttle, rightThrottle, throttle, rightThrottle);

        //State when the linear slide is reset
        if (reset_state) {

            //Move the adjust and linear motor back to encoder 0 after 2 secs
                //Adjust motor should move between +25 and -25 of the target range
                if (adjustMotor.getCurrentPosition() > ADJUST_MOTOR_TARGET + 25) {
                    adjustMotor.setPower(-0.05);
                } else if (adjustMotor.getCurrentPosition() < ADJUST_MOTOR_TARGET - 25) {
                    adjustMotor.setPower(0.05);
                } else {
                    adjustMotor.setPower(0);
                    //Clear reset state after linear motor stops
                    if (Math.abs(linearMotor.getCurrentPosition() - LINEAR_MOTOR_TARGET) < 25) {
                        reset_state = false;
                    }
                }
                //Linear motor should move between +25 and -25 of the target range
                if (linearMotor.getCurrentPosition() > LINEAR_MOTOR_TARGET + 25) {
                    linearMotor.setPower(-0.5);
                } else if (linearMotor.getCurrentPosition() < LINEAR_MOTOR_TARGET - 25) {
                    linearMotor.setPower(0.5);
                } else {
                    linearMotor.setPower(0);
                    //Clear reset state after linear motor stops
                    if (Math.abs(adjustMotor.getCurrentPosition() - ADJUST_MOTOR_TARGET) < 25) {
                        reset_state = false;
                    }
                }
            }

         //DONT TOUCH
         /*else if (hang_back_state) {
            while (new Date().getTime() - hangStartingTime.getTime() > 4000) {
            hangMotor.setPower(-0.8);
            }
            hangMotor.setPower(0);
            hang_back_state = false;
        } */
        else {
            if (gamepad1.left_trigger == 0 && gamepad2.left_trigger == 0 && gamepad1.right_trigger == 0 && gamepad2.right_trigger == 0
                    && gamepad1.x == false && gamepad2.x == false) {
                //DONT TOUCH
                // hangMotor.setPower(0);
            }
            if (gamepad1.b == false && gamepad2.b == false && gamepad1.x == false && gamepad2.x == false)
            {
                //Stop the adjust motor if the x axis of the dpad is not touched
                adjustMotor.setPower(0);
            }
            if (gamepad1.x  || gamepad2.x) {
                //Adjust motor turns right and twist servo sets to turn left
                adjustMotor.setPower(-0.05);
                twistServo.setPosition(0.6);
            }
            if (gamepad1.b || gamepad2.b) {
                //Adjust motor turns left and twist servo sets to turn right
                adjustMotor.setPower(0.05);
                twistServo.setPosition(0.32);
            }
            if (gamepad1.y || gamepad2.y) {
                //Twist servo hits the climber zip lines
                //releaseServo.setPosition(RELEASE_SERVO_INIT);
                //twistServo.setPosition(1);
                resetStartingTime = new Date();
                releaseServo.setPosition(0.55);
                if (new Date().getTime() - resetStartingTime.getTime() > 2000) {
                    //Reset release servo
                    releaseServo.setPosition(RELEASE_SERVO_INIT);
                    twistServo.setPosition(TWIST_SERVO_INIT);
                }

            }
            if (gamepad1.a || gamepad2.a) {
                //Start the reset state
                //Release the box's contents
                //twistServo.setPosition(0.2);
                releaseServo.setPosition(RELEASE_SERVO_INIT);
                twistServo.setPosition(TWIST_SERVO_INIT);
                reset_state = true;
            }
            if (gamepad1.right_bumper || gamepad2.right_bumper) {
                //Harvester Move
                harvesterMotor.setPower(0.3);
            }
            if (gamepad1.left_bumper || gamepad1.left_bumper) {
                //Harvester Stop
                harvesterMotor.setPower(0);
            }
            if (gamepad1.dpad_left || gamepad2.dpad_left) {
                //DONT TOUCH(x not enabled)
                //hang_back_state = true;
                //hangStartingTime = new Date();
                //hangMotor.setPower(0.5);
                //temp way to reset all clear servo
                hookServo.setPosition(1);
            }
            if (gamepad1.dpad_up || gamepad2.dpad_up) {
                //Clamp Servo up
                clampServo.setPosition(CLAMP_SERVO_INIT);
            }
            if (gamepad1.dpad_right || gamepad2.dpad_right) {
                //Hook servo hits the all clear signal
                hookServo.setPosition(0.20);
            }
            if (gamepad1.dpad_down || gamepad2.dpad_down) {
                //Clamp Servo down
                clampServo.setPosition(0);
            }
            if (gamepad1.left_trigger == 1 || gamepad2.left_trigger == 1) {
                //Linear Motor shrinks
                linearMotor.setPower(-0.5);
                //DONT TOUCH
                if (up_pressed) {
                   // hangMotor.setPower(-0.5);
                }
            } else if (gamepad1.right_trigger == 1 || gamepad2.right_trigger == 1) {
                //Linear Motor extends
                linearMotor.setPower(0.5);
                //DONT TOUCH
                if (up_pressed) {
                   // hangMotor.setPower(0.5);
                }
            } else {
                //Linear Motor stops
                linearMotor.setPower(0);
                //hangMotor.setPower(0);
            }
            //Telemetry displays the encoder values
            telemetry.addData("Current Adjust Encoder Value: ", adjustMotor.getCurrentPosition());
            telemetry.addData("Current Linear Encoder Value: ", linearMotor.getCurrentPosition());
            //telemetry.addData("Current Hang Power: ", hangMotor.getPower());
        }
    }
    public void stop() {

    }

}