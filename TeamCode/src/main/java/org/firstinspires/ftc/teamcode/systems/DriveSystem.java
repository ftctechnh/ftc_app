package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Mahim on 11/4/2017.
 */

public class DriveSystem {
    private DcMotor rightMotor, leftMotor;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;

    public DriveSystem (HardwareMap hardwareMap, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;
        this.rightMotor = hardwareMap.get(DcMotor.class, "right motor");
        this.leftMotor = hardwareMap.get(DcMotor.class, "left motor");
        this.leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double leftSpeed, double rightSpeed) {
        this.rightMotor.setPower(rightSpeed);
        this.leftMotor.setPower(leftSpeed);
    }

    public void driveForward(double speed) {
        drive(-speed, -speed);
    }

    public void driveBackwards(double speed) {
        drive(speed, speed);
    }

    public void rcCarDrive() {
        if (gamepad.left_stick_y > 0) {
            drive(gamepad.left_stick_y + gamepad.right_stick_x, gamepad.left_stick_y - gamepad.right_stick_x);
        } else if (gamepad.left_stick_y < 0) {
            drive(gamepad.left_stick_y - gamepad.right_stick_x, gamepad.left_stick_y + gamepad.right_stick_x);
        } else {
            drive(-gamepad.right_stick_x, gamepad.right_stick_x);
        }
    }

    public void stop() {
        drive(0.0,0.0);
    }

    public void tankDrive() {
        drive(gamepad.left_stick_y, gamepad.right_stick_y);
    }

    public double getRightSpeed() {
        return this.rightMotor.getPower();
    }

    public double getLeftSpeed() {
        return this.leftMotor.getPower();
    }
}