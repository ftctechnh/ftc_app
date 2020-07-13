package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.*;

@Autonomous (name = "AutonomousTest", group = "Tests")
public class AutonomousTest extends LinearOpMode {
    //Sets global variables outside of the run opmode method.
        static final double MOTOR_TICK_COUNT = 1120;
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

//Initializes motors/servos and maps them to the hardware device in the DriverStation Config.
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            backLeft = hardwareMap.dcMotor.get("backLeft");
            backRight = hardwareMap.dcMotor.get("backRight");

            slide = hardwareMap.dcMotor.get("slide");

            claw = hardwareMap.servo.get("claw");

//Reverses the left motors so the robot can move forward on positive power.
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);

            waitForStart();


        }
        public void DriveForwardDistance(int inches, int power){
            int diameter = 1;

            //Rest Encoders.
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            double circumference = 3.14*diameter;
            double rotationsNeeded = inches/circumference;
            int encoderDrivingTarget = (int)(rotationsNeeded*1120);

            frontLeft.setTargetPosition(encoderDrivingTarget);
            frontRight.setTargetPosition(encoderDrivingTarget);
            backLeft.setTargetPosition(encoderDrivingTarget);
            backRight.setTargetPosition(encoderDrivingTarget);

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while(frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy()){
                telemetry.addData("Satus", "Driving");
                telemetry.update();
            }

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            telemetry.addData("Status", "Driving Complete");
            telemetry.update();
        }
    }

