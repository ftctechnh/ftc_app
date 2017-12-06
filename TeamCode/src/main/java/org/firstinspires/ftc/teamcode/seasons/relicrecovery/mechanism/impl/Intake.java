package org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

/**
 *
 */

public class Intake implements IMechanism {
    private Servo leftArmServo, rightArmServo;
    private CRServo leftWheelServo, rightWheelServo;

    public Intake(Robot robot) {
        HardwareMap hwMap = robot.getCurrentOpMode().hardwareMap;

        this.leftArmServo = hwMap.servo.get("al");
        this.rightArmServo = hwMap.servo.get("ar");

        this.leftWheelServo = hwMap.crservo.get("wl");
        this.rightWheelServo = hwMap.crservo.get("wr");
    }

    public void lowerIntake() {
        leftArmServo.setPosition(0.05);
        rightArmServo.setPosition(0.9);
    }

    public void raiseIntake() {
        leftArmServo.setPosition(0.8);
        rightArmServo.setPosition(0.2);
    }

    public void setIntakePower(double power) {
        leftWheelServo.setPower(power);

        // this servo needs to run in the opposite direction:
        rightWheelServo.setPower(-power);
    }
}
