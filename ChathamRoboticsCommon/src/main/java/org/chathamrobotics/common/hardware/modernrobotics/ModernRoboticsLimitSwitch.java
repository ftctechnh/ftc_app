package org.chathamrobotics.common.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.chathamrobotics.common.hardware.LimitSwitch;

@SuppressWarnings("unused")
public class ModernRoboticsLimitSwitch implements LimitSwitch {
    private final DigitalChannel digitalChannel;
    private final DigitalChannelController digitalChannelController;
    private final int physicalPort;

    /**
     * Creates a instance of ModernRoboticsLimitSwitch
     * @param digitalChannel    the digital channel for the limit switch
     */
    public ModernRoboticsLimitSwitch(DigitalChannel digitalChannel) {
        this.digitalChannel = digitalChannel;
        this.digitalChannelController = null;
        this.physicalPort = 0;
    }

    /**
     * Creates a instance of ModernRoboticsLimitSwitch
     * @param digitalController the device's digital controller
     * @param physicalPort      the device's physical port
     */
    public ModernRoboticsLimitSwitch(DigitalChannelController digitalController, int physicalPort) {
        this.digitalChannel = null;
        this.digitalChannelController = digitalController;
        this.physicalPort = physicalPort;
    }

    /**
     * Whether or not the limit switch is being pressed
     * @return  Whether or not the limit switch is being pressed
     */
    @Override
    public boolean isPressed() {
        if (digitalChannelController != null) {
            return digitalChannelController.getDigitalChannelState(physicalPort);
        }

        return digitalChannel.getState();
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.ModernRobotics;
    }

    @Override
    public String getDeviceName() {
        return "Modern Robotics Touch Sensor"; //TODO: figure out what this should actually be
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getConnectionInfo() {
        if (digitalChannelController != null) {
            return digitalChannelController.getConnectionInfo() + "; digital port " + physicalPort;
        }

        return digitalChannel.getConnectionInfo();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {}

    @Override
    public void close() {}
}
