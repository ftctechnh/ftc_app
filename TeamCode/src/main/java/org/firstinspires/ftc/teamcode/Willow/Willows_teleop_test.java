package org.firstinspires.ftc.teamcode.Willow;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

/**
 * Created by windorabug on 11/3/16.
 */


public class Willows_teleop_test extends OpMode {

    //motor controllers
private DcMotorController dc_drive_controller;

  //~the motors used
 private DcMotor  dc_drive_left;
 private DcMotor dc_drive_right;

  @Override
    public void init() {
   dc_drive_controller = HardwareMap.dcmotorcontroller.get("drive_controllor");
   dc_drive_left = HardwareMap.Dcmotor.get("drive_left");
   dc_drive_right = HardwareMap.dcmotor.get("drive_right");
  }

  @Override
    public void loop () {
    dc_drive_left.setPower(gamepad1.left_stick_y);
    dc_drive_right.setPower(gamepad1.right_stick_y);
  }




}
