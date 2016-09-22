package edu.usrobotics.opmode.task;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Max on 9/18/2016.
 */
public class MotorTask implements Task {

    private DcMotor motor; // The motor hardware device
    private Integer encoderGoal; // Encoder value motor attempts to reach, nullable
    private int motorSpeed; // Max encoder ticks (1/4 deg) per second
    private double power;
    private float damping;

    private boolean encoderReset;


    public MotorTask (DcMotor motor, @Nullable Integer encoderGoal, int motorSpeed, double power, float damping) {
        this.motor = motor;
        this.encoderGoal = encoderGoal;
        this.motorSpeed = motorSpeed;
        this.power = power;
        this.damping = damping;
    }

    public MotorTask (DcMotor motor) {
        this (motor, null, 3696, 1d, 0f);
    }

    private double getDampedPower (double power) {
        if (encoderGoal == null) return power; // We can't use damping if there is no goal

        float percentToTarget = (float)motor.getTargetPosition() / (float)encoderGoal; // Start must be 0, so this works.
        float percentToZero = 1f - (percentToTarget - damping) / ((float)motor.getTargetPosition()*(1f-damping));

        return (percentToTarget < damping ? power : power * percentToZero);
    }

    @Override
    public boolean execute() {
        if (encoderGoal != null) { // If we want to run to a encoderGoal

            if (!encoderReset) { // Wait for encoder to reset
                encoderReset = motor.getCurrentPosition() == 0;
                if (!encoderReset) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            } else { // If encoder reset, run to encoderGoal
                if (motor.getCurrentPosition() == encoderGoal) return true; // If we reached encoderGoal, return true

                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.setTargetPosition(encoderGoal);
                motor.setPower(getDampedPower(power));
                motor.setMaxSpeed(motorSpeed);

            }

        } else { // If we are running blind until onExecuted() returns true
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(power);
            motor.setMaxSpeed(motorSpeed);
        }

        return false;
    }

    @Override
    public TaskType getType() {
        return TaskType.MOTOR;
    }

    @Override
    public void onReached() {

    }

    @Override
    public boolean onExecuted() {
        return false;
    }

    @Override
    public void onCompleted() {

    }
}
