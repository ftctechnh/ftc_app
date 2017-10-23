package com.borsch;

import com.borsch.sim.RobotConfiguration;
import com.borsch.sim.VuforiaConfiguration;
import com.borsch.sim.hardware.SimulatedColorSensor;
import com.borsch.sim.hardware.SimulatedDcMotor;
import com.borsch.sim.hardware.SimulatedDeviceInterfaceModule;
import com.borsch.sim.hardware.SimulatedServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class OpModeSimulation {

    private OpMode opMode;
    public static VuforiaConfiguration vuforiaConfiguration;

    public OpModeSimulation(Class <? extends OpMode> opModeClass, RobotConfiguration robot) {
        create(opModeClass, robot, new VuforiaConfiguration());
    }

    public OpModeSimulation(Class <? extends OpMode> opModeClass, RobotConfiguration robot, VuforiaConfiguration vuforiaConfig) {
        create(opModeClass, robot, vuforiaConfig);
    }

    public OpModeSimulation(String classPath, RobotConfiguration robot) {
        create(classPath, robot, new VuforiaConfiguration());
    }

    public OpModeSimulation(String classPath, RobotConfiguration robot, VuforiaConfiguration vuforiaConfig) {
        create(classPath, robot, vuforiaConfig);
    }

    /**
     * Creates a simulation of the given OpMode class (path) and provides it with a simulated HardwareMap created from the devicesMap (DeviceName, DeviceType)
     * Ex.
     * deviceMap.put ("frontRight", DcMotor.class)
     * new OpModeSimulation ("edu.usrobotics.opmode.BaseOp", deviceMap)
     */
    private void create(String classPath, RobotConfiguration robot, VuforiaConfiguration vuforiaConfig) {
        Class<OpMode> opModeClass = null;
        try {
            URLClassLoader urlcl = new URLClassLoader(new URL[] {new URL("file://" + System.getProperty("user.dir") + "/TeamCode/build/intermediates/classes/release/")});
            opModeClass = (Class<OpMode>) urlcl.loadClass(classPath);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        create(opModeClass, robot, vuforiaConfig);
    }

    /**
     * Creates a simulation of the given OpMode class and provides it with a simulated HardwareMap created from the devicesMap (DeviceName, DeviceType)
     * Ex.
     * deviceMap.put ("frontRight", DcMotor.class)
     * new OpModeSimulation (edu.usrobotics.opmode.BaseOp.class, deviceMap)
    */
    private void create(Class<? extends OpMode> opModeClass, RobotConfiguration robot, VuforiaConfiguration vuforiaConfig) {
        vuforiaConfiguration = vuforiaConfig;

        // Create simulated HardwareMap
        HardwareMap hardwareMap = new HardwareMap(null);

        // Add requested devices to simulated HardwareMap
        for (Map.Entry<String, Class <? extends HardwareDevice>> entry : robot.getDeviceMap().entrySet()) {
            createHardwareDevice (hardwareMap, entry.getKey(), entry.getValue());
        }

        try {
            // Create instance of the OpMode & pass it the simulated HardwareMap
            opMode = opModeClass.newInstance();
            opMode.hardwareMap = hardwareMap;

        } catch (InstantiationException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simulates init() event call on the OpMode.
     */
    public void init () {
        opMode.init();
    }

    /**
     * Simulates start() event call on the OpMode.
     */
    public void start () {
        opMode.start();
    }

    /**
     * Simulates stop() event call on the OpMode.
     */
    public void stop () {
        opMode.stop();
    }

    /**
     *
     * @return The OpMode instance being simulated.
     */
    public OpMode getOpMode () {
        return opMode;
    }

    /**
     * Creates a new HardwareDevice from the device class provided.
     *
     * @param hardwareMap The HardwareMap to add the new device to.
     * @param deviceName The name of the new device.
     * @param deviceType The class that the new HardwareDevice should be.  @return A new HardwareDevice of the type provided.
     */
    private HardwareDevice createHardwareDevice(HardwareMap hardwareMap, String deviceName, Class<? extends HardwareDevice> deviceType) {
        HardwareDevice device = null;

        if (deviceType.equals(DcMotor.class)) {
            System.out.println ("Created Simulated HardwareDevice: SimulatedDcMotor");
            device = new SimulatedDcMotor(); // BYPASS CONTROLLER
            hardwareMap.dcMotor.put(deviceName, (DcMotor) device);

        } else if (deviceType.equals(Servo.class)) {
            System.out.println ("Created Simulated HardwareDevice: ServoImpl(SimulatedServoController)");
            device = new ServoImpl(new SimulatedServoController(), 0);
            hardwareMap.servo.put(deviceName, (Servo) device);

        } else if (deviceType.equals(ColorSensor.class)) {
            System.out.println ("Created Simulated HardwareDevice: SimulatedColorSensor()");
            device = new SimulatedColorSensor(); // DOES NOT USE CONTROLLER
            hardwareMap.colorSensor.put(deviceName, (ColorSensor) device);

        } else if (deviceType.equals(DeviceInterfaceModule.class)) {
            System.out.println ("Created Simulated HardwareDevice: SimulatedDeviceInterfaceModule()");
            device = new SimulatedDeviceInterfaceModule(); // DOES NOT USE CONTROLLER
            hardwareMap.deviceInterfaceModule.put(deviceName, (DeviceInterfaceModule) device);

        }

        return device;
    }
}
