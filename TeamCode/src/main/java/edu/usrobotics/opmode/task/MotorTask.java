package edu.usrobotics.opmode.task;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.usrobotics.opmode.task.Task;
import edu.usrobotics.opmode.task.TaskType;

/**
 * Created by Max on 9/18/2016.
 */
public class MotorTask implements Task {

    private DcMotor motor; // The motor hardware device
    private Integer encoderGoal; // Encoder value motor attempts to reach, nullable
    private Integer dampingGoal; // Encoder value damping attempts to damp fully before reaching, nullable
    private int maxMotorSpeed; // Max encoder ticks (1/4 deg) per second
    private double power; // Max power of motor
    private float damping; // Percent along path to begin deceleration damping.

    private boolean completed;
    private boolean encoderReset;


    public MotorTask (DcMotor motor, @Nullable Integer encoderGoal, @Nullable Integer maxMotorSpeed, double power, float damping, @Nullable Integer dampingGoal) {
        this.motor = motor;
        this.encoderGoal = encoderGoal;
        this.maxMotorSpeed = maxMotorSpeed != null ? maxMotorSpeed : Integer.MAX_VALUE;
        this.power = power;
        this.damping = damping;
        this.dampingGoal = dampingGoal;//encoderGoal != null ? encoderGoal
    }

    public MotorTask (DcMotor motor) {
        this (motor, null, null, 1d, 0f, null);
    }

    private double getDampedPower (double power) {
        if (dampingGoal == null) return power; // We can't use damping if there is no damping goal

        float percentToTarget = Math.min(1, Math.max(0, (float)motor.getTargetPosition() / (float)dampingGoal)); // Start must be 0, so this works.
        float percentToZero = 1f - (percentToTarget - damping) / ((float)motor.getTargetPosition()*(1f-damping));

        return (percentToTarget < damping ? power : Math.max(0.2, power * percentToZero));
    }

    @Override
    public boolean execute() {
        if (encoderGoal != null) { // If we want to run to a encoderGoal

            if (!encoderReset) { // Wait for encoder to reset
                encoderReset = motor.getCurrentPosition() == 0;
                if (!encoderReset) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            } else { // If encoder reset, run to encoderGoal
                if (Math.abs(motor.getCurrentPosition()) >= Math.abs(encoderGoal)) return true; // If we reached encoderGoal, return true

                //**
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.setTargetPosition(encoderGoal);
                //motor.setPower (1f);
                motor.setPower(getDampedPower(power)); //TODO: This is changed
                //motor.setMaxSpeed(maxMotorSpeed);
                /**/

                /**
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor.setMaxSpeed(maxMotorSpeed);
                motor.setPower(getDampedPower(power));
                **/

            }

        } else { // If we are running blind until onExecuted() returns true
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setMaxSpeed(maxMotorSpeed);
            motor.setPower(power);
        }

        return false;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public TaskType getType() {
        return TaskType.MOTOR;
    }

    @Override
    public void onReached() {
        completed = false;
        encoderReset = false;
    }

    @Override
    public boolean onExecuted() {
        return false;
    }

    @Override
    public void onCompleted() {
        motor.setPower(0);
        completed = true;
    }
}
