package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.ColorSensor;
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
    private Servo leftClaw, rightClaw, jewelArmServo;
    private ColorSensor colorSensor;
    private HardwareMap hardwareMap;
    private double initialPosition = 0.07;
    private double downPosition = 0.8;


    public ArmSystem (HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.armMotor = hardwareMap.get(DcMotor.class, "arm motor");
        this.leftClaw = hardwareMap.get(Servo.class, "left servo");
        this.rightClaw = hardwareMap.get(Servo.class, "right servo");
        this.jewelArmServo = hardwareMap.get(Servo.class, "jewel arm servo");
        this.colorSensor = hardwareMap.colorSensor.get("color sensor");
        this.rightClaw.setDirection(Servo.Direction.REVERSE);
        this.jewelArmServo.setDirection(Servo.Direction.REVERSE);
    }

    public void init() {
        this.jewelArmServo.setPosition(initialPosition);
        this.colorSensor.enableLed(false);
    }

    public void goUp() {
        this.armMotor.setPower(1.0);
    }

    public void enableColorSensor() {
        this.colorSensor.enableLed(true);
    }

    public void goDown() {
        this.armMotor.setPower(-1.0);
    }

    public void setClaw(float position) {
        this.rightClaw.setPosition(position);
        this.leftClaw.setPosition(position);
    }

    public void setInitialPosition() {
        this.jewelArmServo.setPosition(initialPosition);
    }

    public void setDownPosition() {
        this.jewelArmServo.setPosition(downPosition);
    }

    public void setJewelArmServo(float position) {
        this.jewelArmServo.setPosition(position);
    }

    public double getArmMotorSpeed() {
        return this.armMotor.getPower();
    }

    public void stopArm() {
        this.armMotor.setPower(0.0);
    }

    public double getLeftServoPosition() {
        return this.leftClaw.getPosition();
    }

    public double getRightServoPosition() {
        return this.rightClaw.getPosition();
    }

    public double getJewelArmPosition() {
        return this.jewelArmServo.getPosition();
    }

    public int getRed() {
        return colorSensor.red();
    }

    public int getBlue() {
        return colorSensor.blue();
    }

    public int getGreen() {
        return colorSensor.green();
    }

    public int getAlpha() { return colorSensor.alpha(); }

    public boolean isBlue() {
        return (getBlue() > getRed()) && (getBlue() > getGreen());
    }

    public boolean isRed() {
        return (getRed() > getBlue()) && (getRed() > getGreen());
    }
}