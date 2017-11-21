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

    public void teleOPDrive() {
        drive(gamepad.left_stick_y, gamepad.right_stick_y);
    }
}