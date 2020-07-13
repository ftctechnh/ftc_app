package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "AutonomousTest", group = "Tests")
public class AutonomousTest extends LinearOpMode {

        //Sets global variables outside of the run opmode method.
        static final int MOTOR_TICK_COUNT = 1120;
        private static final double CLAW_DOWN_POSITION = 0.5;
        private static final double CLAW_UP_POSITION = 0;
        DcMotor frontLeft = null;
        DcMotor frontRight = null;
        DcMotor backLeft = null;
        DcMotor backRight = null;
        DcMotor slide = null;
        Servo claw = null;

        @Override
        public void runOpMode() throws InterruptedException {

            //Maps Motors and Servos to the hardware device in the DriverStation Config.
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            backLeft = hardwareMap.dcMotor.get("backLeft");
            backRight = hardwareMap.dcMotor.get("backRight");

            slide = hardwareMap.dcMotor.get("slide");

            claw = hardwareMap.servo.get("claw");

            //set the motors to RUN_USING_ENCODER.
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Reverses the left motors so the robot can move forward on positive power.
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);

            //Wait for the Start Button to be pressed.
            waitForStart();

            DriveForwardInches(10,.5);

        }
        public void DriveForwardInches(int inches, int power) {
            //Reset Encoder values.
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            //Calculates the circumference of the wheel.
            double circumference = 3.14*0.00;
            //Calculates the number rotations the wheels needs to make to drive the specified number inches.
            double rotationsNeeded = inches/circumference;
            //Convert the double value into an integer tick value to feed into the motor position.
            int encoderDrivingTarget = (int)(rotationsNeeded*1120);

            //Set the motor target positions to the tick value just calculated.
            frontLeft.setTargetPosition(encoderDrivingTarget);
            frontRight.setTargetPosition(encoderDrivingTarget);
            backLeft.setTargetPosition(encoderDrivingTarget);
            backRight.setTargetPosition(encoderDrivingTarget);

            //Set motor powers.
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            //Set motor modes to Run to the encoderDrivingTarget.
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Do nothing until motors are finished driving.
            while (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy() ) {
                //Indicate that motors are driving in telemetry.
                telemetry.addData("Status", "Driving");
                telemetry.update();
            }

            //Stop motors after driving is finished.
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            //Indicate that the motors are stopped and driving is complete in telemetry.
            telemetry.addData("Status", "Driving Complete");
            telemetry.update();

        }
    }
