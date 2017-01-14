package com.borsch.sim.hardware;

import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by Max on 12/18/2016.
 */

public class SimulatedServoController implements ServoController {
    @Override
    public void pwmEnable() {

    }

    @Override
    public void pwmDisable() {

    }

    @Override
    public PwmStatus getPwmStatus() {
        return null;
    }

    @Override
    public void setServoPosition(int i, double v) {

    }

    @Override
    public double getServoPosition(int i) {
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
}
