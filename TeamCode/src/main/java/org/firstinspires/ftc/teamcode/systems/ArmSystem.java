package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Mahim on 11/13/2017.
 */

public class ArmSystem {
    private DcMotor armMotor;
    private Servo leftClaw, rightClaw;
    private HardwareMap hardwareMap;

    public ArmSystem (HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.armMotor = hardwareMap.get(DcMotor.class, "arm motor");
        this.leftClaw = hardwareMap.get(Servo.class, "left servo");
        this.rightClaw = hardwareMap.get(Servo.class, "right servo");
    }

    public void goUp() {
        this.armMotor.setPower(1.0);
    }

    public void goDown() {
        this.armMotor.setPower(-1.0);
    }

    public void setClaw(float position) {
        this.rightClaw.setPosition(position);
        this.leftClaw.setPosition(-position);
    }

    public double getArmMotorSpeed() {
        return this.armMotor.getPower();
    }

    public double getleftServoPosition() {
        return this.leftClaw.getPosition();
    }

    public double getRightServoPosition() {
        return this.rightClaw.getPosition();
    }
}