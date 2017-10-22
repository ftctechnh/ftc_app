//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.borsch.sim.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

public class SimulatedDcMotor implements DcMotor {
    protected Direction direction;
    protected MotorConfigurationType motorType;

    private double motorPower;
    private ZeroPowerBehavior zeroPowerBehavior;
    private int targetPosition;
    private RunMode motorMode;


    public SimulatedDcMotor() {
        this.direction = Direction.FORWARD;
        this.motorType = new MotorConfigurationType();
    }

    public Manufacturer getManufacturer() {
        return Manufacturer.Unknown;
    }

    public String getDeviceName() {
        return "Simulated DcMotor";
    }

    public String getConnectionInfo() {
        return "Simulated Connection";
    }

    public int getVersion() {
        return 1;
    }

    public void resetDeviceConfigurationForOpMode() {
        this.setDirection(Direction.FORWARD);
    }

    public void close() {
        this.setPowerFloat();
    }

    public MotorConfigurationType getMotorType() {
        return this.motorType;
    }

    public void setMotorType(MotorConfigurationType motorType) {
        this.motorType = motorType;
    }

    public DcMotorController getController() {
        return null;
    }

    public synchronized void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public int getPortNumber() {
        return 0;
    }

    public synchronized void setPower(double power) {
        if (this.getMode() == RunMode.RUN_TO_POSITION) {
            power = Math.abs(power);
        } else {
            power = this.adjustPower(power);
        }

        this.internalSetPower(power);
    }

    protected void internalSetPower(double power) {
        this.motorPower = power;
    }

    public synchronized double getPower() {
        double power = this.motorPower;
        if (this.getMode() == RunMode.RUN_TO_POSITION) {
            power = Math.abs(power);
        } else {
            power = this.adjustPower(power);
        }

        return power;
    }

    public boolean isBusy() {
        return false;
    }

    public synchronized void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        this.zeroPowerBehavior = zeroPowerBehavior;
    }

    public synchronized ZeroPowerBehavior getZeroPowerBehavior() {
        return this.zeroPowerBehavior;
    }

    /** @deprecated */
    @Deprecated
    public synchronized void setPowerFloat() {
        this.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
        this.setPower(0.0D);
    }

    public synchronized boolean getPowerFloat() {
        return this.getZeroPowerBehavior() == ZeroPowerBehavior.FLOAT && this.getPower() == 0.0D;
    }

    public synchronized void setTargetPosition(int position) {
        position = this.adjustPosition(position);
        this.internalSetTargetPosition(position);
    }

    protected void internalSetTargetPosition(int position) {
        this.targetPosition = position;
    }

    public synchronized int getTargetPosition() {
        int position = this.targetPosition;
        return this.adjustPosition(position);
    }

    public synchronized int getCurrentPosition() {
        int position = this.targetPosition;
        return this.adjustPosition(position);
    }

    protected int adjustPosition(int position) {
        if (this.getOperationalDirection() == Direction.REVERSE) {
            position = -position;
        }

        return position;
    }

    protected double adjustPower(double power) {
        if (this.getOperationalDirection() == Direction.REVERSE) {
            power = -power;
        }

        return power;
    }

    protected Direction getOperationalDirection() {
        return this.motorType.getOrientation() == Rotation.CCW ? this.direction.inverted() : this.direction;
    }

    public synchronized void setMode(RunMode mode) {
        mode = mode.migrate();
        this.internalSetMode(mode);
    }

    protected void internalSetMode(RunMode mode) {
        this.motorMode = mode;
    }

    public RunMode getMode() {
        return this.motorMode;
    }
}
