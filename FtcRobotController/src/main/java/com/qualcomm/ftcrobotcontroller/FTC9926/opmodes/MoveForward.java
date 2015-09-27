package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

  import com.qualcomm.robotcore.eventloop.opmode.OpMode;
  import com.qualcomm.robotcore.hardware.DcMotor;
  import com.qualcomm.robotcore.util.Range;
  import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by ibravo on 9/19/15.
 * based on team FTC 7123
 */
public class MoveForward extends OpMode {

    DcMotor Motor1;
    DcMotor Motor2;
    Servo Servo1;
    Servo Servo2;


    @Override
    public void init() {
        Motor1 = hardwareMap.dcMotor.get("M1");
        Motor2 = hardwareMap.dcMotor.get("M2");
        Servo1 = hardwareMap.servo.get("SM1");
        Servo2 = hardwareMap.servo.get("SM2");

    }

    @Override
    public void start() {
     //   hardwareMap.dcMotor.get("DC1");
     //   hardwareMap.dcMotor.get("DC2");

    //    leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        float M1 = gamepad1.right_stick_y;
        float M2 = -gamepad1.left_stick_y;
        float SM1 = gamepad1.left_trigger;
        float SM2 = gamepad1.right_trigger;


        M1 = Range.clip(M1, -1, 1);
        M2 = Range.clip(M2, -1, 1);
        SM1 = Range.clip(SM1, 0, 1);
        SM2 = Range.clip(SM2, 0, 1);

        Motor1.setPower(M1);
        Motor2.setPower(M2);
        Servo1.setPosition(0.4);
        Servo2.setPosition(SM2);

        telemetry.addData("M1", "M1: " + M1);
        telemetry.addData("M2", "M2: " + M2);
        telemetry.addData("SM1", "SM1 Pos: " + SM1);
        telemetry.addData("SM2", "SM2 Pos: " + SM2);

    }

    @Override
    public void stop() {

    }
}