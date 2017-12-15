package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ForkLift {
    private Servo rightClaw;
    private Servo leftClaw;
    private DcMotor motor;
    private DigitalChannel topButton;
    private DigitalChannel bottomButton;
    private double clawHighEnd = 1; //0.85
    private double clawLowEnd = 0; //0.3
    private Telemetry telemetry;

    public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor motor, DigitalChannel topButton, DigitalChannel bottomButton, Telemetry telemetry) {
        if(rightClaw.getManufacturer().equals(Manufacturer.ModernRobotics)){
            clawHighEnd=0.85;
            clawLowEnd=0.3;
        }
        this.rightClaw = rightClaw;
        this.leftClaw = leftClaw;
        this.motor = motor;
        this.topButton = topButton;
        this.bottomButton = bottomButton;
        this.rightClaw.setDirection(Servo.Direction.REVERSE);
        resetEncoder();
        this.telemetry = telemetry;
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor motor, Telemetry telemetry) {
        this.rightClaw = rightClaw;
        this.leftClaw = leftClaw;
        this.motor = motor;
        this.topButton = null;
        this.bottomButton = null;
        this.telemetry = telemetry;
        resetEncoder();
    }


    public void init() {
        openClaw();
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
        telemetry.addData("input", speed);
        if (bottomButton != null) {
            if (speed < 0) {
                if (!bottomButton.getState()) {
                    speed = 0;
                }
            }
            if (speed > 0) {
                if (!topButton.getState()) {
                    speed = 0;
                }
            }
        }
        telemetry.addData("motor speed", speed);
        motor.setPower(speed);
        telemetry.update();
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