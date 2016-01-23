package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * Main TeleOp
 */
@org.swerverobotics.library.interfaces.TeleOp(name = "TeleOp")
public class TeleOp extends SynchronousOpMode {
    //Declare hardware
    DcMotor frontRightWheel;
    DcMotor frontLeftWheel;
    DcMotor backRightWheel;
    DcMotor backLeftWheel;

    DcMotor linearSlideR;
    DcMotor linearSlideL;

    Servo containerTilt;
    Servo tubeExtender;

    Servo mountainClimber;
    Servo mountainClimberRelease;

    DcMotor sweeper;

    Servo bumper;

    //Declare gamepad objects
    float rightWheel;
    float leftWheel;

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

    boolean bumperDown = true;
    boolean bumperUp = false;

    float slowDriveBack;
    float slowDriveForward;

    @Override
    public void main() throws InterruptedException {
        //Initialize hardware
        frontRightWheel = hardwareMap.dcMotor.get("frontRightWheel");
        frontLeftWheel = hardwareMap.dcMotor.get("frontLeftWheel");
        backRightWheel = hardwareMap.dcMotor.get("backRightWheel");
        backLeftWheel = hardwareMap.dcMotor.get("backLeftWheel");

        linearSlideR = hardwareMap.dcMotor.get("linearSlideR");
        linearSlideL = hardwareMap.dcMotor.get("linearSlideL");

        containerTilt = hardwareMap.servo.get("containerTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");

        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");

        sweeper = hardwareMap.dcMotor.get("sweeper");

        bumper = hardwareMap.servo.get("bumper");

        //Set motor channel modes and direction
        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        frontRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
        frontLeftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRightWheel.setDirection(DcMotor.Direction.FORWARD);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        backLeftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        linearSlideR.setDirection(DcMotor.Direction.REVERSE);
        linearSlideR.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        linearSlideL.setDirection(DcMotor.Direction.FORWARD);
        linearSlideL.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        containerTilt.setDirection(Servo.Direction.REVERSE);
        containerTilt.setPosition(0.5);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeExtender.setPosition(0.5);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        bumper.setPosition(0);
        //Wait for the game to start
        waitForStart();
        long endTime = System.currentTimeMillis() + 120000;
        //Game Loop
        while (opModeIsActive() && System.currentTimeMillis() < endTime) {
            //Defines gamepad buttons for buttons that are held
            containerTiltRight = gamepad1.dpad_left || gamepad2.dpad_left;
            containerTiltLeft = gamepad1.dpad_right || gamepad2.dpad_right;

            tubeExtend = gamepad1.x || gamepad2.x;
            tubeRetract = gamepad1.b || gamepad2.b;

            positionClimbersForward = gamepad1.dpad_up || gamepad2.dpad_up;
            positionClimbersBackward = gamepad1.dpad_down || gamepad2.dpad_down;
            releaseClimbers = gamepad1.start || gamepad2.start;

            //Moves robot when some of the buttons are held
            if(tubeExtend) {
                tubeExtender.setPosition(0.75);
            } else if(tubeRetract) {
                tubeExtender.setPosition(-0.9);
            } else {
                tubeExtender.setPosition(0.5);
            }

            if(containerTiltRight) {
                containerTilt.setPosition(containerTilt.getPosition() + 0.01);
            } else if(containerTiltLeft) {
                containerTilt.setPosition(containerTilt.getPosition() - 0.01);
            } else {
                containerTilt.setPosition(containerTilt.getPosition());
            }

            if(positionClimbersForward) {
                try {
                    mountainClimber.setPosition(mountainClimber.getPosition() + 0.01);
                } catch(Exception e) {

                }
                positionClimbersBackward = false;
            } else if(positionClimbersBackward) {
                try {
                    mountainClimber.setPosition((mountainClimber.getPosition() - 0.01));
                } catch(Exception e) {

                }
                positionClimbersForward = false;
            } else {
                mountainClimber.setPosition(mountainClimber.getPosition());
            }
            if(releaseClimbers) {
                mountainClimberRelease.setPosition(1.0);
            } else {
                mountainClimberRelease.setPosition(0.0);
            }

            //For buttons that are not held
            if (updateGamepads()) {
                //Defines gamepad buttons
                rightWheel = gamepad1.right_stick_y;
                leftWheel = gamepad1.left_stick_y;

                linearSlideForward = gamepad1.right_bumper || gamepad2.right_bumper;
                linearSlideBackward = gamepad1.left_bumper || gamepad2.left_bumper;

                slowDriveBack = gamepad1.left_trigger;
                slowDriveForward = gamepad1.right_trigger;

                if(gamepad1.back || gamepad2.back) {
                    bumperUp = !bumperUp;
                    bumperDown = !bumperDown;
                }

                if (gamepad1.a || gamepad2.a) {
                    sweeperForward = !sweeperForward;
                    sweeperBackward = false;
                } else if (gamepad1.y || gamepad2.y) {
                    sweeperBackward = !sweeperBackward;
                    sweeperForward = false;
                }

                sweeperForward = gamepad1.a || gamepad2.a;
                sweeperBackward = gamepad1.y || gamepad2.y;

                //Use gamepad values to move robot
                if(slowDriveForward != 0 || slowDriveBack != 0) {
                    Functions.moveTwoMotors(backRightWheel, frontRightWheel, (slowDriveForward - slowDriveBack) * 0.25);
                    Functions.moveTwoMotors(backLeftWheel, frontLeftWheel, (slowDriveForward - slowDriveBack) * 0.25);
                } else if(rightWheel != 0 || leftWheel != 0) {
                    Functions.moveTwoMotors(backRightWheel, frontRightWheel, Functions.convertGamepad(rightWheel));
                    Functions.moveTwoMotors(backLeftWheel, frontLeftWheel, Functions.convertGamepad(leftWheel));
                } else {
                    Functions.moveTwoMotors(backRightWheel, frontRightWheel, 0);
                    Functions.moveTwoMotors(backLeftWheel, frontLeftWheel, 0);
                }

                if (linearSlideForward) {
                    Functions.moveTwoMotors(linearSlideR, linearSlideL, 0.3);
                } else if (linearSlideBackward) {
                    Functions.moveTwoMotors(linearSlideR, linearSlideL, -0.3);
                } else {
                    Functions.moveTwoMotors(linearSlideR, linearSlideL, 0.0);
                }

                if (sweeperForward) {
                    sweeper.setPower(-1.0);
                } else if (sweeperBackward) {
                    sweeper.setPower(1.0);
                } else {
                    sweeper.setPower(0);
                }

                if(bumperDown) {
                    bumper.setPosition(0);
                } else {
                    bumper.setPosition(1);
                }
            }

            idle();
        }
    }
}