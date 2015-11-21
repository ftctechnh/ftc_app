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

    double climbservoPosition;
    double climbservoPosition2;
    double clampPosition;
    double buttonservoPosition;
    double boxservoPosition;
    double lastTime;

    double climbServoEnd = 0.95;
    double climbServo2End = 0.05;
    double buttonServoDelta = 0.1;
    double clampDelta = 0.69;
    double boxServoRight = 1;
    double boxServoLeft = 0.25;
    double boxServoNull = 0.65;

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

        climbservo = hardwareMap.servo.get("climbservo");
        climbservoPosition = 0.4;
        climbservo2 = hardwareMap.servo.get("climbservo2");
        climbservoPosition2 = 0.7;
        //buttonservo = hardwareMap.servo.get("buttonservo");
        //buttonservoPosition = 0.0;
        boxservo = hardwareMap.servo.get("boxservo");
        boxservoPosition = 0.65;


        climbservo.setPosition(climbservoPosition);
        climbservo2.setPosition(climbservoPosition2);
        //buttonservo.setPosition(buttonservoPosition);
        boxservo.setPosition(boxservoPosition);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    public void loop() {
        float throttle = -gamepad1.left_stick_y;
        float rightThrottle = -gamepad1.right_stick_y;
        float secondThrottle = -gamepad2.left_stick_y;
        float secondRightThrottle = -gamepad2.right_stick_y;

        if (Math.abs(throttle) < 0.01 && Math.abs(rightThrottle) < 0.01) {
            rightMotor.setPower(secondRightThrottle);
            leftMotor.setPower(secondThrottle);
        } else {
            rightMotor.setPower(rightThrottle);
            leftMotor.setPower(throttle);
        }

        if (gamepad1.left_trigger == 1 || gamepad2.left_trigger == 1 ) {
            harvester.setPower(-0.5);
        } else if (gamepad1.right_trigger == 1 || gamepad2.right_trigger == 1){
            harvester.setPower(0.5);
        } else {
            harvester.setPower(0);
        }

        if (gamepad1.b || gamepad2.b) {
            //harvester.setPower(0);
            linearSlide.setPower(0);
        }
        if (gamepad1.y || gamepad2.y) {
            linearSlide.setPower(.5);
        }
        if (gamepad1.a || gamepad2.a) {
            linearSlide.setPower(-.5);
        }
        if (gamepad1.right_bumper || gamepad2.right_bumper) {
            climbservo.setPosition(climbServoEnd);
            climbservo2.setPosition(climbServo2End);
            //buttonservoPosition -= buttonServoDelta;
        }
        if (gamepad1.left_bumper || gamepad1.left_bumper) {
            climbservo.setPosition(climbservoPosition);
            climbservo2.setPosition(climbservoPosition2);
            //buttonservoPosition += buttonServoDelta;
        }
        if (gamepad1.dpad_left || gamepad2.dpad_left) {
            boxservo.setPosition(boxServoLeft);
        }
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            boxservo.setPosition(boxServoNull);
        }
        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            boxservo.setPosition(boxServoRight);
        }
        climbservoPosition = Range.clip(climbservoPosition, CLIMBSERVO_MIN_RANGE, CLIMBSERVO_MAX_RANGE);
        //climbservo.setPosition(climbservoPosition);
        climbservoPosition2 = Range.clip(climbservoPosition2, CLIMBSERVO2_MIN_RANGE, CLIMBSERVO2_MAX_RANGE);
        //climbservo2.setPosition(climbservoPosition2);
        //buttonservoPosition = Range.clip(buttonservoPosition, BUTTONSERVO_MIN_RANGE, BUTTONSERVO_MAX_RANGE);
        //buttonservo.setPosition(buttonservoPosition);
        boxservoPosition = Range.clip(boxservoPosition, BOXSERVO_MIN_RANGE, BOXSERVO_MAX_RANGE);
        //telemetry.addData("Button Servo Position: ", buttonservoPosition);
        telemetry.addData("Climb Servo Position: ", climbservo.getPosition());
        telemetry.addData("Climb Servo Position2: ", climbservo2.getPosition());
    }
    public void stop() {

    }

}
