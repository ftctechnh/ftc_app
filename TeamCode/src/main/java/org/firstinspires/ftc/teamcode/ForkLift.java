package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ForkLift {
    private Servo rightClaw;
    private Servo leftClaw;
    private DcMotor motor;
    private TouchSensor topButton;
    private TouchSensor bottomButton;
    private double clawPosition = 0.25; //0.25 on the other robot
    private double clawHighEnd = 1; //0.85
    private double clawLowEnd = 0; //0.3
    private Telemetry telemetry;
    private HardwareDevice.Manufacturer manufacturer;

    public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor motor, TouchSensor topButton, TouchSensor bottomButton, Telemetry telemetry) {
        if(rightClaw.getManufacturer().equals("ModernRobotics")){
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
        if (bottomButton != null) {
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