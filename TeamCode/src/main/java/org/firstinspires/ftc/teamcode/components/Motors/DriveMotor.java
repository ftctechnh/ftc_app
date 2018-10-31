package org.firstinspires.ftc.teamcode.components.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class DriveMotor {

    private DcMotor motor;

    public DriveMotor(DcMotor dcMotor) {
        this.motor = dcMotor;
    }

    public void run(double power) {
        EnsurePowerIsInRange(power);
        motor.setPower(power);
    }

    private void EnsurePowerIsInRange(double power) {
        if (power > 1) {
            throw new IllegalArgumentException("Power must be less than 1.0");
        }
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    public MotorConfigurationType getMotorType() {
        return motor.getMotorType();
    }

    public void setMotorType(MotorConfigurationType motorType) {
        motor.setMotorType(motorType);
    }

    public DcMotorController getController() {
        return motor.getController();
    }

    public int getPortNumber() {
        return motor.getPortNumber();
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return motor.getZeroPowerBehavior();
    }

    public void setTargetPosition(int position) {
        motor.setTargetPosition(position);
    }

    public int getTargetPosition() {
        return motor.getTargetPosition();
    }

    public boolean isBusy() {
        return motor.isBusy();
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public void setMode(DcMotor.RunMode mode) {
        motor.setMode(mode);
    }

    public DcMotor.RunMode getMode() {
        return motor.getMode();
    }
}
