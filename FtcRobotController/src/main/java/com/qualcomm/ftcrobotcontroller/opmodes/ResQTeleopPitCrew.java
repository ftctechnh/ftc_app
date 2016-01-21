package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.technicbots.MainRobot;

import java.util.Date;


public class ResQTeleopPitCrew extends OpMode {
    private ElapsedTime mStateTime = new ElapsedTime();
    //experimental
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
    Servo zipLineServo;

    private static int ADJUST_MOTOR_TARGET = 0;
    private static int LINEAR_MOTOR_TARGET = 0;

    private static double RELEASE_SERVO_INIT = 0; //Lower is left, higher is right
    private static double CLAMP_SERVO_INIT = 0.25;
    private static double TWIST_SERVO_INIT = 1;
    private static double HOOK_SERVO_INIT = 1;
    //Timing
    private Date resetStartingTime;
    private Date hangStartingTime;
    //States
    boolean reset_state = false;
    boolean hang_back_state = false;
    boolean up_pressed = false;

    OpticalDistanceSensor opticalDistanceSensor;
    UltrasonicSensor ultrasonicSensor;
    ColorSensor colorsensor;

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
        lButtonServo.setPosition(0.9);
        rButtonServo = hardwareMap.servo.get("rightbutton");
        rButtonServo.setPosition(0.7);
        //All clear servo
        //hookServo = hardwareMap.servo.get("signal");
       // hookServo.setPosition(HOOK_SERVO_INIT);
        //Zipline servo
        zipLineServo = hardwareMap.servo.get("zipline");
        zipLineServo.setPosition(1);

        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("light");
        colorsensor = hardwareMap.colorSensor.get("color");
        colorsensor.enableLed(false);
    }
    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    public void loop() {
        float throttle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;
        float secondThrottle = gamepad2.left_stick_y;
        float secondRightThrottle = gamepad2.right_stick_y;

        double distance = ultrasonicSensor.getUltrasonicLevel();
        double reflectance = opticalDistanceSensor.getLightDetected();

        if (Math.abs(throttle) < 0.01 && Math.abs(rightThrottle) < 0.01) {
            frontRightMotor.setPower(secondRightThrottle);
            backRightMotor.setPower(secondRightThrottle);
            frontLeftMotor.setPower(secondThrottle);
            backLeftMotor.setPower(secondThrottle);
        } else {
            frontRightMotor.setPower(rightThrottle);
            backRightMotor.setPower(rightThrottle);
            frontLeftMotor.setPower(throttle);
            backLeftMotor.setPower(throttle);
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
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            rButtonServo.setPosition(0.5);
            lButtonServo.setPosition(0.5);
            //hookServo.setPosition(0.5);
            twistServo.setPosition(0.5);
            releaseServo.setPosition(0.5);
            clampServo.setPosition(0);
            zipLineServo.setPosition(0.1);
        }
        if (gamepad1.dpad_down || gamepad1.dpad_down) {
            adjustMotor.setPower(0);
            /*leftsweeper.setPosition(0);
            rightsweeper.setPosition(0);
            buttonServo.setPosition(0);
            button2Servo.setPosition(0);
            climberservo.setPosition(0);*/
            //buttonservoPosition += buttonServoDelta;
        }
        if (gamepad1.dpad_left || gamepad2.dpad_left) {
            adjustMotor.setPower(0.05);
        }
        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            adjustMotor.setPower(-0.05);
        }
        if (gamepad1.left_trigger == 1 || gamepad2.left_trigger == 1) {
            //Linear Motor shrinks
            linearMotor.setPower(-0.5);
        } else if (gamepad1.right_trigger == 1 || gamepad2.right_trigger == 1) {
            //Linear Motor extends
            linearMotor.setPower(0.5);
        } else {
            //Linear Motor stops
            linearMotor.setPower(0);
            //hangMotor.setPower(0);
        }
       // boxservoPosition = Range.clip(boxservoPosition, BOXSERVO_MIN_RANGE, BOXSERVO_MAX_RANGE);
        //boxservo.setPosition(boxservoPosition);
        //telemetry.addData("Button Servo Position: ", buttonservoPosition);
        //telemetry.addData("Climb Servo Position: ", climbservo.getPosition());
        //telemetry.addData("Climb Servo Position2: ", climbservo2.getPosition());

        telemetry.addData("Reflectance Value", reflectance);
        telemetry.addData("Ultrasonic Value", distance);
        telemetry.addData("Red", colorsensor.red());
        telemetry.addData("Blue", colorsensor.blue());
    }
    public void stop() {

    }

}
