package org.chathamrobotics.common.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.chathamrobotics.common.hardware.LimitSwitch;

/**
 * Created by carsonstorm on 10/26/2017.
 */

@SuppressWarnings("unused")
public class ModernRoboticsLimitSwitch implements LimitSwitch {
    private final DigitalChannelController digitalChannelController;
    private final int physicalPort;

    /**
     * Creates a instance of ModernRoboticsLimitSwitch
     * @param digitalController the device's digital controller
     * @param physicalPort      the device's physical port
     */
    public ModernRoboticsLimitSwitch(DigitalChannelController digitalController, int physicalPort) {
        this.digitalChannelController = digitalController;
        this.physicalPort = physicalPort;
    }

    @Override
    public boolean isPressed() {
        return digitalChannelController.getDigitalChannelState(physicalPort);
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
        return digitalChannelController.getConnectionInfo() + "; digital port " + physicalPort;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {}

    @Override
    public void close() {}
}
