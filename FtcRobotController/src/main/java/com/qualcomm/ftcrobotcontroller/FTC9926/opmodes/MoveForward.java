package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

  import com.qualcomm.robotcore.eventloop.opmode.OpMode;
  import com.qualcomm.robotcore.hardware.DcMotor;
  import com.qualcomm.robotcore.util.Range;
  import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by ibravo on 9/19/15.
 * based on team FTC 7123.
 * Objective is to test motors and servos in the test bench
 */
public class MoveForward extends OpMode {

    // Define position, max/min range, move interval
    final static double SM1_Min = 0.0;
    final static double SM1_Max = 0.95;
    double SM1_Position;
    double SM1_Interval = 0.01;

    DcMotor Motor1;
    DcMotor Motor2;
    Servo Servo1;
    Servo Servo2;


    @Override
    public void init() {
        // Link with Phone Configuration Names
        Motor1 = hardwareMap.dcMotor.get("M1");
        Motor2 = hardwareMap.dcMotor.get("M2");
        Motor2.setDirection(DcMotor.Direction.REVERSE);

        Servo1 = hardwareMap.servo.get("SM1");
        Servo2 = hardwareMap.servo.get("SM2");

        //Set starting postion for SM1
        SM1_Position = 0.5;

    }

    @Override
    public void start() {
     //   hardwareMap.dcMotor.get("DC1");
     //   hardwareMap.dcMotor.get("DC2");

    //    leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {

        // Tank Mode movement:
     //   float M1 = gamepad1.right_stick_y;
     //   float M2 = gamepad1.left_stick_y;

        // Direction and throttle movement;
        // Copy from K9TeleOp
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float M1 = throttle - direction;
        float M2 = throttle + direction;

        // Update the servos based on the press of the controller buttons.
        if (gamepad1.a) {
            SM1_Position += SM1_Interval;
        }
        if (gamepad1.b) {
            SM1_Position -= SM1_Interval;
        }

        M1 = Range.clip(M1, -1, 1);
        M2 = Range.clip(M2, -1, 1);
        SM1_Position = Range.clip(SM1_Position, SM1_Min, SM1_Max);

        Motor1.setPower(M1);
        Motor2.setPower(M2);
        Servo1.setPosition(SM1_Position);

        telemetry.addData("M1", "M1: " + M1);
        telemetry.addData("M2", "M2: " + M2);
        telemetry.addData("SM1", "SM1 Pos: " + String.format("%.2f", SM1_Position));

    }

    @Override
    public void stop() {

    }
}