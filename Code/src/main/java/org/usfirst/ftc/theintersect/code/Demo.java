package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * Main TeleOp
 */
@org.swerverobotics.library.interfaces.TeleOp(name = "Demo")
public class Demo extends SynchronousOpMode {
    //Declare hardware
    static DcMotor rightWheel;
    static DcMotor leftWheel;

    static DcMotor linearSlide;

    static DcMotor sweeper;

    static DcMotor leftHangString;
    static DcMotor rightHangString;

    static Servo tubeTilt;
    static Servo tubeExtender;

    static Servo mountainClimber;
    static Servo mountainClimberRelease;

    static Servo bumper;

    static Servo leftChurroHook;
    static Servo rightChurroHook;

    static Servo leftBarHook;
    static Servo rightBarHook;

    //Declare gamepad objects
    float rightWheelPower;
    float leftWheelPower;

    boolean linearSlideForward = false;
    boolean linearSlideBackward = false;

    boolean containerTiltRight = false;
    boolean containerTiltLeft = false;

    boolean sweeperForward = false;
    boolean sweeperBackward = false;

    boolean tubeExtend = false;
    boolean tubeRetract = false;

    boolean positionClimbersForward = false;
    boolean positionClimbersBackward = false;
    boolean releaseClimbers = false;

    boolean bumperUp = false;
    boolean bumperDown = false;
    boolean toggleChurroHooks = false;
    boolean churroHooksDown = false;
    boolean churroHooksUp = false;

    boolean barHooksDown = false;
    boolean toggleBarHooks = false;

    boolean extendLeftHangString = false;
    boolean extendRightHangString = false;
    boolean retractRightHangString = false;
    boolean retractLeftHangString = false;

    boolean extendLeftHangStringTurbo = false;
    boolean extendRightHangStringTurbo = false;
    boolean retractRightHangStringTurbo = false;
    boolean retractLeftHangStringTurbo = false;

    float slowDriveBack;
    float slowDriveForward;

    @Override
    public void main() throws InterruptedException {
        //Initialize hardware
        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        leftWheel = hardwareMap.dcMotor.get("leftWheel");

        linearSlide = hardwareMap.dcMotor.get("linearSlide");

        leftHangString = hardwareMap.dcMotor.get("leftHangString");
        rightHangString = hardwareMap.dcMotor.get("rightHangString");


        tubeTilt = hardwareMap.servo.get("tubeTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");

        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");

        sweeper = hardwareMap.dcMotor.get("sweeper");

        bumper = hardwareMap.servo.get("bumper");

        leftChurroHook = hardwareMap.servo.get("leftChurroHook");
        rightChurroHook = hardwareMap.servo.get("rightChurroHook");

        leftBarHook = hardwareMap.servo.get("leftBarHook");
        rightBarHook = hardwareMap.servo.get("rightBarHook");


        //Set motor channel modes and direction
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        rightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        leftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        linearSlide.setDirection(DcMotor.Direction.REVERSE);
        linearSlide.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        leftHangString.setDirection(DcMotor.Direction.FORWARD);
        leftHangString.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        rightHangString.setDirection(DcMotor.Direction.FORWARD);
        rightHangString.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        tubeTilt.setDirection(Servo.Direction.REVERSE);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        rightChurroHook.setDirection(Servo.Direction.REVERSE);
        rightBarHook.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);

        //Wait for the game to start
        teleInit();
        waitForStart();
        long endTime = System.currentTimeMillis() + 120000;
        //Game Loop
        while (opModeIsActive()) {
            int n = 0;
            while (n < 10) {
                bumper.setPosition(bumper.getPosition() + 0.1);
                n++;
            }
            Functions.waitFor(5000);
            bumper.setPosition(Functions.bumperInitPosition);
            sweeper.setPower(Functions.sweeperPower);
            Functions.waitFor(8000);
            sweeper.setPower(0);
            moveSlidesUpTime(3, Functions.linearSlidePower);
            Functions.waitFor(2000);
            tubeTilt.setPosition(0.3);
            Functions.waitFor(3000);
            tubeExtender.setPosition(0.1);
            Functions.waitFor(3000);
            tubeExtender.setPosition(0.5);
            tubeTilt.setPosition(0.2);
            Functions.waitFor(250);
            tubeTilt.setPosition(0.3);
            Functions.waitFor(3000);
            tubeExtender.setPosition(1.0);
            Functions.waitFor(4000);
            tubeExtender.setPosition(0.5);
            tubeTilt.setPosition(Functions.tubeTiltInitPosition);
            Functions.waitFor(1000);
            linearSlide.setPower(Functions.linearSlidePower);
            sweeper.setPower(0);
            Functions.waitFor(2000);
            sweeper.setPower(0);
            linearSlide.setPower(0);
            sweeper.setPower(0);
            int m = 0;
            while (m < 10) {
                bumper.setPosition(bumper.getPosition() - 0.1);
                m++;
            }
            Functions.waitFor(5000);
            bumper.setPosition(Functions.bumperInitPosition);
            stopRobot();
            end();
            Functions.waitFor(1000000000);
        }
    }


    public static void moveSlidesUpTime(int time, double power) {
        linearSlide.setPower(-power);
        Functions.waitFor(time * 1000);
        linearSlide.setPower(0);
    }

    public static void stopRobot() {
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }

    public static void moveRobotForward(double leftPower, double rightPower) {
        leftWheel.setPower(-leftPower);
        rightWheel.setPower(-rightPower);
    }

    public static void moveRobotBackward(double leftPower, double rightPower) {
        leftWheel.setPower(leftPower);
        rightWheel.setPower(rightPower);
    }

    public static void moveRobotForwardTime(int seconds, double power) {
        moveRobotForward(power, power);
        Functions.waitFor(seconds * 1000);
        stopRobot();
    }

    public static void moveRobotBackwardTime(double seconds, double power) {
        moveRobotBackward(power, power);
        Functions.waitFor((int) seconds * 1000);
        stopRobot();
    }

    public static void autoHookChurro(int time, double speed) {
        moveSlidesUpTime(2, 0.5);
        moveRobotForwardTime(time, speed);
        leftChurroHook.setPosition(Functions.churroHookDownPos);
        rightChurroHook.setPosition(Functions.churroHookDownPos);
    }

    public static void autoUnhookChurro(int time, double speed) {
        moveRobotForwardTime(time, speed);
        leftChurroHook.setPosition(Functions.churroHookDownPos);
        rightChurroHook.setPosition(Functions.churroHookDownPos);
    }

    public static void teleInit() {
        mountainClimber.setPosition(Functions.mountainClimberTeleInitPosition);
        mountainClimberRelease.setPosition(Functions.mountainClimberReleaseClose);
        tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
        tubeTilt.setPosition(Functions.tubeTiltInitPosition);
        bumper.setPosition(Functions.bumperInitPosition);
        rightChurroHook.setPosition(Functions.churroHookUpPos);
        leftChurroHook.setPosition(Functions.churroHookUpPos);
        rightBarHook.setPosition(Functions.barHookTeleInitPos);
        leftBarHook.setPosition(Functions.barHookTeleInitPos);
    }

    public static void end() {
        rightWheel.setPower(0);
        leftWheel.setPower(0);
        linearSlide.setPower(0);
        sweeper.setPower(0);
        leftHangString.setPower(0);
        rightHangString.setPower(0);
        bumper.setPosition(Functions.bumperInitPosition);
        tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
    }
}
