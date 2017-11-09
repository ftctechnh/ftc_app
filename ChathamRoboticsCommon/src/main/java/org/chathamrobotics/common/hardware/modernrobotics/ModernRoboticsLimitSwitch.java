package org.chathamrobotics.common.hardware.modernrobotics;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/28/2017
 */

import android.util.Log;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.chathamrobotics.common.hardware.LimitSwitch;

/**
 * The representation of a modern robotics limit switch
 */
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
    public boolean getState() {
        return  isPressed();
    }

    @Override
    public void setState(boolean state) {
        Log.d("Limit Switch State", String.valueOf(state));

        if (digitalChannelController != null) {
            digitalChannelController.setDigitalChannelState(physicalPort, state);
        }

        digitalChannel.setState(state);
    }

    @Override
    public Mode getMode() {
        if (digitalChannelController != null) {
            return digitalChannelController.getDigitalChannelMode(physicalPort);
        }

        return digitalChannel.getMode();
    }

    @Override
    public void setMode(DigitalChannelController.Mode mode) {
        if (digitalChannelController != null) {
            digitalChannelController.setDigitalChannelMode(physicalPort, mode);
        }

        digitalChannel.setMode(mode);
    }

    @Override
    public void setMode(Mode mode) {
        if (digitalChannelController != null) {
            digitalChannelController.setDigitalChannelMode(physicalPort, mode);
        }

        digitalChannel.setMode(mode);
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.ModernRobotics;
    }

    @Override
    public String getDeviceName() {
        return "Modern Robotics Limit Sensor"; //TODO: figure out what this should actually be
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

    @Override
    public String toString() {
        return getDeviceName() + " " + getConnectionInfo();
    }
}
