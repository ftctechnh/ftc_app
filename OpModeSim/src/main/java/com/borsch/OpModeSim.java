package com.borsch;

import com.borsch.sim.hardware.SimulatedColorSensor;
import com.borsch.sim.hardware.SimulatedDcMotorController;
import com.borsch.sim.hardware.SimulatedDeviceInterfaceModule;
import com.borsch.sim.hardware.SimulatedServoController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class OpModeSim {

    private OpMode opMode;

    public static void main (String[] args) {
        HashMap<String, Class <? extends HardwareDevice>> deviceMap = new HashMap<>();
        deviceMap.put("fr", DcMotor.class);
        deviceMap.put("fl", DcMotor.class);
        deviceMap.put("br", DcMotor.class);
        deviceMap.put("bl", DcMotor.class);
        deviceMap.put("harvester", DcMotor.class);
        deviceMap.put("lft", DcMotor.class);
        deviceMap.put("sr", DcMotor.class);
        deviceMap.put("sl", DcMotor.class);
        deviceMap.put("ls", Servo.class);
        deviceMap.put("los", Servo.class);
        deviceMap.put("cs", ColorSensor.class);
        deviceMap.put("lcs", ColorSensor.class);
        deviceMap.put("dim", DeviceInterfaceModule.class);


        // Create simulation
        OpModeSim sim = new OpModeSim(args[0], deviceMap);
        sim.init ();
        sim.start ();
    }

    public OpModeSim(Class <? extends OpMode> opModeClass, HashMap<String, Class <? extends HardwareDevice>> deviceMap) {
        create(opModeClass, deviceMap);
    }

    public OpModeSim(String classPath, HashMap<String, Class<? extends HardwareDevice>> deviceMap) {
        create (classPath, deviceMap);
    }

    /**
     * Creates a simulation of the given OpMode class (path) and provides it with a simulated HardwareMap created from the devicesMap (DeviceName, DeviceType)
     * Ex.
     * deviceMap.put ("frontRight", DcMotor.class)
     * new OpModeSim ("edu.usrobotics.opmode.BaseOp", deviceMap)
     */
    private void create(String classPath, HashMap<String, Class<? extends HardwareDevice>> deviceMap) {
        Class<OpMode> opModeClass = null;
        try {
            URLClassLoader urlcl = new URLClassLoader(new URL[] {new URL("file://" + System.getProperty("user.dir") + "/TeamCode/build/intermediates/classes/release/")});
            opModeClass = (Class<OpMode>) urlcl.loadClass(classPath);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        create (opModeClass, deviceMap);
    }

    /**
     * Creates a simulation of the given OpMode class and provides it with a simulated HardwareMap created from the devicesMap (DeviceName, DeviceType)
     * Ex.
     * deviceMap.put ("frontRight", DcMotor.class)
     * new OpModeSim (edu.usrobotics.opmode.BaseOp.class, deviceMap)
    */
    private void create(Class<? extends OpMode> opModeClass, HashMap<String, Class<? extends HardwareDevice>> deviceMap) {
        // Create simulated HardwareMap
        HardwareMap hardwareMap = new HardwareMap(null);

        // Add requested devices to simulated HardwareMap
        for (Map.Entry<String, Class <? extends HardwareDevice>> entry : deviceMap.entrySet()) {
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
     *@param deviceType The class that the new HardwareDevice should be.  @return A new HardwareDevice of the type provided.
     */
    private HardwareDevice createHardwareDevice(HardwareMap hardwareMap, String deviceName, Class<? extends HardwareDevice> deviceType) {
        HardwareDevice device = null;

        if (deviceType.equals(DcMotor.class)) {
            System.out.println ("Created Simulated HardwareDevice: DcMotorImpl(SimulatedDcMotorController)");
            device = new DcMotorImpl(new SimulatedDcMotorController(), 0);
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
