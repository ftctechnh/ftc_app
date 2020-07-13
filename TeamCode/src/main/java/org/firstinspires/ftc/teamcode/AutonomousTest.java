package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "AutonomousTest", group = "Tests")
public class AutonomousTest extends LinearOpMode {
    //Sets global variables outside of the run opmode method
        private static final double CLAW_DOWN_POSITION = 0.5;
        private static final double CLAW_UP_POSITION = 0;


        @Override
        public void runOpMode() throws InterruptedException {

//Initializes motors/servos and maps them to the hardware device in the DriverStation Config.
            DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");
            DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
            DcMotor backLeft = hardwareMap.dcMotor.get("backLeft");
            DcMotor backRight = hardwareMap.dcMotor.get("backRight");

            DcMotor slide = hardwareMap.dcMotor.get("slide");

            Servo claw = hardwareMap.servo.get("claw");

//Reverses the left motors so the robot can move forward on positive power.
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);

            waitForStart();

//Goes forward for 500 milliseconds.
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);

            sleep(500);

//Stops
            frontRight.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
            backLeft.setPower(0);

            idle();

        }
    }
