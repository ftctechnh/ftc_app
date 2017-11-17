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
    private Gamepad gamepad;

    public ArmSystem (HardwareMap hardwareMap, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;
        this.armMotor = hardwareMap.get(DcMotor.class, "arm motor");
        this.leftClaw = hardwareMap.get(Servo.class, "left servo");
        this.rightClaw = hardwareMap.get(Servo.class, "right servo");
    }
    public void armDown() {
        this.armMotor.setPower(-1.0);
    }

    public void armUp() {
        this.armMotor.setPower(1.0);
    }

    public void setClaw(float position) {
        this.rightClaw.setPosition(position);
    }
}
