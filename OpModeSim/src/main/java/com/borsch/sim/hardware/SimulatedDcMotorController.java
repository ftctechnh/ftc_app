package com.borsch.sim.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Created by Max on 12/17/2016.
 */

public class SimulatedDcMotorController implements DcMotorController, VoltageSensor {
    @Override
    public void setMotorMode(int i, DcMotor.RunMode runMode) {

    }

    @Override
    public DcMotor.RunMode getMotorMode(int i) {
        return null;
    }

    @Override
    public void setMotorPower(int i, double v) {

    }

    @Override
    public double getMotorPower(int i) {
        return 0;
    }

    @Override
    public void setMotorMaxSpeed(int i, int i1) {

    }

    @Override
    public int getMotorMaxSpeed(int i) {
        return 0;
    }

    @Override
    public boolean isBusy(int i) {
        return false;
    }

    @Override
    public void setMotorZeroPowerBehavior(int i, DcMotor.ZeroPowerBehavior zeroPowerBehavior) {

    }

    @Override
    public DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int i) {
        return null;
    }

    @Override
    public boolean getMotorPowerFloat(int i) {
        return false;
    }

    @Override
    public void setMotorTargetPosition(int i, int i1) {

    }

    @Override
    public int getMotorTargetPosition(int i) {
        return 0;
    }

    @Override
    public int getMotorCurrentPosition(int i) {
        return 0;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }

    @Override
    public double getVoltage() {
        return 0;
    }
}
