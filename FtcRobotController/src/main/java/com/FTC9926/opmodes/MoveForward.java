package com.FTC9926.opmodes;

  import com.qualcomm.robotcore.eventloop.opmode.OpMode;
  import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ibravo on 9/19/15.
 * based on team FTC 7123
 */
public class MoveForward extends OpMode{

    DcMotor rightMotor;
    DcMotor leftMotor;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        hardwareMap.dcMotor.get("DC1");
        hardwareMap.dcMotor.get("CC2");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        rightMotor.setPower(gamepad1.right_stick_y);
        leftMotor.setPower(gamepad1.left_stick_y);
    }

    @Override
    public void stop() {

    }
}