package com.borsch.sim.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.concurrent.locks.Lock;

/**
 * Created by Max on 12/18/2016.
 */

public class SimulatedDeviceInterfaceModule implements DeviceInterfaceModule {
    @Override
    public int getDigitalInputStateByte() {
        return 0;
    }

    @Override
    public void setDigitalIOControlByte(byte b) {

    }

    @Override
    public byte getDigitalIOControlByte() {
        return 0;
    }

    @Override
    public void setDigitalOutputByte(byte b) {

    }

    @Override
    public byte getDigitalOutputStateByte() {
        return 0;
    }

    @Override
    public boolean getLEDState(int i) {
        return false;
    }

    @Override
    public void setLED(int i, boolean b) {

    }

    @Override
    public double getAnalogInputVoltage(int i) {
        return 0;
    }

    @Override
    public double getMaxAnalogInputVoltage() {
        return 0;
    }

    @Override
    public void setAnalogOutputVoltage(int i, int i1) {

    }

    @Override
    public void setAnalogOutputFrequency(int i, int i1) {

    }

    @Override
    public void setAnalogOutputMode(int i, byte b) {

    }

    @Override
    public SerialNumber getSerialNumber() {
        return null;
    }

    @Override
    public void setPulseWidthOutputTime(int i, int i1) {

    }

    @Override
    public void setPulseWidthPeriod(int i, int i1) {

    }

    @Override
    public int getPulseWidthOutputTime(int i) {
        return 0;
    }

    @Override
    public int getPulseWidthPeriod(int i) {
        return 0;
    }

    @Override
    public void enableI2cReadMode(int i, I2cAddr i2cAddr, int i1, int i2) {

    }

    @Override
    public void enableI2cWriteMode(int i, I2cAddr i2cAddr, int i1, int i2) {

    }

    @Override
    public byte[] getCopyOfReadBuffer(int i) {
        return new byte[0];
    }

    @Override
    public byte[] getCopyOfWriteBuffer(int i) {
        return new byte[0];
    }

    @Override
    public void copyBufferIntoWriteBuffer(int i, byte[] bytes) {

    }

    @Override
    public void setI2cPortActionFlag(int i) {

    }

    @Override
    public void clearI2cPortActionFlag(int i) {

    }

    @Override
    public boolean isI2cPortActionFlagSet(int i) {
        return false;
    }

    @Override
    public void readI2cCacheFromController(int i) {

    }

    @Override
    public void writeI2cCacheToController(int i) {

    }

    @Override
    public void writeI2cPortFlagOnlyToController(int i) {

    }

    @Override
    public boolean isI2cPortInReadMode(int i) {
        return false;
    }

    @Override
    public boolean isI2cPortInWriteMode(int i) {
        return false;
    }

    @Override
    public boolean isI2cPortReady(int i) {
        return false;
    }

    @Override
    public Lock getI2cReadCacheLock(int i) {
        return null;
    }

    @Override
    public Lock getI2cWriteCacheLock(int i) {
        return null;
    }

    @Override
    public byte[] getI2cReadCache(int i) {
        return new byte[0];
    }

    @Override
    public byte[] getI2cWriteCache(int i) {
        return new byte[0];
    }

    @Override
    public void registerForI2cPortReadyCallback(I2cPortReadyCallback i2cPortReadyCallback, int i) {

    }

    @Override
    public I2cPortReadyCallback getI2cPortReadyCallback(int i) {
        return null;
    }

    @Override
    public void deregisterForPortReadyCallback(int i) {

    }

    @Override
    public void registerForPortReadyBeginEndCallback(I2cPortReadyBeginEndNotifications i2cPortReadyBeginEndNotifications, int i) {

    }

    @Override
    public I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback(int i) {
        return null;
    }

    @Override
    public void deregisterForPortReadyBeginEndCallback(int i) {

    }

    @Override
    public boolean isArmed() {
        return false;
    }

    @Override
    public void readI2cCacheFromModule(int i) {

    }

    @Override
    public void writeI2cCacheToModule(int i) {

    }

    @Override
    public void writeI2cPortFlagOnlyToModule(int i) {

    }

    @Override
    public Mode getDigitalChannelMode(int i) {
        return null;
    }

    @Override
    public void setDigitalChannelMode(int i, Mode mode) {

    }

    @Override
    public boolean getDigitalChannelState(int i) {
        return false;
    }

    @Override
    public void setDigitalChannelState(int i, boolean b) {

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
}
