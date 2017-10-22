package com.borsch.sim;

import com.qualcomm.robotcore.hardware.*;

import java.util.HashMap;

public class RobotConfiguration {
    private final HashMap<String, Class <? extends HardwareDevice>> deviceMap = new HashMap<>();

    public void addDcMotor (String name) {
        addDevice(name, DcMotor.class);
    }

    public void addServo (String name) {
        addDevice(name, Servo.class);
    }

    public void addColorSensor (String name) {
        addDevice(name, ColorSensor.class);
    }

    public void addDeviceInterfaceModule (String name) {
        addDevice(name, DeviceInterfaceModule.class);
    }

    private void addDevice(String name, Class<? extends HardwareDevice> device) {
        deviceMap.put(name, device);
    }

    public HashMap<String, Class<? extends HardwareDevice>> getDeviceMap() {
        return deviceMap;
    }
}
