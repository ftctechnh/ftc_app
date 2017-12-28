package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RelicClaw {
    private Servo claw;
    private Servo arm;
    private DcMotor motor;
    private Telemetry telemetry;
    private final double CLOSE_POSITION = 0.0;
    private final double OPEN_POSITION = 1.0;
    private final double DOWN_POSITION = 0;
    private final double UP_POSITION = 1;



    public RelicClaw(Servo claw, Servo arm, DcMotor motor, Telemetry telemetry) {
        this.claw = claw;
        this.arm = arm;
        this.motor = motor;
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;
    }
    public void init() {
        down();
        openClaw();
    }
    public void closeClaw() {claw.setPosition(CLOSE_POSITION);}
    public void openClaw() {
    	claw.setPosition(OPEN_POSITION);
    }
    public void up() {
        setArmPosition(UP_POSITION);
    }
    public void down() {
        setArmPosition(DOWN_POSITION);
    }
    private void setArmPosition(double position) {
    	arm.setPosition(position);
    }
    public void moveMotor(double speed) {
    	motor.setPower(speed);
    }
    public void moveMotor(double speed, long time) {
        moveMotor(speed);
        sleep(time);
        stop();
    }
    private void sleep(long time) {try {Thread.sleep(time);} catch (InterruptedException e) {}}
    private void stop() {moveMotor(0);}
}