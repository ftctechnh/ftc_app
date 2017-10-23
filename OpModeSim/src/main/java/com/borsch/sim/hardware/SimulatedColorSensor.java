package com.borsch.sim.hardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

/**
 * Created by Max on 12/18/2016.
 */

public class SimulatedColorSensor implements ColorSensor {
    @Override
    public int red() {
        return 0;
    }

    @Override
    public int green() {
        return 0;
    }

    @Override
    public int blue() {
        return 0;
    }

    @Override
    public int alpha() {
        return 0;
    }

    @Override
    public int argb() {
        return 0;
    }

    @Override
    public void enableLed(boolean b) {

    }

    @Override
    public void setI2cAddress(I2cAddr i2cAddr) {

    }

    @Override
    public I2cAddr getI2cAddress() {
        return null;
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
