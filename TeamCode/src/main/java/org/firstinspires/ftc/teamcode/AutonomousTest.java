package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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

    }

