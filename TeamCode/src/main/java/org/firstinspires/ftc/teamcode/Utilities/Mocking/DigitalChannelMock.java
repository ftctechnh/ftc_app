package org.firstinspires.ftc.teamcode.Utilities.Mocking;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

public class DigitalChannelMock implements DigitalChannel {

    // True means open
    // False means closed
    private boolean state;

    public DigitalChannelMock() {
        state = true;
    }

    public DigitalChannelMock(boolean state) {
        this.state = state;
    }

    @Override
    public Mode getMode() {
        return null;
    }

    @Override
    public void setMode(Mode mode) {

    }

    @Override
    public boolean getState() {
        return state;
    }

    @Override
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public void setMode(DigitalChannelController.Mode mode) {

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
