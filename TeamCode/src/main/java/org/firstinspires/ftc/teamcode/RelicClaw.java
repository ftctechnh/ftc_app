package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RelicClaw {
    private Servo claw;
    private double clawClosePos = 0.25;
    private double clawOpenPos = 0.75;
    private Servo arm;
    private double armInitPos = 1;
    private DcMotor motor;
    private Telemetry telemetry;


    public RelicClaw(Servo claw, Servo arm, DcMotor motor, Telemetry telemetry) {
        this.claw = claw;
        this.arm = arm;
        this.motor = motor;
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;
    }
    public void init() {
        setArmPosition(armInitPos);
        openClaw();
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