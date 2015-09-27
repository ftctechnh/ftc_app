package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ibravo on 9/27/15.
 * Tests Servo Movements
 */
public class MoveServo extends OpMode {

    // Define position, max/min range, move interval
    final static double SM1_Min = 0.0;
    final static double SM1_Max = 0.95;
    double SM1_Position;
    double SM2_Position;
    double SM1_Interval = 0.01;

    Servo Servo1;
    Servo Servo2;


    @Override
    public void init() {

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

        // Update the servos based on the press of the controller buttons.
        if (gamepad1.a) {
            SM1_Position += SM1_Interval;
        }
        if (gamepad1.b) {
            SM1_Position -= SM1_Interval;
        }

        SM1_Position = Range.clip(SM1_Position, SM1_Min, SM1_Max);
        SM2_Position = Range.clip(gamepad1.right_trigger,0,1);

        Servo1.setPosition(SM1_Position);
        Servo2.setPosition(SM2_Position);

        telemetry.addData("SM1", "SM1 Pos: " + String.format("%.2f", SM1_Position));
        telemetry.addData("SM2", "SM2 Pos: " + String.format("%.2f", SM2_Position));

    }

    @Override
    public void stop() {

    }
}
