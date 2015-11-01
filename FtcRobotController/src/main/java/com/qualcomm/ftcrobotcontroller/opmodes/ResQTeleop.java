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

    final static double CLAMP_MIN_RANGE = 0.01;
    final static double CLAMP_MAX_RANGE = 0.70;

    double climbservoPosition;
    double climbservoPosition2;
    double clampPosition;
    double lastTime;

    double climbServoDelta = 0.19;
    double climbServo2Delta = -0.19;
    double clampDelta = 0.69;

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor linearSlide;
    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor harvester;

    Servo climbservo;
    Servo climbservo2;
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

        climbservo = hardwareMap.servo.get("climbservo");
        climbservoPosition = 0.0;
        climbservo2 = hardwareMap.servo.get("climbservo2");
        climbservoPosition2 = 1.0;
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
        if (gamepad1.x) {
            //harvester.setPower(.5);
        }
        if (gamepad1.b) {
            //harvester.setPower(0);
            linearSlide.setPower(0);
        }
        if (gamepad1.y) {
            linearSlide.setPower(.5);
        }
        if (gamepad1.a) {
            linearSlide.setPower(-.5);
        }
        if (gamepad1.dpad_left) {
            climbservoPosition -= climbServoDelta;
            climbservoPosition2 -= climbServo2Delta;
        }
        if (gamepad1.dpad_right) {
            climbservoPosition += climbServoDelta;
            climbservoPosition2 += climbServo2Delta;
        }
        climbservoPosition = Range.clip(climbservoPosition, CLIMBSERVO_MIN_RANGE, CLIMBSERVO_MAX_RANGE);
        climbservo.setPosition(climbservoPosition);
        climbservoPosition2 = Range.clip(climbservoPosition2, CLIMBSERVO2_MIN_RANGE, CLIMBSERVO2_MAX_RANGE);
        climbservo2.setPosition(climbservoPosition2);
    }
    public void stop() {

    }

}
