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

    final static double CLAMP_MIN_RANGE = 0.01;
    final static double CLAMP_MAX_RANGE = 0.70;

    double climbservoPositionStart = 0.0;
    double climbservoPositionStart2 = 1.0;
    double climbservoPositionWork = 0.4;
    double climbservoPositionWork2 =0.6;
    double clampPosition;
    double buttonservoPosition;
    double lastTime;

    double climbServoDelta = 0.10;
    double buttonServoDelta = 0.1;
    double climbServo2Delta = -0.10;
    double clampDelta = 0.69;

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor linearSlide;
    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor harvester;

    Servo climbservo;
    Servo climbservo2;
    Servo buttonservo;
    Servo clamp;

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
        //harvester = hardwareMap.dcMotor.get("sweeper");
        linearSlide = hardwareMap.dcMotor.get("linearmotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setDirection(DcMotor.Direction.FORWARD);

        //climbservo = hardwareMap.servo.get("climbservo");
        //climbservoPosition = 0.0;
        //climbservo2 = hardwareMap.servo.get("climbservo2");
        //climbservoPosition2 = 1.0;
        //buttonservo = hardwareMap.servo.get("buttonservo");
        //buttonservoPosition = 0.0;

        //climbservo.setPosition(climbservoPositionStart);
        //climbservo2.setPosition(climbservoPositionStart2);
        //buttonservo.setPosition(buttonservoPosition);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    public void loop() {
        float throttle = -gamepad1.left_stick_y;
        float rightThrottle = -gamepad1.right_stick_y;

        rightMotor.setPower(rightThrottle);
        leftMotor.setPower(throttle);
        if (gamepad1.x || gamepad2.x) {
            //harvester.setPower(.5);
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
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            //climbservoPosition -= climbServoDelta;
            //climbservoPosition2 -= climbServo2Delta;
            //climbservo.setPosition(climbservoPositionStart);
            //climbservo2.setPosition(climbservoPositionStart2);
            //buttonservoPosition -= buttonServoDelta;
        }
        if (gamepad1.dpad_down || gamepad1.dpad_down) {
            //climbservoPosition += climbServoDelta;
            //climbservoPosition2 += climbServo2Delta;
            //climbservo.setPosition(climbservoPositionWork);
            //climbservo2.setPosition(climbservoPositionWork2);
            //buttonservoPosition += buttonServoDelta;
        }
        //climbservoPosition = Range.clip(climbservoPosition, CLIMBSERVO_MIN_RANGE, CLIMBSERVO_MAX_RANGE);
        //climbservo.setPosition(climbservoPosition);
        //climbservoPosition2 = Range.clip(climbservoPosition2, CLIMBSERVO2_MIN_RANGE, CLIMBSERVO2_MAX_RANGE);
        //climbservo2.setPosition(climbservoPosition2);
        //buttonservoPosition = Range.clip(buttonservoPosition, BUTTONSERVO_MIN_RANGE, BUTTONSERVO_MAX_RANGE);
        //buttonservo.setPosition(buttonservoPosition);
        //telemetry.addData("Button Servo Position: ", buttonservoPosition);
        //telemetry.addData("Climb Servo Position: ", climbservoPosition);
        //telemetry.addData("Climb Servo Position2: ", climbservoPosition2);
    }
    public void stop() {

    }

}
