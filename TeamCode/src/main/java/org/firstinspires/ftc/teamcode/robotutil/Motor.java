package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Motor {

    private DcMotor motor;
    private LinearOpMode opMode;

    private PID pid;
    private double minError;
    private double error;
    private double target;

    public Motor(DcMotor motor, LinearOpMode opMode) {
        this.motor = motor;
        this.opMode = opMode;
    }

    public DcMotor getMotor() {
        return motor;
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public void setTarget(int ticks) {
        target = getCurrentPosition() + ticks;
    }

    public void updateError() {
        error = target - getCurrentPosition();
    }

    public double getError() {
        return error;
    }

    public void setMinError(double minError) {
        this.minError = minError;
    }

    public boolean withinMinError() {
        updateError();
        if (Math.abs(error) < minError) {
            return true;
        } else {
            return false;
        }
    }

    public void addPID(double kp, double ki, double kd) {
        pid = new PID(kp, ki, kd, opMode.telemetry);
    }

    public double getPIDOutput() {
        updateError();
        return pid.getOutput(error);
    }

}
