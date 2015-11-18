package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.HashMap;

public class DragonoidsOpMode extends OpMode {
    protected HashMap<String, DcMotor> driveMotors = new HashMap<String, DcMotor>();
    protected HashMap<String, DcMotor> climbMotors = new HashMap<String, DcMotor>();

    @Override
    public void init() {
        driveMotors.put("right", hardwareMap.dcMotor.get("rightDrive"));
        driveMotors.put("left", hardwareMap.dcMotor.get("leftDrive"));
        driveMotors.get("left").setDirection(DcMotor.Direction.REVERSE);

        climbMotors.put("right", hardwareMap.dcMotor.get("rightClimb"));
        climbMotors.put("left", hardwareMap.dcMotor.get("leftClimb"));
        climbMotors.get("left").setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        outputTelemetry();
    }
    @Override
    public void stop() {
        // Stop all motors
        driveMotors.get("right").setPower(0);
        driveMotors.get("left").setPower(0);
        climbMotors.get("right").setPower(0);
        climbMotors.get("left").setPower(0);
    }

    private void outputTelemetry() {
        telemetry.addData("Right drive motor power", driveMotors.get("right").getPower());
        telemetry.addData("Left drive motor power", driveMotors.get("left").getPower());
        telemetry.addData("Right climb motor power", climbMotors.get("right").getPower());
        telemetry.addData("Left climb motor power", climbMotors.get("left").getPower());
    }
}
