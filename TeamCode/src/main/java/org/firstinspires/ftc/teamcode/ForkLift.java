package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ForkLift {
    private Servo rightClaw;
    private Servo leftClaw;
    private DcMotor motor;
    private TouchSensor topButton;
    private TouchSensor bottomButton;
    private double clawPosition = 0.25;
    private double clawHighEnd = 0.7;
    private double clawLowEnd = 0.3;
    private double up = 0;
    private double down = 0;

    public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor motor, TouchSensor topButton, TouchSensor bottomButton) {
        this.rightClaw = rightClaw;
        this.leftClaw = leftClaw;
        this.motor = motor;
        this.topButton = topButton;
        this.bottomButton = bottomButton;
        this.rightClaw.setDirection(Servo.Direction.REVERSE);
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        resetEncoder();
    }


    public void init() {
        setClawPosition(clawPosition);
        //motor.setPower(-1);
        //while (!bottomButton.isPressed()) {

        //}
        //motor.setPower(0);
        //resetEncoder();
    }

    public void closeClaw() {
        setClawPosition(clawHighEnd);
    }

    public void openClaw() {
        setClawPosition(clawLowEnd);
    }

    public void moveUpDown(double speed) {
        if (speed < 0) {
            if (bottomButton.isPressed()) {
                speed = 0;
            }
        }
        if (speed > 0) {
            if (topButton.isPressed()) {
                speed = 0;
            }
        }
        motor.setPower(speed);
    }

    public void setClawPosition(double position) {
        rightClaw.setPosition(position);
        rightClaw.setPosition(position);
        leftClaw.setPosition(position);
        leftClaw.setPosition(position);
    }
    private void resetEncoder() {
    	motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}