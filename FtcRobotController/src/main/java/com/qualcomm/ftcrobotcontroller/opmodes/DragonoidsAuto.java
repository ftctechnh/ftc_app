package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;

public class DragonoidsAuto extends OpMode {
    HashMap<String, DcMotor> driveMotors = new HashMap<String, DcMotor>();

    @Override
    public void init() {
        driveMotors.put("right", hardwareMap.dcMotor.get("rightDrive"));
        driveMotors.put("left", hardwareMap.dcMotor.get("leftDrive"));
        driveMotors.get("left").setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        driveMotors.get("right").setPower(0.5);
        driveMotors.get("left").setPower(0.5);
    }

}