package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

  import com.qualcomm.robotcore.eventloop.opmode.OpMode;
  import com.qualcomm.robotcore.hardware.DcMotor;
  import com.qualcomm.robotcore.util.Range;

/**
 * Created by ibravo on 9/19/15.
 * based on team FTC 7123
 */
public class MoveForward extends OpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    @Override
    public void init() {
        rightMotor = hardwareMap.dcMotor.get("DC1");
        leftMotor = hardwareMap.dcMotor.get("DC2");
    }

    @Override
    public void start() {
     //   hardwareMap.dcMotor.get("DC1");
     //   hardwareMap.dcMotor.get("DC2");

    //    leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        float right = gamepad1.right_stick_y;
        float left = gamepad1.left_stick_y;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        rightMotor.setPower(right);
        leftMotor.setPower(left);

        telemetry.addData("Right", "Right Pwr: " + right);
        telemetry.addData("Left", "Left" + left);

    }

    @Override
    public void stop() {

    }
}