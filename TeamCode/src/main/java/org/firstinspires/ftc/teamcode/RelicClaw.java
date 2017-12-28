package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RelicClaw {
    private Servo claw;
    private final double closePosition = 0.0;
    private final double openPosition = 1.0;
    private final double downPosition = 0;
    private final double upPosition = 1;
    private Servo arm;
    private final double armInitPos = 1;
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
    public void closeClaw() {claw.setPosition(closePosition);}
    public void openClaw() {
    	claw.setPosition(openPosition);
    }
    public void up() {
        setArmPosition(upPosition);
    }
    public void down() {
        setArmPosition(downPosition);
    }
    public void setArmPosition(double position) {
    	arm.setPosition(position);
    }
    public void moveMotor(double speed) {
    	motor.setPower(speed);
    }

}