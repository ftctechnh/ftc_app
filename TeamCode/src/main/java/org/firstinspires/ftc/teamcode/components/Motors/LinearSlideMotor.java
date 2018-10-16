package org.firstinspires.ftc.teamcode.components.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class LinearSlideMotor {

    private DcMotor rightMotor;
    private DcMotor leftMotor;

    public LinearSlideMotor(DcMotor rightMotor, DcMotor leftMotor) {
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
    }

    public void run(double power) {
        EnsurePowerIsInRange(power);
        leftMotor.setPower(-power);
        rightMotor.setPower(power);
    }

    private void EnsurePowerIsInRange(double power) {
        if (power > 1) {
            throw new IllegalArgumentException("Power must be less than 1.0");
        }
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        if (direction == DcMotorSimple.Direction.FORWARD) {
            leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        } else {
            leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        rightMotor.setDirection(direction);
    }

    public MotorConfigurationType getMotorType() {
        return rightMotor.getMotorType();
    }

    public void setMotorType(MotorConfigurationType motorType) {
        leftMotor.setMotorType(motorType);
        rightMotor.setMotorType(motorType);
    }

    public DcMotorController getController() {
        return rightMotor.getController();
    }

    // This is not going to work with two motors
    public int getPortNumber() {
        return rightMotor.getPortNumber();
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        leftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        rightMotor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return rightMotor.getZeroPowerBehavior();
    }

    public void setTargetPosition(int position) {
        leftMotor.setTargetPosition(position);
        rightMotor.setTargetPosition(position);
    }

    public int getTargetPosition() {
        return rightMotor.getTargetPosition();
    }

    public boolean isBusy() {
        return (rightMotor.isBusy() || leftMotor.isBusy());
    }

    public int getCurrentPosition() {
        return rightMotor.getCurrentPosition();
    }

    public void setMode(DcMotor.RunMode mode) {
        leftMotor.setMode(mode);
        rightMotor.setMode(mode);
    }

    public DcMotor.RunMode getMode() {
        return rightMotor.getMode();
    }
}
