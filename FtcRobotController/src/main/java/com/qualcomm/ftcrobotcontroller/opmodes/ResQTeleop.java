package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.technicbots.MainRobot;


public class ResQTeleop extends OpMode {
    private ElapsedTime mStateTime = new ElapsedTime();
    //experimental
    final static double CLIMBSERVO_MIN_RANGE  = 0.01;
    final static double CLIMBSERVO_MAX_RANGE  = 1;
    final static double CLIMBSERVO2_MIN_RANGE  = 0.01;
    final static double CLIMBSERVO2_MAX_RANGE  = 1;
    final static double BUTTONSERVO_MIN_RANGE  = 0.01;
    final static double BUTTONSERVO_MAX_RANGE  = 1;
    final static double BOXSERVO_MIN_RANGE = 0.01;
    final static double BOXSERVO_MAX_RANGE = 1;
    final static double CLAMP_MIN_RANGE = 0.01;
    final static double CLAMP_MAX_RANGE = 0.70;
    final static double RELEASESERVO_MIN_RANGE = 0.01;
    final static double RELEASESERVO_MAX_RANGE = 1;
    final static double ADJUSTSERVO_MIN_RANGE = 0.01;
    final static double ADJUSTSERVO_MAX_RANGE = 1;

    double climbservoPosition;
    double climbservo2Position;
    double clampPosition;
    double buttonservoPosition;
    double boxservoPosition;
    double releaseservoPosition;
    double adjustservoPosition;

    double climbServoEnd = 0.7;
    double climbServo2End = 0.05;
    double buttonServoDelta = 0.1;
    double clampDelta = 0.69;
    double adjustServoLeft = 0.25;
    double adjustServoNull = 0.5;
    double adjustServoRight = 0.75;
    double releaseServoEnd = 0.5;

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor linearSlide;
    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor harvester;

    Servo climbservo;
    Servo climbservo2;
    Servo buttonservo;
    Servo clamp;
    Servo boxservo;
    Servo releaseservo;
    Servo adjustservo;
    MainRobot mainRobot;
    public ResQTeleop() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    public void init() {

        mainRobot = new MainRobot(leftMotor, rightMotor, null, null, null, null, null);
        leftMotor = hardwareMap.dcMotor.get("leftwheel");
        rightMotor = hardwareMap.dcMotor.get("rightwheel");
        harvester = hardwareMap.dcMotor.get("sweeper");
        linearSlide = hardwareMap.dcMotor.get("linearmotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setDirection(DcMotor.Direction.FORWARD);

        climbservo2 = hardwareMap.servo.get("leftclimb");
        climbservo2Position = 0.7;
        climbservo = hardwareMap.servo.get("rightclimb");
        climbservoPosition = 0;
        //buttonservo = hardwareMap.servo.get("buttonservo");
        //buttonservoPosition = 0.0;
        //boxservo = hardwareMap.servo.get("boxservo");
        //boxservoPosition = 0.65;
        releaseservo = hardwareMap.servo.get("release");
        releaseservoPosition = 0;
        adjustservo = hardwareMap.servo.get("adjustservo");
        //adjustservoPosition = 0;


        climbservo.setPosition(climbservoPosition);
        climbservo2.setPosition(climbservo2Position);
        //buttonservo.setPosition(buttonservoPosition);
        //boxservo.setPosition(boxservoPosition);
        releaseservo.setPosition(releaseservoPosition);
        adjustservo.setPosition(0.5);
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

        if (Math.abs(throttle) < 0.01 && Math.abs(rightThrottle) < 0.01) {
            rightMotor.setPower(secondRightThrottle);
            leftMotor.setPower(secondThrottle);
        } else {
            rightMotor.setPower(rightThrottle);
            leftMotor.setPower(throttle);
        }

        if (gamepad1.left_trigger == 1 || gamepad2.left_trigger == 1 ) {
            linearSlide.setPower(-0.5);
        } else if (gamepad1.right_trigger == 1 || gamepad2.right_trigger == 1){
            linearSlide.setPower(0.5);
        } else {
            linearSlide.setPower(0);
        }

        if (gamepad1.b || gamepad2.b) {
            //harvester.setPower(0);
            harvester.setPower(0);
        }
        if (gamepad1.y || gamepad2.y) {
            harvester.setPower(.5);
        }
        if (gamepad1.a || gamepad2.a) {
            harvester.setPower(-.5);
        }
        if (gamepad1.right_bumper || gamepad2.right_bumper) {
            climbservo.setPosition(climbServoEnd);
            climbservo2.setPosition(climbServo2End);
            //buttonservoPosition -= buttonServoDelta;
        }
        if (gamepad1.left_bumper || gamepad1.left_bumper) {
            climbservo.setPosition(0);
            climbservo2.setPosition(0.7);
            //buttonservoPosition += buttonServoDelta;
        }
        if (gamepad1.dpad_left || gamepad2.dpad_left) {
            adjustservo.setPosition(0);
        }
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            releaseservo.setPosition(0);
            adjustservo.setPosition(0.5);
        }
        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            adjustservo.setPosition(1);
        }
        if (gamepad1.dpad_down || gamepad2.dpad_down) {
            releaseservo.setPosition(releaseServoEnd);
        }
        climbservoPosition = Range.clip(climbservoPosition, CLIMBSERVO_MIN_RANGE, CLIMBSERVO_MAX_RANGE);
        climbservo2Position = Range.clip(climbservo2Position, CLIMBSERVO2_MIN_RANGE, CLIMBSERVO2_MAX_RANGE);
        //climbservo.setPosition(climbservoPosition);
        //climbservo2.setPosition(climbservoPosition2);
        //buttonservo.setPosition(buttonservoPosition);
        //boxservoPosition = Range.clip(boxservoPosition, BOXSERVO_MIN_RANGE, BOXSERVO_MAX_RANGE);
        releaseservoPosition = Range.clip(releaseservoPosition, RELEASESERVO_MIN_RANGE, RELEASESERVO_MAX_RANGE);
        adjustservoPosition = Range.clip(adjustservoPosition, ADJUSTSERVO_MIN_RANGE, ADJUSTSERVO_MAX_RANGE);
        //telemetry.addData("Button Servo Position: ", buttonservoPosition);
        telemetry.addData("Climb Servo Position: ", climbservo.getPosition());
        telemetry.addData("Adjust Servo Position: ", adjustservo.getPosition());
    }
    public void stop() {

    }

}