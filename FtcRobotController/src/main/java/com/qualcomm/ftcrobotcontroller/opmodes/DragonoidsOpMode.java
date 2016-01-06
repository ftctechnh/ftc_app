package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;

public class DragonoidsOpMode extends OpMode {
    protected HashMap<String, DcMotor> driveMotors = new HashMap<String, DcMotor>();
    protected HashMap<String, DcMotor> auxMotors = new HashMap<String, DcMotor>();
    protected HashMap<String, Servo> servos = new HashMap<String, Servo>();

    @Override
    public void init() {
        driveMotors.put("rightOne", hardwareMap.dcMotor.get("rightOneDrive"));
        driveMotors.put("rightTwo", hardwareMap.dcMotor.get("rightTwoDrive"));
        driveMotors.put("leftOne", hardwareMap.dcMotor.get("leftOneDrive"));
        driveMotors.put("leftTwo", hardwareMap.dcMotor.get("leftTwoDrive"));

        driveMotors.get("rightOne").setDirection(DcMotor.Direction.REVERSE);
        driveMotors.get("leftOne").setDirection(DcMotor.Direction.REVERSE);

        auxMotors.put("conveyor", hardwareMap.dcMotor.get("conveyor"));
        auxMotors.put("knocker", hardwareMap.dcMotor.get("knocker"));

        servos.put("gate", hardwareMap.servo.get("gate"));
    }
    @Override
    public void loop() {
        outputTelemetry();
    }

    public void setDrivePower(double rightPower, double leftPower) {
        driveMotors.get("rightOne").setPower(rightPower);
        driveMotors.get("rightTwo").setPower(rightPower);
        driveMotors.get("leftOne").setPower(leftPower);
        driveMotors.get("leftTwo").setPower(leftPower);
    }
    @Override
    public void stop() {
        // Stop all motors
        driveMotors.get("rightOne").setPower(0);
        driveMotors.get("rightTwo").setPower(0);
        driveMotors.get("leftOne").setPower(0);
        driveMotors.get("leftOTwo").setPower(0);
        auxMotors.get("conveyor").setPower(0);
        auxMotors.get("knocker").setPower(0);

    }

    private void outputTelemetry() {
        telemetry.addData("Right drive motor power", driveMotors.get("right").getPower());
        telemetry.addData("Left drive motor power", driveMotors.get("left").getPower());
        telemetry.addData("Conveyor motor power", auxMotors.get("conveyor").getPower());
        telemetry.addData("Knocker motor power", auxMotors.get("knocker").getPower());
    }
}
