package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class RelicClaw {
    private Servo claw;
    private double clawClosePos = 0.25;
    private double clawOpenPos = 0.75;
    private Servo arm;
    private DcMotor motor;


    public RelicClaw(Servo claw, Servo arm, DcMotor motor) {
        this.claw = claw;
        this.arm = arm;
        this.motor = motor;
    }
    public void closeClaw() {
    	claw.setPosition(clawClosePos);
    }
    public void openClaw() {
    	claw.setPosition(clawOpenPos);
    }
    public void setArmPosition(double position) {
    	arm.setPosition(position);
    }
    public double getArmPosition() {
    	return arm.getPosition();
    }
    public void moveMotor(double speed) {
    	motor.setPower(speed);
    }

}