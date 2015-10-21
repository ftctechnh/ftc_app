package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ExampleDriveInCircle extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

  @Override
  public void init() {
      leftMotor = hardwareMap.dcMotor.get("left_drive");
      rightMotor = hardwareMap.dcMotor.get("right_drive");

      rightMotor.setDirection(DcMotor.Direction.REVERSE);
  }

  @Override
  public void loop() {
      leftMotor.setPower(0.75);
      rightMotor.setPower(0.15);
  }
}
